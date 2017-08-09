package mindriot.akkaseed.seed

import akka.actor.{ActorSystem, Props}
import akka.cluster.client.ClusterClientReceptionist
import com.typesafe.config.ConfigFactory

object SeedApp {

  def main(args: Array[String]): Unit = {

    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [seed]")).
      withFallback(ConfigFactory.load())

    ActorSystem("AkkaSeedSystem", config)
  }
}
