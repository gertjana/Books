package code {
package model {

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
}

