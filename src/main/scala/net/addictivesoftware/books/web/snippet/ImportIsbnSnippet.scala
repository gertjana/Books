package net.addictivesoftware.books.web.snippet {

import net.liftweb._
import common._
import http._
import mapper.By
import util._
import Helpers._
import xml.{Text, NodeSeq}
import net.addictivesoftware.books.web.model.{Book, BookUser, User}

  class Importisbn {
    var count = 0
    var skipped = 0
    var notvalid = 0
    var isbnNumbers = ""

    def loggedInUser = User.currentUser

    def form(in: NodeSeq) : NodeSeq = {
      var alreadyInDb : String = "";

      def processIsbnNrs() = {
        isbnNumbers = ",".r.replaceAllIn(isbnNumbers, " ")
        isbnNumbers = "\r\n".r.replaceAllIn(isbnNumbers, " ")
        isbnNumbers = "\r".r.replaceAllIn(isbnNumbers, " ")
        isbnNumbers = "\n".r.replaceAllIn(isbnNumbers, " ")
        isbnNumbers = "\t".r.replaceAllIn(isbnNumbers, " ")

        isbnNumbers split " " map {addBook _}
        S.warning(Text("added: " + count + " , skipped: " + skipped + " , notvalid: " + notvalid))

      }

      def addBook(nrAsText : String) {

        var nr : Long = nrAsText match {
          case Long(x) => x
          case _ => 0
        }

        if (nr != 0) {
          Book.find(By(Book.isbn, nrAsText)) match {
            case (Full(book)) => {
              addBookUser(book)
              skipped += 1
            }
            case (_) => {
              val book = Book.create.isbn(nrAsText)
              book.save
              BookUser.create.book(book).user(loggedInUser).save
              count += 1
            }
          }
        } else {
          notvalid += 1
        }
      }


      def addBookUser(book : Book) {
        BookUser.find(By(BookUser.book, book), By(BookUser.user, loggedInUser)) match {
          case (Full(BookUser)) => // book exists and is associated with user
          case (_) => BookUser.create.book(book).user(loggedInUser).save
        }
      }

      bind("entry", in,
        "text" -> SHtml.textarea(isbnNumbers, isbnNumbers = _),
        "submit" -> SHtml.submit("Add", processIsbnNrs))
    }
  }

  object Long {
    def unapply(s : String) : Option[Long] = try {
      Some(s.toLong)
    } catch {
      case _ => None
    }
  }

}


