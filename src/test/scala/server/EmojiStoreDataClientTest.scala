package server

import org.scalatest._

class EmojiStoreDataClientTest extends FlatSpec with Matchers {

  "EmojiStoreDataClient" should "return an emoji representation of the string if valid" in {
    val description = "smile"
    val client = new EmojiStoreDataClient
    client.getEmoji(description) shouldBe ":)"
  }
  it should "default to original description when emoji representation not found" in {
    val description = "sadness"
    val client = new EmojiStoreDataClient
    client.getEmoji(description) shouldBe "sadness"
  }
}
