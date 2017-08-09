package mindriot.akkaseed.client

import akka.actor.{Actor, ActorLogging, ActorPath}
import akka.cluster.client.{ClusterClient, ClusterClientSettings}
import akka.pattern.Patterns
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import akkaseed.Protocol.{SampleTask, TaskFailed, TaskResult}
import akkaseed.RandomString

class Sender extends Actor with ActorLogging with RandomString {

  val initialContacts = Set(
    ActorPath.fromString("akka.tcp://AkkaSeedSystem@127.0.0.1:2551/system/receptionist")
  )

  val settings = ClusterClientSettings(context.system).withInitialContacts(initialContacts)

  val client = context.system.actorOf(ClusterClient.props(settings), "sender")

  def receive = {
    case TaskResult(result) => log.debug(s"Client response $result")

    case SendMessage(id) => {
      val job = SampleTask(generate + id)
      implicit val timeout = Timeout(5 seconds)
      val result = Patterns.ask(client, ClusterClient.Send("/user/master", job, localAffinity = true), timeout)

      result.onComplete {
        case Success(taskResult) => {
          ///println(s"Client saw result: $transformationResult")
          self ! taskResult
        }
        case Failure(t) => println("An error has occured: " + t.getMessage)
      }
    }
  }
}