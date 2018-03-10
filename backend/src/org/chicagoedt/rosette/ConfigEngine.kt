package org.chicagoedt.rosette

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Levels.LevelProperties
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Robots.Robot
import org.chicagoedt.rosette.Robots.RobotOrientation
import org.chicagoedt.rosette.Tiles.NeutralTile
import org.chicagoedt.rosette.Tiles.ObstacleTile
import org.chicagoedt.rosette.Tiles.VictoryTile


fun getRobots() : HashMap<String, Robot>{
    val robots = HashMap<String, Robot>()

    val surus = Robot("Surus", "", 1, 1)
    val hushpuppy = Robot("Hushpuppy", "", 1, 1)

    robots[surus.name] = surus
    robots[hushpuppy.name] = hushpuppy

    return robots
}

fun getLevels() : HashMap<String, Level>{
    val levels = HashMap<String, Level>()

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
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), ObstacleTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
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

    levels[level1.properties.name] = level1
    levels[level2.properties.name] = level2

    return levels
}

fun getLevelOrder() : ArrayList<String>{
    return arrayListOf("Levels 1","Levels 2")
}
