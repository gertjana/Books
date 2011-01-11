package net.addictivesoftware.books.web.remote {

  import scala.xml._
  import scala.collection.immutable.TreeMap
  import java.net.URL

  class AmazonApiClient extends SignedRequestsAmazonApi {

    def getBookInformationForIsbn(isbn : String) : Elem = {
      var params = TreeMap.empty[String, String]
      params = params.insert("Operation", "ItemLookup")
      params = params.insert("ResponseGroup", "ItemAttributes")
      params = params.insert("ItemId", isbn)

      executeRequest(sign(params))
    }

    def getAuthorInformationForName(name : String) : Elem = {
      var params = TreeMap.empty[String, String]
      params = params.insert("Operation", "ItemSearch")
      params = params.insert("SearchIndex", "Books")
      params = params.insert("author", name) // ??

      executeRequest(sign(params))
    }


    def executeRequest(url : String) : Elem = {
      val conn = new URL(url).openConnection();
      XML.load(conn.getInputStream);
    }
  }
}