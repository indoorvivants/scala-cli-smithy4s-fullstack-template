package fullstack_scala.protocol

import smithy4s.Endpoint
import smithy4s.Hints
import smithy4s.Schema
import smithy4s.Service
import smithy4s.ShapeId
import smithy4s.Transformation
import smithy4s.kinds.PolyFunction5
import smithy4s.kinds.toPolyFunction5.const5
import smithy4s.schema.OperationSchema
import smithy4s.schema.Schema.unit

trait TestServiceGen[F[_, _, _, _, _]] {
  self =>

  def listTests(): F[Unit, Nothing, ListTestsOutput, Nothing, Nothing]

  def transform: Transformation.PartiallyApplied[TestServiceGen[F]] = Transformation.of[TestServiceGen[F]](this)
}

object TestServiceGen extends Service.Mixin[TestServiceGen, TestServiceOperation] {

  val id: ShapeId = ShapeId("fullstack_scala.protocol", "TestService")
  val version: String = "1.0.0"

  val hints: Hints = Hints(
    alloy.SimpleRestJson(),
  ).lazily

  def apply[F[_]](implicit F: Impl[F]): F.type = F

  object ErrorAware {
    def apply[F[_, _]](implicit F: ErrorAware[F]): F.type = F
    type Default[F[+_, +_]] = Constant[smithy4s.kinds.stubs.Kind2[F]#toKind5]
  }

  val endpoints: Vector[smithy4s.Endpoint[TestServiceOperation, ?, ?, ?, ?, ?]] = Vector(
    TestServiceOperation.ListTests,
  )

  def input[I, E, O, SI, SO](op: TestServiceOperation[I, E, O, SI, SO]): I = op.input
  def ordinal[I, E, O, SI, SO](op: TestServiceOperation[I, E, O, SI, SO]): Int = op.ordinal
  override def endpoint[I, E, O, SI, SO](op: TestServiceOperation[I, E, O, SI, SO]) = op.endpoint
  class Constant[P[-_, +_, +_, +_, +_]](value: P[Any, Nothing, Nothing, Nothing, Nothing]) extends TestServiceOperation.Transformed[TestServiceOperation, P](reified, const5(value))
  type Default[F[+_]] = Constant[smithy4s.kinds.stubs.Kind1[F]#toKind5]
  def reified: TestServiceGen[TestServiceOperation] = TestServiceOperation.reified
  def mapK5[P[_, _, _, _, _], P1[_, _, _, _, _]](alg: TestServiceGen[P], f: PolyFunction5[P, P1]): TestServiceGen[P1] = new TestServiceOperation.Transformed(alg, f)
  def fromPolyFunction[P[_, _, _, _, _]](f: PolyFunction5[TestServiceOperation, P]): TestServiceGen[P] = new TestServiceOperation.Transformed(reified, f)
  def toPolyFunction[P[_, _, _, _, _]](impl: TestServiceGen[P]): PolyFunction5[TestServiceOperation, P] = TestServiceOperation.toPolyFunction(impl)

}

sealed trait TestServiceOperation[Input, Err, Output, StreamedInput, StreamedOutput] {
  def run[F[_, _, _, _, _]](impl: TestServiceGen[F]): F[Input, Err, Output, StreamedInput, StreamedOutput]
  def ordinal: Int
  def input: Input
  def endpoint: Endpoint[TestServiceOperation, Input, Err, Output, StreamedInput, StreamedOutput]
}

object TestServiceOperation {

  object reified extends TestServiceGen[TestServiceOperation] {
    def listTests(): ListTests = ListTests()
  }
  class Transformed[P[_, _, _, _, _], P1[_ ,_ ,_ ,_ ,_]](alg: TestServiceGen[P], f: PolyFunction5[P, P1]) extends TestServiceGen[P1] {
    def listTests(): P1[Unit, Nothing, ListTestsOutput, Nothing, Nothing] = f[Unit, Nothing, ListTestsOutput, Nothing, Nothing](alg.listTests())
  }

  def toPolyFunction[P[_, _, _, _, _]](impl: TestServiceGen[P]): PolyFunction5[TestServiceOperation, P] = new PolyFunction5[TestServiceOperation, P] {
    def apply[I, E, O, SI, SO](op: TestServiceOperation[I, E, O, SI, SO]): P[I, E, O, SI, SO] = op.run(impl) 
  }
  final case class ListTests() extends TestServiceOperation[Unit, Nothing, ListTestsOutput, Nothing, Nothing] {
    def run[F[_, _, _, _, _]](impl: TestServiceGen[F]): F[Unit, Nothing, ListTestsOutput, Nothing, Nothing] = impl.listTests()
    def ordinal: Int = 0
    def input: Unit = ()
    def endpoint: smithy4s.Endpoint[TestServiceOperation,Unit, Nothing, ListTestsOutput, Nothing, Nothing] = ListTests
  }
  object ListTests extends smithy4s.Endpoint[TestServiceOperation,Unit, Nothing, ListTestsOutput, Nothing, Nothing] {
    val schema: OperationSchema[Unit, Nothing, ListTestsOutput, Nothing, Nothing] = Schema.operation(ShapeId("fullstack_scala.protocol", "ListTests"))
      .withInput(unit)
      .withOutput(ListTestsOutput.schema)
      .withHints(smithy.api.Http(method = smithy.api.NonEmptyString("GET"), uri = smithy.api.NonEmptyString("/api/tests"), code = 200), smithy.api.Readonly())
    def wrap(input: Unit): ListTests = ListTests()
  }
}

