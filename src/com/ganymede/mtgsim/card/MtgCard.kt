package com.ganymede.mtgsim.card

data class MtgCard(
    val artist: String,
    val borderColor: String,
    val colorIdentity: ColorSet,
    val colorIndicator: ColorSet,
    val colors: ColorSet,
    val convertedManaCost: Float,
    val flavorText: String,
    val name: String,
    val text: String
)

class CardSet(private val cards: Iterable<MtgCard>): Iterable<MtgCard> by cards

data class ColorSet(private val colors: List<MtgColor>): Iterable<MtgColor> by colors

enum class MtgColor(val abbreviation: String) {
    WHITE("W"),
    BLUE("U"),
    BLACK("B"),
    GREEN("G"),
    RED("R");

    companion object {
        private val abbreviationLookup = MtgColor.values().map { it.abbreviation to it }.toMap()

        fun fromAbbreviation(abbreviation: String): MtgColor? {
            return abbreviationLookup[abbreviation]
        }
    }
}