package controllers.EndToEnd

import data.TestDataHelper
import fixtures.ApplicationPerSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.test._
import play.api.test.Helpers._

class CollectionManControllerSpec extends AnyWordSpec
  with Matchers
  with ApplicationPerSuite
  with FutureAwaits
  with DefaultAwaitTimeout
  with Injecting {

  "CollectionManController" should {

    "return bad request if given invalid json" in {
      val request = FakeRequest(POST, "/convert").withFormUrlEncodedBody("body" -> "invalid json")
      val result = route(app, request).get

      status(result) mustBe BAD_REQUEST
      contentAsString(result) must include("Could not parse Json")
    }

    "return bad request if given invalid postman collection" in {

      val json = TestDataHelper.readJsonFromFile("test/data/invalid_info.json")

      val request = FakeRequest(POST, "/convert").withFormUrlEncodedBody("body" -> json.toString())
      val result = route(app, request).get

      status(result) mustBe BAD_REQUEST
      contentAsString(result) must include("Invalid Postman collection Json!")
    }

    "return expected yaml when given valid postman collection" in {
      val json = TestDataHelper.readJsonFromFile("test/data/small_test_data.json")

      val request = FakeRequest(POST, "/convert").withFormUrlEncodedBody("body" -> json.toString())
      val result = route(app, request).get

      status(result) mustBe OK
      contentAsString(result) must include("f21e770c-7d3b-4092-bdd6-288bb1d8d825")
    }
  }
}
