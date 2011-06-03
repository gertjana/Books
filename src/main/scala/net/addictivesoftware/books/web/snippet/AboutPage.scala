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
package net.addictivesoftware.books.web.snippet {
   
    import scala.xml.{NodeSeq, Text, Unparsed}
    import net.liftweb.util._
    import net.liftweb.common._
    import net.liftweb.mapper._
    import Helpers._
    import net.addictivesoftware.books.web.model._
    import net.addictivesoftware.books.web.util._
    import net.addictivesoftware.books.web.lib._
    
    class Aboutpage {

      def container(html : NodeSeq): NodeSeq = {
        val licenseContent = Content.find(By(Content.key, "about_license")) openOr Content.emptyContent
        val sourcesContent = Content.find(By(Content.key, "about_sources")) openOr Content.emptyContent
        val generalContent = Content.find(By(Content.key, "about_general")) openOr Content.emptyContent
        val contactContent = Content.find(By(Content.key, "about_contact")) openOr Content.emptyContent
        Helpers.bind("text", html, 
           "general_title" -> generalContent.title.is,
           "general_text" -> StringHelper.unescapeXml(generalContent.summary.is),
           "license_title" -> licenseContent.title.is,
           "license_text" ->  StringHelper.unescapeXml(licenseContent.summary.is),
           "sources_title" -> sourcesContent.title.is,
           "sources_text" -> StringHelper.unescapeXml(sourcesContent.summary.is),
           "contact_title" -> contactContent.title.is,
           "contact_text" ->  StringHelper.unescapeXml(contactContent.summary.is)
        )
      }

      
    }
}
