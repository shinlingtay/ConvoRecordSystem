package ch.makery.address.model

import ch.makery.address.util.Database
import scalafx.beans.property.{StringProperty}
import scalikejdbc._

import scala.util.Try

class Absence(val eventName: StringProperty) extends Database {

  def this(eventNameS: String) = this(StringProperty(eventNameS))
}

object Absence extends Database {

  def initializeTable(): Unit = {
    DB autoCommit { implicit session =>
      // Check if the table exists
      val tableExists = sql"""
      SELECT 1
      FROM SYS.SYSTABLES
      WHERE TABLENAME = 'ABSENCE'
    """.map(_.int(1)).single.apply().isDefined

      // Create the table only if it does not exist
      if (!tableExists) {
        sql"""
        CREATE TABLE ABSENCE (
          id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
          studentname VARCHAR(64) NOT NULL,
          DAY1 VARCHAR(100),
          day2 VARCHAR(100),
          DAY3 VARCHAR(100),
          PRIMARY KEY (id)
        )
      """.execute.apply()
      }
    }
  }


  def getAllEvents(eventName: String): List[(String, String)] = {
    val columnName = eventName match {
      case "DAY1"           => "DAY1"
      case "DAY2"           => "DAY2"
      case "DAY3"           => "DAY3"
      case _               => throw new IllegalArgumentException("Invalid event name")
    }

    DB readOnly { implicit session =>
      sql"""
        SELECT studentname, ${SQLSyntax.createUnsafely(columnName)} FROM ABSENCE
      """.map(rs => (rs.string("studentname"), rs.string(columnName))).list.apply()
    }
  }
}
