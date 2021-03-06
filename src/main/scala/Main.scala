import java.io.File
import java.net.URL

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.language.postfixOps
import CrawlServer.{CrawlRequest, CrawlResponse}
import Database.{Entry, RetrieveEntry, Shutdown}

import scala.io.Source

object Main extends App {
  println(s"Current Time ${System.currentTimeMillis}")
  Utility.readFile()


  val system = ActorSystem();
//  val receptionist = system.actorOf(Props [CrawlServer], "CrawlServer")
//  val main = system.actorOf(Props[Main](new Main(receptionist, "https://en.wikipedia.org/wiki/Enrico_Fermi", 1)), "BBCActor")
  val database = system.actorOf(Props [Database],"Database")
//  database ! RetrieveEntry()
    database ! Shutdown()

  Thread.sleep(1000)
  system.terminate() //todo graceful shutdown
}


class Main(receptionist: ActorRef, url: String, depth: Integer) extends Actor {
  receptionist ! CrawlRequest(url, depth)
  def receive = {
    case CrawlResponse(root, links) =>
      println(s"Root: $root")
      println(s"Links: ${links.toList.sortWith(_.length < _.length).mkString("\n")}")
      println("=========")
      println(s"Current Time ${System.currentTimeMillis}")
  }
}