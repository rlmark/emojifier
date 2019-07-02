package parser

trait Parser[F[_], A] {
// "Hi how are you? :smile:" ==> "Hi how are you? :)"
  def parseMessage(message: String): F[A]
}
