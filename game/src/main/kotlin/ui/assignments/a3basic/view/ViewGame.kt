package ui.assignments.a3basic.view

import ui.assignments.a3basic.*
import ui.assignments.a3basic.model.*
import javafx.event.EventHandler
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.input.MouseButton
import javafx.scene.layout.AnchorPane
import java.util.Timer
import java.util.TimerTask
import kotlin.math.absoluteValue

var selectedShip: Rectangle? = null

// swap the h(height) with w(width) in ship
fun swapWH(ship: Ship) {
    val oldWidth = ship.w
    ship.w = ship.h
    ship.h = oldWidth
}

// animate the transition to original position in the Player Harbour
fun back(ship: Ship) {
    if (selectedShip == ship) {
        var step = 1
        val eachx = (ship.initx - ship.x) / 10
        val eachy = (ship.inity - ship.y) / 10
        Timer().apply {
            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    if (step < 11) {
                        ship.x += eachx
                        ship.y += eachy
                        if (ship.orientation == Orientation.Horizontal) {
                            ship.rotate = 90.0 + 9.0 * step
                        }
                        step++
                    } else {
                        ship.orientation = Orientation.Vertical
                        this.cancel()
                    }
                }
            }, 0L, 20L)
        }
    }
}


class ViewGame(val model: Game, anchor: AnchorPane): Pane(), IView {
    init {
        this.children.add(anchor)
        // destroyer
        destroyer.apply {
            stroke = Color.BLACK
            strokeWidth = 1.0
            fill = Color.LIGHTPINK
        }
        this.children.add(destroyer)
        addEvent(destroyer)

        // cruiser
        cruiser.apply {
            stroke = Color.BLACK
            strokeWidth = 1.0
            fill = Color.AQUAMARINE
        }
        this.children.add(cruiser)
        addEvent(cruiser)

        // submarine
        submarine.apply {
            stroke = Color.BLACK
            strokeWidth = 1.0
            fill = Color.LIGHTSTEELBLUE
        }
        this.children.add(submarine)
        addEvent(submarine)

        // battleship
        battleship.apply {
            stroke = Color.BLACK
            strokeWidth = 1.0
            fill = Color.LIGHTSALMON
        }
        this.children.add(battleship)
        addEvent(battleship)

        // carrier
        carrier.apply {
            stroke = Color.BLACK
            strokeWidth = 1.0
            fill = Color.LEMONCHIFFON
        }
        this.children.add(carrier)
        addEvent(carrier)

        this.onMouseMoved = EventHandler {
            if (selectedShip != null) {
                selectedShip!!.x = it.sceneX - (selectedShip!!.width / 2)
                selectedShip!!.y = it.sceneY - (selectedShip!!.height / 2)
            }
        }
        model.listeners.add(this)
    }
    override fun update() {
        if (model.start) {
            destroyer.isMouseTransparent = true
            cruiser.isMouseTransparent = true
            submarine.isMouseTransparent = true
            battleship.isMouseTransparent = true
            carrier.isMouseTransparent = true
        }
    }

    // animate the rotation from horizontal to vertical or from vertical to horizontal
    private fun rotation(ship: Ship) {
        if (selectedShip == ship) {
            var step = 1
            Timer().apply {
                scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        if (step < 11) {
                            if (ship.orientation == Orientation.Horizontal) {
                                ship.rotate = 90.0 + 9.0 * step
                            } else {
                                ship.rotate = 9.0 * step
                            }
                            step++
                        } else {
                            if (ship.orientation == Orientation.Vertical) {
                                ship.orientation = Orientation.Horizontal
                            } else {
                                ship.orientation = Orientation.Vertical
                            }
                            this.cancel()
                        }
                    }
                }, 0L, 20L)
            }
        }
    }


    // return shipid if we can place ship in the board horizontally and -1 otherwise
    private fun horizontalCheck(ship: Ship, xa: Double, ya: Double, xmove: Double, ymove: Double): Int {
        var x = ((ship.x - xa) / 30.0).toInt()
        if ((ship.x - xa) % 30.0 in 15.0.. 30.0) x++
        val y = ((ship.y - ya) / 30.0).toInt()
        val place = model.placeShip(ship.shipType, ship.orientation, x, y)
        if (place >= 0) {
            ship.x = xa + x * 30.0 + xmove
            ship.y = ya + y * 30.0 + ymove
            ship.id = place
        }
        return place
    }

    // return shipid if we can place ship in board and -1 otherwise
    private fun checkPlace(ship: Ship): Int {
        val place: Int
        if (ship.orientation == Orientation.Vertical) {
            var x = ((ship.x - 99.0) / 30.0).toInt()
            if (((ship.x - 99.0) % 30.0) in 22.5..30.0) x++
            var y = ((ship.y - 45.0 ) / 30.0).toInt()
            if (((ship.y - 45.0 ) % 30.0).absoluteValue in 15.0..30.0) y++
            place = model.placeShip(ship.shipType, ship.orientation, x, y)
            if (place >= 0) {
                ship.x = 99.0 + x * 30.0 + 6.0
                ship.y = 45.0 + y * 30.0
                ship.id = place
            }
        } else {
            place = when(ship.shipType) {
                ShipType.Destroyer -> horizontalCheck(ship, 121.5, 15.0, 0.0, 15.0)
                ShipType.Cruiser -> horizontalCheck(ship, 136.5, 4.0, 0.0, 11.0)
                ShipType.Submarine -> horizontalCheck(ship, 136.5, 4.0, 0.0, 11.0)
                ShipType.Battleship -> horizontalCheck(ship, 151.5, -13.0, 0.0, 13.0 )
                ShipType.Carrier -> horizontalCheck(ship, 166.5, -31.0, 0.0, 17.0)
            }
        }
        return place
    }

    // addEvent adds the mouse event to ship
    private fun addEvent(ship: Ship) {
        ship.setOnMouseClicked { event ->
            if (event.button == MouseButton.PRIMARY) {
                if (selectedShip == null) {
                    selectedShip = ship
                    // if move ship and put it in another place again
                    if (ship.id != -1) {
                        model.removeShip(ship.id)
                        ship.id = -1
                    }
                } else if (selectedShip == ship) {
                    // if not placed, go back
                    selectedShip = if (checkPlace(ship) == -1) {
                        back(ship)
                        swapWH(ship)
                        null
                    } else {
                        null
                    }
                }
            } else if (event.button == MouseButton.SECONDARY) {
                if (selectedShip == ship) {
                    rotation(ship)
                    swapWH(ship)
                    ship.x = event.sceneX - (ship.width / 2)
                    ship.y = event.sceneY - (ship.height / 2)
                }
            }
        }
    }
}