package mindriot.akkaseed.mindriot.akkaseed.api.routes

import java.time.LocalDate

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.MalformedQueryParamRejection
import mindriot.akkaseed.api.model.Quote


trait QuoteServiceRoute extends SprayJsonSupport {

  import StatusCodes._

  val quotes =
    Map(
      "MSFT" ->
        List(
          Quote(open = 10.00, close = 10.24, high = 10.48, low = 9.96, volume = 100000, date = LocalDate.now),
          Quote(open = 10.20, close = 10.23, high = 10.34, low = 9.86, volume = 200000, date = LocalDate.now.minusDays(1)),
          Quote(open = 10.00, close = 10.05, high = 10.11, low = 9.26, volume = 110000, date = LocalDate.now.minusDays(2)),
          Quote(open = 10.00, close = 10.24, high = 10.48, low = 9.96, volume = 111000, date = LocalDate.now.minusDays(3)),
          Quote(open = 10.00, close = 10.24, high = 10.48, low = 9.96, volume = 120000, date = LocalDate.now.minusDays(4))
        )
    )

  val quoteRoute = pathPrefix("quote") {
    pathEndOrSingleSlash {
      get {
        complete(OK)
      }
    } ~
      pathPrefix(Segment) { symbol =>
        pathEndOrSingleSlash {
          get {
            quotes.get(symbol)
              .map(complete(_))
              .getOrElse(reject(MalformedQueryParamRejection("symbol", "Instrument not found")))
          }
        }
      }
  }
}
