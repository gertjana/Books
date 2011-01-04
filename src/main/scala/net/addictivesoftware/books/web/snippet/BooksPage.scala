package net.addictivesoftware.books.web.snippet {

import scala.xml.{NodeSeq, Text}
import net.liftweb._
import common._
import http._
import util._
import net.addictivesoftware.books.web.util._
import Helpers._
import net.addictivesoftware.books.web.model._
import mapper._
import net.liftweb.http.PaginatorSnippet

class Bookspage extends PaginatorSnippet[Book] {
  override def count = Book.count
  override def page = Book.findAll(StartAt(curPage*itemsPerPage), MaxRows(itemsPerPage))

  def detailPage = "book?id=";

  def button(book : Book) : NodeSeq = {
    User.loggedIn_? match {
      case (true) => (SHtml.hidden(() => addToCollection(book)) ++ <input type="submit" value="add"/>)
      case (_) => Text("")
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
                "add" -> button(book)
          )
        )

      case _ =>
        <p> no book found with this id</p>
    }
  }

  def addToCollection(book : Book) = {
    if (User.loggedIn_?) {
      BookUser.find(By(BookUser.user, User.currentUser), By(BookUser.book, book)) match {
        case (Full(bookuser)) => {
          S.warning("Book already part of your collection")
        }
        case (_) => {
          BookUser.create.user(User.currentUser).book(book).save
          S.warning("added this book to your collection")
        }
      }
    } else {

    }
  }

}

}


