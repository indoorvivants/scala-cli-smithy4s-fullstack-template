package fullstack_scala
package backend

import cats.effect.*
import com.comcast.ip4s.Port
import com.comcast.ip4s.host
import org.http4s.ember.server.EmberServerBuilder

import scala.concurrent.duration.*

object Server extends IOApp:

  override def run(args: List[String]) =
    val port = args.headOption
      .flatMap(_.toIntOption)
      .flatMap(Port.fromInt)
      .getOrElse(sys.error("port missing or invalid"))

    val server =
      for
        routes <- routesResource(TestServiceImpl())
        server <- EmberServerBuilder
          .default[IO]
          .withPort(port)
          .withHost(host"0.0.0.0")
          .withHttpApp(handleErrors(scribe.cats.io, routes))
          .withShutdownTimeout(0.seconds)
          .build
          .map(_.baseUri)
          .evalTap(uri => IO.println(s"Server running on $uri"))
      yield server

    server.useForever
      .as(ExitCode.Success)

  end run
end Server
