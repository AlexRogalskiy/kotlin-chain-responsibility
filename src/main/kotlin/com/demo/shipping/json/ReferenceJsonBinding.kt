package com.demo.shipping.json

import com.demo.shipping.domain.Reference
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.ObjectCodec
import com.fasterxml.jackson.databind.*

import java.io.IOException

class ReferenceJsonDeserializer : JsonDeserializer<Reference>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser,
                             deserializationContext: DeserializationContext): Reference {
        val objectCodec = jsonParser.codec
        val jsonNode = objectCodec.readTree<JsonNode>(jsonParser)

        return Reference(jsonNode.asText())

    }
}

class ReferenceJsonSerializer : JsonSerializer<Reference>() {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(
            reference: Reference, jsonGenerator: JsonGenerator,
            serializerProvider: SerializerProvider) {
        jsonGenerator.writeString(reference.id)
    }

}
 