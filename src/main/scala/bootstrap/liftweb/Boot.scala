package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import sitemap._
import Loc._
import mapper._

import net.liftweb.http.auth.{AuthRole,HttpBasicAuthentication,userRoles}


import net.addictivesoftware.books.web.model._
import net.addictivesoftware.books.web.rest._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    //if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = 
	      new StandardDBVendor(
            Props.get("db.driver") openOr "org.h2.Driver",
			      Props.get("db.url") openOr "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
			      Props.get("db.user"),
            Props.get("db.password")
        )

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    //}

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User, Book, Author, News, Content, BookAuthor, BookUser)

    // where to search snippet
    LiftRules.addToPackages("net.addictivesoftware.books.web")

    // Build SiteMap
    val homeLoc = Loc("HomePage", "index" :: Nil, "Home Page", Hidden)
    val aboutLoc = Loc("AboutPage", "about" :: Nil, "About Page")
    val booksLoc = Loc("Bookspage", "book_list" :: Nil, "Books")
    val bookLoc = Loc("Bookpage", "book" :: Nil, "Book", Hidden)
    val authorsLoc = Loc("Authorspage", "author_list" :: Nil, "Authors")
    val authorLoc = Loc("Authorpage", "author" :: Nil, "Author", Hidden)
    val authorBookLoc = Loc("AuthorBookpage", "author_books" :: Nil, "AuthorBooks", Hidden)
    val importIsbnLoc = Loc("ImportIsbnPage", "import_isbn" :: Nil, "ImportIsbn", If(User.loggedIn_? _, ""))
    val myBooksLoc = Loc("MyBooksPage", "mybooks" :: Nil, "MyBooks", If(User.loggedIn_? _, ""))
    val enhanceLoc = Loc("EnhanceBook", "book_enhance" :: Nil, "EnhanceBook",  If(User.loggedIn_? _, ""))


    val clockLoc = Loc("Clock", "clock" :: Nil, "Clock")

    var menu = Menu(clockLoc) :: Menu(homeLoc) :: Menu(aboutLoc) :: Menu(booksLoc) ::
               Menu(bookLoc) :: Menu(authorsLoc) :: Menu(authorLoc) ::
               Menu(authorBookLoc) :: Menu(importIsbnLoc) :: Menu(myBooksLoc) :: Menu(enhanceLoc) ::
               User.sitemap
   
    val crudMenu = Book.menus ::: Author.menus ::: News.menus ::: Content.menus
    menu = menu ::: crudMenu
    
    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(menu:_*))

    //rest api hook up
    LiftRules.dispatch.append(BookRestApi)
    // Hook in our REST API auth

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Sets the output to HTML5

    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)


    // authentication setup
    val roles = AuthRole("admin",
                  AuthRole("writer",
                    AuthRole("reader")))

    //LiftRules.httpAuthProtectedResource.append(BookRestApi.protection(roles))


    LiftRules.authentication = HttpBasicAuthentication("Books") {
      case (userEmail, userPass, _) => {
        User.find(By(User.email, userEmail)).map { user =>
          if (user.password.match_?(userPass)) {
            println("API Key: " + S.param("key"))
            User.logUserIn(user)
            if (user.superUser) {
              userRoles(AuthRole("admin"))
            } else {
              userRoles(AuthRole("reader"))
            }
            true
          } else {
            false
          }
        } openOr {
          false
        }
      }
    }
  }
}
