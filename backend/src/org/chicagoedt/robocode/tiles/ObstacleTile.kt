package org.chicagoedt.robocode.tiles

import org.chicagoedt.robocode.collectibles.ItemInventory

/**
 * A tile that robots cannot pass through
 */
class ObstacleTile(itemsList: IntArray = intArrayOf()) : Tile {
    override val type = TileType.OBSTACLE
    override val items: ItemInventory = ItemInventory()
}