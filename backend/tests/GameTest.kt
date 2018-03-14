import kotlin.test.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Instructions.ConditionalWithList
import org.chicagoedt.rosette.Instructions.MoveInstruction
import org.chicagoedt.rosette.Instructions.Operations.TopicEqualsComparison
import org.chicagoedt.rosette.Instructions.ReadSensorInstruction
import org.chicagoedt.rosette.Instructions.TurnInstruction
import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.*
import org.chicagoedt.rosette.Sensors.DistanceSensor
import org.chicagoedt.rosette.Tiles.NeutralTile
import org.chicagoedt.rosette.Tiles.ObstacleTile
import org.chicagoedt.rosette.Tiles.VictoryTile


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

                val moveInstruction = MoveInstruction()
                game.currentLevel.attachInstruction(name, moveInstruction)
                val turnInstruction = TurnInstruction()
                game.currentLevel.attachInstruction(name, turnInstruction)

                val list = game.currentLevel.getInstructions(name)
                assertEquals(list[0].name, moveInstruction.name)
                assertEquals(list[1].name, turnInstruction.name)
            }
            game.nextLevel()
        }
    }

    @Test
    fun RobotInstructionTurn() {
        val instruction = TurnInstruction()
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
        val next = nextDirection(game.currentLevel.players[name]!!.direction, rotation)
        val turn = TurnInstruction()
        turn.parameter = rotation
        game.currentLevel.attachInstruction(name, turn)
        game.currentLevel.runInstructionsFor(name)
        game.currentLevel.removeInstruction(name, turn)
        if (assert) assertEquals(game.currentLevel.players[name]!!.direction, next)
    }

    private fun move(name: String, orientation: RobotOrientation, parameter: Int, assert: Boolean) {
        val instruction = MoveInstruction()
        instruction.parameter = parameter
        val y = game.currentLevel.players[name]!!.y
        val x = game.currentLevel.players[name]!!.x
        game.currentLevel.attachInstruction(name, instruction)
        game.currentLevel.runInstructionsFor(name)
        game.currentLevel.removeInstruction(name, instruction)

        val difference = instruction.distanceCanMove(x, y, orientation, parameter, game.currentLevel)
        if (orientation == RobotOrientation.DIRECTION_UP) {
            if (assert)assertEquals(game.currentLevel.players[name]!!.y, y + difference)
            if (assert)assertEquals(game.currentLevel.players[name]!!.x, x)
        }
        else if (orientation == RobotOrientation.DIRECTION_DOWN) {
            if (assert)
                assertEquals(game.currentLevel.players[name]!!.y, y - difference)
            if (assert)assertEquals(game.currentLevel.players[name]!!.x, x)
        }
        else if (orientation == RobotOrientation.DIRECTION_LEFT) {
            if (assert)assertEquals(game.currentLevel.players[name]!!.y, y)
            if (assert)assertEquals(game.currentLevel.players[name]!!.x, x - difference)
        }
        else if (orientation == RobotOrientation.DIRECTION_RIGHT) {
            if (assert)assertEquals(game.currentLevel.players[name]!!.y, y)
            if (assert)assertEquals(game.currentLevel.players[name]!!.x, x + difference)
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

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_DOWN)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        val level1 = Level(Level.Properties("Levels 1", 0, 3, 3), list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile())))

        testLevels.add(level1)
        game = Game(testLevels, testRobots)

        val instruction = MoveInstruction()
        instruction.parameter = 1
        game.currentLevel.attachInstruction(surus.name, instruction)
        game.currentLevel.runInstructionsFor(surus.name)
        assertEquals(level1.players[surus.name]!!.x, 0)
        assertEquals(level1.players[surus.name]!!.y, 0)

        game.currentLevel.removeInstruction(surus.name, instruction)
        val turnInstruction = TurnInstruction()
        turnInstruction.parameter = RobotRotation.CLOCKWISE
        game.currentLevel.attachInstruction(surus.name, turnInstruction)
        game.currentLevel.runInstructionsFor(surus.name)

        game.currentLevel.removeInstruction(surus.name, turnInstruction)
        game.currentLevel.attachInstruction(surus.name, instruction)
        game.currentLevel.runInstructionsFor(surus.name)
        assertEquals(level1.players[surus.name]!!.x, 0)
        assertEquals(level1.players[surus.name]!!.y, 0)
    }

    @Test
    fun MoveObstacle(){
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        val level1 = Level(Level.Properties("Levels 1", 0, 3, 3), list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), ObstacleTile(), NeutralTile())))

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveInstruction()
        instruction.parameter = 1
        game.currentLevel.attachInstruction(surus.name, instruction)

        game.currentLevel.runInstructionsFor(surus.name)

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

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        val level1 = Level(Level.Properties("Levels 1", 0, 3, 3), list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), VictoryTile(), NeutralTile())))

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveInstruction()
        instruction.parameter = 1
        game.currentLevel.attachInstruction(surus.name, instruction)

        game.attachEventListener { won = true }

        game.currentLevel.runInstructionsFor(surus.name)

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

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT)
        val distanceSensor = DistanceSensor()
        robotPlayer1.addSensorTo(RobotPosition.FRONT, distanceSensor)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        val level1 = Level(Level.Properties("Levels 1", 0, 3, 3), list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(VictoryTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), ObstacleTile(), NeutralTile())))

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val readSensorInstruction = ReadSensorInstruction(game.mainTopic)
        readSensorInstruction.parameter = distanceSensor
        game.currentLevel.attachInstruction(surus.name, readSensorInstruction)

        val instruction = ConditionalWithList()
        instruction.parameter = TopicEqualsComparison(game.mainTopic, 1)
        val turnInstruction = TurnInstruction()
        turnInstruction.parameter = RobotRotation.COUNTERCLOCKWISE
        instruction.addToList(turnInstruction)
        instruction.addToList(MoveInstruction())
        game.currentLevel.attachInstruction(surus.name, instruction)

        game.attachEventListener { won = true }

        game.currentLevel.runInstructionsFor(surus.name)

        assertEquals(won, true)
    }

    @Test
    fun ReadDistanceSensorFalse(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT)
        val distanceSensor = DistanceSensor()
        robotPlayer1.addSensorTo(RobotPosition.FRONT, distanceSensor)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        val level1 = Level(Level.Properties("Levels 1", 0, 3, 3), list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(VictoryTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), ObstacleTile(), NeutralTile())))

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val readSensorInstruction = ReadSensorInstruction(game.mainTopic)
        readSensorInstruction.parameter = distanceSensor
        game.currentLevel.attachInstruction(surus.name, readSensorInstruction)

        val instruction = ConditionalWithList()
        instruction.parameter = TopicEqualsComparison(game.mainTopic, 2)
        val turnInstruction = TurnInstruction()
        turnInstruction.parameter = RobotRotation.COUNTERCLOCKWISE
        instruction.addToList(turnInstruction)
        instruction.addToList(MoveInstruction())
        game.currentLevel.attachInstruction(surus.name, instruction)

        game.attachEventListener { won = true }

        game.currentLevel.runInstructionsFor(surus.name)

        assertEquals(won, false)
    }
}