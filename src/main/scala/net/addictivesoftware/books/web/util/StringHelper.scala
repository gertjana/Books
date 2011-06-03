/*
Copyright [2011] [Addictive Software]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
