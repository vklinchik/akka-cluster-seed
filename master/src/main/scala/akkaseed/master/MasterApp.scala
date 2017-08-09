package akkaseed.master

import akka.actor.{ActorSystem, Props}
import akka.cluster.client.ClusterClientReceptionist
import com.typesafe.config.ConfigFactory

object MasterApp {

  def main(args: Array[String]): Unit = {

    // Override the configuration of the port when specified as program argument
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [master]")).
      withFallback(ConfigFactory.load())

    implicit val system = ActorSystem("AkkaSeedSystem", config)
    val master = MasterActor()
    ClusterClientReceptionist(system).registerService(master)
  }
}
