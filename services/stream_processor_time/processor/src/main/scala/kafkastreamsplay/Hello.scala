package kafkastreamsplay

import java.util.Properties
import java.time.Duration

import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

import Serdes._

object Hello extends Greeting {
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}

object TimestampEditApp extends App {
  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "timestamp-edit-application")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-in1-url:9092")
    p
  }

  val builder: StreamsBuilder = new StreamsBuilder
  val textLines: KStream[String, String] = builder.stream[String, String]("json-time-topic")
  textLines.to("json-edited-time-topic")

  val streams: KafkaStreams = new KafkaStreams(builder.build(), props)
  streams.start()

  sys.ShutdownHookThread {
     streams.close(Duration.ofSeconds(10, 0))
  }
}
