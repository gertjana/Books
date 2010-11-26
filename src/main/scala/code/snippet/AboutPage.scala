package code {
  package snippet {
   
    import _root_.scala.xml.{NodeSeq, Text, Unparsed}
    import _root_.net.liftweb.util._
    import _root_.net.liftweb.common._
    import code.lib._
    import Helpers._
    import code.model._
    import code.util._
    import _root_.net.liftweb.mapper._
    
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
}
