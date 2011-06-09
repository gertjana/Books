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

object UserPutRestApi extends RestHelper  {
  serve {
    case "api" :: key :: "user" :: "books" :: "add" :: AsLong(bookId) :: _ XmlGet _ => {
      var userId = getUserIdFromKey(key);
      BookUser.find(By(BookUser.user, userId), By(BookUser.book, bookId)) match {
        case Full(bu) => {
          XmlResponse(errorNode("book is already part of your collection"), 409, "application/xml")
        }
        case (_) => {
          <id>{
            BookUser.create.user(userId).book(bookId).saveMe.id;
            }</id>
        }
      }
    }
    case "api" :: key :: "user" :: "books" :: "add" :: AsLong(bookId) :: _ JsonGet _ => {

      var userId = getUserIdFromKey(key);
      BookUser.find(By(BookUser.user, userId), By(BookUser.book, bookId)) match {
        case Full(bu) => {
          XmlResponse(errorNode("book is already part of your collection"), 409, "application/xml")
        }
        case (_) => {
          JsonWrapper("id", BookUser.create.user(userId).book(bookId).saveMe.id.is);
        }
      }
    }
  }

  def JsonWrapper(name : String, content : JValue) : JValue = {
    (name -> content)
  }

  def errorNode(text : String) : Elem = {
    <error>{text}</error>
  }

  def getUserIdFromKey(key:String) : Long = {
      User.find(By(User.uniqueId, key)) match {
        case Full(user) => user.id
        case (_) => 0
      }
  }
}

