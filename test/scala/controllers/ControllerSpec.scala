package scala.controllers

import java.util.UUID

import dao.CarAdvertDao
import models.{Car, Diesel}
import org.scalatest.Outcome
import play.api.libs.json.Json
import play.api.mvc.RequestHeader
import play.api.test.Helpers._
import play.api.test._
import scala.concurrent.duration._

import scala.concurrent.Await
import scala.fixture._

class ControllerSpec extends CarAdvFlatSpec {
  override type FixtureParam = RequestHeader

  behavior of "Car advertisements Controller"

  it must "correctly query for a car by id" in { ctx =>
    running(SharedPlayApp.cachedApplication) {
      val request = FakeRequest(GET, s"/cars/${car.encodedId.toString}").withHeaders(HOST -> "localhost:9000")
      val getById = route(request).get
      contentAsString(getById) must be(Json.toJson(car).toString())
    }
  }

  it must "correctly modify the car" in { ctx =>
    running(SharedPlayApp.cachedApplication) {
      val updatedCar = car.copy(title = "updatedTitle")
      val request = FakeRequest(POST, s"/cars/update/${car.encodedId.toString}")
                    .withHeaders(HOST -> "localhost:9000")
                    .withBody(Json.toJson(updatedCar))
      val updateResult = route(request).get
      contentAsString(updateResult) must be(Json.toJson(updatedCar).toString())
    }
  }

  private val car = random[Car].copy(id = Some(UUID.randomUUID()), fuel = Diesel)
  private val carAdvertDao = instanceOf[CarAdvertDao]

  override protected def afterAll(): Unit = {
    val remove = carAdvertDao.remove(car.encodedId)
    Await.result(remove, 10.seconds)
  }

  override protected def beforeAll(): Unit = {
    //val upsert = carAdvertDao.upsert(car)
    //Await.result(upsert, 10.seconds)
  }

  override def withFixture(test: OneArgTest): Outcome = {
    val request = FakeRequest()
    test(request)
  }
}
