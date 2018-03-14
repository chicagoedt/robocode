package org.chicagoedt.rosette.Tiles

/**
 * The types of tiles possible on the map
 * @property NEUTRAL A tile that robots can freely pass through and ignore
 * @property OBSTACLE A tile that robots cannot pass through
 * @property VICTORY A tile that, when reached, the player wins the level
 */
enum class TileType {
    NEUTRAL,
    OBSTACLE,
    VICTORY
}

/**
 * A location on the grid
 * @property type The type of this tile
 */
interface Tile{
    val type : TileType
}