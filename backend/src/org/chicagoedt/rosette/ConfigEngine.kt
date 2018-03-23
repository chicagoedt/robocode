package org.chicagoedt.rosette

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Robots.Robot
import org.chicagoedt.rosette.Robots.RobotOrientation
import org.chicagoedt.rosette.Tiles.NeutralTile
import org.chicagoedt.rosette.Tiles.ObstacleTile
import org.chicagoedt.rosette.Tiles.VictoryTile


/**
 * @return All robots for the current game configuration
 */
fun getRobots() : ArrayList<Robot>{
    val robots = ArrayList<Robot>()

    val surus = Robot("Surus", "")
    val hushpuppy = Robot("Hushpuppy", "")

    robots.add(surus)
    robots.add(hushpuppy)

    return robots
}

/**
 * @return All levels for the current game configuration
 */
fun getLevels() : ArrayList<Level>{
    val levels = ArrayList<Level>()

    val level1 = Level(Level.Properties("Levels 1", 0, 10, 10))
    val level2 = Level(Level.Properties("Levels 2", 0, 5, 5))

    val robotPlayer1 = RobotPlayer("Surus", 5, 5, RobotOrientation.DIRECTION_UP, level1)
    val robotPlayer2 = RobotPlayer("Hushpuppy", 3, 3, RobotOrientation.DIRECTION_UP, level1)
    val robotPlayer3 = RobotPlayer("Hushpuppy", 3, 3, RobotOrientation.DIRECTION_UP, level2)

    val list1 = ArrayList<RobotPlayer>()
    val list2 = ArrayList<RobotPlayer>()

    list1.add(robotPlayer1)
    list1.add(robotPlayer2)
    list2.add(robotPlayer3)

    level1.setPlayers(list1)
    level2.setPlayers(list2)

    level1.makeGrid(arrayListOf(
            arrayListOf(VictoryTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), ObstacleTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile())))

    level2.makeGrid(arrayListOf(
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), ObstacleTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile())))

    levels.add(level1)
    levels.add(level2)

    return levels
}
