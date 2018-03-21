package org.chicagoedt.rosette.Tiles

import org.chicagoedt.rosette.Collectibles.Collectible

/**
 * A tile that robots cannot pass through
 */
class ObstacleTile(override val items: ArrayList<Collectible> = ArrayList()) : Tile {
    override val type = TileType.OBSTACLE
}