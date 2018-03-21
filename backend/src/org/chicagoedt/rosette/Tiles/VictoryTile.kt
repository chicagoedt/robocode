package org.chicagoedt.rosette.Tiles

import org.chicagoedt.rosette.Collectibles.Collectible

/**
 * A tile that, when reached, the player wins the level
 */
class VictoryTile : Tile {
    override val type = TileType.VICTORY
    override val items = ArrayList<Collectible>()
}