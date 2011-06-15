/*
Copyright [2011] [Addictive Software]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package net.addictivesoftware.books.web.model {

import net.liftweb._
import http._
import mapper._
import sitemap.Loc._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
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

  def updateFromJSON(toUpdate: Author, json: JsonAST.JValue) : Author = {
    super.updateFromJSON_!(toUpdate, json.asInstanceOf[JsonAST.JObject]);
  }

  def toJSON (author : Author) : JValue = {

    ("author" ->
      ("id" -> author.id.is) ~
      ("firstname" -> author.firstName.is) ~
      ("lastname" -> author.lastName.is) ~
      ("nrofbooks" -> author.books.get.size)
    )
  }

  //calculates nrofbooks for the current user
  def toJSON(author : Author, userid : Long) : JValue = {
    ("author" ->
      ("id" -> author.id.is) ~
      ("firstname" -> author.firstName.is) ~
      ("lastname" -> author.lastName.is) ~
      ("nrofbooks" -> getNrOfBooksForUser(author, userid))
    )
  }

  def toXML (author : Author) : Node = Xml.toXml(toJSON(author)).head

  def toXML (author : Author, userid : Long) : Node = Xml.toXml(toJSON(author, userid)).head

  def getNrOfBooksForUser(author:Author, id: Long) : Long = {
      Book.findAll(
        In(Book.id, BookAuthor.book, By(BookAuthor.author, author.id)),
        In(Book.id, BookUser.book, By(BookUser.user, id))
      ).size
  }

}


}
