package br.ufpa.smartufpa.utils

import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializer
import java.lang.reflect.Type


class BooleanTypeAdapter : JsonDeserializer<Boolean> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Boolean? {
        try {
            val code = json.asInt
            return if (code == 0) false else if (code == 1) true else null
        } catch (e: Exception) {
            return json.asBoolean
        }

    }
}
