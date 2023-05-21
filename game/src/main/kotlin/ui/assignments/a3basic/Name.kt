package ui.assignments.a3basic

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight

// class for name of board
class Name(t: String): Label() {
    init {
        this.text = t
        font = Font.font(null, FontWeight.BOLD, FontPosture.REGULAR, 15.0)
        this.alignment = Pos.CENTER
        this.prefHeight = 25.0
        this.prefWidth = 300.0
    }
}