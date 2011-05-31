package net.addictivesoftware.books.web.comet

import net.liftweb._
import http._
import util._
import java.util.Date
import TimeHelpers._
import common._
import js.JsCmds.SetHtml
import scala.xml.Text

class Clock extends CometActor {
  override def defaultPrefix = Full("clk")

  def render = bind("time" -> timeSpan)

  def timeSpan = (<span id="time">{timeNow}</span>)

  // schedule a ping every 10 seconds so we redraw
  ActorPing.schedule(this, Tick, 10000L)

  override def lowPriority : PartialFunction[Any, Unit] = {
    case Tick => {
      //println("Got tick " + new Date());
      partialUpdate(SetHtml("time", Text(timeNow.toString)))
      // schedule an update in 10 seconds
      ActorPing.schedule(this, Tick, 10000L)
    }
  }
}
case object Tick

