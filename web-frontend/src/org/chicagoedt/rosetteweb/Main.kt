package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.browser.*
import org.chicagoedt.rosette.*

/**
 * The context for the grid canvas
 */
internal lateinit var gridContext : CanvasRenderingContext2D

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
    //document.querySelector(".actionBlock").
    val gridCanvas = document.getElementById("grid") as HTMLCanvasElement
    gridContext = gridCanvas.getContext("2d") as CanvasRenderingContext2D

    positionCanvases()

    gridDriver = GridDriver(game, gridContext)
    gridDriver.calculateNewLevel()

    editorDriver = EditorDriver(game, (document.getElementById("editor") as HTMLElement))
    editorDriver.calculateNewLevel()

    drawRefresh()
}

@JsName("onResize")
fun onResize(){
    if (::gridContext.isInitialized) {
        positionCanvases()
        fullRefresh()
    }
}

/**
 * The main function to run when the page loads
 * @param args The arguments to run. Not currently used at all.
 */
fun main(args: Array<String>) {
    game.attachEventListener(::update)

    //js("if (window typeof !== 'undefined') window.onload = onLoad;")
    //js("if (typeof window !== 'undefined')" + 
    //    "{" +
    //        "window.onresize = onResize;" +
    //        "window.onload = onLoad;" + 
     //   "}")

    //js("if (typeof window !== 'undefined')")
    //    window.onresize = {onResize()}

    //js("if (typeof window !== 'undefined')")
    //    window.onload = {onLoad()}

    val windowType = js("typeof window")
    if (windowType != "undefined"){
        window.onresize = {onResize()}
        window.onload = {onLoad()}
    } 

    //js("if (window typeof !== 'undefined') window.onresize = onResize;")
}

fun getTypeOf(elem : Any) : String{
    return js("typeof elem")
}

/**
 * Defines the position for the canvases on the screen
 */
 fun positionCanvases(){
    /*val maximumGrid = (document.documentElement!!.clientHeight / 2.0).toInt()
    gridContext.canvas.width = (document.documentElement!!.clientWidth.toDouble() / 3.0).toInt()
    if (gridContext.canvas.width > maximumGrid) gridContext.canvas.width = maximumGrid
    gridContext.canvas.height = gridContext.canvas.width*/

    //setCanvasDPI(gridContext)

    //(document.getElementById("editor") as HTMLElement).style.left = gridContext.canvas.style.width
 }

/**
 * Calculate and draws the view on the screen
 */
fun fullRefresh(){
    if (::gridDriver.isInitialized){
        gridDriver.calculateTiles()
        drawRefresh()
    }
}

/**
 * Only redraw the previously calculated coordinates
 */
fun drawRefresh(){
    gridDriver.drawGrid()
}

/**
 * Sets the DPI for the canvas
 * @param context The context for the canvas to set the DPI for
 */
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

/**
 * Updates according to the event from the game
 * @param e The event coming from the game
 */
fun update(e : Event){
    when (e){
        Event.LEVEL_UPDATE -> fullRefresh()
    }
}