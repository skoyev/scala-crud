package models.json

import models.FuelType
import play.api.libs.json._

import scala.reflect.runtime.universe

object FuelTypeFormat extends Reads[FuelType] with Writes[FuelType] {

  private val tpe = universe.typeOf[FuelType]
  private val subTypes = tpe.typeSymbol.asClass.knownDirectSubclasses
  private val subTypeNames = subTypes.map(_.name.toString)

  override def reads(json: JsValue): JsResult[FuelType] = json match {
    case JsString(fuelTypeString) =>
      val optionFuelType = FuelType.fromString(fuelTypeString)
      optionFuelType match {
        case Some(fuelType) => JsSuccess(fuelType)
        case None => JsError(JsPath() -> JsonValidationError(s"Expected a valid fuel type string. Available fuel types are: ${subTypeNames.mkString(", ")}"))
      }
    case _ =>
      JsError(Seq(JsPath() -> Seq(JsonValidationError(s"Expected a valid fuel type string. Available fuel types are: ${subTypeNames.mkString(", ")}"))))
  }

  override def writes(o: FuelType): JsValue = {
    JsString(o.toString)
  }
}
