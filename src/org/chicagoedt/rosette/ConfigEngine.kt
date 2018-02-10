package org.chicagoedt.rosette


fun getRobots() : ArrayList<Robot>{
    val robots = ArrayList<Robot>()
    robots.add(Robot("Surus", "", 1, 1))
    robots.add(Robot("Hushpuppy", "", 1, 1))

    return robots
}

fun getLevels() : ArrayList<Level>{
    val levels = ArrayList<Level>()

    val robotPlayer1 = RobotPlayer("Surus", 0, 0);
    val robotPlayer2 = RobotPlayer("Hushpuppy", 0, 1)

    val list1 = ArrayList<RobotPlayer>()
    val list2 = ArrayList<RobotPlayer>()
    list1.add(robotPlayer1)
    list1.add(robotPlayer2)
    list2.add(robotPlayer2)

    levels.add(Level(LevelProperties("Level 1", 0, list1)))
    levels.add(Level(LevelProperties("Level 2", 0, list2)))

    return levels
}
