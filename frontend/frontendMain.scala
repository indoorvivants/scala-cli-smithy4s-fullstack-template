package fullstack_scala
package frontend

import fullstack_scala.protocol.*
import smithy4s_fetch.SimpleRestJsonFetchClient
import com.raquo.laminar.api.L.*
import scala.scalajs.js.Promise
import org.scalajs.dom

def createApiClient(uri: String) = SimpleRestJsonFetchClient(
  TestService,
  uri
).make

extension [T](p: => Promise[T]) def stream = EventStream.fromJsPromise(p)

@main def hello =
  val allTests = Var(List.empty[Test])
  val apiClient = createApiClient(dom.window.location.href)
  val app =
    div(
      // Initial loading
      apiClient.listTests().stream.map(_.tests) --> allTests,
      // Render list of items
      children <-- allTests.signal.split(_.id)((_, _, testSignal) =>
        div(
          p(
            b("Title: "),
            child.text <-- testSignal.map(_.attributes.title.value)
          ),
          p(
            b("Description: "),
            blockQuote(
              child.maybe <-- testSignal.map(
                _.attributes.description.map(_.value)
              )
            )
          )
        )
      )
    )

  renderOnDomContentLoaded(dom.document.getElementById("content"), app)
end hello
