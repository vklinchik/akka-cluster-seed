package mindriot.akkaseed.api.model

import spray.json._
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateJsonFormat extends RootJsonFormat[LocalDate] {

  private val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

  override def write(date: LocalDate) = JsString(formatter.format(date))

  override def read(json: JsValue) : LocalDate = json match {
    case JsString(dateString) => LocalDate.parse(dateString, formatter)
    case _ => throw new DeserializationException("Invalid format")
  }
}


// http://wern-ancheta.com/blog/2015/04/05/getting-started-with-the-yahoo-finance-api/

object Pricing extends Enumeration {
  val Ask = Value("a")
  val Bid = Value("b")
  val AskRealtime = Value("b2")
  val BidRealtime = Value("b3")
  val PreviousClose = Value("p")
  val Open = Value("o")

}

//http://finance.yahoo.com/d/quotes.csv?s=MSFT&f=e1sl1d1t1c6p2v&e=.csv

case class Quote(
  open: BigDecimal,
  close: BigDecimal,
  high: BigDecimal,
  low: BigDecimal,
  volume: BigDecimal,
  date: LocalDate
)

object Quote extends DefaultJsonProtocol {
  implicit val dateJsonFormat = DateJsonFormat

  implicit val quoteFormat = jsonFormat6(Quote.apply)
}
