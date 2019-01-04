package com.ganymede.mtgsim.io

import com.ganymede.mtgsim.card.*
import com.google.gson.*
import java.lang.Exception
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object JsonIO {
    val mtgJsonDeserializer: Gson = GsonBuilder()
        .registerTypeAdapter(MtgCardData::class.java, CardReader)
        .registerTypeAdapter(CardSet::class.java, SetReader)
        .registerTypeAdapter(ColorSet::class.java, ColorSetReader)
        .create()

    fun cardsFromJson(dataFile: Path): CardSet = mtgJsonDeserializer.fromJson(String(Files.readAllBytes(dataFile)), CardSet::class.java)

    inline fun <reified T>fromJson(jsonData: JsonElement): T = mtgJsonDeserializer.fromJson(jsonData, T::class.java)
}


object CardReader: JsonDeserializer<MtgCardData> {

    private const val ARTIST_KEY = "artist"
    private const val BORDER_COLOR_KEY = "borderColor"
    private const val COLOR_IDENTITY_KEY = "colorIdentity"
    private const val COLOR_INDICATOR_KEY = "colorIndicator"
    private const val COLORS_KEY = "colors"
    private const val CONVERTED_MANA_COST_KEY = "convertedManaCost"
    private const val DUEL_DECK_KEY = "duelDeck"
    private const val FACE_CONVERTED_MANA_COST_KEY = "faceConvertedManaCost"
    private const val FLAVOR_TEXT_KEY = "flavorText"
    private const val FRAME_EFFECT_KEY = "frameEffect"
    private const val FRAME_VERSION_KEY = "frameVersion"
    private const val HAS_FOIL_KEY = "hasFoil"
    private const val HAS_NON_FOIL_KEY = "hasNonFoil"
    private const val IS_ALTERNATIVE_KEY = "isAlternative"
    private const val IS_FOIL_ONLY_KEY = "isFoilOnly"
    private const val IS_ONLINE_ONLY_KEY = "isOnlineOnly"
    private const val IS_OVERSIZED_KEY = "isOversized"
    private const val IS_RESERVED_KEY = "isReserved"
    private const val IS_TIMESHIFTED_KEY = "isTimeshifted"
    private const val LAYOUT_KEY = "layout"
    private const val LEGALITIES_KEY = "legalities"
    private const val LOYALTY_KEY = "loyalty"
    private const val MANA_COST_KEY = "manaCost"
    private const val MULTIVERSE_ID_KEY = "multiverseId"
    private const val NAME_KEY = "name"
    private const val NAMES_KEY = "names"
    private const val NUMBER_KEY = "number"
    private const val ORIGINAL_TEXT_KEY = "originalText"
    private const val ORIGINAL_TYPE_KEY = "originalType"
    private const val PRINTINGS_KEY = "printings"
    private const val POWER_KEY = "power"
    private const val RARITY_KEY = "rarity"
    private const val RULINGS_KEY = "rulings"
    private const val RULING_DATE_KEY = "date"
    private const val RULING_TEXT_KEY = "text"
    private const val SCRYFALL_ID_KEY = "scryfallId"
    private const val SIDE_KEY = "side"
    private const val STARTER_KEY = "starter"
    private const val SUBTYPES_KEY = "subtypes"
    private const val SUPERTYPES_KEY = "supertypes"
    private const val TEXT_KEY = "text"
    private const val TOUGHNESS_KEY = "toughness"
    private const val TYPE_KEY = "type"
    private const val TYPES_KEY = "types"
    private const val UUID_KEY = "uuid"
    private const val VARIATIONS_KEY = "variations"
    private const val WATERMARK_KEY = "watermark"

    private fun JsonObject.getBoolean(key: String, default: Boolean = false): Boolean = this.get(key)?.asBoolean ?: default
    private fun JsonObject.getFloat(key: String, default: Float = 0.0f): Float = this.get(key)?.asFloat ?: default
    private fun JsonObject.getInt(key: String, default: Int = 0): Int = this.get(key)?.asInt ?: default
    private fun JsonObject.getString(key: String, default: String = ""): String = this.get(key)?.asString ?: default

