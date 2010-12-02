package net.addictivesoftware.books.web.model {

import net.liftweb._
import http._
import mapper._
import sitemap.Loc._

class Author extends LongKeyedMapper[Author] with IdPK {
  def getSingleton = Author

  object firstName extends MappedString(this, 100)
  object lastName extends MappedString(this, 100)
  object birthDate extends MappedDate(this)
 
  def fullName = {
    this.firstName + " " + this.lastName
  } 

}

object Author extends Author with LongKeyedMetaMapper[Author] with CRUDify[Long, Author] {
	override def editMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.editMenuLocParams
	override def viewMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.viewMenuLocParams
	override def createMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.createMenuLocParams
	override def showAllMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.showAllMenuLocParams

}


}
