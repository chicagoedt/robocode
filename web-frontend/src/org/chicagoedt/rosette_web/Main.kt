package org.chicagoedt.rosette_web

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*

private lateinit var canvas : HTMLCanvasElement
private lateinit var context: CanvasRenderingContext2D
private const val interval = 17

/**
 * The main function to run when the page loads
 * @param args The arguments to run. Not currently used at all.
 */
fun main(args: Array<String>) {
    window.onload = {
        canvas = document.createElement("canvas") as HTMLCanvasElement
        context = canvas.getContext("2d") as CanvasRenderingContext2D
        context.canvas.width = window.innerWidth
        context.canvas.height = window.innerHeight
        document.body!!.appendChild(canvas)
        draw()
    }

    window.onresize = {
        if (::context.isInitialized) {
            context.canvas.width = window.innerWidth
            context.canvas.height = window.innerHeight
            draw()
        }
    }
}

/**
 * Draws the view on the screen
 */
fun draw(){
    context.font = "20px Courier New";
    context.fillText("Hello World", 10.0, 20.0)
}