package com.next.nivell.alerts

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.next.nivell.alerts.model.Alert

class AlertsLogger extends Actor {

  override def receive: Receive =  {

    case message: Alert => {
      println(message)
    }
    case _ => println("Unhandled message")
  }
}
