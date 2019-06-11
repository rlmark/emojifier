package server

trait EmojiDataClient {
  def getEmoji(description: String): String
}

class EmojiStoreDataClient extends EmojiDataClient {
  val store: Map[String, String] = EmojiStore.store

  override def getEmoji(description: String): String =
    store.getOrElse(description, description)
}
