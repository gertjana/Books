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