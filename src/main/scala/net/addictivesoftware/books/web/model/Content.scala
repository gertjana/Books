package net.addictivesoftware.books.web.model {

import net.liftweb._
import http._
import mapper._
import sitemap.Loc._

class Content extends LongKeyedMapper[Content] with IdPK with CreatedUpdated {
  def getSingleton = Content

  object title extends MappedString(this, 100)
  object author extends MappedString(this, 100)
  object summary extends MappedTextarea(this, 4000)
  object key extends MappedString(this, 100)

  def emptyContent():Content = {
    var content = new Content
    content.title("Empty")
    return content
  }

}

object Content extends Content with LongKeyedMetaMapper[Content] with CRUDify[Long, Content] {
	override def editMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.editMenuLocParams
	override def viewMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.viewMenuLocParams
	override def createMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.createMenuLocParams
	override def showAllMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/user_mgt/login")) :: super.showAllMenuLocParams
}


}
