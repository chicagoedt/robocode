package org.chicagoedt.rosette.Tiles

import org.chicagoedt.rosette.Collectibles.Collectible

/**
 * A tile that robots can freely pass through and ignore
 */
class NeutralTile(override val items: ArrayList<Collectible> = ArrayList()) : Tile {
    override val type = TileType.NEUTRAL
}