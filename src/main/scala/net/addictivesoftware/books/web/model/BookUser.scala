package net.addictivesoftware.books.web.model {

  import net.liftweb.mapper._

  class BookUser extends LongKeyedMapper[BookUser] with IdPK {
    def getSingleton = BookUser
    object book extends MappedLongForeignKey(this, Book)
    object user extends MappedLongForeignKey(this, User)
  }

  object BookUser extends BookUser with LongKeyedMetaMapper[BookUser] {
    def join(book : Book, user : User) =
      this.create.book(book).user(user).save
  }
}