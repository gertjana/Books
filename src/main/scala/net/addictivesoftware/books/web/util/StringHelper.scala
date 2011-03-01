package net.addictivesoftware.books.web.util {

  import scala.xml.{Group, Unparsed}
  import net.liftweb.util.Log
  import _root_.java.util.Date
  import _root_.java.text.SimpleDateFormat

  object StringHelper extends StringHelper

  class StringHelper {

    def unescapeXml(text: String): Group = {
      <xml:group>{
        Unparsed(text)
      }</xml:group>
    }

    def listToString(list:List[String]) = {
      list match {
        case head::tail => tail.foldLeft(head)(_ + ", " + _)
        case Nil => ""
      }
    }

    def dateToString(date : Date) {
      def timestamp = new SimpleDateFormat("yyyy-MM-dd’T’HH:mm:ss’Z’")

      timestamp.format(date)

    }
  }

}
