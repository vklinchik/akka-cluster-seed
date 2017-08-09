package mindriot.akkaseed.api.model

import spray.json.DefaultJsonProtocol

case class Instrument(id: String, name: String)

object Instrument extends DefaultJsonProtocol {
  implicit val instrumentFormat = jsonFormat2(Instrument.apply)
}
