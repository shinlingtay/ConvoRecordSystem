package ch.makery.address.model

import scalafx.beans.property.{ObjectProperty, StringProperty}

import java.time.LocalDate
import ch.makery.address.util.Database
import ch.makery.address.util.DateUtil.StringFormater
import scalikejdbc._

import scala.util.{Failure, Success, Try}

class Student(val StudentNameS: String, val StudentICS: String) extends Database {
  def this() = this(null, null)

  var StudentName = new StringProperty(StudentNameS)
  var StudentIC = new StringProperty(StudentICS)
  var dob = ObjectProperty[LocalDate](LocalDate.of(2000, 1, 1))
  var gender = new StringProperty("prefer not to say")
  var nationality = new StringProperty("Malaysian")
  var contactNumber = new StringProperty("some number")
  var email = new StringProperty("some Email")

  def save(): Try[Unit] = {
    if (!isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
          INSERT INTO Student (StudentName, StudentIC, dob, gender, nationality, contactNumber, email)
          VALUES (${StudentName.value}, ${StudentIC.value}, ${dob.value.toString}, ${gender.value}, ${nationality.value}, ${contactNumber.value}, ${email.value})
        """.update.apply()

        sql"""
          INSERT INTO Absence (StudentName, day1, day2, day3)
          VALUES (${StudentName.value}, null, null, null)
        """.update.apply()
      })
    } else {
      Try(DB autoCommit { implicit session =>
        sql"""
          UPDATE Student
          SET
            StudentName = ${StudentName.value},
            StudentIC = ${StudentIC.value},
            dob = ${dob.value.toString},
            gender = ${gender.value},
            nationality = ${nationality.value},
            contactNumber = ${contactNumber.value},
            email = ${email.value}
          WHERE StudentName = ${StudentName.value}
        """.update.apply()

        sql"""
          UPDATE Absence
          SET
            StudentName = ${StudentName.value}
          WHERE StudentName = ${StudentName.value}
        """.update.apply()
      })
    }
  }

  def delete(): Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
          DELETE FROM Student
          WHERE StudentName = ${StudentName.value} AND StudentIC = ${StudentIC.value}
        """.update.apply()

        sql"""
          DELETE FROM Absence
          WHERE StudentName = ${StudentName.value}
        """.update.apply()
      })
    } else {
      throw new Exception("Student does not exist in Database")
    }
  }

  def isExist: Boolean = {
    DB readOnly { implicit session =>
      sql"""
        SELECT 1 FROM Student
        WHERE StudentName = ${StudentName.value}
      """.map(_.int(1)).single.apply().isDefined
    }
  }
}

object Student extends Database {
  def apply(
             StudentNameS: String,
             StudentICS: String,
             dobS: String,
             genderS: String,
             nationalityS: String,
             contactNumberS: String,
             emailS: String
           ): Student = {

    new Student(StudentNameS, StudentICS) {
      dob.value = dobS.parseLocalDate
      gender.value = genderS
      nationality.value = nationalityS
      contactNumber.value = contactNumberS
      email.value = emailS
    }
  }

  def initializeTable(): Unit = {
    DB autoCommit { implicit session =>
      sql"""
        CREATE TABLE Student (
          id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
          StudentName VARCHAR(64),
          StudentIC VARCHAR(64),
          dob VARCHAR(64),
          gender VARCHAR(64),
          nationality VARCHAR(100),
          contactNumber VARCHAR(64),
          email VARCHAR(64)
          PRIMARY KEY (id)
        )
      """.execute.apply()
    }
  }

  def getAllStudent: List[Student] = {
    DB readOnly { implicit session =>
      sql"select * from Student".map(rs => Student(rs.string("StudentName"), rs.string("StudentIC"),
        rs.string("dob"), rs.string("gender"), rs.string("nationality"), rs.string("contactNumber"),
        rs.string("email"))).list.apply()
  }
    }
}
