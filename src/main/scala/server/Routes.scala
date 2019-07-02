package server

import cats.Functor
import cats.effect._
import cats.syntax._
import cats.instances._
import cats.implicits._
import models.EmojiRequest
import org.http4s.{EntityDecoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._
import parser.Parser

class Routes() {
  def healthCheck[F[_]: Sync] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F]{case GET -> Root / "health" => Sync[F].suspend(Ok("healthy"))}
  }

  def emojify[F[_]: Sync](parser: Parser[F, String]) = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    import EmojiRequest._
    HttpRoutes.of[F]{case req @ POST -> Root / "emojify" =>
      implicit val y: EntityDecoder[F, EmojiRequest] = jsonOf[F, EmojiRequest](Sync[F], emojiDecoder) //
      val emojiRequest: F[EmojiRequest] = req.as[EmojiRequest]
      val res = for {
        request <- emojiRequest
        renderMessage <-   parser.parseMessage(request.text)
      } yield renderMessage

      println(res)

      Ok(res)
    }
  }
}
