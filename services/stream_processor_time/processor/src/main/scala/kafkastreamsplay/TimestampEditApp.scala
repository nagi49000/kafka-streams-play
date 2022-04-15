package kafkastreamsplay

import java.util.Properties
import java.time.Duration

import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}
import org.slf4j.{Logger, LoggerFactory}

import Serdes._

object Hello extends Greeting {
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}

// POJOs for JSON schema
class ProducerIntegers (
  val randint: Int
) {}

class ProducerMessage (
  val time: String,
  val integers: ProducerIntegers
) {}


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
  val source: KStream[String, String] = builder.stream[String, String]("json-time-topic")

  def timeValueTweak(k: String, v: String): String = {
    log.error(k)
    if (k == "time") {
      v.replace("Z", "+00:00")
    }
    else v
  }

  // def timeValueTweak(k: String, v: String) = if (k == "time") v.replace("Z", "+00:00") else v

  source.
    map {
      (key, value) => (key, timeValueTweak(key, value))
    }.
    to("json-edited-time-topic")

  val topology: Topology = builder.build()
  val streams: KafkaStreams = new KafkaStreams(topology, props)
  log.info(topology.describe().toString())
  log.info(props.toString())
  streams.start()

  sys.ShutdownHookThread {
     streams.close(Duration.ofSeconds(10, 0))
  }
}
