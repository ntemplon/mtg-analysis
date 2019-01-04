package com.ganymede.mtgsim

import com.ganymede.mtgsim.io.JsonIO
import java.nio.file.Paths

fun main(args: Array<String>) {
    val cards = JsonIO.cardsFromJson(Paths.get("./resources/AllCards_4-2_2018-12-31.json"))
    val test = cards.first { card -> card.name.contains("Goblin Banner") }
    println(test)
}
