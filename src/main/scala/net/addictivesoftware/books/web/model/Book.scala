package net.addictivesoftware.books.web.model {

import net.liftweb._
import http._
import mapper._
import sitemap.Loc._

class Book extends LongKeyedMapper[Book] with IdPK {
  def getSingleton = Book

  def loggedInUser = User.currentUser

  object title extends MappedString(this, 100)

  object authors extends HasManyThrough(this, Author, BookAuthor, BookAuthor.book, BookAuthor.author)

  object isbn extends MappedString(this, 20)

  object imageurl extends MappedString(this,100)

  object publisher extends MappedString(this, 100)

  object publishedYear extends MappedInt(this)

  object link extends MappedString(this, 100)

  object user extends HasManyThrough(this, User, BookUser, BookUser.user, BookUser.book)


}

object Book extends Book with LongKeyedMetaMapper[Book] with CRUDify[Long, Book] {
	override def editMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.editMenuLocParams
	override def viewMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.viewMenuLocParams
	override def createMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.createMenuLocParams
	override def showAllMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.showAllMenuLocParams
}


}
