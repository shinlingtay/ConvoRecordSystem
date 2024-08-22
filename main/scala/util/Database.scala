package ch.makery.address.util

import scalikejdbc._
import ch.makery.address.model.{Event, Login, Student}

trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"

  val dbURL = "jdbc:derby:myDB;create=true;";
  // initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)

  ConnectionPool.singleton(dbURL, "me", "mine")

  // ad-hoc session provider on the REPL
  implicit val session = AutoSession


}

object Database extends Database {
  def setupDB() = {
    if (!hasStudentDBInitialize) {
      Student.initializeTable()
    }
    if (!hasEventDBInitialize) {
      Event.initializeTable()
    }
    if (!hasLoginDBInitialize) {
      Login.initializeTable()
    }
  }

  def hasStudentDBInitialize: Boolean = {
    DB getTable "Student" match {
      case Some(x) => true
      case None => false
    }
  }

  def hasEventDBInitialize: Boolean = {
    DB getTable "Event" match {
      case Some(x) => true
      case None => false
    }
  }

  def hasLoginDBInitialize: Boolean = {
    DB getTable "Login" match {
      case Some(x) => true
      case None => false
    }
  }
}
