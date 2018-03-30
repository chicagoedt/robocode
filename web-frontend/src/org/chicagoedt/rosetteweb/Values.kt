package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D

val DRAWER_HEIGHT get() = editorContext.canvas.height * (1.0/6.0) / pixelRatio(editorContext)
val DRAWER_WIDTH get() = editorContext.canvas.width / pixelRatio(editorContext)

val PANEL_HEIGHT get() = editorContext.canvas.height * (5.0/6.0) / pixelRatio(editorContext)
val PANEL_WIDTH get() = editorContext.canvas.width / pixelRatio(editorContext) / game.currentLevel.players.size
val PANEL_HEADER_HEIGHT get() = PANEL_HEIGHT / 15.0
val PANEL_HEADER_WIDTH get() = PANEL_WIDTH
val PANEL_RUN_BUTTON_WIDTH get() = PANEL_WIDTH / 5.0

val BLOCK_HEIGHT get() = PANEL_HEIGHT / 20.0
val BLOCK_WIDTH get() = PANEL_WIDTH
val BLOCK_LIFT_SHADOW get() = 20.0 / pixelRatio(editorContext)
val BLOCK_DOWN_SHADOW get() = 5.0 / pixelRatio(editorContext)
val BLOCK_CORNER_RADIUS get() = 20.0 // pixelRatio(editorContext)

val TILE_WIDTH get() = (gridContext.canvas.width / game.currentLevel.properties.width) / pixelRatio(gridContext)
val TILE_HEIGHT get() = (gridContext.canvas.height / game.currentLevel.properties.height) / pixelRatio(gridContext)

fun pixelRatio(context : CanvasRenderingContext2D) : Double{
    var ratio = 1.0
    js("var dpr = window.devicePixelRatio || 1," +
            "bsr = context.webkitBackingStorePixelRatio ||" +
            "context.mozBackingStorePixelRatio ||" +
            "context.msBackingStorePixelRatio ||" +
            "context.oBackingStorePixelRatio ||" +
            "context.backingStorePixelRatio || 1;" +
            "ratio = dpr / bsr")
    return ratio
    //return 1.0
}