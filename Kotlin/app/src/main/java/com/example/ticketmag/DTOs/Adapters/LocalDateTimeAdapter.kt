package com.example.ticketmag.DTOs.Adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : TypeAdapter<LocalDateTime?>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun write(out: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(formatter.format(value))
        }
    }

    override fun read(`in`: JsonReader): LocalDateTime? {
        return when (`in`.peek()) {
            com.google.gson.stream.JsonToken.NULL -> {
                `in`.nextNull()
                null
            }
            com.google.gson.stream.JsonToken.STRING -> {
                val dateStr = `in`.nextString()
                if (dateStr.isEmpty()) null else LocalDateTime.parse(dateStr, formatter)
            }
            else -> throw IllegalStateException("Expected STRING or NULL for LocalDateTime, got ${`in`.peek()}")
        }
    }
}