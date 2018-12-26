package com.ganymede.mtgsim.io

import com.ganymede.mtgsim.card.CardSet
import com.ganymede.mtgsim.card.ColorSet
import com.ganymede.mtgsim.card.MtgCard
import com.ganymede.mtgsim.card.MtgColor
import com.google.gson.*
import java.lang.Exception
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path

object JsonIO {
    val mtgJsonDeserializer: Gson = GsonBuilder()
        .registerTypeAdapter(MtgCard::class.java, CardReader)
        .registerTypeAdapter(CardSet::class.java, SetReader)
        .registerTypeAdapter(ColorSet::class.java, ColorSetReader)
        .create()

    fun cardsFromJson(dataFile: Path): CardSet = mtgJsonDeserializer.fromJson(String(Files.readAllBytes(dataFile)), CardSet::class.java)

    inline fun <reified T>fromJson(jsonData: JsonElement): T = mtgJsonDeserializer.fromJson(jsonData, T::class.java)
}


object CardReader: JsonDeserializer<MtgCard> {

    private const val ARTIST_KEY = "artist"
    private const val BORDER_COLOR_KEY = "borderColor"
    private const val COLOR_IDENTITY_KEY = "colorIdentity"
    private const val COLOR_INDICATOR_KEY = "colorIndicator"
    private const val COLORS_KEY = "colors"
    private const val CONVERTED_MANA_COST_KEY = "convertedManaCost"
    private const val FLAVOR_TEXT_KEY = "flavorText"
    private const val NAME_KEY = "name"
    private const val TEXT_KEY = "text"

    override fun deserialize(p0: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): MtgCard {
        if (p0 != null) {
            val obj = p0.asJsonObject

            val artist = obj.get(ARTIST_KEY)?.asString ?: ""
            val borderColor = obj.get(BORDER_COLOR_KEY)?.asString ?: ""
            val colorIdentity = JsonIO.fromJson<ColorSet>(obj.get(COLOR_IDENTITY_KEY))
            val colorIndicator = JsonIO.fromJson<ColorSet>(obj.get(COLOR_INDICATOR_KEY))
            val colors = JsonIO.fromJson<ColorSet>(obj.get(COLORS_KEY))
            val cmc = obj.get(CONVERTED_MANA_COST_KEY)?.asFloat ?: 0.0f
            val flavorText = obj.get(FLAVOR_TEXT_KEY)?.asString ?: ""
            val name = obj.get(NAME_KEY)?.asString ?: ""
            val text = obj.get(TEXT_KEY)?.asString ?: ""

            return MtgCard(
                artist = artist,
                borderColor = borderColor,
                colorIdentity = colorIdentity,
                colorIndicator = colorIndicator,
                colors = colors,
                convertedManaCost = cmc,
                flavorText = flavorText,
                name = name,
                text = text
            )
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
            return CardSet(obj.keySet().map { key -> JsonIO.fromJson<MtgCard>(obj.get(key)) })
        }
        else {
            throw Exception("The provided data isn't appropriate!")
        }
    }

}


object ColorSetReader: JsonDeserializer<ColorSet> {
    override fun deserialize(p0: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): ColorSet {
        if (p0 != null) {
            val obj = p0.asJsonArray
            return ColorSet(obj.map { it -> MtgColor.fromAbbreviation(it.asString)!! })
        }
        else {
            throw Exception("The provided data isn't appropriate!")
        }
    }
}