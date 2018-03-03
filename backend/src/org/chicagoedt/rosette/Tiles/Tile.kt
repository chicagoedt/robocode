package org.chicagoedt.rosette.Tiles

enum class TileType {
    NEUTRAL,
    OBSTACLE,
    VICTORY
}


interface Tile{
    val type : TileType
}