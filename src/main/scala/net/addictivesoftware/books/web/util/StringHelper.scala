package net.addictivesoftware.books.web.util {

  import scala.xml.{Group, Unparsed}
  import net.liftweb.util.Log


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

  }

}
