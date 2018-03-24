package org.chicagoedt.rosette.tiles

import org.chicagoedt.rosette.collectibles.ItemInventory

/**
 * A tile that, when reached, the player wins the level
 */
class VictoryTile : Tile {
    override val type = TileType.VICTORY
    override val items = ItemInventory()
}