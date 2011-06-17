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

import scala.xml._

object UserRestApi extends RestHelper with RestUtils {
  serve {
    //all books for the current user
    case "api" :: key :: "user" :: "books" :: "all" :: _ XmlGet _ => {
      <Books>
        {
           Book.findAll(In(Book.id ,BookUser.book, By(BookUser.user, getUserIdFromKey(key))))
              .map(book => book.toXml);
        }
      </Books>
    }
    case "api" :: key :: "user" :: "books" :: "all" :: _ JsonGet _ => {
      JsonWrapper("books",
        Book.findAll(In(Book.id ,BookUser.book, By(BookUser.user, getUserIdFromKey(key))))
            .map(book => Book.toJSON(book))
      )
    }
    case "api" :: key :: "user" :: "books" :: AsLong(id) :: _ XmlGet _ => {
      Book.find(By(Book.id, id), In(Book.id ,BookUser.book, By(BookUser.user, getUserIdFromKey(key)))) match {
        case Full(book) => {
          Book.toXML(book)
        }
        case (_) => {
          XmlResponse(errorNode("book with this id not found for current user"), 402, "application/xml", Nil);
        }
      }
    }
    case "api" :: key :: "user" :: "books" :: AsLong(id) :: _ JsonGet _ => {
      Book.find(By(Book.id, id), In(Book.id ,BookUser.book, By(BookUser.user, getUserIdFromKey(key)))) match {
        case Full(book) => {
          Book.toJSON(book)
        }
        case (_) => {
          XmlResponse(errorNode("book with this id not found for current user"), 402, "application/xml", Nil);
        }
      }
    }
    case "api" :: key :: "user" :: "authors" :: "all" :: _ XmlGet _ => {
      var id = getUserIdFromKey(key)
      <Authors> {
        Book.findAll(In(Book.id ,BookUser.book, By(BookUser.user, id)))
          .map(book => { book.authors.get })
          .foldLeft(List[Author]())(_ ++ _)
          .distinct
          .sortWith((author1:Author, author2:Author) => author1.lastName.is < author2.lastName.is)
          .map(author => Author.toXML(author, id))
        }
      </Authors>
    }
    case "api" :: key :: "user" :: "authors" :: "all" :: _ JsonGet _ => {
      var id = getUserIdFromKey(key);
      JsonWrapper ("authors", {
        Book.findAll(In(Book.id ,BookUser.book, By(BookUser.user, id)))
          .map(book => { book.authors.get })
          .foldLeft(List[Author]())(_ ++ _)
          .distinct
          .sortWith((author1:Author, author2:Author) => author1.lastName.is < author2.lastName.is)
          .map(author => Author.toJSON(author, id))
        })
    }

    case "api" :: key :: "user" :: "authors" :: AsLong(id) :: "books" :: _ XmlGet _ => {
      <Books> {
        Book.findAll(
                In(Book.id, BookUser.book, By(BookUser.user, getUserIdFromKey(key))),
                In(Book.id, BookAuthor.book, By(BookAuthor.author, id)))
            .map(book => book.toXml)
      }
      </Books>
    }
    case "api" :: key :: "user" :: "authors" :: AsLong(id) :: "books" :: _ JsonGet _ => {
      JsonWrapper("books", {
        Book.findAll(
                In(Book.id, BookUser.book, By(BookUser.user, getUserIdFromKey(key))),
                In(Book.id, BookAuthor.book, By(BookAuthor.author, id)))
            .map(book => Book.toJSON(book))
      })
    }
  }
}