    override fun deserialize(p0: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): MtgCardData {
        if (p0 != null && p0.isJsonObject) {
            val obj = p0.asJsonObject

            val artist = obj.getString(ARTIST_KEY)
            val borderColor = obj.getString(BORDER_COLOR_KEY)
            val colorIdentity = JsonIO.fromJson<ColorSet>(obj.get(COLOR_IDENTITY_KEY))

            val colorIndicatorJson = obj.get(COLOR_INDICATOR_KEY)
            val colorIndicator: ColorSet = if(colorIndicatorJson != null) {
                JsonIO.fromJson(colorIndicatorJson)
            } else {
                ColorSet.NO_COLORS
            }

            val colors = JsonIO.fromJson<ColorSet>(obj.get(COLORS_KEY))
            val cmc = obj.getFloat(CONVERTED_MANA_COST_KEY)
            val duelDeck = obj.getString(DUEL_DECK_KEY)
            val faceCmc = obj.getFloat(FACE_CONVERTED_MANA_COST_KEY)
            val flavorText = obj.getString(FLAVOR_TEXT_KEY)
            val frameEffect = obj.getString(FRAME_EFFECT_KEY)
            val frameVersion = obj.getString(FRAME_VERSION_KEY)
            val hasFoil = obj.getBoolean(HAS_FOIL_KEY)
            val hasNonFoil = obj.getBoolean(HAS_NON_FOIL_KEY)
            val isAlternative = obj.getBoolean(IS_ALTERNATIVE_KEY)
            val isFoilOnly = obj.getBoolean(IS_FOIL_ONLY_KEY)
            val isOnlineOnly = obj.getBoolean(IS_ONLINE_ONLY_KEY)
            val isOversized = obj.getBoolean(IS_OVERSIZED_KEY)
            val isReserved = obj.getBoolean(IS_RESERVED_KEY)
            val isTimeshifted = obj.getBoolean(IS_TIMESHIFTED_KEY)
            val layout = obj.getString(LAYOUT_KEY)

            val legalitiesObj = obj.get(LEGALITIES_KEY)?.asJsonObject
            val legalities = legalitiesObj
                ?.keySet()
                ?.map { key -> key to legalitiesObj.getString(key) }
                ?.toMap() ?: mapOf()

            val loyalty = obj.getString(LOYALTY_KEY)
            val manaCost = ManaCost.fromMtgJsonString(obj.getString(MANA_COST_KEY))
            val multiverseID = obj.getInt(key = MULTIVERSE_ID_KEY, default = -1)
            val name = obj.getString(NAME_KEY)
            val names = obj.get(NAMES_KEY)?.asJsonArray?.mapNotNull { it.asString } ?: listOf()
            val number = obj.getString(NUMBER_KEY)
            val originalText = obj.getString(ORIGINAL_TEXT_KEY)
            val originalType = obj.getString(ORIGINAL_TYPE_KEY)
            val printings = obj.get(PRINTINGS_KEY)?.asJsonArray?.map { it.asString } ?: listOf()
            val power = obj.getString(POWER_KEY)
            val rarity = obj.getString(RARITY_KEY)

            val rulings = obj.get(RULINGS_KEY)?.asJsonArray
                ?.filter { it.isJsonObject }
                ?.map { it.asJsonObject }
                ?.map { rulingJson ->
                    val date = rulingJson.getString(RULING_DATE_KEY)
                    val text= rulingJson.getString(RULING_TEXT_KEY)
                    Ruling(date, text)
                } ?: listOf()

            val scryfallId = obj.getString(SCRYFALL_ID_KEY)
            val side = obj.getString(SIDE_KEY)
            val starter = obj.getBoolean(STARTER_KEY)
            val subtypes = obj.get(SUBTYPES_KEY)?.asJsonArray?.map { it.asString } ?: listOf()
            val supertypes = obj.get(SUPERTYPES_KEY)?.asJsonArray?.map { it.asString } ?: listOf()
            val text = obj.getString(TEXT_KEY)
            val toughness = obj.getString(TOUGHNESS_KEY)
            val type = obj.getString(TYPE_KEY)
            val types = obj.get(TYPES_KEY)?.asJsonArray?.map { it.asString } ?: listOf()
            val uuid = UUID.fromString(obj.getString(UUID_KEY))
            val variations = obj.get(VARIATIONS_KEY)?.asJsonArray?.map { UUID.fromString(it.asString) } ?: listOf()
            val watermark = obj.getString(WATERMARK_KEY)

            return MtgCardData(
                artist = artist,
                borderColor = borderColor,
                colorIdentity = colorIdentity,
                colorIndicator = colorIndicator,
                colors = colors,
                convertedManaCost = cmc,
                duelDeck = duelDeck,
                faceConvertedManaCost = faceCmc,
                flavorText = flavorText,
                frameEffect = frameEffect,
                frameVersion = frameVersion,
                hasFoil = hasFoil,
                hasNonFoil = hasNonFoil,
                isAlternative = isAlternative,
                isFoilOnly = isFoilOnly,
                isOnlineOnly = isOnlineOnly,
                isOversized = isOversized,
                isReserved = isReserved,
                isTimeshifted = isTimeshifted,
                layout = layout,
                legalities = legalities,
                loyalty = loyalty,
                manaCost = manaCost,
                multiverseId = multiverseID,
                name = name,
                names = names,
                number = number,
                originalText = originalText,
                originalType = originalType,
                printings = printings,
                power = power,
                rarity = rarity,
                rulings = rulings,
                scryfallId = scryfallId,
                side = side,
                starter = starter,
                subtypes = subtypes,
                supertypes = supertypes,
                text = text,
                toughness = toughness,
                type = type,
                types = types,
                uuid = uuid,
                variations = variations,
                watermark = watermark
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
            return CardSet(obj.keySet().map { key -> JsonIO.fromJson<MtgCardData>(obj.get(key)) })
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