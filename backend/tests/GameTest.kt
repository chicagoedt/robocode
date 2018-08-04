import kotlin.test.*
import org.chicagoedt.robocode.*
import org.chicagoedt.robocode.actions.Action
import org.chicagoedt.robocode.actions.ConditionalWithList
import org.chicagoedt.robocode.actions.operations.TopicEqualsComparison
import org.chicagoedt.robocode.actions.robotActions.*
import org.chicagoedt.robocode.collectibles.ItemInventory
import org.chicagoedt.robocode.collectibles.ItemManager
import org.chicagoedt.robocode.collectibles.etc.*
import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.levels.VictoryType
import org.chicagoedt.robocode.robots.*
import org.chicagoedt.robocode.sensors.DistanceSensor
import org.chicagoedt.robocode.tiles.*

/**
 * @return All levels for the current game configuration
 */
fun getLevels() : ArrayList<Level>{
    val levels = ArrayList<Level>()

    val level1 = Level(Level.Properties("levels 1", 0, 10, 10))
    val level2 = Level(Level.Properties("levels 2", 0, 5, 5))

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
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

    level2.makeGrid(arrayListOf(
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), ObstacleTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile()),
            arrayListOf(NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

    levels.add(level1)
    levels.add(level2)

    return levels
}

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

class BackendTests {
    private lateinit var game : Game
    private var levels = getLevels()
    private var robots = getRobots()

    @BeforeTest
    fun SetUp(){
        levels = getLevels()
        robots = getRobots()
        game = Game(levels, robots)
    }

    @Test
    fun GameRobots() {
        for(level in levels){
            val levelName = level.properties.name
            for((name, robot) in level.players){
                val robot = level.players[name]!!

                assertEquals(game.currentLevel.players[robot.name]!!.name, robot.name)
                assertEquals(game.currentLevel.players[robot.name]!!.x, robot.x)
                assertEquals(game.currentLevel.players[robot.name]!!.y, robot.y)
            }
            game.nextLevel()
        }
    }

    @Test
    fun GameLevels() {
        for(level in levels){
            assertEquals(game.currentLevel, level)
            game.nextLevel()
        }
    }

    @Test
    fun GameGrid() {
        for(level in levels){
            assertEquals(game.currentLevel.properties.width, level.properties.width)
            assertEquals(game.currentLevel.properties.height, level.properties.height)
            game.nextLevel()
        }
    }

    @Test
    fun RobotInstructionAttach() {
        for(level in levels){
            for((name, robot) in level.players){

                val moveInstruction = MoveAction()
                robot.appendAction(moveInstruction)
                val turnInstruction = TurnAction()
                robot.appendAction(turnInstruction)

                val list = robot.getProcedure()
                assertEquals(list[0].name, moveInstruction.name)
                assertEquals(list[1].name, turnInstruction.name)
            }
            game.nextLevel()
        }
    }

    @Test
    fun RobotInstructionTurn() {
        val instruction = TurnAction()
        for(level in levels){
            for((name, robot) in level.players){
                for (i in 0..3) {
                    turn(name, RobotRotation.CLOCKWISE, true)
                }
            }
            game.nextLevel()
        }
    }

    private fun turn(name: String, rotation: RobotRotation, assert: Boolean){
        val robot = game.currentLevel.players[name]!!
        val next = nextDirection(robot.direction, rotation)
        val turn = TurnAction()
        turn.parameter = rotation
        robot.appendAction(turn)
        robot.runInstructions(false)
        robot.removeAction(turn)
        if (assert) assertEquals(robot.direction, next)
    }

    private fun move(name: String, orientation: RobotOrientation, parameter: Int, assert: Boolean) {
        val robot = game.currentLevel.players[name]!!
        val instruction = MoveAction()
        instruction.parameter = parameter
        val y = robot.y
        val x = robot.x
        robot.appendAction(instruction)
        robot.runInstructions(false)
        robot.removeAction(instruction)

        val difference = distanceCanMove(x, y, orientation, parameter, game.currentLevel)
        if (orientation == RobotOrientation.DIRECTION_UP) {
            if (assert)assertEquals(robot.y, y + difference)
            if (assert)assertEquals(robot.x, x)
        }
        else if (orientation == RobotOrientation.DIRECTION_DOWN) {
            if (assert)
                assertEquals(robot.y, y - difference)
            if (assert)assertEquals(robot.x, x)
        }
        else if (orientation == RobotOrientation.DIRECTION_LEFT) {
            if (assert)assertEquals(robot.y, y)
            if (assert)assertEquals(robot.x, x - difference)
        }
        else if (orientation == RobotOrientation.DIRECTION_RIGHT) {
            if (assert)assertEquals(robot.y, y)
            if (assert)assertEquals(robot.x, x + difference)
        }
    }

    @Test
    fun RobotInstructionMove(){
        for(level in levels){
            for((name, robot) in level.players){
                move(name, robot.direction, 2, true)
                turn(name, RobotRotation.CLOCKWISE, false)
                move(name, robot.direction, 4, true)
                turn(name, RobotRotation.CLOCKWISE, false)
                move(name, robot.direction, 5, true)
                turn(name, RobotRotation.CLOCKWISE, false)
                move(name, robot.direction, 3, true)
            }
            game.nextLevel()
        }
    }

    @Test
    fun MoveEdge(){
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_DOWN, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)
        game = Game(testLevels, testRobots)

        val instruction = MoveAction()
        instruction.parameter = 1
        robotPlayer1.appendAction(instruction)
        robotPlayer1.runInstructions(false)
        assertEquals(robotPlayer1.x, 0)
        assertEquals(robotPlayer1.y, 0)

        robotPlayer1.removeAction(instruction)
        val turnInstruction = TurnAction()
        turnInstruction.parameter = RobotRotation.CLOCKWISE
        robotPlayer1.appendAction(turnInstruction)
        robotPlayer1.runInstructions(false)

        robotPlayer1.removeAction(turnInstruction)
        robotPlayer1.appendAction(instruction)
        robotPlayer1.runInstructions(false)
        assertEquals(level1.players[surus.name]!!.x, 0)
        assertEquals(level1.players[surus.name]!!.y, 0)
    }

    @Test
    fun MoveObstacle(){
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), ObstacleTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveAction()
        instruction.parameter = 1
        robotPlayer1.appendAction(instruction)

        robotPlayer1.runInstructions(false)

        assertEquals(level1.players[surus.name]!!.x, 0)
        assertEquals(level1.players[surus.name]!!.y, 0)
    }

    @Test
    fun EventRobotRunStart(){
        var started = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        level1.victoryType = VictoryType.TILE

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)


        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), VictoryTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveAction()
        instruction.parameter = 1
        robotPlayer1.appendAction(instruction)

        game.attachEventListener {e ->
            when (e){
                Event.ROBOT_RUN_START -> started = true
            }
        }

        robotPlayer1.runInstructions(false)

        assertEquals(started, true)
    }

    @Test
    fun EventRobotRunEndVictory(){
        var ended = false
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        level1.victoryType = VictoryType.TILE

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)


        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), VictoryTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveAction()
        instruction.parameter = 1
        robotPlayer1.appendAction(instruction)

        game.attachEventListener {e ->
            when (e){
                Event.ROBOT_RUN_START -> ended = true
                Event.LEVEL_VICTORY -> won = true
            }
        }

        robotPlayer1.runInstructions(false)

        assertEquals(ended, true)
        assertEquals(won, true)
    }

    @Test
    fun EventRobotRunEndFailure(){
        var ended = false
        var lost = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        level1.victoryType = VictoryType.TILE

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)


        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), VictoryTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveAction()
        instruction.parameter = 1
        robotPlayer1.appendAction(instruction)

        game.attachEventListener {e ->
            when (e){
                Event.ROBOT_RUN_START -> ended = true
                Event.LEVEL_FAILURE -> lost = true
            }
        }

        robotPlayer1.runInstructions(false)

        assertEquals(ended, true)
        assertEquals(lost, true)
    }

    @Test
    fun RemoveEventListener(){
        var listenerCalled = false
        val listener = { e : Event ->
            listenerCalled = true
        }
        game.attachEventListener(listener)
        game.removeEventListener(listener)
        for(level in levels){
            for((name, robot) in level.players){
                robot.runInstructions(true)
            }
            game.nextLevel()
        }
        assertEquals(false, listenerCalled)
    }

    @Test
    fun LevelTileVictory(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        level1.victoryType = VictoryType.TILE

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)


        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), VictoryTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveAction()
        instruction.parameter = 1
        robotPlayer1.appendAction(instruction)

        game.attachEventListener {e -> 
            when (e){
                Event.LEVEL_VICTORY -> won = true
                Event.LEVEL_FAILURE -> won = false
            }
        }

        robotPlayer1.runInstructions(false)

        assertEquals(won, true)
    }

    @Test
    fun LevelTileFailure(){
        var failed = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        level1.victoryType = VictoryType.TILE

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)


        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), VictoryTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveAction()
        instruction.parameter = 1
        robotPlayer1.appendAction(instruction)

        game.attachEventListener {e -> 
            when (e){
                Event.LEVEL_FAILURE -> failed = true
            }
        }

        robotPlayer1.runInstructions(false)

        assertEquals(failed, true)
    }

    @Test
    fun LevelItemPositionVictory(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_UP, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        val itemInventory = ItemInventory()
        itemInventory.addItem(Sand.id)
        itemInventory.addItem(Sand.id)
        val itemPositionData = Level.ItemPositionData(0, 2, itemInventory)

        level1.victoryType = VictoryType.ITEM_POSITION
        level1.itemPositionData = itemPositionData

        val itemsTile = NeutralTile()
        itemsTile.items.addItem(Sand.id)
        itemsTile.items.addItem(Sand.id)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(itemsTile, NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveAction()
        instruction.parameter = 1
        robotPlayer1.appendAction(instruction)

        val instruction2 = ItemPickupAction()
        instruction2.parameter = Sand.id
        robotPlayer1.appendAction(instruction2)

        val instruction3 = ItemPickupAction()
        instruction3.parameter = Sand.id
        robotPlayer1.appendAction(instruction3)

        val instruction4 = MoveAction()
        instruction4.parameter = 1
        robotPlayer1.appendAction(instruction4)

        val instruction5 = ItemDropAction()
        instruction5.parameter = Sand.id
        robotPlayer1.appendAction(instruction5)

        val instruction6 = ItemDropAction()
        instruction6.parameter = Sand.id
        robotPlayer1.appendAction(instruction6)

        game.attachEventListener {e ->
            when (e){
                Event.LEVEL_VICTORY -> won = true
            }
        }

        robotPlayer1.runInstructions(false)

        assertEquals(won, true)
    }

    @Test
    fun LevelItemPositionFailure(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_UP, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        val itemInventory = ItemInventory()
        itemInventory.addItem(Sand.id)
        itemInventory.addItem(Sand.id)
        val itemPositionData = Level.ItemPositionData(0, 2, itemInventory)

        level1.victoryType = VictoryType.ITEM_POSITION
        level1.itemPositionData = itemPositionData

        val itemsTile = NeutralTile()
        itemsTile.items.addItem(Water.id)
        itemsTile.items.addItem(Water.id)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(itemsTile, NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveAction()
        instruction.parameter = 1
        robotPlayer1.appendAction(instruction)

        val instruction2 = ItemPickupAction()
        instruction2.parameter = Water.id
        robotPlayer1.appendAction(instruction2)

        val instruction3 = ItemPickupAction()
        instruction3.parameter = Water.id
        robotPlayer1.appendAction(instruction3)

        val instruction4 = MoveAction()
        instruction4.parameter = 1
        robotPlayer1.appendAction(instruction4)

        val instruction5 = ItemDropAction()
        instruction5.parameter = Water.id
        robotPlayer1.appendAction(instruction5)

        game.attachEventListener {e ->
            when (e){
                Event.LEVEL_VICTORY -> won = true
            }
        }

        robotPlayer1.runInstructions(false)

        assertEquals(won, false)
    }

    @Test
    fun addSensors(){
        val distanceSensor  = DistanceSensor()

        for(level in levels){
            for((name, robot) in level.players){
                assertEquals(robot.sensorCountAt(RobotPosition.FRONT), 1)
                robot.addSensorTo(RobotPosition.FRONT, distanceSensor)
                assertEquals(robot.getSensors(RobotPosition.FRONT)[0], distanceSensor)

                val tryAddingMore = robot.addSensorTo(RobotPosition.FRONT, distanceSensor)
                assertEquals(tryAddingMore, false)
            }
            game.nextLevel()
        }
    }

    @Test
    fun increaseSensorCount(){
        val distanceSensor  = DistanceSensor()
        val distanceSensor2  = DistanceSensor()

        for(level in levels){
            for((name, robot) in level.players){
                assertEquals(robot.sensorCountAt(RobotPosition.FRONT), 1)
                robot.addSensorTo(RobotPosition.FRONT, distanceSensor)
                robot.setSensorCountAt(RobotPosition.FRONT, 2)
                assertEquals(robot.sensorCountAt(RobotPosition.FRONT), 2)
                robot.addSensorTo(RobotPosition.FRONT, distanceSensor2)

                assertEquals(robot.getSensors(RobotPosition.FRONT)[0], distanceSensor)
                assertEquals(robot.getSensors(RobotPosition.FRONT)[1], distanceSensor2)
            }
            game.nextLevel()
        }
    }

    @Test
    fun decreaseSensorCount(){
        val distanceSensor  = DistanceSensor()
        val distanceSensor2  = DistanceSensor()

        for(level in levels){
            for((name, robot) in level.players){
                robot.setSensorCountAt(RobotPosition.FRONT, 2)
                assertEquals(robot.sensorCountAt(RobotPosition.FRONT), 2)

                robot.addSensorTo(RobotPosition.FRONT, distanceSensor)
                robot.addSensorTo(RobotPosition.FRONT, distanceSensor2)

                robot.setSensorCountAt(RobotPosition.FRONT, 1)
                assertEquals(robot.sensorCountAt(RobotPosition.FRONT), 1)

                robot.setSensorCountAt(RobotPosition.FRONT, 2)
                assertEquals(robot.sensorCountAt(RobotPosition.FRONT), 2)

                assertEquals(robot.getSensors(RobotPosition.FRONT)[0], distanceSensor)
                assertNotEquals(robot.getSensors(RobotPosition.FRONT)[1], distanceSensor2)
            }
            game.nextLevel()
        }
    }

    @Test
    fun removeSensor(){
        val distanceSensor  = DistanceSensor()
        val distanceSensor2  = DistanceSensor()

        for(level in levels){
            for((name, robot) in level.players){
                robot.addSensorTo(RobotPosition.FRONT, distanceSensor)
                assertEquals(robot.getSensors(RobotPosition.FRONT)[0], distanceSensor)
                robot.removeSensorFrom(RobotPosition.FRONT, distanceSensor)
                assertNotEquals(robot.getSensors(RobotPosition.FRONT)[0], distanceSensor)

                robot.addSensorTo(RobotPosition.FRONT, distanceSensor)
                assertEquals(robot.getSensors(RobotPosition.FRONT)[0], distanceSensor)
                robot.removeSensorFrom(RobotPosition.FRONT, 0)
                assertNotEquals(robot.getSensors(RobotPosition.FRONT)[0], distanceSensor)

            }
            game.nextLevel()
        }
    }

    @Test
    fun SensorPlayers(){
        val sensor1 = DistanceSensor()
        val sensor2 = DistanceSensor()

        assertEquals(null, sensor1.player)
        assertEquals(null, sensor2.player)

        for(level in levels){
            for((name, robot) in level.players){
                robot.addSensorTo(RobotPosition.FRONT, sensor1)
                assertEquals(robot, sensor1.player)
                assertEquals(null, sensor2.player)

            }
            game.nextLevel()
        }

        //remove the sensor from the player
        sensor1.player!!.removeSensorFrom(sensor1.sensorPosition!!, sensor1)

        assertEquals(null, sensor1.player)
        assertEquals(null, sensor2.player)
    }

    @Test
    fun ReadDistanceSensor(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_UP, level1)
        val distanceSensor = DistanceSensor()
        robotPlayer1.addSensorTo(RobotPosition.FRONT, distanceSensor)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(VictoryTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), ObstacleTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val readSensorInstruction = ReadSensorAction(mainTopic)
        readSensorInstruction.parameter = distanceSensor
        robotPlayer1.appendAction(readSensorInstruction)

        robotPlayer1.runInstructions(false)

        game.attachEventListener {}

        assertEquals(mainTopic.value, 2)
    }

    @Test
    fun ReadDistanceSensorObstacle(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)
        val distanceSensor = DistanceSensor()
        robotPlayer1.addSensorTo(RobotPosition.FRONT, distanceSensor)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(VictoryTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), ObstacleTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)


        val readSensorInstruction = ReadSensorAction(mainTopic)
        readSensorInstruction.parameter = distanceSensor
        robotPlayer1.appendAction(readSensorInstruction)

        robotPlayer1.runInstructions(false)

        game.attachEventListener {}

        assertEquals(mainTopic.value, 0)
    }

    @Test
    fun itemManager() {
        assertEquals(ItemManager.itemExist(Sand.id), true)
        assertEquals(ItemManager.getItem(Sand.id), Sand)
    }

    @Test
    fun itemInventory() {
        val inventory = ItemInventory()
        val itemsList = intArrayOf(Sand.id, Sand.id, Sand.id, Sand.id, Sand.id)

        // Test new inventory with no initial items
        assertEquals(inventory.hasItem(-1), false)
        assertEquals(inventory.hasItem(Sand.id), false)

        // Test addItem, hasItem
        inventory.addItem(Sand.id)
        assertEquals(inventory.hasItem(Sand.id), true)

        // Test addItems, itemQuantity
        inventory.addItems(itemsList)
        assertEquals(inventory.itemQuantity(Sand.id), 6)

        // Test removeItem
        inventory.removeItem(Sand.id)
        assertEquals(inventory.itemQuantity(Sand.id), 5)

        // Test remove more item than in inventory
        assertEquals(inventory.removeItem(Sand.id, 6), false)
        assertEquals(inventory.itemQuantity(Sand.id), 5)

        // Test remove exactly all items
        assertEquals(inventory.removeItem(Sand.id, 5), true)
        assertEquals(inventory.hasItem(Sand.id), false)
        assertEquals(inventory.itemQuantity(Sand.id), 0)

        // Test removal of non-existent item
        assertEquals(inventory.removeItem(-1), false)
        assertEquals(inventory.removeItem(-1, 1), false)
        assertEquals(inventory.removeItem(Sand.id), false)
        assertEquals(inventory.removeItem(Sand.id, 5), false)
    }

    @Test
    fun itemPickup() {
        for (level in levels) {
            for ((name, robot) in level.players) {
                val tileOldQuantity = level.tileAt(robot.x, robot.y).items.itemQuantity(Sand.id)
                val robotOldQuantity = robot.itemInventory.itemQuantity(Sand.id)

                level.tileAt(robot.x, robot.y).items.addItem(Sand.id)
                assertEquals(level.tileAt(robot.x, robot.y).items.itemQuantity(Sand.id), tileOldQuantity + 1)

                val action = ItemPickupAction()
                action.parameter = Sand.id

                robot.appendAction(action)
                robot.runInstructions(false)
                robot.removeAction(action)

                assertEquals(level.tileAt(robot.x, robot.y).items.itemQuantity(Sand.id), tileOldQuantity)
                assertEquals(robot.itemInventory.itemQuantity(Sand.id), robotOldQuantity + 1)
            }
        }
    }

    @Test
    fun ItemDropTypeMatch(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        level1.tileAt(0, 0).items.oneTypeOnly = false

        level1.tileAt(0, 0).items.addItem(Sand.id)

        game = Game(testLevels, testRobots)

        robotPlayer1.itemInventory.addItem(Water.id)

        val dropInstruction = ItemDropAction()
        dropInstruction.parameter = Water.id
        robotPlayer1.appendAction(dropInstruction)

        robotPlayer1.runInstructions(false)

        assertEquals(robotPlayer1.itemInventory.itemQuantity(Water.id), 0)
        assertEquals(level1.tileAt(0, 0).items.itemQuantity(Water.id), 1)
        assertEquals(level1.tileAt(0, 0).items.itemQuantity(Sand.id), 1)
    }

    @Test
    fun ItemDropTypeMismatch(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        level1.tileAt(0, 0).items.addItem(Sand.id)

        game = Game(testLevels, testRobots)

        robotPlayer1.itemInventory.addItem(Water.id)

        val dropInstruction = ItemDropAction()
        dropInstruction.parameter = Water.id
        robotPlayer1.appendAction(dropInstruction)

        robotPlayer1.runInstructions(false)

        assertEquals(robotPlayer1.itemInventory.itemQuantity(Water.id), 1)
        assertEquals(level1.tileAt(0, 0).items.itemQuantity(Water.id), 0)
        assertEquals(level1.tileAt(0, 0).items.itemQuantity(Sand.id), 1)
    }

    @Test
    fun itemOneTypeOnly() {
        for (level in levels) {
            for (x in 0 until level.properties.width){
                for (y in 0 until level.properties.height){
                    level.tileAt(x, y).items.oneTypeOnly = true
                    level.tileAt(x, y).items.addItem(Sand.id)
                    level.tileAt(x, y).items.addItem(Sand.id)
                    level.tileAt(x, y).items.addItem(Water.id)

                    assertEquals(level.tileAt(x, y).items.itemQuantity(Sand.id), 2)
                    assertEquals(level.tileAt(x, y).items.itemQuantity(Water.id), 0)
                }
            }
        }
    }

    @Test
    fun itemOneTypeOnlyFalse() {
        for (level in levels) {
            for (x in 0 until level.properties.width){
                for (y in 0 until level.properties.height){
                    level.tileAt(x, y).items.oneTypeOnly = false
                    level.tileAt(x, y).items.addItem(Sand.id)
                    level.tileAt(x, y).items.addItem(Sand.id)
                    level.tileAt(x, y).items.addItem(Water.id)

                    assertEquals(level.tileAt(x, y).items.itemQuantity(Sand.id), 2)
                    assertEquals(level.tileAt(x, y).items.itemQuantity(Water.id), 1)
                }
            }
        }
    }

    @Test
    fun ItemRestoreCheckpoint(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)
        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(VictoryTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), ObstacleTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        level1.tileAt(0,0).items.addItem(Sand.id)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val pickupAction = ItemPickupAction()
        pickupAction.parameter = Sand.id
        robotPlayer1.appendAction(pickupAction)

        robotPlayer1.runInstructions(true)

        assertEquals(robotPlayer1.itemInventory.itemQuantity(Sand.id), 0)
        assertEquals(level1.tileAt(0,0).items.itemQuantity(Sand.id), 1)
    }

    @Test
    fun ItemRestoreCheckpointVictoryFalse(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)
        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(VictoryTile(), ObstacleTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        level1.tileAt(0,0).items.addItem(Sand.id)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val pickupAction = ItemPickupAction()
        pickupAction.parameter = Sand.id
        robotPlayer1.appendAction(pickupAction)

        robotPlayer1.runInstructions(true)

        assertEquals(robotPlayer1.itemInventory.itemQuantity(Sand.id), 1)
        assertEquals(level1.tileAt(0,0).items.itemQuantity(Sand.id), 0)
    }

    @Test
    fun RobotCheckpointRestore(){
        for(level in levels){
            for((name, robot) in level.players){
                val moveAction = MoveAction()
                val turnAction = TurnAction()
                val x = robot.x
                val y = robot.y
                val direction = robot.direction
                robot.appendAction(moveAction)
                robot.appendAction(turnAction)
                robot.runInstructions(true)
                robot.removeAction(moveAction)
                robot.removeAction(turnAction)
                assertEquals(robot.x, x)
                assertEquals(robot.y, y)
                assertEquals(robot.direction, direction)
            }
            game.nextLevel()
        }
    }

    @Test
    fun RobotCheckpointRestoreTopic(){
        mainTopic.reset()
        for(level in levels){
            for((name, robot) in level.players){
                val originalVal = mainTopic.value
                val action = ReadSensorAction(mainTopic)

                val sensor = DistanceSensor()
                robot.addSensorTo(RobotPosition.FRONT, sensor)

                action.parameter = sensor

                robot.appendAction(action)
                robot.runInstructions(true)
                assertEquals(originalVal, mainTopic.value)
            }
            game.nextLevel()
        }
    }

    @Test
    fun RobotCheckpointRestoreTopicFalse(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)
        mainTopic.reset()

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_UP, level1)
        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(VictoryTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), ObstacleTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val sensor = DistanceSensor()
        robotPlayer1.addSensorTo(RobotPosition.FRONT, sensor)

        val action = ReadSensorAction(mainTopic)
        action.parameter = sensor
        robotPlayer1.appendAction(action)

        robotPlayer1.runInstructions(false)

        assertEquals(mainTopic.value, 2)
    }

    @Test
    fun RobotCheckpointRestoreVictoryFalse(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)
        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(VictoryTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), ObstacleTile())) as ArrayList<ArrayList<Tile>>)

        level1.tileAt(0,0).items.addItem(Sand.id)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val sensor = DistanceSensor()
        robotPlayer1.addSensorTo(RobotPosition.FRONT, sensor)

        val action = ReadSensorAction(mainTopic)
        action.parameter = sensor
        robotPlayer1.appendAction(action)

        val turnAction = TurnAction()
        turnAction.parameter = RobotRotation.COUNTERCLOCKWISE
        robotPlayer1.appendAction(turnAction)

        val moveAction = MoveAction()
        robotPlayer1.appendAction(moveAction)

        robotPlayer1.runInstructions(true)

        assertEquals(robotPlayer1.y, 1)
        assertEquals(robotPlayer1.direction, RobotOrientation.DIRECTION_UP)
        assertEquals(mainTopic.value, 1)
    }

    @Test
    fun MoveActionMacroSetParameter(){
        val action = MoveActionMacro()
        assertEquals(action.parameter, 1)
        assertEquals(action.getMacro().size, 1)

        action.parameter = 5
        assertEquals(action.parameter, 5)
        assertEquals(action.getMacro().size, 5)

        action.parameter = 3
        assertEquals(action.parameter, 3)
        assertEquals(action.getMacro().size, 3)

        action.parameter = 7
        assertEquals(action.parameter, 7)
        assertEquals(action.getMacro().size, 7)
    }

    @Test
    fun MoveWithMoveActionMacro(){
        for(level in levels){
            for((name, robot) in level.players){
                val action = MoveActionMacro()
                
                val y = robot.y
                val x = robot.x
                val orientation = robot.direction
                action.parameter = 2
                robot.appendAction(action)
                robot.runInstructions(false)
                robot.removeAction(action)

                val difference = distanceCanMove(x, y, orientation, action.parameter, game.currentLevel)
                if (orientation == RobotOrientation.DIRECTION_UP) {
                    assertEquals(robot.y, y + difference)
                    assertEquals(robot.x, x)
                }
                else if (orientation == RobotOrientation.DIRECTION_DOWN) {
                    assertEquals(robot.y, y - difference)
                    assertEquals(robot.x, x)
                }
                else if (orientation == RobotOrientation.DIRECTION_LEFT) {
                    assertEquals(robot.y, y)
                    assertEquals(robot.x, x - difference)
                }
                else if (orientation == RobotOrientation.DIRECTION_RIGHT) {
                    assertEquals(robot.y, y)
                    assertEquals(robot.x, x + difference)
                }
            }
            game.nextLevel()
        }
    }

    @Test
    fun AddAction(){
        for(level in levels){
            for((name, robot) in level.players){
                val action = MoveAction()
                val action2 = TurnAction()
                val action3 = MoveActionMacro()

                robot.appendAction(action)
                robot.insertAction(action3, 5)

                assertEquals(robot.getProcedure()[0] is MoveAction, true)
                assertEquals(robot.getProcedure()[1] is MoveActionMacro, true)

                robot.insertAction(action2, 1)
                
                assertEquals(robot.getProcedure()[0] is MoveAction, true)
                assertEquals(robot.getProcedure()[1] is TurnAction, true)
                assertEquals(robot.getProcedure()[2] is MoveActionMacro, true)

            }
            game.nextLevel()
        }
    }

    @Test
    fun ForLoopActionMove(){
        for(level in levels){
            for((name, robot) in level.players){
                val action = MoveAction()
                action.parameter = 1

                val loopAction = ForLoopAction()
                loopAction.addToMacro(action, robot.getLimitDifference())
                loopAction.parameter = 3

                robot.appendAction(loopAction)

                val distance = distanceCanMove(robot.x, robot.y, robot.direction, 3, level)
                val originalX = robot.x
                val originalY = robot.y
                robot.runInstructions(false)

                if (robot.direction == RobotOrientation.DIRECTION_DOWN){
                    assertEquals(robot.y, originalY - distance)
                }
                else if (robot.direction == RobotOrientation.DIRECTION_UP){
                    assertEquals(robot.y, originalY + distance)
                }
                else if (robot.direction == RobotOrientation.DIRECTION_RIGHT){
                    assertEquals(robot.x, originalX + distance)
                }
                else if (robot.direction == RobotOrientation.DIRECTION_LEFT){
                    assertEquals(robot.x, originalX - distance)
                }
            }
            game.nextLevel()
        }
    }

    @Test
    fun ForLoopActionMoveMacro(){
        for(level in levels){
            for((name, robot) in level.players){
                val action = MoveActionMacro()
                action.parameter = 2

                val loopAction = ForLoopAction()
                loopAction.addToMacro(action, robot.getLimitDifference())
                loopAction.parameter = 3

                robot.appendAction(loopAction)

                val distance = distanceCanMove(robot.x, robot.y, robot.direction, 6, level)
                val originalX = robot.x
                val originalY = robot.y
                robot.runInstructions(false)

                if (robot.direction == RobotOrientation.DIRECTION_DOWN){
                    assertEquals(robot.y, originalY - distance)
                }
                else if (robot.direction == RobotOrientation.DIRECTION_UP){
                    assertEquals(robot.y, originalY + distance)
                }
                else if (robot.direction == RobotOrientation.DIRECTION_RIGHT){
                    assertEquals(robot.x, originalX + distance)
                }
                else if (robot.direction == RobotOrientation.DIRECTION_LEFT){
                    assertEquals(robot.x, originalX - distance)
                }
            }
            game.nextLevel()
        }
    }

    @Test
    fun ForLoopActionNested(){
        for(level in levels){
            for((name, robot) in level.players){
                val action = MoveActionMacro()
                action.parameter = 1

                val loopAction = ForLoopAction()
                loopAction.addToMacro(action, robot.getLimitDifference())
                loopAction.parameter = 2

                val loopAction2 = ForLoopAction()
                loopAction2.addToMacro(loopAction, robot.getLimitDifference())
                loopAction2.parameter = 3

                robot.appendAction(loopAction2)

                val distance = distanceCanMove(robot.x, robot.y, robot.direction, 6, level)
                val originalX = robot.x
                val originalY = robot.y
                robot.runInstructions(false)

                if (robot.direction == RobotOrientation.DIRECTION_DOWN){
                    assertEquals(robot.y, originalY - distance)
                }
                else if (robot.direction == RobotOrientation.DIRECTION_UP){
                    assertEquals(robot.y, originalY + distance)
                }
                else if (robot.direction == RobotOrientation.DIRECTION_RIGHT){
                    assertEquals(robot.x, originalX + distance)
                }
                else if (robot.direction == RobotOrientation.DIRECTION_LEFT){
                    assertEquals(robot.x, originalX - distance)
                }
            }
            game.nextLevel()
        }
    }

    @Test
    fun TopicListener(){
        var topicVal = 6
        mainTopic.topicListeners.add{value -> topicVal = value as Int}

        mainTopic.value = 8

        assertEquals(topicVal, mainTopic.value)
    }

    @Test
    fun fullProcedureSize(){
        for(level in levels){
            for((name, robot) in level.players){
                val action1 = MoveActionMacro()
                action1.parameter = 3
                val action2 = TurnAction()
                val action3 = TurnAction()

                val action4 = ForLoopAction()
                val action5 = TurnAction()
                val action6 = TurnAction()
                action4.addToMacro(action5, robot.getLimitDifference())
                action4.addToMacro(action6, robot.getLimitDifference())

                val action7 = ForLoopAction()
                val action8 = TurnAction()
                action7.addToMacro(action8, robot.getLimitDifference())
                action4.addToMacro(action7, robot.getLimitDifference())

                robot.appendAction(action1)
                robot.appendAction(action2)
                robot.appendAction(action3)
                robot.appendAction(action4)

                assertEquals(8, robot.getFullProcedureSize(robot.getProcedure()))
            }
            game.nextLevel()
        }
    }

    @Test
    fun actionLimitNoMacro(){
        for(level in levels){
            for((name, robot) in level.players){
                robot.actionLimit = 3
                val action1 = MoveActionMacro()
                action1.parameter = 3
                val action2 = TurnAction()
                val action3 = TurnAction()
                val action4 = MoveActionMacro()

                assertEquals(true, robot.appendAction(action1))
                assertEquals(true, robot.appendAction(action2))
                assertEquals(true, robot.appendAction(action3))
                assertEquals(false, robot.appendAction(action4))

                assertEquals(3, robot.getFullProcedureSize(robot.getProcedure()))
                assertEquals(robot.getProcedure()[0], action1)
                assertEquals(robot.getProcedure()[1], action2)
                assertEquals(robot.getProcedure()[2], action3)

            }
            game.nextLevel()
        }
    }

    @Test
    fun actionLimitFullMacroAdded(){
        for(level in levels){
            for((name, robot) in level.players){
                robot.actionLimit = 3
                val action1 = MoveActionMacro()
                action1.parameter = 3
                val action2 = TurnAction()

                val action3 = ForLoopAction()
                val action4 = TurnAction()
                val action5 = TurnAction()

                action3.addToMacro(action4, robot.getLimitDifference())
                action3.addToMacro(action5, robot.getLimitDifference())

                assertEquals(true, robot.appendAction(action1))
                assertEquals(true, robot.appendAction(action2))
                assertEquals(false, robot.appendAction(action3))

                assertEquals(2, robot.getFullProcedureSize(robot.getProcedure()))
                assertEquals(robot.getProcedure()[0], action1)
                assertEquals(robot.getProcedure()[1], action2)

            }
            game.nextLevel()
        }
    }

    @Test
    fun actionLimitUnlimitedNoMacro(){
        for(level in levels){
            for((name, robot) in level.players){
                robot.actionLimit = 3
                robot.actionLimit = -1
                val action1 = MoveActionMacro()
                action1.parameter = 3
                val action2 = TurnAction()
                val action3 = TurnAction()
                val action4 = MoveActionMacro()

                assertEquals(true, robot.appendAction(action1))
                assertEquals(true, robot.appendAction(action2))
                assertEquals(true, robot.appendAction(action3))
                assertEquals(true, robot.appendAction(action4))

                assertEquals(4, robot.getFullProcedureSize(robot.getProcedure()))
                assertEquals(robot.getProcedure()[0], action1)
                assertEquals(robot.getProcedure()[1], action2)
                assertEquals(robot.getProcedure()[2], action3)
                assertEquals(robot.getProcedure()[3], action4)

            }
            game.nextLevel()
        }
    }

    @Test
    fun actionLimitMacroActionAddedWhileAttached(){
        for(level in levels){
            for((name, robot) in level.players){
                robot.actionLimit = 3
                val action1 = TurnAction()

                val action2 = ForLoopAction()
                val action3 = TurnAction()
                val action4 = MoveActionMacro()

                assertEquals(true, robot.appendAction(action1))
                assertEquals(true, action2.addToMacro(action3, robot.getLimitDifference()))

                assertEquals(true, robot.appendAction(action2))

                assertEquals(false, action2.addToMacro(action4, robot.getLimitDifference()))

                assertEquals(3, robot.getFullProcedureSize(robot.getProcedure()))
                assertEquals(robot.getProcedure()[0], action1)
                assertEquals(robot.getProcedure()[1], action2)
                assertEquals(action2.getMacro().size, 1)
                assertEquals(action2.getMacro()[0], action3 as Action<Any>)
            }
            game.nextLevel()
        }
    }

    @Test
    fun actionLimitMacroNestedLoops(){
        for(level in levels){
            for((name, robot) in level.players){
                robot.actionLimit = 3
                val action1 = TurnAction()
                val action2 = TurnAction()
                val action3 = TurnAction()

                val forLoopAction1 = ForLoopAction()
                forLoopAction1.addToMacro(action1, -1)
                forLoopAction1.addToMacro(action2, -1)
                forLoopAction1.addToMacro(action3, -1)

                val forLoopAction2 = ForLoopAction()

                assertEquals(true, robot.appendAction(forLoopAction2))
                assertEquals(false, forLoopAction2.addToMacro(forLoopAction1, robot.getLimitDifference()))

                assertEquals(1, robot.getFullProcedureSize(robot.getProcedure()))
                assertEquals(robot.getProcedure()[0], forLoopAction2)
                assertEquals(forLoopAction2.getMacro().size, 0)
            }
            game.nextLevel()
        }
    }

    @Test
    fun actionLimitMacroNestedLoopsTrue(){
        for(level in levels){
            for((name, robot) in level.players){
                robot.actionLimit = 3
                val action1 = TurnAction()

                val forLoopAction1 = ForLoopAction()
                forLoopAction1.addToMacro(action1, -1)

                val forLoopAction2 = ForLoopAction()

                assertEquals(true, robot.appendAction(forLoopAction2))
                assertEquals(true, forLoopAction2.addToMacro(forLoopAction1, robot.getLimitDifference()))

                assertEquals(3, robot.getFullProcedureSize(robot.getProcedure()))
            }
            game.nextLevel()
        }
    }
}