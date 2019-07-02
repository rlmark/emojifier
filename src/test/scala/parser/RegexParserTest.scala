package parser

import cats.effect.{IO, Sync}
import org.scalatest._
import server.{EmojiDataClient, EmojiStoreDataClient}

class RegexParserTest extends FlatSpec with Matchers {

  def withMocks[F[_]: Sync](test: (EmojiDataClient, RegexParser[F]) => Unit): Unit = {
    val stubEmojiDataClient = new EmojiStoreDataClient
    val regexParser = new RegexParser[F](stubEmojiDataClient)
    test(stubEmojiDataClient, regexParser)
  }
  "parseMessage" should "correctly parse an emoji included in message" in withMocks[IO] { (stubEmojiDataClient,regexParser)  =>
    val result: IO[String] = regexParser.parseMessage("Hi. How are you? :smile:")
    result.unsafeRunSync() shouldBe "Hi. How are you? :)"
  }

  it should "handle when a colon is not surrounding an emoji keuy" in withMocks[IO] { (stubEmojiDataClient,regexParser) =>
    val stringWithRandomColons = ":Here are the options: not: an: emoji:!"
    regexParser.parseMessage(stringWithRandomColons).unsafeRunSync() shouldBe stringWithRandomColons
  }

  it should "handle messages that include no colons" in withMocks[IO] { (stubEmojiDataClient,regexParser) =>
    val stringWithNoColons = "Here is a simple string."
    regexParser.parseMessage(stringWithNoColons).unsafeRunSync() shouldBe stringWithNoColons
  }

  it  should "work" in withMocks[IO]{ (_, regexParser) =>
    val test = "Hello there. :smile: testing"
    regexParser.parseMessage(test).unsafeRunSync()  shouldBe "Hello there. :) testing"

  }

}
