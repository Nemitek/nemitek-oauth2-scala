package com.nemitek.oauth2

import akka.io.IO
import spray.can.Http
import spray.http._
import akka.actor._
import akka.pattern.ask
import spray.routing.SimpleRoutingApp
import scala.xml.NodeSeq
import scala.concurrent.duration._
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Await

object AuthorizationServer extends App {
  // we need an ActorSystem to host our application in??????
  implicit val system = ActorSystem("nemitek-oauth2-authorization-server")

//  val commodities = system.actorOf(Props[], "commodities")

  // create and start our service actor
  val service = system.actorOf(Props[AuthServiceActor], "connections")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "0.0.0.0", port = 8080)
}