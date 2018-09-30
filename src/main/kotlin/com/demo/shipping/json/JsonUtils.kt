package com.demo.shipping.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException


val mapper = ObjectMapper()

@Throws(IOException::class)
fun Any.toJson(): String {
    mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS)
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
}
