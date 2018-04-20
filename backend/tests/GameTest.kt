import kotlin.test.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.actions.ConditionalWithList
import org.chicagoedt.rosette.actions.operations.TopicEqualsComparison
import org.chicagoedt.rosette.actions.robotActions.*
import org.chicagoedt.rosette.collectibles.ItemInventory
import org.chicagoedt.rosette.collectibles.ItemManager
import org.chicagoedt.rosette.collectibles.etc.Sand
import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.*
import org.chicagoedt.rosette.sensors.DistanceSensor
import org.chicagoedt.rosette.tiles.*

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
    private val levels = getLevels()
    private val robots = getRobots()

    @BeforeTest
    fun SetUp(){
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

        val difference = instruction.distanceCanMove(x, y, orientation, parameter, game.currentLevel)
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
    fun LevelVictory(){
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
                arrayListOf(NeutralTile(), VictoryTile(), NeutralTile())) as ArrayList<ArrayList<Tile>>)

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveAction()
        instruction.parameter = 1
        robotPlayer1.appendAction(instruction)

        game.attachEventListener { won = true }

        robotPlayer1.runInstructions(false)

        assertEquals(won, true)
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
    fun ReadDistanceSensor(){
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

        val readSensorInstruction = ReadSensorAction(game.mainTopic)
        readSensorInstruction.parameter = distanceSensor
        robotPlayer1.appendAction(readSensorInstruction)

        val instruction = ConditionalWithList()
        instruction.parameter = TopicEqualsComparison(game.mainTopic, 1)
        val turnInstruction = TurnAction()
        turnInstruction.parameter = RobotRotation.COUNTERCLOCKWISE
        instruction.addToMacro(turnInstruction)
        instruction.addToMacro(MoveAction())
        robotPlayer1.appendAction(instruction)

        game.attachEventListener {e ->
            when (e){
                Event.LEVEL_VICTORY -> won = true
            }
        }

        robotPlayer1.runInstructions(false)

        game.attachEventListener {}

        assertEquals(won, true)
    }

    @Test
    fun ReadDistanceSensorFalse(){
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

        val readSensorInstruction = ReadSensorAction(game.mainTopic)
        readSensorInstruction.parameter = distanceSensor
        robotPlayer1.appendAction(readSensorInstruction)

        val instruction = ConditionalWithList()
        instruction.parameter = TopicEqualsComparison(game.mainTopic, 2)
        val turnInstruction = TurnAction()
        turnInstruction.parameter = RobotRotation.COUNTERCLOCKWISE
        instruction.addToMacro(turnInstruction)
        instruction.addToMacro(MoveAction())
        robotPlayer1.appendAction(instruction)

        game.attachEventListener {e ->
            when (e){
                Event.LEVEL_VICTORY -> won = true
            }
        }

        robotPlayer1.runInstructions(false)

        game.attachEventListener {}

        assertEquals(won, false)
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
    fun itemDrop() {
        for (level in levels) {
            for ((name, robot) in level.players) {
                val tileOldQuantity = level.tileAt(robot.x, robot.y).items.itemQuantity(Sand.id)
                val robotOldQuantity = robot.itemInventory.itemQuantity(Sand.id)

                robot.itemInventory.addItem(Sand.id)
                assertEquals(robot.itemInventory.itemQuantity(Sand.id), robotOldQuantity + 1)

                val action = ItemDropAction()
                action.parameter = Sand.id

                robot.appendAction(action)
                robot.runInstructions(false)
                robot.removeAction(action)

                assertEquals(level.tileAt(robot.x, robot.y).items.itemQuantity(Sand.id), tileOldQuantity + 1)
                assertEquals(robot.itemInventory.itemQuantity(Sand.id), robotOldQuantity)
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
}