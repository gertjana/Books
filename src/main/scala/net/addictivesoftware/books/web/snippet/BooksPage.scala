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
package net.addictivesoftware.books.web.snippet {

import scala.xml.{NodeSeq, Text}
import net.liftweb._
import common._
import http._
import util._
import Helpers._
import net.addictivesoftware.books.web.model._
import net.addictivesoftware.books.web.util._
import net.addictivesoftware.books.web.remote._
import mapper._
import net.liftweb.http.PaginatorSnippet
import js._
import JsCmds._
import SHtml._

class Bookspage extends PaginatorSnippet[Book] {
  //val logger = Logger(classOf[Bookspage])

  override def count = Book.count
  override def page = Book.findAll(StartAt(curPage*itemsPerPage), MaxRows(itemsPerPage))

  def detailPage = "book?id=";

  def addButton(book : Book) : NodeSeq = {
    if (User.loggedIn_?) {
      ajaxButton(Text("Add to collection"), {() =>
        addToCollection(book)
      })
    } else {
      <span></span>
    }
  }

  def enhanceButton(book : Book) : NodeSeq = {
      if (User.loggedIn_?) {
        ajaxButton(Text("Enhance"), {() =>
          try {
            var enhancedBook = enhanceByIsbn(book.isbn.is)
            SetHtml("my-div", Text(enhancedBook.title))
          }
          catch {
            case e: Exception => {
              println(e.getMessage())
              SetHtml("my-div", Text(e.getMessage()))
            }
          }
          finally {
         }
        })
      } else {
        <span></span>
      }
    }

  def list(in: NodeSeq) : NodeSeq = {
    
    def bindBooks(template: NodeSeq): NodeSeq = {
      page.flatMap {
                book => bind("book", template, 
			"title" -> book.title.is, 
			"author" -> StringHelper.listToString(book.authors.get.map(_.fullName)) ,
			AttrBindParam("imageurl",book.imageurl.is match { 
						case("") => "/images/nocover.gif";
						case _ => book.imageurl.is }, "src"),
			AttrBindParam("detail", detailPage + book.id.is, "href") 
			)
      }
   }

   Helpers.bind("books", in, "list" -> bindBooks _)
  }

  def detail(in: NodeSeq) : NodeSeq = {
    val id = S.param("id") openOr ""
    Book.findByKey(id.toLong) match {

      case(Full(book)) =>
        SHtml.ajaxForm(
          Helpers.bind("book", in,
                "title" -> book.title.is,
                "author" -> StringHelper.listToString(book.authors.get.map(_.fullName)) ,
                "isbn" -> book.isbn.is,
                "publisher" -> book.publisher.is,
                "published" -> book.publishedYear,
                AttrBindParam("imageurl",book.imageurl.is match {
                      case("") => "/images/nocover.gif";
                      case _ => book.imageurl.is }, "src"),
                "add" -> addButton(book),
                "enhance" -> enhanceButton(book)
            )
          )

      case _ =>
        <p> no book found with this id</p>
    }
  }

  def enhanceByIsbn(isbn : String) : Book = {
    val eb = new EnhanceBook
    eb.enhanceBookByIsbn(isbn)
  }

  def addToCollection(book : Book) : JsCmd = {
    if (User.loggedIn_?) {
      BookUser.find(By(BookUser.user, User.currentUser), By(BookUser.book, book)) match {
        case (Full(bookUser)) => {
          Alert("Book already part of your collection")
        }
        case (_) => {
          BookUser.create.user(User.currentUser).book(book).save
          Alert("added this book to your collection")
        }
      }
    } else {
      Alert("you need to be logged in")
    }
  }
}

}


