package org.chicagoedt.robocodeweb.editor

import jQuery
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocode.actions.*
import org.chicagoedt.robocodeweb.game
import org.chicagoedt.robocodeweb.showActionBlockLimitPopup
import org.w3c.dom.*
import org.w3c.dom.events.Event
import kotlin.browser.*
import kotlin.dom.addClass
import kotlin.js.*
import kotlin.*
import kotlin.dom.removeClass

/**
 * The area to show and manage the code corresponding to the robot
 * @param parent The parent element for this panel
 * @param robot The robot corresponding to this panel
 * @param drawer The drawer where the blocks are being dragged from
 * @property element The HTML element for this panel
 * @property lastHoveredBlock The last block that a draggable was hovered over
 * @property hoverOverHeader True if the header is being hovered over, false otherwise
 * @property runButton The button that gets pressed to run the actions in this panel
 * @property runButtonListener The listener for Robocode Events when the button is pressed
 */
class Panel(val parent : HTMLElement, val robot : RobotPlayer, val drawer : Drawer){
    lateinit var element : HTMLDivElement
    var lastHoveredBlock : ActionBlock<Action<Any>>? = null
    private var hoverOverHeader = false
    lateinit var runButton : HTMLButtonElement
    private lateinit var runButtonListener : (org.chicagoedt.robocode.Event) -> Unit
    private var hintElement = document.createElement("div") as HTMLElement
    private var remainingElement = document.createElement("div") as HTMLElement
    private var remainingElementListener : (org.chicagoedt.robocode.Event) -> Unit = {}

    init {
        val tdElement = document.createElement("td") as HTMLElement
        tdElement.addClass("panelTd")

        element = document.createElement("div") as HTMLDivElement
        element.addClass("panel")
        element.asDynamic().panelObject = this

        val hintImageElement = document.createElement("img") as HTMLImageElement
        hintImageElement.src = "res/drag_hint.svg"
        hintImageElement.addClass("dragHintImage")

        val hintTextElement = document.createElement("div") as HTMLElement
        hintTextElement.innerHTML = "Drag Blocks Here"
        hintTextElement.addClass("dragHintText")

        hintElement.addClass("dragHint")
        hintElement.appendChild(hintTextElement)
        hintElement.appendChild(hintImageElement)

        element.appendChild(getHeader())
        element.appendChild(hintElement)
        tdElement.appendChild(element)
        parent.appendChild(tdElement)

        addDrop()
    }

