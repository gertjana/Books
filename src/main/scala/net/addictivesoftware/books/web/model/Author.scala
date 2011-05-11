package net.addictivesoftware.books.web.model {

import net.liftweb._
import http._
import mapper._
import sitemap.Loc._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
//import net.liftweb.json.JsonAST._
import scala.xml.Node
import net.addictivesoftware.books.web.util.StringHelper

class Author extends LongKeyedMapper[Author] with IdPK {
  def getSingleton = Author

  object firstName extends MappedString(this, 100)
  object lastName extends MappedString(this, 100)
  object birthDate extends MappedDate(this)

  object books extends HasManyThrough(this, Book, BookAuthor, BookAuthor.author, BookAuthor.book)

  def fullName = {
    this.firstName + " " + this.lastName
  } 

}

object Author extends Author with LongKeyedMetaMapper[Author] with CRUDify[Long, Author] {
	override def editMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.editMenuLocParams
	override def viewMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.viewMenuLocParams
	override def createMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.createMenuLocParams
	override def showAllMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.showAllMenuLocParams

  def toJSON (author : Author) : JValue = {

    ("author" ->
      ("id" -> author.id.is) ~
      ("firstname" -> author.firstName.is) ~
      ("lastname" -> author.lastName.is) //~
      //("birthdate" -> StringHelper.dateToString(author.birthDate))
    )
  }

  def toXML (author : Author) : Node = Xml.toXml(toJSON(author)).head
}


}
