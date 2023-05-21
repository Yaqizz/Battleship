package ui.assignments.a3basic

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight

// class for title of Player Harbour
class Title(t: String): Label() {
    init {
        this.text = t
        font = Font.font(null, FontWeight.BOLD, FontPosture.REGULAR, 12.0)
        this.alignment = Pos.CENTER
        this.prefHeight = 25.0
        this.prefWidth = 99.0
    }
}