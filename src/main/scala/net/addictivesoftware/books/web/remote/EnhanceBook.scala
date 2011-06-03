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


  import net.addictivesoftware.books.web.model.Book

  class EnhanceBook {

    def enhanceBookByIsbn(isbn : String) : Book = {
      val book = Book.create.isbn(isbn)

      val client = new AmazonApiClient()
      val result = client.getBookInformationForIsbn(isbn)
      val isValid = result\"ItemLookupResponse"\"Items"\"Request"\"IsValid"
      if (isValid.text.equalsIgnoreCase("true")) {
        val itemAttributes = result\"ItemLookupResponse"\"Items"\"ItemAttributes"
        val titleNode = itemAttributes\"Title"
        val publisherNode = itemAttributes\"Publisher"

        book.title(titleNode.text)
        book.publisher(publisherNode.text)
      }
      return book
    }
  }

}