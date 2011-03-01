package net.addictivesoftware.books.web.snippet {

import scala.xml.{NodeSeq}
import net.liftweb._
import common._
import http._
import util._
import net.addictivesoftware.books.web.util._
import Helpers._
import net.addictivesoftware.books.web.model._
import mapper._
import net.liftweb.http.PaginatorSnippet

class Mybookspage extends PaginatorSnippet[Book] {
  val userId : String = User.currentUserId openOr "0"

  override def page = Book.findAll(
                            In(Book.id ,BookUser.book, By(BookUser.user, userId.toLong)),
                            StartAt(curPage*itemsPerPage),
                            MaxRows(itemsPerPage))

  override def count = Book.findAll(In(Book.id, BookUser.book, By(BookUser.user, userId.toLong))).size

  def detailPage = "book?id=";

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
                      case _ => book.imageurl.is }, "src")
          )
        )

      case _ =>
        <p> no book found with this id</p>
    }
  }

}

}


