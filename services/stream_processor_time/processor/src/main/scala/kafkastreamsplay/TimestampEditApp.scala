package kafkastreamsplay

import scala.beans.BeanProperty

import java.util.Properties
import java.time.Duration

import spray.json.DefaultJsonProtocol

import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.streams.kstream.{Materialized, Consumed, Produced}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}
import org.slf4j.{Logger, LoggerFactory}
// import Serdes._

object Hello extends Greeting {
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}

// POJOs for JSON schema
sealed trait ApiModel

case class ProducerIntegers (
  randint: Int
) extends ApiModel

case class ProducerMessage (
  time: String,
  integers: ProducerIntegers
) extends ApiModel

object ApiModel extends DefaultJsonProtocol {
  implicit val ProducerIntegersFormat = jsonFormat1(ProducerIntegers)
  implicit val ProducerMessageFormat = jsonFormat2(ProducerMessage)
}

// will be exposed as main exe in jar
object TimestampEditApp extends App {
  val log: Logger = LoggerFactory.getLogger(TimestampEditApp.getClass)

  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "timestamp-edit-application")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-in1-url:9092")
    p
  }

  val builder: StreamsBuilder = new StreamsBuilder
  // val source: KStream[String, String] = builder.stream[String, String]("json-time-topic")
  val source: KStream[String, ProducerMessage] = builder.stream[String, ProducerMessage]("json-time-topic")(Consumed.`with`(Serdes.String, new JsonSerde[ProducerMessage]))

  source.
    mapValues {
      v => ProducerMessage(v.time.replace("Z", "+00:00"), v.integers)
    }.
    to("json-edited-time-topic")(Produced.`with`(Serdes.String, new JsonSerde[ProducerMessage]))

  val topology: Topology = builder.build()
  val streams: KafkaStreams = new KafkaStreams(topology, props)
  log.info(topology.describe().toString())
  log.info(props.toString())
  streams.start()

  sys.ShutdownHookThread {
     streams.close(Duration.ofSeconds(10, 0))
  }
}
