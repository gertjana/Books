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
import net.liftweb.common._

class User extends MegaProtoUser[User] {
  def getSingleton = User

  override def toString = firstName + " " + lastName

  object apiKey extends MappedString(this, 100) {
    override def defaultValue = net.liftweb.util.Helpers.md5(email)
  }
}

object User extends User with MetaMegaProtoUser[User] {
  override def dbTableName = "users" // define the DB table name
  override def screenWrap = Full(<lift:surround with="default" at="content">
			       <lift:bind /></lift:surround>)
  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email,
  locale, timezone, password, apiKey)

  override def skipEmailValidation = true
 
  override def lostPasswordMenuLoc = Empty
  
}



}
