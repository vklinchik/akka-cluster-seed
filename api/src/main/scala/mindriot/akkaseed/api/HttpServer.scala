package mindriot.akkaseed.api

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives._
import mindriot.akkaseed.mindriot.akkaseed.api.routes.{InstrumentServiceRoute, QuoteServiceRoute}

trait HttpService extends QuoteServiceRoute with InstrumentServiceRoute {

  val routes =
    pathEndOrSingleSlash {
      get {
        complete(OK)
      }
    } ~
      pathPrefix("api") {
        pathPrefix("v1") {
          quoteRoute ~
            instrumentRoute
        }
      }
}
