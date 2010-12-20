package net.addictivesoftware.books.web.snippet {

import net.liftweb._
import common._
import http._
import mapper.By
import util._
import Helpers._
import js.JsCmds.SetHtml
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
        Log.info("posted   :"+isbnnrs)
        isbnnrs = ",".r.replaceAllIn(isbnnrs, " ")
        Log.info("sanitized:"+isbnnrs)
        isbnnrs split " " map {addBook _}
      }

      def addBook(nr : String) {
        Book.find(By(Book.isbn, nr)) match {
          case (Full(book)) =>
          {
            SetHtml("#m", Text(nr + " is already in db"))
            alreadyInDb += nr
          }
          case (_) =>
          {
            Log.info("addding " + nr)
            Book.create.isbn(nr).saveMe
            SetHtml("#m", Text("added "+nr))
          }
        }
        msg = saveMsg + " skipped: " + alreadyInDb
        isbnnrs = ""
        SetHtml("#m", Text(msg))
        Log.info("end")
      }

      SetHtml("#m", Text("bla"))
      SetHtml("m", Text("bla"))
      SetHtml("m", Text("bla"))

      SHtml.ajaxForm(
        bind("entry", in,
          "text" -> SHtml.textarea(isbnnrs, isbnnrs = _),
          "submit" -> (SHtml.hidden(processIsbnNrs) ++ <input type="submit" value="Add"/>))
      )
    }
  }
}


