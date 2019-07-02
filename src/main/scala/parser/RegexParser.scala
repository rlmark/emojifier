package parser

import cats.effect.Sync
import cats.implicits._
import server.EmojiDataClient

class RegexParser[F[_]: Sync](emojiDataClient: EmojiDataClient) extends Parser[F, String] {
  override def parseMessage(message: String): F[String] = {
    tokenize(message).map(tokens => parser(tokens).mkString(" "))
  }

  def tokenize(message: String): F[Vector[String]] = {
    lazy val splitWhitespace = message.split(" ").toVector
    Sync[F].delay(splitWhitespace)
  }

  def parser(tokens: Vector[String]): Vector[String] = {
    tokens.map(t => if (isEmoji(t)) emojiDataClient.getEmoji(t) else t)
  }

  def isEmoji(token: String): Boolean = {
    val regexPattern = ":(.*):".r
    val maybeMatch = regexPattern.findFirstIn(token).isDefined
    maybeMatch
  }
}
