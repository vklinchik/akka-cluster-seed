package akkaseed.client

import java.util.concurrent.atomic.AtomicInteger

import scala.concurrent.duration._
import akka.actor.{ActorSystem, Props}

import scala.io.StdIn

object ClientApp {

  def main(args : Array[String]) {
    val system = ActorSystem("AkkaSeedSenderSystem")
    val sender = system.actorOf(Props[Sender], name = "sender")

    val counter = new AtomicInteger
    import system.dispatcher
    system.scheduler.schedule(2.seconds, 2.seconds) {
      sender ! SendMessage(counter.incrementAndGet())
      Thread.sleep(1000)
    }

    StdIn.readLine()
    system.terminate()
  }

}
