package mindriot.akkaseed.mindriot.akkaseed.api.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.MalformedQueryParamRejection
import mindriot.akkaseed.api.model.Instrument


trait InstrumentServiceRoute extends SprayJsonSupport {

  val instruments = Map("MSFT" -> "Microsoft", "AAPL" -> "Apple")

  val instrumentRoute = pathPrefix("instrument") {
    pathEndOrSingleSlash {
      get {
        val instrumentList = instruments.map(kv => Instrument(kv._1, kv._2))
        complete(instrumentList)
      }
    } ~
      pathPrefix(Segment) { symbol =>
        pathEndOrSingleSlash {
          get {
            instruments.get(symbol)
              .map(Instrument(symbol, _))
              .map(complete(_))
              .getOrElse(reject(MalformedQueryParamRejection("symbol", "Instrument not found")))
          }
        }
      }
  }


}