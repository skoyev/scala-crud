package database

import java.net.URI

import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.{HttpClient, HttpRequestClient}
import javax.inject.Inject
import play.api.Configuration

class ElasticsearchHttpClient @Inject()(configuration: Configuration) extends HttpClient {

  val refresh = configuration.getBoolean("elasticsearch.refresh").getOrElse(false)

  private val httpClient = {
    val esUrl = new URI(configuration.getString("elasticsearch.url").get)
    val esHost = esUrl.getHost
    val esPort = if (esUrl.getPort == 0) 9200 else esUrl.getPort
    HttpClient(ElasticsearchClientUri(esHost, esPort))
  }
  override def close(): Unit = httpClient.close()
  override def client: HttpRequestClient = httpClient.client
}
