package org.chicagoedt.rosetteweb

import org.w3c.dom.HTMLElement
import kotlin.browser.*
import org.chicagoedt.rosette.*

/**
 * The game that the browser is running
 */
internal val game = Game(getLevels(), getRobots())

/**
 * The driver for the grid canvas
 */
private lateinit var gridDriver : GridDriver

/**
 * The driver for the editor area
 */
 private lateinit var editorDriver : EditorDriver

@JsName("onLoad")
fun onLoad(){
    gridDriver = GridDriver(game)
    gridDriver.calculateNewLevel()

    editorDriver = EditorDriver(game, (document.getElementById("editor") as HTMLElement))
    editorDriver.calculateNewLevel()

    val header = document.getElementById("header") as HTMLElement
    header.innerHTML = game.currentLevel.properties.name
}

@JsName("onResize")
fun onResize(){
    //do nothing
}

/**
 * The main function to run when the page loads
 * @param args The arguments to run. Not currently used at all.
 */
fun main(args: Array<String>) {
    game.attachEventListener(::update)
    val windowType = js("typeof window")
    if (windowType != "undefined"){
        window.onresize = {onResize()}
        window.onload = {onLoad()}
    } 
}

fun getTypeOf(elem : Any) : String{
    return js("typeof elem")
}

/**
 * Updates according to the event from the game
 * @param e The event coming from the game
 */
fun update(e : Event){
    when (e){
        Event.LEVEL_UPDATE -> refresh()
    }
}

/**
 * Refreshes the display of the game
 */
fun refresh(){
    if (::gridDriver.isInitialized){
        gridDriver.refresh()
    }
}