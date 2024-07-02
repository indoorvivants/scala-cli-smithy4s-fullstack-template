package fullstack_scala
package backend

import cats.data.Kleisli
import cats.effect.IO
import fullstack_scala.protocol.*
import org.http4s.HttpApp
import scribe.Scribe
import smithy4s.http4s.SimpleRestJsonBuilder

def handleErrors(logger: Scribe[IO], routes: HttpApp[IO]): HttpApp[IO] =
  import cats.syntax.all.*
  routes.onError { exc =>
    Kleisli(request => logger.error("Request failed", request.toString, exc))
  }

class TestServiceImpl() extends TestService[IO]:
  override def listTests(): IO[ListTestsOutput] =
    IO(
      ListTestsOutput(
        List(
          Test(
            TestId(1),
            TestAttributes(
              TestTitle("yass"),
              description = Some(TestDescription("qween"))
            )
          ),
          Test(
            TestId(2),
            TestAttributes(
              TestTitle("bless"),
              description = None
            )
          )
        )
      )
    )
end TestServiceImpl

def routesResource(service: TestService[IO]) =
  import org.http4s.implicits.*
  SimpleRestJsonBuilder
    .routes(service)
    .resource
    .map(_.orNotFound)
