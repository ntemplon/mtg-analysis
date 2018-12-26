package com.ganymede.mtgsim.io

import com.ganymede.mtgsim.card.CardSet
import com.ganymede.mtgsim.card.MtgCard
import com.google.gson.*
import java.lang.Exception
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path

object JsonIO {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(MtgCard::class.java, CardReader)
        .registerTypeAdapter(CardSet::class.java, SetReader)
        .create()

    fun cardsFromJson(dataFile: Path): CardSet = gson.fromJson(String(Files.readAllBytes(dataFile)), CardSet::class.java)

    fun cardFromJson(jsonData: JsonElement): MtgCard = gson.fromJson(jsonData, MtgCard::class.java)
}


object CardReader: JsonDeserializer<MtgCard> {

    private const val NAME_KEY = "name"
    private const val TEXT_KEY = "text"

    override fun deserialize(p0: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): MtgCard {
        if (p0 != null) {
            val obj = p0.asJsonObject

            val name = obj.get(NAME_KEY).asString
            val text = obj.get(TEXT_KEY)?.asString ?: ""

            return MtgCard(name, text)
        }
        else {
            throw Exception("The provided data isn't appropriate!")
        }
    }

}


object SetReader: JsonDeserializer<CardSet> {

    override fun deserialize(p0: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): CardSet {
        if (p0 != null) {
            val obj = p0.asJsonObject
            return CardSet(obj.keySet().map { key -> JsonIO.cardFromJson(obj.get(key)) })
        }
        else {
            throw Exception("The provided data isn't appropriate!")
        }
    }

}