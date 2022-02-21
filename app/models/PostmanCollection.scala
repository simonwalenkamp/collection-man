package models

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Reads}

case class PostmanCollection(info: Info, Items: Seq[Item])
case class Item(name: String, request: Option[Endpoint], items: Option[Seq[Item]])
case class Endpoint(method: String, headers: Seq[Map[String, String]], path: Seq[String], variable: Option[Seq[Map[String, String]]])
case class Info(id: String, name: String, description: String, schema: String)

object PostmanCollection {

  implicit val reads: Reads[PostmanCollection] = (
    (JsPath \ "info").read[Info] and
      (JsPath \ "item").read[Seq[Item]]
  )(PostmanCollection.fromJson(_: Info, _: Seq[Item]))

  def fromJson(info: Info, requests: Seq[Item]): PostmanCollection =
    PostmanCollection(info, requests)

}

object Item {

  implicit val reads: Reads[Item] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "request").readNullable[Endpoint] and
      (JsPath \ "item").lazyReadNullable(implicitly[Reads[Seq[Item]]])
  )(Item.fromJson(_: String, _: Option[Endpoint], _: Option[Seq[Item]]))

  def fromJson(name: String, request: Option[Endpoint], item: Option[Seq[Item]]): Item =
    Item(name, request, item)

}

object Endpoint {

  implicit val reads: Reads[Endpoint] = (
    (JsPath \ "method").read[String] and
      (JsPath \ "header").read[Seq[Map[String, String]]] and
        (JsPath \ "url" \ "path").read[Seq[String]] and
        (JsPath \ "url" \ "variable").readNullable[Seq[Map[String, String]]]
    )(Endpoint.fromJson(_: String, _: Seq[Map[String, String]], _: Seq[String], _: Option[Seq[Map[String, String]]]))


  def fromJson(method: String, headers: Seq[Map[String, String]], path: Seq[String], variable: Option[Seq[Map[String, String]]]): Endpoint =
    Endpoint(method, headers, path, variable)
}

object Info {

  implicit val reads: Reads[Info] = (
    (JsPath \ "_postman_id").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "schema").read[String]
    )(Info.fromJson(_: String, _: String, _: String, _: String))

    def fromJson(id: String, name: String, description: String, schema: String): Info =
      Info(id, name, description, schema)
}
