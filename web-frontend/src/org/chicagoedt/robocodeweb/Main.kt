package org.chicagoedt.robocodeweb

import jQuery
import org.w3c.dom.*
import kotlin.browser.*
import org.chicagoedt.robocode.*
import org.chicagoedt.robocode.robots.*
import org.chicagoedt.robocode.levels.*
import kotlin.dom.addClass
import kotlin.math.exp

/**
 * The game that the browser is running
 */
internal lateinit var game : Game

/**
 * The driver for the grid canvas
 */
internal lateinit var gridDriver : GridDriver

/**
 * The driver for the editor area
 */
internal lateinit var editorDriver : EditorDriver

/**
 * The configurator for the game
 */
private lateinit var configDriver : ConfigDriver

/**
 * The conditions for the current level
 */
internal lateinit var currentLevelConditions : Level.Conditions

fun setup(robots : ArrayList<Robot>, levels : ArrayList<Level>, themes: ArrayList<Level.Theme>){
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
    currentLevelConditions = game.currentLevel.conditions
    gridDriver = GridDriver(game)
    gridDriver.calculateNewLevel()
    jQuery("#leftPane").hide()

    editorDriver = EditorDriver(game, (document.getElementById("editor") as HTMLElement))
    editorDriver.calculateNewLevel()

    setTopicListener()

    updateHeader()
    fadeInGame()
}

@JsName("onResize")
fun onResize(){
    gridDriver.handleResize()
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

fun fadeInGame(){
    val fadeSpeed = 500
    val intervalTime = 100
    var count = 1
    var interval = 0;
    val runner = {
        if (count == 1){
            jQuery("#leftPane").fadeIn(fadeSpeed)
        }
        if (count == 2){
            jQuery("#panelContainer").fadeIn(fadeSpeed)
        }
        if (count > 2) {
            window.clearInterval(interval)
        }
        count++
    }
    jQuery("#drawer").fadeIn(fadeSpeed)
    interval = window.setInterval(runner, intervalTime)
}

/**
 * Updates according to the event from the game
 * @param e The event coming from the game
 */
fun update(e : Event){
    when (e){
        Event.LEVEL_UPDATE -> refresh()
        Event.LEVEL_VICTORY -> {
            if (game.hasNextLevel())showPopup("Victory!", "Next Level", ::nextLevel)
            else showPopup("Victory!", "Done", ::toEndScreen)
        }
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

fun setTopicListener(){
    val topic = document.getElementById("topicValue")
    topic!!.innerHTML = mainTopic.value.toString()

    mainTopic.topicListeners.add {value ->
        topic.innerHTML = (value as Int).toString()
    }
}

fun nextLevel(){
    game.nextLevel()
    currentLevelConditions = game.currentLevel.conditions
    gridDriver.calculateNewLevel()
    editorDriver.calculateNewLevel()
    updateHeader()
}

fun toEndScreen(){
    window.location.href = "done.html"
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


    val dismissButton = document.createElement("img") as HTMLImageElement
    dismissButton.classList.add("popupDismissButton")
    dismissButton.src = "res/x.svg"
    dismissButton.onclick = {
        document.body!!.removeChild(popup)
    }

    if (buttonTitle != ""){
        val popupButton = document.createElement("button") as HTMLElement
        popupButton.classList.add("popupButton")

        popupButton.innerHTML = buttonTitle

        popupButton.onclick = {
            document.body!!.removeChild(popup)
            buttonClick()
        }

        popup.appendChild(popupButton)

        popupText.style.bottom = jQuery(popupButton).css("height")
    }
    else{
        popup.style.textAlign = "center"
    }

    popup.prepend(dismissButton)

    document.body!!.appendChild(popup)

    if (oldPopups.length == 0) jQuery(popup).fadeIn("fast", {checkForExpand(popup)})
    else jQuery(popup).fadeIn(0, {checkForExpand(popup)})
}

fun checkForExpand(popup : HTMLElement) : dynamic{
    val textElement = popup.getElementsByClassName("popupText")[0] as HTMLElement

    var scroll = false

    if (textElement.scrollHeight > textElement.clientHeight) scroll = true

    if (scroll){
        val expandElement = document.createElement("img") as HTMLImageElement
        expandElement.addClass("popupExpandButton")
        expandElement.src = "res/arrow.svg"

        var undoExpand = { event : org.w3c.dom.events.Event ->}

        val expand = { event : org.w3c.dom.events.Event ->
            popup.style.height = textElement.scrollHeight.toString() + "px"
            jQuery(expandElement).css("transform", "rotate(180deg)")
            expandElement.onclick = undoExpand
        }

        expandElement.onclick = expand

        undoExpand = {
            popup.style.height = ""
            jQuery(expandElement).css("transform", "rotate(0deg)")
            expandElement.onclick = expand
        }

        popup.appendChild(expandElement)
    }

    return true
}

fun showPopup(title: String){
    showPopup(title, "", {})
}

fun showActionBlockLimitPopup(){
    showPopup("Too many blocks!")
}