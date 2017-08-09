package mindriot.akkaseed.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

object ApiApp extends HttpService {

  implicit val system = ActorSystem("AkkaSeedSenderSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def main(args : Array[String]) {

    val port = if (args.isEmpty) "0" else args(0)

    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [api]")).
      withFallback(ConfigFactory.load())

    Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))

    StdIn.readLine()
    system.terminate()
  }
}

