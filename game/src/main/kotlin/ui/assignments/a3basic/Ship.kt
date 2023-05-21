package ui.assignments.a3basic

import javafx.scene.shape.Rectangle
import ui.assignments.a3basic.model.*

// ship
class Ship(x: Double, y: Double, width: Double, height: Double, type: ShipType) :
    Rectangle(x, y, width, height) {
    val initx = x
    val inity = y
    var shipType = type
    var orientation = Orientation.Vertical
    var w = 30.0
    var h = height
    var id = -1
}