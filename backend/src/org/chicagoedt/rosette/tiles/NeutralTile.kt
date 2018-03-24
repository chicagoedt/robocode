package org.chicagoedt.rosette.tiles

import org.chicagoedt.rosette.collectibles.ItemInventory

/**
 * A tile that robots can freely pass through and ignore
 */
class NeutralTile(itemsList: IntArray = intArrayOf()) : Tile {
    override val type = TileType.NEUTRAL
    override val items: ItemInventory = ItemInventory()
}