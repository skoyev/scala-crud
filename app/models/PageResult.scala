package models

import play.api.libs.json._

case class PageResult(
                       cars: List[Car],
                       pageSize: Int,
                       totalResult: Long,
                       pageNumber: Int,
                       totalPages: Int
                     )

object PageResult {
  implicit val pageResultFormat = Json.format[PageResult]
}