package net.addictivesoftware.books.web.snippet {

    import scala.xml.NodeSeq
    import net.liftweb._
    import http._
    import util._
    import common._
    import Helpers._
    import net.addictivesoftware.books.web.model._
    import mapper._
    import net.liftweb.http.PaginatorSnippet

    class Authorpage extends PaginatorSnippet[Author] {
        override def count = Author.count
        override def page = Author.findAll(StartAt(curPage*itemsPerPage),
                                           MaxRows(itemsPerPage),
                                           OrderBy(Author.lastName, Ascending))

        def authorsbookUrl = "author_books?id=";
        def bookUrl = "book?id="

        def author(in: NodeSeq) : NodeSeq = {
          var id = S.param("id") openOr ""
          Author.findByKey(id.toLong) match {
            case Full(author) => Helpers.bind("author", in, "fullname" -> author.fullName);
            case _ => <p>no author found with this id</p>
          }


        }

        def list(in: NodeSeq) : NodeSeq = {

            def bindAuthors(template: NodeSeq): NodeSeq = {
                page.flatMap {
                          author => bind("author", template,
                          "fullname" -> author.fullName,
                          "birthdate" -> author.birthDate,
                          AttrBindParam("detail", authorsbookUrl + author.id.is, "href"))
                }
            }

            Helpers.bind("authors", in, "list" -> bindAuthors _)
        }

        def books(in: NodeSeq) : NodeSeq = {
            val id = S.param("id") openOr ""
            val books = Book.findAll(By(Book.author, id.toLong), OrderBy(Book.title, Ascending))

            def bindBooks(template : NodeSeq): NodeSeq = {
                books.flatMap {
                          book => bind("book", template,
                                    "title" -> book.title.is,
                                    "author" -> book.author.obj.map(_.fullName).openOr("No Author") ,
                                    AttrBindParam("imageurl",book.imageurl.is match {
                                          case("") => "/images/nocover.gif";
                                          case _ => book.imageurl.is }, "src"),
                                    AttrBindParam("detail", bookUrl + book.id.is, "href")
                                  )
                }
            }
            Helpers.bind("books", in, "list" -> bindBooks _)
        }
    }
}