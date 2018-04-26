package org.chicagoedt.robocode.tiles

import org.chicagoedt.robocode.collectibles.ItemInventory

/**
 * A tile that, when reached, the player wins the level
 */
class VictoryTile : Tile {
    override val type = TileType.VICTORY
    override val items = ItemInventory()
}