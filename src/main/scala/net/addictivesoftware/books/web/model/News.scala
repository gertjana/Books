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

import net.liftweb._
import http._
import mapper._
import common.{Box, Full, Empty}
import sitemap.Loc._

class News extends LongKeyedMapper[News] with IdPK with CreatedUpdated {
  def getSingleton = News

  object title extends MappedString(this, 100)
  object author extends MappedString(this, 100)
  object summary extends MappedTextarea(this, 4000)
  object showOnHomepage extends MappedBoolean(this)

}

object News extends News with LongKeyedMetaMapper[News] with CRUDify[Long, News] {
	override def editMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.editMenuLocParams
	override def viewMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.viewMenuLocParams
	override def createMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.createMenuLocParams
	override def showAllMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.showAllMenuLocParams
}


}

