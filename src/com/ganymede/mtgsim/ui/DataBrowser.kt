package com.ganymede.mtgsim.ui

import com.ganymede.mtgsim.card.MtgCardData
import com.ganymede.mtgsim.io.JsonIO
import tornadofx.*
import java.nio.file.Paths

class DataBrowser: Fragment() {

    var cardData: MtgCardData?
        get() = this.cardView.cardData
        set(value) {
            this.cardView.cardData = value
        }

    private val cardView = CardView()
    override val root = vbox {
        this += cardView
    }

    init {
        val cards = JsonIO.cardsFromJson(Paths.get("./resources/AllCards_4-2_2018-12-31.json"))
        val test = cards.first { card -> card.name.contains("Whisper Agent") }
        cardData = test
    }

}


class DataBrowserApp: App(DataBrowser::class) {

}


fun main(args: Array<String>) {
    launch<DataBrowserApp>(args)
}