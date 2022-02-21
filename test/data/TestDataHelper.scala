package data

import play.api.libs.json.{JsValue, Json}

import scala.io.Source

object TestDataHelper {

  def readJsonFromFile(path: String): JsValue = {
    val bufferedSource = Source.fromFile(path)
    val content        = bufferedSource.getLines().mkString

    bufferedSource.close

    Json.parse(content)
  }

}