    /**
     * Adds the necessary options for this panel to be a droppable
     */
    fun addDrop(){
        val drop = jQuery(element).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "scope", "actions")
        drop.droppable("option", "drop", ::drop)
        drop.droppable("option", "over", ::over)
        drop.droppable("option", "out", ::overout)
    }

    /**
     * Called when a draggable is dropped over this panel
     * @param event The drop event
     * @param ui The element being dropped
     */
    fun drop(event : Event, ui : dynamic){
        val blockElement : HTMLElement = ui.draggable[0]
        blockElement.style.top = "0px"
        blockElement.style.left = "0px"
        element.style.boxShadow = ""

        val newActionBlock : ActionBlock<*> = blockElement.asDynamic().block

        var insertBlockElement = {}
        var removeBlockElement = {}
        var undoRemoveBlockElement = {}

        if (blockElement.parentElement!!.classList.contains("panel")){
            val otherPanel = blockElement.parentElement!!.asDynamic().panelObject as Panel
            val originalPosition = otherPanel.robot.procedure.lastIndexOf(newActionBlock.action)
            removeBlockElement = {otherPanel.robot.removeAction(newActionBlock.action)}
            undoRemoveBlockElement = {otherPanel.robot.insertAction(newActionBlock.action, originalPosition)}
        }
        else if (newActionBlock.macroParent != null){
            val originalPosition = newActionBlock.macroParent!!.action.getMacro().lastIndexOf(newActionBlock.action)
            removeBlockElement = {newActionBlock.macroParent!!.action.removeFromMacro(newActionBlock.action)}
            val oldRobotParent = newActionBlock.macroParent!!.getRobotParent(newActionBlock.macroParent!!)
            undoRemoveBlockElement = {newActionBlock.macroParent!!.action.addToMacroAt(newActionBlock.action, originalPosition, oldRobotParent.getLimitDifference())}
        }

        var blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
        blocks = trimToDirectChildren(blocks.toMutableList())
        var pos = 0
        if (hoverOverHeader){
            if (blocks.size > 0) insertBlockElement = {element.insertBefore(blockElement, blocks[0])}
            else insertBlockElement = {element.appendChild(blockElement)}
            hoverOverHeader = false
        }
        else{
            try{
                lastHoveredBlock!!.element.style.marginBottom = ""
                pos = blocks.indexOf(lastHoveredBlock!!.element) + 1
                val block = blocks[pos] as HTMLElement
                insertBlockElement = {element.insertBefore(blockElement, block)}
            }
            catch(e : Exception){
                pos = blocks.size
                insertBlockElement = {element.appendChild(blockElement)}
            }
        }

        lastHoveredBlock = null

        removeBlockElement()
        val canInsert = robot.canInsertAction(blockElement.asDynamic().block.action)

        if (canInsert){
            insertBlockElement()

            robot.insertAction(blockElement.asDynamic().block.action, pos)
            if (newActionBlock is ActionBlockMacro<*>){
                newActionBlock.panelParent = this
            }

            newActionBlock.macroParent = null

            if (jQuery(hintElement).`is`(":visible")){
                jQuery(hintElement).hide()
            }
        }
        else{
            showActionBlockLimitPopup()
            undoRemoveBlockElement()
        }

        drawer.populate()
    }

    /**
     * Shows the "Drag Blocks Here" hint if there are no blocks left
     */
    fun checkAndShowHint(){
        val blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
        if (blocks.size == 0){
            jQuery(hintElement).show()
        }

    }

    /**
     * Called when a draggable is hovered over this panel
     * @param event The over event
     * @param ui The element being hovered
     */
    fun over(event : Event, ui : dynamic){
        element.style.boxShadow = "0px 0px 2px grey"
    }

    /**
     * Called when a draggable that was hovering over this panel is moved out
     * @param event The out event
     * @param ui The element being moved
     */
    fun overout(event : Event, ui : dynamic){
        element.style.boxShadow = ""
        lastHoveredBlock = null
    }

    /**
     * Generates the header for the panel
     * @return The HTMLElement for the header
     */
    private fun getHeader() : HTMLElement{
        val header = document.createElement("div") as HTMLElement
        header.addClass("panelHeader")

        header.innerHTML = robot.name

        addHeaderDroppable(header)

        runButton = document.createElement("button") as HTMLButtonElement
        runButton.addClass("panelHeaderButton")
        runButton.innerHTML = "Go"

        runButton.onclick = {
            var interval = 0
            robot.runInstructions(true,
                    {runner ->
                        interval = window.setInterval(runner, 500)
                    },
                    {
                        window.clearInterval(interval)
                    })
        }
        header.appendChild(runButton)

        remainingElement.addClass("panelBlocksRemaining")
        remainingElement.innerText = "Blocks: "

        header.appendChild(remainingElement)
        val remainingElementNumber = document.createElement("div") as HTMLElement
        remainingElementNumber.addClass("panelBlocksRemainingNumber")
        remainingElementNumber.innerText = robot.getLimitDifference().toString()

        remainingElementListener = {
            if (it == org.chicagoedt.robocode.Event.ACTION_ADDED ||
                    it == org.chicagoedt.robocode.Event.ACTION_REMOVED)
                remainingElementNumber.innerText = robot.getLimitDifference().toString()
        }

        game.attachEventListener(remainingElementListener)

        remainingElement.appendChild(remainingElementNumber)

        if (robot.actionLimit == -1) remainingElement.style.visibility = "hidden"
        else header.addClass("panelHeaderWithRemainingBlocks")

        runButtonListener = {
            when (it){
                org.chicagoedt.robocode.Event.ROBOT_RUN_START ->{
                    runButton.disabled = true
                    runButton.addClass("disabledPanelHeaderButton")
                }
                org.chicagoedt.robocode.Event.LEVEL_FAILURE -> {
                    runButton.disabled = false
                    runButton.removeClass("disabledPanelHeaderButton")
                }
            }
        }

        game.attachEventListener(runButtonListener)

        return header
    }

    /**
     * Removes the button event listener from the game
     */
    fun removeListeners(){
        game.removeEventListener(runButtonListener)
        game.removeEventListener(remainingElementListener)
    }

    /**
     * Adds droppable properties to the header. Necessary to determine block drop positions
     * @param header The header element
     */
    fun addHeaderDroppable(header : HTMLElement){
        val onOver = {
            header.style.marginBottom = "10px"
            hoverOverHeader = true
        }

        val onOverOut = {
            header.style.marginBottom = ""
            hoverOverHeader = false
        }

        val onDrop = {
            header.style.marginBottom = ""
        }

        val drop = jQuery(header).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "scope", "actions")
        drop.droppable("option", "drop", onDrop)
        drop.droppable("option", "over", onOver)
        drop.droppable("option", "out", onOverOut)
    }

    /**
     * Trims a list of elements to include only direct children of [element]
     * @param list The list to trim
     * @return A list of elements from [list] which are direct children of [element]
     */
    private fun trimToDirectChildren(list : MutableList<Element>) : List<Element>{
        val toRemove = arrayListOf<Element>()

        for (element in list){
            if (element.parentElement == null || element.parentElement != this.element){
                toRemove.add(element)
            }
        }

        for (element in toRemove){
            list.remove(element)
        }

        return list
    }
}