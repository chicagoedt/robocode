package org.chicagoedt.rosette.tiles

import org.chicagoedt.rosette.collectibles.Collectible

/**
 * A tile that robots can freely pass through and ignore
 */
class NeutralTile(override val items: ArrayList<Collectible> = ArrayList()) : Tile {
    override val type = TileType.NEUTRAL
}