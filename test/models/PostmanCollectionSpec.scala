package models

import data.TestDataHelper
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{JsError, JsPath, JsSuccess}


class PostmanCollectionSpec extends AnyWordSpec {

  "PostmanCollection model" when {

    "converting from json" should {

      "throw js error if given invalid input" in {

        val json = TestDataHelper.readJsonFromFile("test/data/invalid_info.json")

        val result = PostmanCollection.reads.reads(json)

        result mustBe JsError(JsPath \ "info" \ "schema", "error.path.missing")
      }

      "return expected model" in {

        val info = Info(
          "f21e770c-7d3b-4092-bdd6-288bb1d8d825",
          "Postman to OpenAPI",
          "1",
          "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
        )

        val endpoint = Endpoint(
          "GET",
          Seq(Map("key" -> "x-api-key", "value" -> "{{apiKey}}", "type" -> "text")),
          Seq("collections",
            ":collectionId"),
          Some(Seq(Map("key" -> "collectionId", "value" -> "{{collectionId}}")))
        )

        val item = Item(
          "Generate an OpenAPI Schema",
          Some(endpoint),
          None
        )
        
        val expected = PostmanCollection(info, Seq(item))
        val json = TestDataHelper.readJsonFromFile("test/data/small_test_data.json")

        val result = PostmanCollection.reads.reads(json)

        result mustBe JsSuccess(expected)
      }

    }

    "convert to yaml" should {

      "return expected yaml" in {

        val expected = TestDataHelper.readYamlFromFile("test/data/small_test_data.yml")

        val info = Info(
          "f21e770c-7d3b-4092-bdd6-288bb1d8d825",
          "Postman to OpenAPI",
          "1",
          "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
        )

        val endpoint = Endpoint(
          "GET",
          Seq(Map("key" -> "x-api-key", "value" -> "{{apiKey}}", "type" -> "text")),
          Seq("collections",
            ":collectionId"),
          Some(Seq(Map("key" -> "collectionId", "value" -> "{{collectionId}}")))
        )

        val item = Item(
          "Generate an OpenAPI Schema",
          Some(endpoint),
          None
        )

        val postmanCollection = PostmanCollection(info, Seq(item))

        val result = postmanCollection.toYamlString

        result mustBe expected
      }

    }
  }

}
