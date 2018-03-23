package org.chicagoedt.rosette.tiles

import org.chicagoedt.rosette.collectibles.Collectible

/**
 * A tile that robots cannot pass through
 */
class ObstacleTile(override val items: ArrayList<Collectible> = ArrayList()) : Tile {
    override val type = TileType.OBSTACLE
}