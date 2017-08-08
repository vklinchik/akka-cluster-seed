package akkaseed.master

import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable, Terminated}
import akka.util.Timeout
import akka.pattern.pipe
import akka.pattern.ask

import scala.concurrent.duration._
import akkaseed.Protocol.{SampleTask, TaskFailed, TaskResult, WorkerRegistration}

import scala.concurrent.ExecutionContext.Implicits.global
import akkaseed.RandomString

class Master extends Actor with ActorLogging with RandomString {

  private val system = this.context.system

  var cancellable: Cancellable = _

  implicit val timeout = Timeout(5 seconds)

  var workers = IndexedSeq.empty[ActorRef]
  var taskCounter = 0

  override def preStart(): Unit = {
    cancellable =
      system.scheduler.schedule(
        10 second,
        5 second,
        self,
        SampleTask(generate)
      )
  }

  override  def postStop(): Unit = {
    cancellable.cancel
  }

  def receive = {
    case task: SampleTask if workers.isEmpty => {
      log.error("************** Workers not configured")
      sender() ! TaskFailed(task, "Service unavailable, try again later")
    }
    case task: SampleTask => {
      taskCounter += 1
      val workerIndex = taskCounter % workers.size
      log.info(s"************** Number of workers ${workers.size}, task #$taskCounter, worker #$workerIndex")
      val result = (workers(workerIndex) ? task).map(x => x.asInstanceOf[TaskResult])
      result.pipeTo(sender)
    }
    case WorkerRegistration if !workers.contains(sender()) => {
      context.watch(sender)
      workers = workers :+ sender
      log.info(s"************** Worker registered #${workers.size - 1}")
    }

    case Terminated(a) => {
      log.info("************** Worker terminated")
      workers = workers.filterNot(_ == a)
    }
  }
}