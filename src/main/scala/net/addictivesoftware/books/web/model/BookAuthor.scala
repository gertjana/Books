package net.addictivesoftware.books.web.model {

  import net.liftweb.mapper._

  class BookAuthor extends LongKeyedMapper[BookAuthor] with IdPK {
    def getSingleton = BookAuthor
    object book extends MappedLongForeignKey(this, Book)
    object author extends MappedLongForeignKey(this, Author)
  }

  object BookAuthor extends BookAuthor with LongKeyedMetaMapper[BookAuthor] {
    def join(book : Book, author : Author) =
      this.create.book(book).author(author).save
  }
}