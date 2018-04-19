package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D

val TILE_WIDTH get() = (gridContext.canvas.width.toDouble() / game.currentLevel.properties.width.toDouble()) / pixelRatio(gridContext)
val TILE_HEIGHT get() = (gridContext.canvas.height.toDouble() / game.currentLevel.properties.height.toDouble()) / pixelRatio(gridContext)
val TILE_TEXT_SIZE get() = 20

fun pixelRatio(context : CanvasRenderingContext2D) : Double{
    var ratio = 1.0
    js("var dpr = window.devicePixelRatio || 1," +
            "bsr = context.webkitBackingStorePixelRatio ||" +
            "context.mozBackingStorePixelRatio ||" +
            "context.msBackingStorePixelRatio ||" +
            "context.oBackingStorePixelRatio ||" +
            "context.backingStorePixelRatio || 1;" +
            "ratio = dpr / bsr")
    //return ratio
    return 1.0
}