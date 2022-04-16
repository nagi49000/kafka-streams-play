package kafkastreamsplay

import akka.util.ByteString
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}
import spray.json._

/**
  * Created by jero on 24/11/17.
  * https://gist.github.com/jeroenr/2895de32accd440c2558261a49952cab#file-jsonserde-scala
  */
class JsonSerializer[T >: Null <: Any : JsonFormat] extends Serializer[T] {
  override def configure(configs: java.util.Map[String, _], isKey: Boolean): Unit = ()

  override def serialize(topic: String, data: T): Array[Byte] = {
    data.toJson.compactPrint.getBytes
  }

  override def close(): Unit = ()
}

class JsonDeserializer[T >: Null <: Any : JsonFormat] extends Deserializer[T] {
  override def configure(configs: java.util.Map[String, _], isKey: Boolean): Unit = ()

  override def close(): Unit = ()

  override def deserialize(topic: String, data: Array[Byte]): T = {
    ByteString(data).utf8String.parseJson.convertTo[T]
  }
}

class JsonSerde[T >: Null <: Any : JsonFormat] extends Serde[T] {
  override def deserializer(): Deserializer[T] = new JsonDeserializer[T]

  override def serializer(): Serializer[T] = new JsonSerializer[T]

  override def configure(configs: java.util.Map[String, _], isKey: Boolean): Unit = ()

  override def close(): Unit = ()
}
