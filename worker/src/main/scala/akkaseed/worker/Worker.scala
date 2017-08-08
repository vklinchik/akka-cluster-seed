package akkaseed.worker

import akka.actor.{Actor, ActorLogging, RootActorPath}
import akka.cluster.{Cluster, Member, MemberStatus}
import akka.cluster.ClusterEvent.{CurrentClusterState, MemberUp}

import akkaseed.Protocol._

class Worker extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  // subscribe to cluster changes, MemberUp
  // re-subscribe when restart
  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberUp])
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  def receive = {

    case EmptyTask => {
      log.info("************** Empty task - nothing to do")
    }

    case SampleTask(text) => {
      val result = text.toUpperCase
      log.info(s"*** XXXXXXXXXXXX *** Worker has processed sample task '$text' into '$result'")
      sender() ! TaskResult(text.toUpperCase)
    }

    case state: CurrentClusterState => {
      state
        .members
        .filter(_.status == MemberStatus.Up)
        .foreach(register)
    }

    case MemberUp(m) => {
      register(m)
    }
  }

  def register(member: Member): Unit = {
    log.info("************** member roles")
    member.roles.foreach(role => log.info(s"---------- role ---------"))
    if (member.hasRole("master")) {
      context.actorSelection(RootActorPath(member.address) / "user" / "master") ! WorkerRegistration
    }
  }
}
