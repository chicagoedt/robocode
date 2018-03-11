package org.chicagoedt.rosette

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Levels.LevelProperties
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Robots.Robot
import org.chicagoedt.rosette.Robots.RobotOrientation
import org.chicagoedt.rosette.Tiles.NeutralTile
import org.chicagoedt.rosette.Tiles.ObstacleTile
import org.chicagoedt.rosette.Tiles.VictoryTile


fun getRobots() : ArrayList<Robot>{
    val robots = ArrayList<Robot>()

    val surus = Robot("Surus", "", 1, 1)
    val hushpuppy = Robot("Hushpuppy", "", 1, 1)

    robots.add(surus)
    robots.add(hushpuppy)

    return robots
}

fun getLevels() : ArrayList<Level>{
    val levels = ArrayList<Level>()

    val robotPlayer1 = RobotPlayer("Surus", 5, 5, RobotOrientation.DIRECTION_UP)
    val robotPlayer2 = RobotPlayer("Hushpuppy", 3, 3, RobotOrientation.DIRECTION_UP)
    val robotPlayer3 = RobotPlayer("Hushpuppy", 3, 3, RobotOrientation.DIRECTION_UP)

    val list1 = HashMap<String, RobotPlayer>()
    val list2 = HashMap<String, RobotPlayer>()
    list1[robotPlayer1.name] = robotPlayer1
    list1[robotPlayer2.name] = robotPlayer2
    list2[robotPlayer3.name] = robotPlayer3

    val level1 = Level(LevelProperties("Levels 1", 0, 10, 10), list1, arrayListOf("Surus", "Hushpuppy"))
    val level2 = Level(LevelProperties("Levels 2", 0, 5, 5), list2, arrayListOf("Hushpuppy"))

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
