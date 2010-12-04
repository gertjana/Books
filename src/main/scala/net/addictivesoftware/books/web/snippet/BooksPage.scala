package net.addictivesoftware.books.web.snippet {

import scala.xml.{NodeSeq, Text}
import net.liftweb._
import http._
import util._
import common._
import _root_.java.util.Date
import net.addictivesoftware.books.web.lib._
import net.addictivesoftware.books.web.util._
import Helpers._
import net.addictivesoftware.books.web.model._
import mapper._
import net.liftweb.http.PaginatorSnippet

class Bookspage extends PaginatorSnippet[Book] {
  override def count = Book.count
  override def page = Book.findAll(StartAt(curPage*itemsPerPage), MaxRows(itemsPerPage))

  def detailPage = "book?id=";

  def list(in: NodeSeq) : NodeSeq = {
    
    def bindBooks(template: NodeSeq): NodeSeq = {
      page.flatMap {
                book => bind("book", template, 
			"title" -> book.title.is, 
			"author" -> book.author.obj.map(_.fullName).openOr("No Author") , 
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

      case(Full(book)) => Helpers.bind("book", in, 
			"title" -> book.title.is, 
			"author" -> book.author.obj.map(_.fullName).openOr("No Author") , 
			"isbn" -> book.isbn.is,
			"publisher" -> book.publisher.is,
			"published" -> book.publishedYear,
			AttrBindParam("imageurl",book.imageurl.is match { 
						case("") => "/images/nocover.gif";
						case _ => book.imageurl.is }, "src"))
      case _ => <p> no book found with this id</p>  
    }
  }


}

}


