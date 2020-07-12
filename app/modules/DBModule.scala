package modules

import com.sksamuel.elastic4s.analyzers.{AsciiFoldingTokenFilter, LowercaseTokenFilter}
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.HttpClient
import database.ElasticsearchHttpClient
import org.slf4j.LoggerFactory
import play.api._
import play.api.inject._
import scala.concurrent.ExecutionContext.Implicits.global

class DBModule extends Module {

  private val logger = LoggerFactory.getLogger(getClass)

  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {

    // Elasticsearch
    val elasticsearch = new ElasticsearchHttpClient(configuration)
    val createIndexTemplateResult = createTemplate(elasticsearch).recover {
      case e =>
        logger.error("Error while creating index template:", e)
    }
    Seq(bind[ElasticsearchHttpClient].toInstance(elasticsearch))
  }

  private def createTemplate(client: HttpClient) = {
    for {
      _ <- client
           .execute {
             createIndexTemplate("generic-template-20180721", "*").normalizers(
               customNormalizer("lowercase-normalizer", LowercaseTokenFilter, AsciiFoldingTokenFilter)
             ).mappings(
               mapping("entry") templates(
                 dynamicTemplate("longs").mapping(
                   dynamicLongField().fields(longField("sort-field"))
                 ) matchMappingType "long",
                 dynamicTemplate("doubles").mapping(
                   dynamicDoubleField().fields(doubleField("sort-field"))
                 ) matchMappingType "double",
                 dynamicTemplate("dates").mapping(
                   dynamicDateField().fields(dateField("sort-field"))
                 ) matchMappingType "date",
                 dynamicTemplate("booleans").mapping(
                   dynamicBooleanField().fields(booleanField("sort-field"))
                 ) matchMappingType "boolean",
                 dynamicTemplate("strings").mapping(
                   dynamicTextField().fields(keywordField("sort-field").normalizer("lowercase-normalizer"))
                 ) matchMappingType "string"
               )
             )
           }
    } yield ()
  }
}