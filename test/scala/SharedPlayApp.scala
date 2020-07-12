package scala


import com.typesafe.config.ConfigFactory
import play.api.Mode.Test
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Configuration}

import scala.reflect.ClassTag

trait SharedPlayApp {

	protected lazy val app: Application = fakeApplication()

	def instanceOf[A: ClassTag]: A = app.injector.instanceOf[A]

	private def fakeApplication(): Application = SharedPlayApp.cachedApplication

}

object SharedPlayApp {

	lazy val cachedApplication: Application = this.synchronized {
		val configuration = Configuration(
			ConfigFactory
        .load("environment/test.conf")
				.resolve())

		new GuiceApplicationBuilder()
			.in(Test)
			.configure(configuration)
			.build()
	}
}