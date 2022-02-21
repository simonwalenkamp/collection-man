package models

import data.TestDataHelper
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{JsError, JsPath}


class PostmanCollectionSpec extends AnyWordSpec {

  "Info model" when {

    "converting from json" should {

      "Throw js error if given invalid input" in {

        val json = TestDataHelper.readJsonFromFile("test/data/invalid_info.json")

        val result = Info.reads.reads(json)

        result mustBe JsError(JsPath \ "schema", "error.path.missing")
      }

    }

  }

}
