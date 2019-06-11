package models

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
case class EmojiRequest(text: String)

object EmojiRequest {
  implicit val emojiEncoder: Encoder[EmojiRequest] = deriveEncoder[EmojiRequest]
  implicit val emojiDecoder: Decoder[EmojiRequest] = deriveDecoder[EmojiRequest]
}

object test extends App {
  import EmojiRequest._
  val req = EmojiRequest("sdfkjs")
  println(emojiEncoder.apply(req))
}
