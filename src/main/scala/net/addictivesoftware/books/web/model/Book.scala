package net.addictivesoftware.books.web.model {

import net.liftweb._
import net.liftweb.http._
import net.liftweb.mapper._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
//import net.liftweb.json.JsonAST._
import sitemap.Loc._
import scala.xml.Node
import net.addictivesoftware.books.web.util.StringHelper

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

  object users extends HasManyThrough(this, User, BookUser, BookUser.user, BookUser.book)

}

object Book extends Book with LongKeyedMetaMapper[Book] with CRUDify[Long, Book] {
	override def editMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.editMenuLocParams
	override def viewMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.viewMenuLocParams
	override def createMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.createMenuLocParams
	override def showAllMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.showAllMenuLocParams


  def toJSON (book : Book) : JValue = {

    ("book" ->
      ("id" -> book.id.is) ~
      ("title" -> book.title.is) ~
      ("authors" -> StringHelper.listToString(book.authors.get.map(_.fullName))) ~
      ("isbn" -> book.isbn.is) ~
      ("imageurl" -> book.imageurl.is) ~
      ("publisher" -> book.publisher.is) ~
      ("publishedYear" -> book.publishedYear.is) ~
      ("link" -> book.link.is)
    )
  }

  def toXML (book : Book) : Node = Xml.toXml(toJSON(book)).head
}


}
