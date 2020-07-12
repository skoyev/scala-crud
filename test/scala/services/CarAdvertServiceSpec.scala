package scala.services

import java.util.UUID

import dao.CarAdvertDao
import models.{Car, Diesel}
import org.scalatest.Outcome
import services.CarAdvertService

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.fixture._

class CarAdvertServiceSpec extends CarAdvFlatSpec {
  private val carAdvertService = instanceOf[CarAdvertService]

  behavior of "Car Advertisement Service"

  it should "correctly remove car" in { ctx =>
    val result = ctx.remove(car.encodedId)
    result.map {
      r => r must be(true)
    }
  }

  override type FixtureParam = CarAdvertService
  override def withFixture(test: OneArgTest): Outcome = {
    test(carAdvertService)
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

}
