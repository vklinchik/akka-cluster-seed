package mindriot.akkaseed.master

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Cancellable, Props, Terminated}
import akka.util.Timeout
import akka.pattern.pipe
import akka.pattern.ask

import scala.concurrent.duration._
import akkaseed.Protocol._

import scala.concurrent.ExecutionContext.Implicits.global
import akkaseed.RandomString

object MasterActor {
  def apply()(implicit system: ActorSystem): ActorRef = {
    system.actorOf(Props[MasterActor], name = "master")
  }
}

class MasterActor extends Actor with ActorLogging with RandomString {

  private val system = this.context.system

  private var cancellable: Cancellable = _

  implicit val timeout = Timeout(5 seconds)

  private var workers = IndexedSeq.empty[ActorRef]
  private var batchCounter = 0

  override def preStart(): Unit = {
    cancellable =
      system.scheduler.schedule(
        10 second,
        5 second,
        self,
        BatchBuilder.generateBatch(5, 10)
      )
  }

  override  def postStop(): Unit = {
    cancellable.cancel
  }

  def receive = {

    case batch: Batch if workers.isEmpty =>
      log.error("************** Workers not configured")
      sender() ! BatchResult(batch)

    case batch: Batch =>
      batchCounter += 1
      val workerIndex = batchCounter % workers.size
      log.info(s"************** Number of workers ${workers.size}, batch #$batchCounter, worker #$workerIndex")
      val result = (workers(workerIndex) ? batch).map(x => x.asInstanceOf[TaskResult])
      result.pipeTo(sender)

    case WorkerRegistration if !workers.contains(sender) =>
      context.watch(sender)
      workers = workers :+ sender
      log.info(s"************** Worker registered #${workers.size - 1}")

    case Terminated(worker) =>
      log.info("************** Worker terminated")
      workers = workers.filterNot(_ == worker)
  }
}