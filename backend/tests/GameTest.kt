import kotlin.test.*
import org.chicagoedt.robocode.*
import org.chicagoedt.robocode.actions.ConditionalWithList
import org.chicagoedt.robocode.actions.operations.TopicEqualsComparison
import org.chicagoedt.robocode.actions.robotActions.*
import org.chicagoedt.robocode.collectibles.Collectible
import org.chicagoedt.robocode.collectibles.ItemInventory
import org.chicagoedt.robocode.collectibles.ItemManager
import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.levels.VictoryType
import org.chicagoedt.robocode.robots.*
import org.chicagoedt.robocode.sensors.DistanceSensor
import org.chicagoedt.robocode.tiles.*

private val SAND_ID = 0
private val WATER_ID = 1

/**
 * @return All levels for the current game configuration
 */
fun getLevels() : ArrayList<Level>{
    if (!ItemManager.itemExist(SAND_ID) && !ItemManager.itemExist(WATER_ID)){
        ItemManager.addItem(Collectible(SAND_ID, "sand", ""))
        ItemManager.addItem(Collectible(WATER_ID, "water", ""))
    }

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
        itemInventory.addItem(SAND_ID)
        itemInventory.addItem(SAND_ID)
        val itemPositionData = Level.ItemPositionData(0, 2, itemInventory)

        level1.victoryType = VictoryType.ITEM_POSITION
        level1.itemPositionData = itemPositionData

        val itemsTile = NeutralTile()
        itemsTile.items.addItem(SAND_ID)
        itemsTile.items.addItem(SAND_ID)

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
        instruction2.parameter = SAND_ID
        robotPlayer1.appendAction(instruction2)

        val instruction3 = ItemPickupAction()
        instruction3.parameter = SAND_ID
        robotPlayer1.appendAction(instruction3)

        val instruction4 = MoveAction()
        instruction4.parameter = 1
        robotPlayer1.appendAction(instruction4)

        val instruction5 = ItemDropAction()
        instruction5.parameter = SAND_ID
        robotPlayer1.appendAction(instruction5)

        val instruction6 = ItemDropAction()
        instruction6.parameter = SAND_ID
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
        itemInventory.addItem(SAND_ID)
        itemInventory.addItem(SAND_ID)
        val itemPositionData = Level.ItemPositionData(0, 2, itemInventory)

        level1.victoryType = VictoryType.ITEM_POSITION
        level1.itemPositionData = itemPositionData

        val itemsTile = NeutralTile()
        itemsTile.items.addItem(WATER_ID)
        itemsTile.items.addItem(WATER_ID)

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
        instruction2.parameter = WATER_ID
        robotPlayer1.appendAction(instruction2)

        val instruction3 = ItemPickupAction()
        instruction3.parameter = WATER_ID
        robotPlayer1.appendAction(instruction3)

        val instruction4 = MoveAction()
        instruction4.parameter = 1
        robotPlayer1.appendAction(instruction4)

        val instruction5 = ItemDropAction()
        instruction5.parameter = WATER_ID
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
        assertEquals(ItemManager.itemExist(SAND_ID), true)
        assertEquals(ItemManager.itemExist(WATER_ID), true)
    }

    @Test
    fun itemInventory() {
        val inventory = ItemInventory()
        val itemsList = intArrayOf(SAND_ID, SAND_ID, SAND_ID, SAND_ID, SAND_ID)

        // Test new inventory with no initial items
        assertEquals(inventory.hasItem(-1), false)
        assertEquals(inventory.hasItem(SAND_ID), false)

        // Test addItem, hasItem
        inventory.addItem(SAND_ID)
        assertEquals(inventory.hasItem(SAND_ID), true)

        // Test addItems, itemQuantity
        inventory.addItems(itemsList)
        assertEquals(inventory.itemQuantity(SAND_ID), 6)

        // Test removeItem
        inventory.removeItem(SAND_ID)
        assertEquals(inventory.itemQuantity(SAND_ID), 5)

        // Test remove more item than in inventory
        assertEquals(inventory.removeItem(SAND_ID, 6), false)
        assertEquals(inventory.itemQuantity(SAND_ID), 5)

        // Test remove exactly all items
        assertEquals(inventory.removeItem(SAND_ID, 5), true)
        assertEquals(inventory.hasItem(SAND_ID), false)
        assertEquals(inventory.itemQuantity(SAND_ID), 0)

        // Test removal of non-existent item
        assertEquals(inventory.removeItem(-1), false)
        assertEquals(inventory.removeItem(-1, 1), false)
        assertEquals(inventory.removeItem(SAND_ID), false)
        assertEquals(inventory.removeItem(SAND_ID, 5), false)
    }

    @Test
    fun itemPickup() {
        for (level in levels) {
            for ((name, robot) in level.players) {
                val tileOldQuantity = level.tileAt(robot.x, robot.y).items.itemQuantity(SAND_ID)
                val robotOldQuantity = robot.itemInventory.itemQuantity(SAND_ID)

                level.tileAt(robot.x, robot.y).items.addItem(SAND_ID)
                assertEquals(level.tileAt(robot.x, robot.y).items.itemQuantity(SAND_ID), tileOldQuantity + 1)

                val action = ItemPickupAction()
                action.parameter = SAND_ID

                robot.appendAction(action)
                robot.runInstructions(false)
                robot.removeAction(action)

                assertEquals(level.tileAt(robot.x, robot.y).items.itemQuantity(SAND_ID), tileOldQuantity)
                assertEquals(robot.itemInventory.itemQuantity(SAND_ID), robotOldQuantity + 1)
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

        level1.tileAt(0, 0).items.addItem(SAND_ID)

        game = Game(testLevels, testRobots)

        robotPlayer1.itemInventory.addItem(WATER_ID)

        val dropInstruction = ItemDropAction()
        dropInstruction.parameter = WATER_ID
        robotPlayer1.appendAction(dropInstruction)

        robotPlayer1.runInstructions(false)

        assertEquals(robotPlayer1.itemInventory.itemQuantity(WATER_ID), 0)
        assertEquals(level1.tileAt(0, 0).items.itemQuantity(WATER_ID), 1)
        assertEquals(level1.tileAt(0, 0).items.itemQuantity(SAND_ID), 1)
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

        level1.tileAt(0, 0).items.addItem(SAND_ID)

        game = Game(testLevels, testRobots)

        robotPlayer1.itemInventory.addItem(WATER_ID)

        val dropInstruction = ItemDropAction()
        dropInstruction.parameter = WATER_ID
        robotPlayer1.appendAction(dropInstruction)

        robotPlayer1.runInstructions(false)

        assertEquals(robotPlayer1.itemInventory.itemQuantity(WATER_ID), 1)
        assertEquals(level1.tileAt(0, 0).items.itemQuantity(WATER_ID), 0)
        assertEquals(level1.tileAt(0, 0).items.itemQuantity(SAND_ID), 1)
    }

    @Test
    fun itemOneTypeOnly() {
        for (level in levels) {
            for (x in 0 until level.properties.width){
                for (y in 0 until level.properties.height){
                    level.tileAt(x, y).items.oneTypeOnly = true
                    level.tileAt(x, y).items.addItem(SAND_ID)
                    level.tileAt(x, y).items.addItem(SAND_ID)
                    level.tileAt(x, y).items.addItem(WATER_ID)

                    assertEquals(level.tileAt(x, y).items.itemQuantity(SAND_ID), 2)
                    assertEquals(level.tileAt(x, y).items.itemQuantity(WATER_ID), 0)
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
                    level.tileAt(x, y).items.addItem(SAND_ID)
                    level.tileAt(x, y).items.addItem(SAND_ID)
                    level.tileAt(x, y).items.addItem(WATER_ID)

                    assertEquals(level.tileAt(x, y).items.itemQuantity(SAND_ID), 2)
                    assertEquals(level.tileAt(x, y).items.itemQuantity(WATER_ID), 1)
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

        level1.tileAt(0,0).items.addItem(SAND_ID)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val pickupAction = ItemPickupAction()
        pickupAction.parameter = SAND_ID
        robotPlayer1.appendAction(pickupAction)

        robotPlayer1.runInstructions(true)

        assertEquals(robotPlayer1.itemInventory.itemQuantity(SAND_ID), 0)
        assertEquals(level1.tileAt(0,0).items.itemQuantity(SAND_ID), 1)
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

        level1.tileAt(0,0).items.addItem(SAND_ID)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val pickupAction = ItemPickupAction()
        pickupAction.parameter = SAND_ID
        robotPlayer1.appendAction(pickupAction)

        robotPlayer1.runInstructions(true)

        assertEquals(robotPlayer1.itemInventory.itemQuantity(SAND_ID), 1)
        assertEquals(level1.tileAt(0,0).items.itemQuantity(SAND_ID), 0)
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

        level1.tileAt(0,0).items.addItem(SAND_ID)

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
                loopAction.addToMacro(action)
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
                loopAction.addToMacro(action)
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
                loopAction.addToMacro(action)
                loopAction.parameter = 2

                val loopAction2 = ForLoopAction()
                loopAction2.addToMacro(loopAction)
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
}