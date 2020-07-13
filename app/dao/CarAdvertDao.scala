package dao

import java.util.UUID

import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.playjson._
import com.sksamuel.elastic4s.searches.sort.{FieldSortDefinition, SortOrder}
import database.ElasticsearchHttpClient
import javax.inject.Inject
import models.{Car, PageResult}
import play.api.Logger

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}

/**
 * Car advertising dao class
 * @author Sergiy Koyev
 */
class CarAdvertDao @Inject()(elasticsearch: ElasticsearchHttpClient)
                            (implicit executionContext: ExecutionContext) {

  val logger = Logger(this.getClass)

  /**
   * Add a new Car into storage
   * @param carModel
   * @return
   */
  def add(carModel: Car): Future[Car] = {
    val target = carModel.copy(id = Some(carModel.encodedId))

    for {
      result <- if (elasticsearch.refresh) elasticsearch.execute {
        indexInto(Car.indexName / "entry")
        .id(target.encodedId.toString)
        .doc(carModel)
        .refreshImmediately
      }
      else {
        elasticsearch.execute {
          indexInto(Car.indexName / "entry")
          .id(target.encodedId.toString)
          .doc(carModel)
        }
      }
    } yield {
      logger.info(s"car: ${target.title} upserted successfully with id: ${target.encodedId.toString}")
      target
    }
  }

  def findById(id: UUID): Future[Option[Car]] = {
    for {
      result <- elasticsearch.execute({
        search(Car.indexName).query(idsQuery(id))
      }).map {
        case Left(failure) =>
          throw new RuntimeException(
            s"Error while looking up for entity: ${failure.error}"
          )
        case Right(response) =>
          response.result.to[Car].headOption
      }
    } yield result
  }

  /**
   * Remove car from storage
   * @param id car identifier
   * @return result of operation Future
   */
  def remove(id: UUID): Future[Boolean] = {
    for {
      result <- elasticsearch.execute(
        delete(id.toString).from(Car.indexName / "entry")
      ).map {
        case Left(failure) =>
          throw new RuntimeException(
            s"Error while looking up for entity: ${failure.error}"
          )
        case Right(response) =>
          val result = response.result.result
          logger.info(s"car: $id delete response: $result")
          true
      }
    } yield {
      logger.info(s"successfully deleted car: $id")
      result
    }
  }

  /**
   * Get All Cars by params from, pageSize, sortField, sortOrder
   * @param from
   * @param pageSize
   * @param sortField
   * @param sortOrder
   * @return collection of the Car objects.
   */
  def getAll(from: Int, pageSize: Int, sortField: String, sortOrder: String): Future[PageResult] = {

    val order = sortOrder.toLowerCase match {
      case "asc" => SortOrder.ASC
      case "desc" => SortOrder.DESC
    }
    elasticsearch
    .execute {
      searchWithType(Car.indexName / "entry")
      .from((from - 1) * pageSize)
      .size(pageSize)
      .sortBy(FieldSortDefinition(sortField + ".sort-field").order(order))
    }
    .map {
      case Left(failure) =>
        throw new RuntimeException(
          s"Error while looking up for entity: ${failure.error}"
        )
      case Right(response) =>
        PageResult(
          response.result.to[Car].toList,
          pageSize,
          response.result.totalHits,
          from,
          Math.ceil(response.result.totalHits / pageSize.toDouble).toInt
        )
    }
  }
}
