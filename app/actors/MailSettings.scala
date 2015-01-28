package actors

import play.api.Play


trait MailSettings {
  private val conf = Play.current.configuration

  lazy val mandrillKey = conf.getString("mail.mandrill.key").get

  lazy val mailBaskets = conf.getStringSeq("mail.baskets").getOrElse(Seq())
  lazy val urgentBaskets = conf.getStringSeq("mail.baskets-urgent").getOrElse(Seq())

  lazy val gap = conf.getInt("mail.gap").getOrElse(60 * 2)
  lazy val gapUrgent = conf.getInt("mail.gap.urgent").getOrElse(30)
}

trait BasketSettings {
  private val conf = Play.current.configuration

  lazy val basketsToClear = conf.getStringSeq("baskets.prunable").getOrElse(Seq())
  lazy val deleteAfter = conf.getInt("baskets.pruning.delay").getOrElse(24 * 3)
}
