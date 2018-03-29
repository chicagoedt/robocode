package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosetteweb.canvas.*

/**
 * The context for the grid canvas
 */
private lateinit var gridContext: CanvasRenderingContext2D

/**
 * The context for the editor canvas
 */
private lateinit var editorContext: CanvasRenderingContext2D

/**
 * The game that the browser is running
 */
internal val game = Game(getLevels(), getRobots())

/**
 * The driver for the grid canvas
 */
private lateinit var gridDriver : GridDriver

/**
 * The driver for the editor canvas
 */
private lateinit var editorDriver : EditorDriver

/**
 * The colors for the page
 */
var colors = Colors()

/**
 * The main function to run when the page loads
 * @param args The arguments to run. Not currently used at all.
 */
fun main(args: Array<String>) {
    window.onload = {
        document.body!!.style.backgroundColor = colors.backgroundColor
        //game.nextLevel()
        val gridCanvas = document.createElement("canvas") as HTMLCanvasElement
        gridContext = gridCanvas.getContext("2d") as CanvasRenderingContext2D
        gridContext.canvas.width = (document.documentElement!!.clientWidth.toDouble() / 3.0).toInt()
        gridContext.canvas.style.position = "absolute"
        gridDriver = GridDriver(game, gridContext)
        
        val editorCanvas = document.createElement("canvas") as HTMLCanvasElement
        editorContext = editorCanvas.getContext("2d") as CanvasRenderingContext2D
        editorContext.canvas.height = document.documentElement!!.clientHeight
        editorContext.canvas.style.position = "absolute"
        editorDriver = EditorDriver(game, editorContext)

        gridDriver.calculateNewLevel()
        editorDriver.calculateNewLevel()
        positionCanvases()

        document.body!!.appendChild(editorCanvas)
        document.body!!.appendChild(gridCanvas)

        fullRefresh()
    }

    window.onresize = {
        if (::gridContext.isInitialized && ::editorContext.isInitialized) {
            positionCanvases()
            fullRefresh()
        }
    }
}

/**
 * Defines the position for the canvases on the screen
 */
 fun positionCanvases(){
    val maximumGrid = (document.documentElement!!.clientHeight / 2.0).toInt()
    gridContext.canvas.width = (document.documentElement!!.clientWidth.toDouble() / 3.0).toInt()
    if (gridContext.canvas.width > maximumGrid) gridContext.canvas.width = maximumGrid
    gridContext.canvas.height = gridContext.canvas.width

    editorContext.canvas.width = (document.documentElement!!.clientWidth - gridContext.canvas.width)
    editorContext.canvas.height = document.documentElement!!.clientHeight
    editorContext.canvas.style.left = gridContext.canvas.width.toString() + "px"

    setCanvasDPI(editorContext)
    setCanvasDPI(gridContext)


    gridDriver.calculateTiles()
    gridDriver.calculatePlayers()
    editorDriver.calculatePanels()
    editorDriver.setOffset(gridContext.canvas.width.toDouble() / pixelRatio(gridContext), 0.0)
 }

/**
 * Calculate and draws the view on the screen
 */
fun fullRefresh(){
    gridDriver.calculateTiles()
    gridDriver.calculatePlayers()
    editorDriver.calculatePanels()
    gridDriver.drawGrid()
    editorDriver.drawEditor()
}

fun setCanvasDPI(context : CanvasRenderingContext2D){
    val ratio = pixelRatio(context)
    val originalWidth = context.canvas.width
    val originalHeight = context.canvas.height
    context.canvas.width = (context.canvas.width.toDouble() * ratio).toInt()
    context.canvas.height = (context.canvas.height.toDouble() * ratio).toInt()
    context.canvas.style.width = originalWidth.toInt().toString() + "px"
    context.canvas.style.height = originalHeight.toInt().toString() + "px"
    context.setTransform(ratio, 0.0, 0.0, ratio, 0.0, 0.0)
}