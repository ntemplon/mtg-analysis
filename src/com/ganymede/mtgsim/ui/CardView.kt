package com.ganymede.mtgsim.ui

import com.ganymede.mtgsim.card.MtgCardData
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import tornadofx.*

class CardView: Fragment() {

    private val cardFlavorTextProperty = SimpleStringProperty("")
    private val cardNameProperty = SimpleStringProperty("")
    private val cardPowerProperty = SimpleStringProperty("")
    private val cardTextProperty = SimpleStringProperty("")
    private val cardToughnessProperty = SimpleStringProperty("")
    private val cardTypeProperty = SimpleStringProperty("")

    private val powerToughnessSeparatorProperty = SimpleStringProperty("")

    var cardData: MtgCardData? = null
        set(value) {
            field = value
            setPropertiesFor(value)
        }

    override val root = vbox {
        spacing = 3.0

        label {
            bind(cardNameProperty)

            font = Font.font(font.name, FontWeight.BOLD, font.size)
        }

        label {
            bind(cardTypeProperty)
        }

        textarea {
            bind(cardTextProperty)
            isWrapText = true
            isEditable = false
        }

        textarea {
            bind(cardFlavorTextProperty)
            isEditable = false
            isWrapText = true

            font = Font.font(font.name, FontPosture.ITALIC, font.size)
        }

        hbox {
            alignment = Pos.CENTER_RIGHT

            label {
                bind(cardPowerProperty)
            }

            label {
                bind(powerToughnessSeparatorProperty)
            }

            label {
                bind(cardToughnessProperty)
            }
        }
    }


    private fun setPropertiesFor(data: MtgCardData?) {
        cardNameProperty.set(data?.name ?: "")
        cardTextProperty.set(data?.text ?: "")
        cardTypeProperty.set(data?.type ?: "")
        cardFlavorTextProperty.set(data?.flavorText ?: "")
        cardPowerProperty.set(data?.power ?: "")
        cardToughnessProperty.set(data?.toughness ?: "")

        if (data?.power.isNullOrEmpty() && data?.toughness.isNullOrEmpty()) {
            powerToughnessSeparatorProperty.set("")
        }
        else {
            powerToughnessSeparatorProperty.set(" / ")
        }
    }

}