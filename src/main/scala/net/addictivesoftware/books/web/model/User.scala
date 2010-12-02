package net.addictivesoftware.books.web.model {

import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.common._

object User extends User with MetaMegaProtoUser[User] {
  override def dbTableName = "users" // define the DB table name
  override def screenWrap = Full(<lift:surround with="default" at="content">
			       <lift:bind /></lift:surround>)
  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email,
  locale, timezone, password)

  // comment this line out to require email validations
  override def skipEmailValidation = true
 
  override def lostPasswordMenuLoc = Empty
  
}

class User extends MegaProtoUser[User] {
  def getSingleton = User // what's the "meta" server

  override def toString = firstName + " " + lastName
}

}
