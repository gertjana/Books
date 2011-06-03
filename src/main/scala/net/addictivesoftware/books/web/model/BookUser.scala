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