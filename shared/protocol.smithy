$version: "2.0"

namespace fullstack_scala.protocol

use alloy#simpleRestJson
use alloy#uuidFormat

@simpleRestJson
service TestService {
  version: "1.0.0",
  operations: [ListTests]
}

@readonly
@http(method: "GET", uri: "/api/tests", code: 200)
operation ListTests {
  output := {
    @required
    tests: Tests
  }
}

list Tests {
  member: Test
}

structure Test {
  @required
  id: TestId

  @required
  attributes: TestAttributes

  deleted: Boolean = false
}

structure TestAttributes {
  @required
  title: TestTitle

  description: TestDescription
}

integer TestId
string TestTitle
string TestDescription
