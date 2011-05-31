package net.addictivesoftware.books.web.comet {

  import net.liftweb._
  import http._
  import common._
  import js.JsCmds.SetHtml
  import scala.xml.Text

class EnhanceBook extends CometActor {
    override def defaultPrefix = Full("enhancebook")

    //def isbn = S.param("isbn") openOr "0"
    //def book = Book.find(By(Book.isbn, "9780446343633")) openOr new Book

    def title = (<span id="enhancedtitle">?</span>)

    def render = {
      bind(
          "title" -> title
        )
    }

    override def lowPriority : PartialFunction[Any, Unit] = {
      case IsbnMessage => {
        println("Got message")
        partialUpdate(SetHtml("enhancedtitle", Text("updated title")))
      }
    }
  }

  case object IsbnMessage {}

}


