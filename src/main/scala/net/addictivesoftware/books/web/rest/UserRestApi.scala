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

object UserRestApi extends RestHelper  {
  serve {
    case "api" :: key :: "user" :: "books" :: "all" :: _ XmlGet _ =>
        {
          <TODO />
        }
    case "api" :: key :: "user" :: "books" :: "all" :: _ JsonGet _ =>
        {
          <TODO />
        }
    case "api" :: "auth" :: email :: password :: _ XmlGet _ => {
      User.find(By(User.email, email)) match {
        case Full(u) =>
          if (u.validated && u.password.match_?(password)) {
            <key>{u.uniqueId.is}</key>
          } else {
            XmlResponse(errorNode("failed to authenticate or user is not validated"), 402, "application/xml", Nil);
          }

        case (_) =>
          XmlResponse(errorNode("unknown email"), 402, "application/xml", Nil);

      }
    }
    case "api" :: "auth" :: user :: pass :: _ JsonGet _ =>
        {
           User.find(By(User.email, user)) match {
            case Full(user) =>
              if (user.validated && user.password.match_?(pass)) {
                JsonWrapper("key", user.uniqueId.is);
              } else {
                JsonResponse(JsonWrapper("error", "failed to authenticate or user is not validated"), Nil, Nil, 402);
              }

            case (_) =>
              JsonResponse(JsonWrapper("error", "unknown email"), Nil, Nil, 402);
          }
        }

  }

  def JsonWrapper(name : String, content : JValue) : JValue = {
    (name -> content)
  }

  def errorNode(text : String) : Elem = {
    <error>{text}</error>
  }

}

