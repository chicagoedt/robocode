package org.chicagoedt.rosetteweb

import jQuery
import org.w3c.dom.*
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.robots.*
import org.chicagoedt.rosette.levels.*

/**
 * The game that the browser is running
 */
internal lateinit var game : Game

/**
 * The driver for the grid canvas
 */
private lateinit var gridDriver : GridDriver

/**
 * The driver for the editor area
 */
private lateinit var editorDriver : EditorDriver

/**
 * The configurator for the game
 */
private lateinit var configDriver : ConfigDriver

fun setup(robots : ArrayList<Robot>, levels : ArrayList<Level>){
    game = Game(levels, robots)
    game.attachEventListener(::update)
    window.onresize = {onResize()}
    if (document.readyState == DocumentReadyState.Companion.COMPLETE){
        onLoad()
    }
    else{
        window.onload = {onLoad()}
    }
}

@JsName("onLoad")
fun onLoad(){
    gridDriver = GridDriver(game)
    gridDriver.calculateNewLevel()

    editorDriver = EditorDriver(game, (document.getElementById("editor") as HTMLElement))
    editorDriver.calculateNewLevel()

    updateHeader()
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
    val windowType = js("typeof window")
    if (windowType != "undefined"){
        configDriver = ConfigDriver("config.xml", ::setup)
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
        Event.LEVEL_VICTORY -> showPopup("Victory!", "Next Level", ::nextLevel)
        Event.LEVEL_FAILURE -> showPopup("Try again!")
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

fun nextLevel(){
    game.nextLevel()
    gridDriver.calculateNewLevel()
    editorDriver.calculateNewLevel()
    updateHeader()
}

fun updateHeader(){
    val header = document.getElementById("header") as HTMLElement
    header.innerHTML = game.currentLevel.properties.name
}

fun showPopup(title: String, buttonTitle: String, buttonClick: () -> Unit){
    val oldPopups = document.body!!.querySelectorAll(".popup")
    for (i in 0 until oldPopups.length){
        document.body!!.removeChild(oldPopups.item(i)!!)
    }

    val popup = document.createElement("div") as HTMLElement
    popup.classList.add("popup")

    val popupText = document.createElement("div") as HTMLElement
    popupText.classList.add("popupText")
    popupText.innerHTML = title
    popup.appendChild(popupText)

    if (buttonTitle != ""){
        val popupButton = document.createElement("button") as HTMLElement
        popupButton.classList.add("popupButton")

        popupButton.innerHTML = buttonTitle

        popupButton.onclick = {
            document.body!!.removeChild(popup)
            buttonClick()
        }

        popup.appendChild(popupButton)
    }
    else{
        popup.style.textAlign = "center"
    }

    val dismissButton = document.createElement("div") as HTMLElement
    dismissButton.classList.add("popupDismissButton")
    dismissButton.innerHTML = "Dismiss"
    dismissButton.onclick = {
        document.body!!.removeChild(popup)
    }
    popup.prepend(dismissButton)

    document.body!!.appendChild(popup)

    if (oldPopups.length == 0) jQuery(popup).fadeIn("slow")
    else jQuery(popup).fadeIn(0)
}

fun showPopup(title: String){
    showPopup(title, "", {})
}