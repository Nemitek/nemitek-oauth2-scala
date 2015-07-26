package com.nemitek.oauth2

import akka.actor.Actor
import spray.httpx.{SprayJsonSupport, Json4sSupport}
import spray.json.DefaultJsonProtocol
import spray.routing._
import spray.http._
import MediaTypes._



// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class AuthServiceActor extends Actor with AuthService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

//grant_type=password&username=USERNAME&password=PASSWORD
case class PasswordTokenRequest(grant_type: String, username: String, password: String)

case class TokenRequest(grant_type: String, username: String, password: String, client_id: String, client_secret: String, refresh_token: String)

//object PersonJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
//  implicit val PortofolioFormats = jsonFormat2(PasswordTokenRequest)
//}

// this trait defines our service behavior independently from the service actor
trait AuthService extends HttpService {

  val myRoute =
    path("token") {
      post {
        entity(as[TokenRequest]) {
          // token?grant_type=password&username=USERNAME&password=PASSWORD&client_id=CLIENT_ID
          case ("password", _, _, _, _, _) => respondWithMediaType(`text/html`) {
            // XML is marshalled to `text/xml` by default, so we simply override here
            complete {
              <html>
                <body>
                  <h1>Say hello to
                    <i>spray-routing</i>
                    on
                    <i>spray-can</i>
                    !</h1>
                </body>
              </html>
            }
          }
          case _ => respondWithMediaType(`text/html`) {
            // XML is marshalled to `text/xml` by default, so we simply override here
            complete {
                    "Unsupported grant type."
            }
          }
        }
      }
    }
}