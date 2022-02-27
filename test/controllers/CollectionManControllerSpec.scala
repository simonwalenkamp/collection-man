package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Play.materializer
import play.api.data.Form
import play.api.test.CSRFTokenHelper.CSRFFRequestHeader
import play.api.test._
import play.api.test.Helpers._

class CollectionManControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "CollectionManController" should {

    "render conversionView" in {
      val controller = new CollectionManController(stubControllerComponents())
      val request = FakeRequest(GET, "/").withCSRFToken
      val result = controller.getConversionView().apply(request)

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("<title>Collection Man</title>")
    }

    "return bad request if given invalid form" in {
      val controller = new CollectionManController(stubControllerComponents())
      val request = FakeRequest(POST, "/convert").withCSRFToken
      val result = controller.convertJson().apply(request)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) must include("Invalid form!")
    }
  }
}
