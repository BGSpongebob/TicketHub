package com.example.ticketmag.DTOs.Adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter : TypeAdapter<LocalDate?>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE // "yyyy-MM-dd" format

    override fun write(out: JsonWriter, value: LocalDate?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(formatter.format(value))
        }
    }

    override fun read(`in`: JsonReader): LocalDate? {
        return when (`in`.peek()) {
            com.google.gson.stream.JsonToken.NULL -> {
                `in`.nextNull()
                null
            }
            com.google.gson.stream.JsonToken.STRING -> {
                val dateStr = `in`.nextString()
                if (dateStr.isEmpty()) null else LocalDate.parse(dateStr, formatter)
            }
            else -> throw IllegalStateException("Expected STRING or NULL for LocalDate, got ${`in`.peek()}")
        }
    }
}