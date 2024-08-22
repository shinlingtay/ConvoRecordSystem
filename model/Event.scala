package ch.makery.address.model

import ch.makery.address.util.Database
import scalafx.beans.property.{StringProperty}
import scalikejdbc._

import scala.util.Try

class Event(val eventNameS: String) extends Database {
  def this() = this(null)

  var eventname = StringProperty(eventNameS)
  var time = StringProperty("")
  var description = StringProperty("")
  var emcee = StringProperty("")
  var asstemcee = StringProperty("")
  var speaker = StringProperty("")

  def save(): Try[Int] = {
    if (!isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
            INSERT INTO event (eventname, time, description, emcee, asstemcee, speaker)
            VALUES (${eventname.value}, ${time.value}, ${description.value}, ${emcee.value},
                    ${asstemcee.value}, ${speaker.value})
          """.update.apply()
      })
    } else {
      Try(DB autoCommit { implicit session =>
        sql"""
          UPDATE event
          SET
          eventname  = ${eventname.value},
          time   = ${time.value},
          description = ${description.value},
          emcee = ${emcee.value},
          asstemcee = ${asstemcee.value},
          speaker = ${speaker.value}
          WHERE eventname = ${eventname.value}
          """.update.apply()
      })
    }
  }

  def delete(): Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
          DELETE FROM event WHERE
          eventname = ${eventname.value} AND time = ${time.value}
          """.update.apply()
      })
    } else {
      throw new Exception("Event does not exist in the database")
    }
  }

  def isExist: Boolean = {
    DB readOnly { implicit session =>
      sql"""
          SELECT 1 FROM event WHERE
          eventname = ${eventname.value}
        """.map(_.int(1)).single.apply().isDefined
    }
  }
}

object Event extends Database {
  def apply(
             eventNameS: String,
             timeS: String,
             descriptionI: String,
             emceeI: String,
             asstemceeI: String,
             speakerI: String
           ): Event = {

    new Event(eventNameS) {
      time.value = timeS
      description.value = descriptionI
      emcee.value = emceeI
      asstemcee.value = asstemceeI
      speaker.value = speakerI
    }
  }

  def initializeTable(): Unit = {
    DB autoCommit { implicit session =>
      sql"""
        CREATE TABLE event (
          id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
          eventname VARCHAR(30),
          time VARCHAR(10),
          description VARCHAR(40),
          emcee VARCHAR(30),
          asstemcee VARCHAR(30),
          speaker VARCHAR(30),
          PRIMARY KEY (id)
        )
        """.execute.apply()
    }
  }

  def getAllEvent: List[Event] = {
    DB readOnly { implicit session =>
      sql"SELECT * FROM event".map(rs => Event(
        rs.string("eventname"),
        rs.string("time"),
        rs.string("description"),
        rs.string("emcee"),
        rs.string("asstemcee"),
        rs.string("speaker")
      )).list.apply()
    }
  }
}
