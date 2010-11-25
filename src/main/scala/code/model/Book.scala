package code {
package model {

import net.liftweb._
import http._
import mapper._
import common._
import sitemap.Loc._

class Book extends LongKeyedMapper[Book] with IdPK {
  def getSingleton = Book

  def loggedInUser = User.currentUser

  object title extends MappedString(this, 100)
 
  object author extends LongMappedMapper[Book, Author](this, Author) {
    override def validSelectValues = Full(Author.findAll.map(a => (a.id.is, a.lastName.is))) 
  }

  object isbn extends MappedString(this, 20)

  object imageurl extends MappedString(this,100)

  object publisher extends MappedString(this, 100)

  object publishedYear extends MappedInt(this)

  object link extends MappedString(this, 100)

  object user extends LongMappedMapper(this, User) {
    //override def dbDisplay_? = false	
    override def beforeCreate = loggedInUser
  }
}

object Book extends Book with LongKeyedMetaMapper[Book] with CRUDify[Long, Book] {
	override def editMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.editMenuLocParams
	override def viewMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.viewMenuLocParams
	override def createMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.createMenuLocParams
	override def showAllMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.showAllMenuLocParams
}


}
}
