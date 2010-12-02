package net.addictivesoftware.books.web.util {

  import scala.xml.{Group, Unparsed}

  object StringHelper extends StringHelper

  class StringHelper {

    def unescapeXml(text: String): Group = {
      <xml:group>{
        Unparsed(text)
      }</xml:group>
    }

  }

}
