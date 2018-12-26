package com.ganymede.mtgsim.card

data class MtgCard(
    val artist: String,
    val borderColor: String,
    val colorIdentity: List<MtgColor>,
    val name: String,
    val text: String
)

class CardSet(private val cards: Iterable<MtgCard>): Iterable<MtgCard> by cards

enum class MtgColor(val abbreviation: String) {
    WHITE("W"),
    BLUE("U"),
    BLACK("B"),
    GREEN("G"),
    RED("R");

    private val abbreviationLookup = MtgColor.values().map { it.abbreviation to it }.toMap()

    fun fromAbbreviation(abbreviation: String): MtgColor? {
        return abbreviationLookup[abbreviation]
    }
}