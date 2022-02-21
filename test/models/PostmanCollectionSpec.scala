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
          "# Getting started\n\nThe easiest way to get started with this collection is to click the fork button to fork this collection to your own workspace and use Postman to send requests.\n\n# Usage\n\n1. [Generate a Postman API key](https://learning.postman.com/docs/developer/intro-api/)\n\n2. Fetch the ID of the Collection you wish to convert. This can be found by selecting the Collection in Postman, navigating to the right-hand sidebar, clicking the `Info` tab (symbolized by the `i` icon), and copying the ID.\n\n3. Update the `Current value` for the associated [collection variables](https://learning.postman.com/docs/sending-requests/variables/#defining-collection-variables) in this collection  with the Postman API key and Collection ID you just retrieved.\n\n4. Run the [Generate an OpenAPI Schema](https://www.postman.com/postman/workspace/405e0480-49cf-463b-8052-6c0d05a8e8f3/request/5922408-3b169388-c199-43d0-8247-721b312c0da4) request. In the response pane, click `Visualize`.\n\n5. Make use of your freshly converted schema!",
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

        //JsSuccess(PostmanCollection(Info(f21e770c-7d3b-4092-bdd6-288bb1d8d825,Postman to OpenAPI,1,https://schema.getpostman.com/json/collection/v2.1.0/collection.json),List(Item(Generate an OpenAPI Schema,Some(Endpoint(GET,List(Map(key -> x-api-key, value -> {{apiKey}}, type -> text)),List(collections, :collectionId),Some(List(Map(key -> collectionId, value -> {{collectionId}}))))),None))),)
        //JsSuccess(PostmanCollection(Info(f21e770c-7d3b-4092-bdd6-288bb1d8d825,Postman to OpenAPI,1,https://schema.getpostman.com/json/collection/v2.1.0/collection.json),List(Item(Generate an OpenAPI Schema,Some(Endpoint(GET,List(Map(key -> x-api-key, value -> {{apiKey}}, type -> text)),List(collections, :collectionId),Some(List(Map(key -> value), Map(value -> {{collectionId}}))))),None))),)

        //JsSuccess(PostmanCollection(Info(f21e770c-7d3b-4092-bdd6-288bb1d8d825,Postman to OpenAPI,1,https://schema.getpostman.com/json/collection/v2.1.0/collection.json),List(Item(Generate an OpenAPI Schema,Some(Endpoint(GET,List(Map(key -> x-api-key, value -> {{apiKey}}, type -> text)),List(collections, :collectionId),Some(List(Map(key -> collectionId, value -> {{collectionId}}))))),None))),)
        //JsSuccess(PostmanCollection(Info(f21e770c-7d3b-4092-bdd6-288bb1d8d825,Postman to OpenAPI,1,https://schema.getpostman.com/json/collection/v2.1.0/collection.json),List(Item(Generate an OpenAPI Schema,Some(Endpoint(GET,List(Map(key -> x-api-key, value -> {{apiKey}}, type -> text)),List(collections, :collectionId),Some(List(Map(key -> value, value -> {{collectionId}}))))),None))),)
        val expected = PostmanCollection(info, Seq(item))
        val json = TestDataHelper.readJsonFromFile("test/data/small_test_data.json")

        val result = PostmanCollection.reads.reads(json)

        result mustBe JsSuccess(expected)
      }

    }

  }

}
