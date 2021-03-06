package server

import cats.effect.{ConcurrentEffect, ExitCode, Timer}
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.syntax._
import cats.implicits._
import parser.RegexParser

import scala.concurrent.ExecutionContext.global

object Server {

  def stream[F[_]: ConcurrentEffect: Timer](): Stream[F, ExitCode] = {
    val dataClient = new EmojiStoreDataClient()
    val parser = new RegexParser[F](dataClient)
    val emojifierApp = new Routes()
    val routes = (emojifierApp.healthCheck <+> emojifierApp.emojify(parser)).orNotFound

    for{
      client <- BlazeClientBuilder[F](global).stream
      exitCode <- BlazeServerBuilder[F]
          .bindHttp(8080, "localhost")
          .withHttpApp(routes)
          .serve
    } yield exitCode
  }
}
