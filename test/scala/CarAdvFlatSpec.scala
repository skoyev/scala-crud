package scala

import scala.concurrent.ExecutionContext
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, MustMatchers}
import org.scalatest.fixture.FlatSpec

abstract class CarAdvFlatSpec extends FlatSpec
	with SharedPlayApp
	with BeforeAndAfterAll
	with BeforeAndAfter
	with MustMatchers {

	implicit lazy val executionContext: ExecutionContext =
		app.injector.instanceOf[ExecutionContext]
}