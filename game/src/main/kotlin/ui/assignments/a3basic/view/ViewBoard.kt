package ui.assignments.a3basic.view

import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import ui.assignments.a3basic.model.*

// draw the board on gc
fun drawBoard(gc: GraphicsContext) {
    gc.apply {
        (0..10).forEach {
            stroke = Color.BLACK
            strokeLine(20.0 + it * 30.0, 20.0, 20.0 + it * 30.0, 320.0)
            strokeLine(20.0, 20.0 + it * 30.0, 320.0, 20.0 + it * 30.0)
        }
    }
}

// draw the label of row and column on gc
fun drawLabel(gc: GraphicsContext, type: String) {
    gc.apply {
        fill = Color.BLACK
        font = Font.font("Arial", 12.0)
        if (type == "ROW") {
            (1..10).forEach {
                fillText(it.toString(), 28.0 + 30.0 * (it - 1), 13.0)
                fillText(it.toString(), 28.0 + 30.0 * (it - 1), 335.0)
            }
        } else {
            val letters = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")
            var i = 0
            letters.forEach {
                fillText(it, 7.0, 40 + 30.0 * i)
                fillText(it, 327.0, 40 + 30.0 * i)
                i++
            }
        }
    }
}

class ViewBoard(val player: String, val model: Game): Canvas(340.0, 340.0), IView {
    init {
        this.graphicsContext2D.apply {
            this.fill = Color.LIGHTBLUE
            this.fillRect( 20.0, 20.0, 300.0, 300.0)     // size of board is 300 * 300 units
            drawBoard(this)
            drawLabel(this, "ROW")
            drawLabel(this, "COL")
        }

        // attack
        this.onMouseClicked = EventHandler {
            if (player == "AI" && model.start && !model.end) {
                val x = ((it.sceneX - 538.0) / 30.0).toInt()
                val y = ((it.sceneY - 45.0) / 30.0).toInt()
                println("attacking cell: (${x+1}, ${y+1})")
                model.humanAttack = true
                model.attackCell(x, y)
            }
        }
        model.listeners.add(this)
    }

    override fun update() {
    }
}