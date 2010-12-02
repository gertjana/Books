package net.addictivesoftware.books.web.snippet {

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.mapper._
import _root_.java.util.Date
import net.addictivesoftware.books.web.lib._
import net.addictivesoftware.books.web.util._
import net.addictivesoftware.books.web.model._
import Helpers._

class Homepage {

  def userinfo(in: NodeSeq): NodeSeq = {
    if(User.loggedIn_?) 
      Helpers.bind("user", in, "name" -> User.currentUser.map(_.toString).open_!)
    else
      Helpers.bind("user", in, "name" -> "anonymous")
  }

  def news(in: NodeSeq) : NodeSeq = {
    val articles = News.findAll(
            By(News.showOnHomepage, true), 
            OrderBy(News.createdAt, Descending), 
            MaxRows[News](5))
    
    def bindArticles(template: NodeSeq): NodeSeq = {
      articles.flatMap {
                case (news) => bind("article", template, 
							"title" -> news.title, 
							"author" -> news.author, 
							"summary" -> StringHelper.unescapeXml(news.summary),
							"created" -> news.createdAt,
							"updated" -> news.updatedAt)
      }
   }

   Helpers.bind("news", in, "articles" -> bindArticles _)
  }

}

}
