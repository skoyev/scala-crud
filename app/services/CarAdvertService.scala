package services

import java.util.UUID

import com.wix.accord._
import dao.{CarAdvertDao, CarAdvertMemoryDao}
import javax.inject.Inject
import models.{Car, PageResult}
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

/**
 * Car advertisment service class.
 * @author Sergiy Koyev
 * @param dao
 * @param executionContext
 */
class CarAdvertService @Inject()(dao: CarAdvertDao)(implicit executionContext: ExecutionContext) {

  val logger = Logger(this.getClass)

  def getAllCars(from: Int, pageSize: Int, sortField: String, sortOrder: String): Future[PageResult] = {
    dao.getAll(from, pageSize, sortField, sortOrder)
  }

  def insert(car: Car)
            (implicit validator: Validator[Car]): Future[Car] = {

    val validationResult = validate(car)
    if (validationResult.isFailure) {
      val violations = validationResult.toFailure.get.violations
      logger.info(s"invalid car details: ${violations.mkString(", ")}")
      return Future.failed(new AssertionError(violations.mkString(", ")))
    }
    dao.add(car)
  }

  def update(car: Car, id: UUID)(implicit validator: Validator[Car]): Future[Option[Car]] = {
    val result = findById(id).flatMap {
      case None => Future.successful(None)
      case Some(_) => insert(car.copy(id = Some(id))).map(r => Some(r))
    }
    result
  }

  def findById(id: UUID): Future[Option[Car]] = dao.findById(id)

  def remove(id: UUID): Future[Boolean] = dao.remove(id)
}
