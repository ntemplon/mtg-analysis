package com.ganymede.mtgsim.card

import java.util.*

data class MtgCardData(
    val artist: String,
    val borderColor: String,
    val colorIdentity: ColorSet,
    val colorIndicator: ColorSet,
    val colors: ColorSet,
    val convertedManaCost: Float,
    val duelDeck: String,
    val faceConvertedManaCost: Float,
    val flavorText: String,
    val frameEffect: String,
    val frameVersion: String,
    val hasFoil: Boolean,
    val hasNonFoil: Boolean,
    val isAlternative: Boolean,
    val isFoilOnly: Boolean,
    val isOnlineOnly: Boolean,
    val isOversized: Boolean,
    val isReserved: Boolean,
    val isTimeshifted: Boolean,
    val layout: String,
    val legalities: Map<String, String>,
    val loyalty: String,
    val manaCost: ManaCost,
    val multiverseId: Int,
    val names: List<String>,
    val name: String,
    val number: String,
    val originalText: String,
    val originalType: String,
    val printings: List<String>,
    val power: String,
    val rarity: String,
    val rulings: List<Ruling>,
    val scryfallId: String,
    val side: String,
    val starter: Boolean,
    val subtypes: List<String>,
    val supertypes: List<String>,
    val text: String,
    val toughness: String,
    val type: String,
    val types: List<String>,
    val uuid: UUID,
    val variations: List<UUID>,
    val watermark: String
)

data class Ruling(val date: String, val text: String)

class CardSet(private val cards: Iterable<MtgCardData>): Iterable<MtgCardData> by cards

data class ColorSet(private val colors: List<MtgColor>): Iterable<MtgColor> by colors {
    companion object {
        val NO_COLORS = ColorSet(listOf())
    }
}

data class ManaCost(private val manaQuantities: Map<Set<MtgColor>, Float>) {

    val convertedManaCost: Float = manaQuantities.map { it.value }.sum()

    override fun toString(): String {
        return super.toString()
    }

    companion object {
        private val manaRegex = Regex("\\{[0-9WUBRG\\/]+\\}")

        fun fromMtgJsonString(mtgJsonString: String): ManaCost {
            val matches = manaRegex.findAll(mtgJsonString).map { it.value }
            val costs = matches.map { match ->
                val bracesRemoved = match.substring(1, match.length - 1)
                val colorlessNumber = bracesRemoved.toFloatOrNull()
                val colors: Set<MtgColor> = if (colorlessNumber != null) {
                    setOf(MtgColor.COLORLESS)
                } else {
                    val colorAbbreviations = bracesRemoved.split('/')
                    colorAbbreviations.mapNotNull { abbr -> MtgColor.fromAbbreviation(abbr) }.toSet()
                }

                val amountOfMana = colorlessNumber ?: 1.0f

                return@map colors to amountOfMana
            }.toMap()

            return ManaCost(costs)
        }
    }
}

enum class MtgColor(val abbreviation: String) {
    WHITE("W"),
    BLUE("U"),
    BLACK("B"),
    GREEN("G"),
    RED("R"),
    COLORLESS("");

    companion object {
        private val abbreviationLookup = MtgColor.values().map { it.abbreviation to it }.toMap()

        val WUBRG = ColorSet(listOf(WHITE, BLUE, BLACK, GREEN, RED))
        val ALL_COLORS = ColorSet(WUBRG + COLORLESS)

        fun fromAbbreviation(abbreviation: String): MtgColor? {
            return abbreviationLookup[abbreviation]
        }
    }
}