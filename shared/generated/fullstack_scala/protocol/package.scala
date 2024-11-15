package fullstack_scala

package object protocol {
  type TestService[F[_]] = smithy4s.kinds.FunctorAlgebra[TestServiceGen, F]
  val TestService = TestServiceGen

  type TestDescription = fullstack_scala.protocol.TestDescription.Type
  type TestId = fullstack_scala.protocol.TestId.Type
  type TestTitle = fullstack_scala.protocol.TestTitle.Type
  type Tests = fullstack_scala.protocol.Tests.Type

}