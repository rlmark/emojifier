import cats.FlatMap
import cats.effect.{Timer, _}
import fs2.Stream
import org.http4s.{Http, HttpApp, HttpRoutes, Request, Response}
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._
import cats.implicits._
import cats.instances._

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    Server.stream[IO].compile.drain.as(ExitCode.Success)
  }
}

object Server {

  def stream[F[_]: ConcurrentEffect: Timer](): Stream[F, ExitCode] = {
    val emojifierApp = Routes.route.orNotFound

    for{
      client <- BlazeClientBuilder[F](global).stream
      exitCode <- BlazeServerBuilder[F]
          .bindHttp(8080, "localhost")
          .withHttpApp(emojifierApp)
          .serve
    } yield exitCode
  }
}

object Routes {
  def route[F[_]: Sync] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F]{case GET -> Root => Sync[F].suspend(Ok("HelloWorld"))}
  }
}
