package dao

import java.util.UUID

import models.{Car, PageResult}
import play.api.Logger

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

class CarAdvertMemoryDao {
  val logger = Logger(this.getClass)
  val cars = ListBuffer[Car]()

  /**
   * Add a new Car into storage
   * @param carModel
   * @return
   */
  def add(carModel: Car) = {
    val target = carModel.copy(id = Some(carModel.encodedId))

    //cars += target

    logger.debug(String.valueOf(cars.length))
  }

  def findById(id: UUID): Option[Car] = {
    return null
  }

  /**
   * Remove car from storage
   * @param id
   * @return
   */
  def remove(id: UUID) = {
  }

  /**
   * Get All Cars.
   * @param from
   * @param pageSize
   * @param sortField
   * @param sortOrder
   * @return
   */
  def getAll(from: Int, pageSize: Int, sortField: String, sortOrder: String) = {
  }
}
