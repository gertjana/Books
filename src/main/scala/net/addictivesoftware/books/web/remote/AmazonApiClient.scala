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