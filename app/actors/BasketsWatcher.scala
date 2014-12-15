package actors

import akka.actor.Actor
import com.microtripit.mandrillapp.lutung.MandrillApi
import com.microtripit.mandrillapp.lutung.view.MandrillMessage
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient
import controllers.SettingsMongo
import org.chepurnoy.timeseries.TimeSeriesDatum
import org.joda.time.{DateTimeZone, DateTime}
import scala.collection.JavaConversions._
import scala.collection.concurrent.TrieMap

class BasketsWatcher extends Actor with SettingsMongo with MailSettings with BasketSettings {

  import actors.BasketsWatcher._

  def sendMail(subject: String, body: String) = {
    val mandrillApi = new MandrillApi(mandrillKey)

    val message = new MandrillMessage()
    message.setSubject(subject)
    message.setHtml("<p>" + body + "</p>")
    message.setAutoText(true)
    message.setFromEmail("team@secureae.com")
    message.setFromName("team")



    val recipient = new Recipient()
    recipient.setEmail("team@secureae.com")
    recipient.setName("team")
    val recipients = List(recipient)

    message.setTo(recipients)
    message.setPreserveRecipients(true)
    message.setTags(List("monitoring", "utilitary"))
    val messageStatusReports = mandrillApi.messages().send(message, false)
    messageStatusReports.map(s => println(s))
    //todo: write reports status to a log, exceptions
  }


  override def receive = {
    case DeleteOld =>
      basketsToClear.map { basket =>
        db.deleteBefore(basket, DateTime.now().minusHours(deleteAfter))
      }

    case SendMail(basket, datum, urgent) =>
      val timeStr = datum.timestamp.withZone(DateTimeZone.UTC).toString()

      val title = if(urgent) "URGENT! " else "" + s"$basket: Verification server got problem @ $timeStr"
      val body = datum.value.toString

      val (oldBody, oldTime) = cache.getOrElse(basket, "" -> new DateTime())

      val g = if(urgent) gapUrgent else gap
      if (body != oldBody || oldTime.plusMinutes(g).getMillis < new DateTime().getMillis) {
        sendMail(title, body)
        cache += basket -> (body -> datum.timestamp)
      }
  }
}


object BasketsWatcher {
  val cache = TrieMap[String, (String, DateTime)]()

  case object DeleteOld

  case class SendMail(basket: String, datum: TimeSeriesDatum, urgent: Boolean)

}
