package net.addictivesoftware.books.web.rest

import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.http.auth._
import net.addictivesoftware.books.web.model._
import net.liftweb.common._
import net.liftweb.mapper._
import net.liftweb.util.Helpers.AsLong
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
//import net.liftweb.json.JsonAST._

import scala.xml.Node

object BookRestApi extends RestHelper  {
  serve {
    // single book
    case "api" :: key :: "books" :: AsLong(id) :: _ XmlGet _=>
        {
          Book.findByKey(id) match {
            case Full(book) => {Book.toXML(book)}
            case (_) => NotFoundResponse("Book with id " + id + " not found\r\n")
          }
        }
    case "api" :: key :: "books" :: AsLong(id) :: _ JsonGet _=>
        {
          Book.findByKey(id) match {
            case Full(book) => {Book.toJSON(book)}
            case (_) => NotFoundResponse("Book with id " + id + " not found\r\n")
          }
        }

    // list of books
    case "api" :: key :: "books" :: "all" :: _ XmlGet _=>
      {
        <Books>
          {
            Book.findAll.map(book => book.toXml)
          }
        </Books>
      }
    case "api" :: key :: "books" :: "all" :: _ JsonGet _=>
      {
        JsonWrapper("books", Book.findAll.map(book => Book.toJSON(book)))
      }

   // list of author books
    case "api" :: key :: "authors" :: AsLong(id) :: "books" :: _ XmlGet _ =>
        <Books>
          {
            Book.findAll(In(Book.id ,BookAuthor.book, By(BookAuthor.author, id))).map(book => Book.toXML(book))
          }
        </Books>
    case "api" :: key :: "authors" :: AsLong(id) :: "books" :: _ JsonGet _ =>
        JsonWrapper("books",
            Book.findAll(In(Book.id ,BookAuthor.book, By(BookAuthor.author, id))).map(book => Book.toJSON(book))
        )

    // list of authors
    case "api" :: key :: "authors" :: "all" :: _ XmlGet _=>
      <Authors>
        {
          Author.findAll(OrderBy(Author.lastName, Ascending)).map(author => Author.toXML(author))
        }
      </Authors>
    case "api" :: key :: "authors" :: "all" :: _ JsonGet _=>
      JsonWrapper("authors",
        Author.findAll(OrderBy(Author.lastName, Ascending)).map(author => Author.toJSON(author))
      )

    // single author
    case "api" :: key :: "authors" :: AsLong(id) :: _ XmlGet _ =>
        {
          Author.findByKey(id) match {
            case Full(author) => {Author.toXML(author)}
            case (_) => NotFoundResponse("Author with id " + id + " not found\r\n")
          }
        }
    case "api" :: key :: "authors" :: AsLong(id) :: _ JsonGet _ =>
        {
          Author.findByKey(id) match {
            case Full(author) => {Author.toJSON(author)}
            case (_) => NotFoundResponse("Author with id " + id + " not found\r\n")
          }
        }
    case "api" :: key :: "user" :: "books" :: "all" :: _ XmlGet _ =>
        {
          <TODO />
        }
    case "api" :: key :: "user" :: "books" :: "all" :: _ JsonGet _ =>
        {
          <TODO />
        }
    case "api" :: "auth" :: user :: pass :: _ XmlGet _ => {
          User.find(By(User.email, user)) match {
            case Full(user) =>
              if (user.validated && user.password.match_?(pass)) {
                <key>{user.apiKey.is}</key>
              } else {
                ResponseWithReason(ForbiddenResponse(""), "failed to authenticate");
              }

            case (_) => ResponseWithReason(UnauthorizedResponse(""), "failed to authenticate");
          }
        }
    case "api" :: "auth" :: user :: pass :: _ JsonGet _ =>
        {
           User.find(By(User.email, user)) match {
            case Full(user) =>
              if (user.validated && user.password.match_?(pass)) {
                JsonWrapper("key", user.apiKey.is);
              } else {
                ResponseWithReason(ForbiddenResponse(""), "failed to authenticate");
              }

            case (_) => ResponseWithReason(UnauthorizedResponse(""), "failed to authenticate");
          }
        }

  }

  def JsonWrapper(name : String, content : JValue) : JValue = {
    (name -> content)
  }

  def protection(roles : Role) : LiftRules.HttpAuthProtectedResourcePF = {
    case Req(List("api", "books"), _, GetRequest) => roles.getRoleByName("reader")
    case Req(List("api", "authors"), _, GetRequest) => roles.getRoleByName("reader")
    case (_) => Empty
  }
}

