package net.addictivesoftware.books.web.snippet {

import net.liftweb._
import common._
import http._
import mapper.By
import util._
import Helpers._
import xml.{Text, NodeSeq}
import net.addictivesoftware.books.web.model.Book

  class ImportIsbn {
    var count = 0;
    var saveMsg:String = "added: " + count + " books "
    var msg = "Enter a list of isbn nr's in here seperated by spaces"
    var isbnnrs = ""
    var m2 = S.attr("#m2") openOr "m2"

    def bulk(in: NodeSeq) : NodeSeq = {
      var alreadyInDb : String = "";

      def processIsbnNrs() = {
        isbnnrs = ",".r.replaceAllIn(isbnnrs, " ")
        isbnnrs split " " map {addBook _}
      }

      def addBook(nr : String) {
        Book.find(By(Book.isbn, nr)) match {
          case (Full(book)) => alreadyInDb += nr
          case (_) => Book.create.isbn(nr).saveMe
        }
        msg = saveMsg + " skipped: " + alreadyInDb
        isbnnrs = ""
        S.warning(Text(msg))
      }

      SHtml.ajaxForm(
        bind("entry", in,
          "text" -> SHtml.textarea(isbnnrs, isbnnrs = _),
          "submit" -> (SHtml.hidden(processIsbnNrs) ++ <input type="submit" value="Add"/>))
      )
    }
  }
}


