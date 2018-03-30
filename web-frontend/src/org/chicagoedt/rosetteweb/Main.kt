package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*

/**
 * The context for the grid canvas
 */
internal val gridContext = (document.createElement("canvas") as HTMLCanvasElement).getContext("2d") as CanvasRenderingContext2D

/**
 * The context for the editor canvas
 */
internal val editorContext  = (document.createElement("canvas") as HTMLCanvasElement).getContext("2d") as CanvasRenderingContext2D

/**
 * The game that the browser is running
 */
internal val game = Game(getLevels(), getRobots())

/**
 * The driver for the grid canvas
 */
private var gridDriver = GridDriver(game, gridContext)

/**
 * The driver for the editor canvas
 */
private var editorDriver = EditorDriver(game, editorContext)

/**
 * The main function to run when the page loads
 * @param args The arguments to run. Not currently used at all.
 */
fun main(args: Array<String>) {
    window.onload = {
        document.body!!.style.backgroundColor = COLOR_BACKGROUND

        gridContext.canvas.style.position = "absolute"
        editorContext.canvas.style.position = "absolute"

        positionCanvases()

        gridDriver.calculateNewLevel()
        editorDriver.calculateNewLevel()

        document.body!!.appendChild(editorContext.canvas)
        document.body!!.appendChild(gridContext.canvas)

        drawRefresh()
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

    editorContext.canvas.width = document.documentElement!!.clientWidth - gridContext.canvas.width
    editorContext.canvas.height = document.documentElement!!.clientHeight
    editorContext.canvas.style.left = gridContext.canvas.width.toString() + "px"

    setCanvasDPI(gridContext)
    setCanvasDPI(editorContext)

    editorDriver.setOffset(gridContext.canvas.width.toDouble() / pixelRatio(gridContext), 0.0)
 }

/**
 * Calculate and draws the view on the screen
 */
fun fullRefresh(){
    gridDriver.calculateTiles()
    editorDriver.calculatePanels()
    drawRefresh()
}

fun drawRefresh(){
    gridDriver.drawGrid()
    editorDriver.drawEditor()
}

fun setCanvasDPI(context : CanvasRenderingContext2D){
    val ratio = pixelRatio(context)
    val originalWidth = context.canvas.width
    val originalHeight = context.canvas.height
    context.canvas.width = (context.canvas.width.toDouble() * ratio).toInt()
    context.canvas.height = (context.canvas.height.toDouble() * ratio).toInt()
    context.canvas.style.width = originalWidth.toString() + "px"
    context.canvas.style.height = originalHeight.toString() + "px"
    context.setTransform(ratio, 0.0, 0.0, ratio, 0.0, 0.0)
}