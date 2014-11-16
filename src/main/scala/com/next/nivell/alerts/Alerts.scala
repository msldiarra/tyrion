package com.next.nivell.alerts

import akka.actor.{Actor, ActorSystem, Props}
import com.next.nivell.alerts.model.Alert
import spray.http.MediaTypes._
import spray.routing._


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class AlertsActor extends Actor with Alerts {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(routes)
}


// this trait defines our service behavior independently from the service actor
trait Alerts extends HttpService {

  val actorSystem = ActorSystem()

  lazy val logger = actorSystem.actorOf(Props[AlertsLogger], "alerts-logger")

  def uuid = java.util.UUID.randomUUID

  val routes =
    put {
      path("alert") {
        detach() {
          respondWithMediaType(`application/json`) {
            parameters('tankReference, 'level.as[Float]) { (tankReference, level) =>
              val alert = new Alert(uuid, tankReference, level)
              logger ! alert
              complete(s"OK")
            }
          }
        }
      }
    }
}