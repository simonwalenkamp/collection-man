package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

class CollectionManControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "CollectionManController GET" should {

    "render conversionView" in {
      val controller = new CollectionManController(stubControllerComponents())
      val home = controller.getConversionView().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Conversion page")
    }
  }
}
