package ui.assignments.a3basic

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import ui.assignments.a3basic.model.*
import ui.assignments.a3basic.view.*


var destroyer: Ship = Ship(419.0, 30.0, 15.0, 60.0,  ShipType.Destroyer)
var cruiser: Ship = Ship(438.8, 30.0, 15.0, 90.0,  ShipType.Cruiser)
var submarine: Ship = Ship(458.6, 30.0, 15.0, 90.0,  ShipType.Submarine)
var battleship: Ship = Ship(478.4, 30.0, 15.0, 120.0,  ShipType.Battleship)
var carrier: Ship = Ship(498.2, 30.0, 15.0, 150.0,  ShipType.Carrier)

val game = Game(10, false)
val player = ViewBoard("Player", game)
val ai = ViewBoard("AI", game)


// change attack state on the board of player in game

// make a ship based on the shipType
fun makeShip(type: ShipType): Ship {
    val ship: Ship = when(type) {
        ShipType.Destroyer -> destroyer
        ShipType.Cruiser -> cruiser
        ShipType.Submarine -> submarine
        ShipType.Battleship -> battleship
        ShipType.Carrier -> carrier
    }
    return ship
}

// put ship to its original place in Player Harbour
fun backit(ship: Ship) {
    ship.x = ship.initx
    ship.y = ship.inity
    if (ship.orientation == Orientation.Horizontal) {
        ship.rotate = 180.0
    }
}

// put all ships that have been sunk on the board, all other ships return to Player Harbour
fun putback(player: Player, game: Game) {
    val listShip: MutableList<Ship> = mutableListOf()
    val b = game.boards[player]!!
    val idtotype = b.idToType
    idtotype.forEach { (key, value) ->
        if (!b.isSunk(key)) {
            val s = makeShip(value)
            listShip.add(s)
        }
    }
    listShip.forEach {
        backit(it)
    }
}

// sunk ships on the board of player in game remain on the board
fun remain(player: Player, board: ViewBoard, game: Game) {
    val gc = board.graphicsContext2D
    val cellsState: List<List<CellState>> = game.getBoard(player)
    gc.apply {
        cellsState.forEachIndexed { j, it ->
            it.forEachIndexed { i, cellState ->
                if (cellState == CellState.ShipSunk) {
                    gc.fill = Color.DARKGRAY
                } else {
                    gc.fill = Color.LIGHTBLUE
                }
                fillRect(20.0 + i * 30, 20.0 + j * 30.0, 30.0, 30.0)
            }
        }
    }
    drawBoard(gc)
}

class Battleship : Application() {
    override fun start(stage: Stage) {
        val computer = AI(game)
        game.startGame()

        val namePlayer = Name("My Formation")
        val nameAI = Name("Opponent's Formation")
        val nameFleet = Title("My Fleet")
        val controller = ViewController(game)
        val anchor = AnchorPane().apply {
            children.add(0, player.apply {
                AnchorPane.setTopAnchor(this, 25.0)
                AnchorPane.setLeftAnchor(this, 79.0)
            })
            children.add(0, ai.apply {
                AnchorPane.setTopAnchor(this, 25.0)
                AnchorPane.setRightAnchor(this, 17.0)
            })
            children.add(0, namePlayer.apply {
                AnchorPane.setTopAnchor(this, 3.0)
                AnchorPane.setLeftAnchor(this, 99.0)
            })
            children.add(0, nameAI.apply {
                AnchorPane.setTopAnchor(this, 3.0)
                AnchorPane.setRightAnchor(this, 37.0)
            })
            children.add(0, nameFleet.apply {
                AnchorPane.setTopAnchor(this, 3.0)
                AnchorPane.setLeftAnchor(this, 419.0)
            })
            children.add(0, controller.apply {
                AnchorPane.setTopAnchor(this, 330.0)
                AnchorPane.setLeftAnchor(this, 419.0)
            })
            prefWidth = 875.0
            prefHeight = 375.0
        }
        val root = ViewGame(game, anchor)

        // add listener to gameState
        game.gameStateProperty.addListener { _, _, newValue ->
            if (newValue == GameState.AiSetup){
                computer.placeShips()
            } else if (newValue == GameState.HumanWon) {
                // sunk ship on the board, other back
                putback(Player.Human, game)
                remain(Player.Human, player, game)
                remain(Player.Ai, ai, game)
                nameFleet.text = "You won!"
                game.end = true
            } else if (newValue == GameState.AiWon) {
                remain(Player.Human, player, game)
                remain(Player.Ai, ai, game)
                nameFleet.text = "You were defeated!"
                game.end = true
            } else if (newValue == GameState.HumanAttack) {
                if (game.start && game.humanAttack) {
                    //changeState(Player.Ai, ai, game)
                    game.humanAttack = false
                }
            } else if (newValue == GameState.AiAttack) {
                if (game.start) {
                    computer.attackCell()
                    //changeState(Player.Human, player, game)
                    game.humanAttack = false
                }
            }
        }

        stage.apply {
            scene = Scene(root, 875.0, 375.0)
            title = "Battleship"
        }.show()
    }


}