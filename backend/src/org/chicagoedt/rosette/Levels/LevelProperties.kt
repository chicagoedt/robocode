package org.chicagoedt.rosette.Levels

/**
 * Stores data from properties
 * @param name The name of the level
 * @param difficulty The difficulty of the level. Used only for categorization, it has no effect on the level itself
 * @param width The width of the grid on the level
 * @param heigh The height of the grid on the level
 */
class LevelProperties(val name: String,
                      val difficulty : Int,
                      val width : Int,
                      val height : Int){


}

