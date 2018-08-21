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
    window.onhashchange = {checkHash()}
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

    editorDriver = EditorDriver(game, (document.getElementById("editor") as HTMLElement))
    editorDriver.calculateNewLevel()

    setTopicListener()

    updateHeader()

    checkHash()
}

fun checkHash(){
    if (window.location.hash != ""){
        try{
            val hashNumber = window.location.hash.substring(1).toInt()
            if (game.jumpToLevel(hashNumber)){
                updateLevel()
            }
            else
                window.location.hash = "#${game.currentLevelNumber}"
        }
        catch (e : NumberFormatException){
            window.location.hash = "#${game.currentLevelNumber}"
        }
    }
    else window.location.hash = "#${game.currentLevelNumber}"
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

/**
 * Updates according to the event from the game
 * @param e The event coming from the game
 */
fun update(e : Event){
    when (e){
        Event.LEVEL_UPDATE -> refresh()
        Event.LEVEL_VICTORY -> {
            if (game.hasNextLevel()){
                editorDriver.disableAllRunButtons()
                showPopup("Victory!", "Next Level", ::nextLevel, false)
            }
            else {
                editorDriver.disableAllRunButtons()
                showPopup("Victory!", "Done", ::toEndScreen, false)
            }
        }
        Event.LEVEL_FAILURE -> showPopup("Try again!", true)
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
    updateLevel()
    editorDriver.enableAllRunButtons()
}

fun updateLevel(){
    window.location.hash = "#${game.currentLevelNumber}"
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

fun showPopup(title: String, buttonTitle: String, buttonClick: () -> Unit, allowDismiss : Boolean){
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

    if (allowDismiss) popup.prepend(dismissButton)

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

fun showPopup(title: String, allowDismiss: Boolean){
    showPopup(title, "", {}, allowDismiss)
}

fun showActionBlockLimitPopup(){
    showPopup("Too many blocks!", true)
}