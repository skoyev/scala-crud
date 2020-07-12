package models

sealed trait FuelType

case object Gasoline extends FuelType

case object Diesel extends FuelType

object FuelType {
  val types = List(Gasoline, Diesel)
  def fromString(value: String):Option[FuelType] = types.find(_.toString == value)
}