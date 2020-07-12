package scala

import java.math.BigInteger
import java.security.SecureRandom
import java.time.ZonedDateTime
import java.util.UUID

import com.github.javafaker.Faker

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

package object fixture {

	private val faker: Faker = new Faker()
	// https://github.com/DiUS/java-faker

	def random[T : TypeTag]: T = {
		val ttag = implicitly[TypeTag[T]]
		val mirror = ttag.mirror
		val classType = ttag.tpe.typeSymbol.asClass
		val constructorMirror = mirror.reflectClass(classType)
		val constructor = ttag.tpe.decl(universe.termNames.CONSTRUCTOR).asMethod

		val concreteArgs = constructor.paramLists.flatMap { pl =>
			pl.map(p => instantiateType(p.typeSignature, p.name.toString))
		}

		constructorMirror
			.reflectConstructor(constructor)
			.apply(concreteArgs: _*)
			.asInstanceOf[T]
	}

	def random[T : TypeTag](first: (String, Any), rest: (String, Any)*): T = {
		val ttag = implicitly[TypeTag[T]]
		val mirror = ttag.mirror
		val classType = ttag.tpe.typeSymbol.asClass
		val constructorMirror = mirror.reflectClass(classType)
		val constructor = ttag.tpe.decl(universe.termNames.CONSTRUCTOR).asMethod

		val argsMap = (first +: rest).toMap

		val concreteArgs = constructor.paramLists.flatMap { pl =>
			pl.map { param =>
				argsMap.getOrElse(param.name.encodedName.toString, instantiateType(param.typeSignature, param.name.toString))
			}
		}

		constructorMirror
			.reflectConstructor(constructor)
			.apply(concreteArgs: _*)
			.asInstanceOf[T]
	}

	private def randomHundredThousandNumber: String = (Math.random() * 100000).toInt.toString

	private def instantiateType(paramType: Type, paramName: String): AnyRef = paramType match {
		case t if t <:< typeOf[Boolean] => Boolean.box(false)
		case t if t <:< typeOf[BigDecimal] => new BigDecimal(new java.math.BigDecimal(Math.random()))
		case t if t <:< typeOf[BigInt] => new BigInt(new BigInteger(1000, new SecureRandom()))
		case t if t <:< typeOf[Int] => Int.box((Math.random() * 1000).toInt)
		case t if t <:< typeOf[Long] => Long.box((Math.random() * 1000).toLong)
		case t if t <:< typeOf[Number] => Int.box((Math.random() * 1000).toInt)
		case t if t <:< typeOf[String] => UUID.randomUUID().toString
		case t if t <:< typeOf[UUID] => UUID.randomUUID()
		case t if t <:< typeOf[ZonedDateTime] => ZonedDateTime.now()
		case t if t <:< typeOf[Option[_]] => None
		case t if t <:< typeOf[List[_]] => List()
		case t if t <:< typeOf[Set[_]] => Set()
		case _ => null
	}
}