package fixtures

import org.scalatest.TestSuite
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Configuration}

trait ApplicationPerSuite extends GuiceOneAppPerSuite {
  this: TestSuite =>

  override def fakeApplication(): Application = GuiceApplicationBuilder(
    configuration = Configuration.apply()).build()

}
