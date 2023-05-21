package ui.assignments.a3basic.view

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import ui.assignments.a3basic.model.*

class ViewController(val model: Game): Pane(), IView {
    private var startGame = Button()
    private var exitGame = Button()

    init {
        startGame.apply {
            text = "Start Game"
            prefWidth = 85.0
            isDisable = true
            translateY = -30.0
        }
        startGame.setOnAction {
            startGame.isDisable = true
            model.start = true
            model.startGame()
        }
        exitGame.apply {
            text = "Exit Game"
            prefWidth = 85.0
        }
        exitGame.setOnAction {
            Platform.exit()
        }
        this.children.addAll(startGame, exitGame)
        this.prefWidth = 99.0
        this.prefHeight = 40.0
        model.listeners.add(this)
    }

    override fun update() {
        if (model.activePlayer == Player.Human &&
            model.getShipsPlacedCount(Player.Human) == 5) {
            startGame.isDisable = false
        }
    }

}