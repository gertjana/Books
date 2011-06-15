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

object UserAddRestApi extends MyRestHelper  {
  serve {
    case "api" :: key :: "user" :: "authors" :: "add" :: _ JsonPut json -> _ => {
      JsonWrapper("author", Author.toJSON(Author.updateFromJSON(Author.create, json).saveMe));
    }
    case "api" :: key :: "user" :: "books" :: "add" :: _ JsonPut json -> _ => {
      JsonWrapper("book", Book.toJSON(Book.updateFromJSON(Book.create, json).saveMe));
    }
  }
}

