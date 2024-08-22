package ch.makery.address.model

import scalafx.beans.property.{StringProperty}

import ch.makery.address.util.Database
import scalikejdbc._

import scala.util.{Failure, Success, Try}

class Login(val userNameS: String, val passwordS: String) extends Database {
  def this() = this(null, null)

  var userName = new StringProperty(userNameS)
  var password = new StringProperty(passwordS)

  def save(): Try[Int] = {
    if (!(isExist)) {
      Try(DB autoCommit { implicit session =>
        sql"""
          insert into login (userName, password) values
            (${userName.value}, ${password.value})
        """.update.apply()
      })
    } else {
      Try(DB autoCommit { implicit session =>
        sql"""
        update login
        set
        userName  = ${userName.value} ,
        password   = ${password.value}
         where userName = ${userName.value} and
         password = ${password.value}
        """.update.apply()
      })
    }
  }

  def isExist: Boolean = {
    DB readOnly { implicit session =>
      sql"""
        select * from login where
        userName = ${userName.value} and password = ${password.value}
      """.map(rs => rs.string("userName")).single.apply()
    } match {
      case Some(x) => true
      case None => false
    }
  }
}

object Login extends Database {
  def apply(userNameS: String, passwordS: String): Login = {
    new Login(userNameS, passwordS) {
      userName.value = userNameS
      password.value = passwordS
    }
  }

  def initializeTable() = {
    DB autoCommit { implicit session =>
      sql"""
      create table login (
        id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        userName varchar(64),
        password varchar(64)
      )
      """.execute.apply()
    }
  }

  def getAllLogin: List[Login] = {
    DB readOnly { implicit session =>
      sql"select * from login".map(rs => Login(rs.string("userName"),
        rs.string("password"))).list.apply()
    }
  }

  def isExist(userNameS: String, passwordS: String): Boolean = {
    DB readOnly { implicit session =>
      sql"""
      select * from login where
      userName = ${userNameS} and password = ${passwordS}
    """.map(rs => rs.string("userName")).single.apply()
    } match {
      case Some(x) => true
      case None => false
    }
  }
}
