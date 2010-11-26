package code {
package snippet {

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import code.lib._
import Helpers._
import code.model._
import _root_.net.liftweb.mapper._

class Homepage {

  def userinfo(in: NodeSeq): NodeSeq = {
    if(User.loggedIn_?) 
      Helpers.bind("user", in, "name" -> User.currentUser.map(_.toString).open_!)
    else
      Helpers.bind("user", in, "name" -> "anonymous")
  }

  def news(in: NodeSeq) : NodeSeq = {
    val articles = News.findAll(By(News.showOnHomepage, true), OrderBy(News.createdAt, Descending), MaxRows[News](5))
    
    def bindArticles(template: NodeSeq): NodeSeq = {
      articles.flatMap {
                case (news) => bind("article", template, 
							"title" -> news.title, 
							"author" -> news.author, 
							"summary" -> news.summary,
							"created" -> news.createdAt,
							"updated" -> news.updatedAt)
      }
   }

   Helpers.bind("news", in, "articles" -> bindArticles _)
  }

}

}
}
