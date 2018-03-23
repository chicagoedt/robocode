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
                robot.attachInstruction(moveInstruction)
                val turnInstruction = TurnInstruction()
                robot.attachInstruction(turnInstruction)

                val list = robot.getInstructions()
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
        val robot = game.currentLevel.players[name]!!
        val next = nextDirection(robot.direction, rotation)
        val turn = TurnInstruction()
        turn.parameter = rotation
        robot.attachInstruction(turn)
        robot.runInstructions()
        robot.removeInstruction(turn)
        if (assert) assertEquals(robot.direction, next)
    }

    private fun move(name: String, orientation: RobotOrientation, parameter: Int, assert: Boolean) {
        val robot = game.currentLevel.players[name]!!
        val instruction = MoveInstruction()
        instruction.parameter = parameter
        val y = robot.y
        val x = robot.x
        robot.attachInstruction(instruction)
        robot.runInstructions()
        robot.removeInstruction(instruction)

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

        val level1 = Level(Level.Properties("Levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_DOWN, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile())))

        testLevels.add(level1)
        game = Game(testLevels, testRobots)

        val instruction = MoveInstruction()
        instruction.parameter = 1
        robotPlayer1.attachInstruction(instruction)
        robotPlayer1.runInstructions()
        assertEquals(robotPlayer1.x, 0)
        assertEquals(robotPlayer1.y, 0)

        robotPlayer1.removeInstruction(instruction)
        val turnInstruction = TurnInstruction()
        turnInstruction.parameter = RobotRotation.CLOCKWISE
        robotPlayer1.attachInstruction(turnInstruction)
        robotPlayer1.runInstructions()

        robotPlayer1.removeInstruction(turnInstruction)
        robotPlayer1.attachInstruction(instruction)
        robotPlayer1.runInstructions()
        assertEquals(level1.players[surus.name]!!.x, 0)
        assertEquals(level1.players[surus.name]!!.y, 0)
    }

    @Test
    fun MoveObstacle(){
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("Levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), ObstacleTile(), NeutralTile())))

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveInstruction()
        instruction.parameter = 1
        robotPlayer1.attachInstruction(instruction)

        robotPlayer1.runInstructions()

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

        val level1 = Level(Level.Properties("Levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)


        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), VictoryTile(), NeutralTile())))

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val instruction = MoveInstruction()
        instruction.parameter = 1
        robotPlayer1.attachInstruction(instruction)

        game.attachEventListener { won = true }

        robotPlayer1.runInstructions()

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

        val level1 = Level(Level.Properties("Levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)
        val distanceSensor = DistanceSensor()
        robotPlayer1.addSensorTo(RobotPosition.FRONT, distanceSensor)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(VictoryTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), ObstacleTile(), NeutralTile())))

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val readSensorInstruction = ReadSensorInstruction(game.mainTopic)
        readSensorInstruction.parameter = distanceSensor
        robotPlayer1.attachInstruction(readSensorInstruction)

        val instruction = ConditionalWithList()
        instruction.parameter = TopicEqualsComparison(game.mainTopic, 1)
        val turnInstruction = TurnInstruction()
        turnInstruction.parameter = RobotRotation.COUNTERCLOCKWISE
        instruction.addToList(turnInstruction)
        instruction.addToList(MoveInstruction())
        robotPlayer1.attachInstruction(instruction)

        game.attachEventListener { won = true }

        robotPlayer1.runInstructions()

        assertEquals(won, true)
    }

    @Test
    fun ReadDistanceSensorFalse(){
        var won = false
        val testRobots = ArrayList<Robot>()
        val surus = Robot("Surus", "")
        testRobots.add(surus)

        val testLevels = ArrayList<Level>()

        val level1 = Level(Level.Properties("Levels 1", 0, 3, 3))

        val robotPlayer1 = RobotPlayer("Surus", 0, 0, RobotOrientation.DIRECTION_RIGHT, level1)
        val distanceSensor = DistanceSensor()
        robotPlayer1.addSensorTo(RobotPosition.FRONT, distanceSensor)

        val list1 = ArrayList<RobotPlayer>()
        list1.add(robotPlayer1)

        level1.setPlayers(list1)

        level1.makeGrid(arrayListOf(
                arrayListOf(NeutralTile(), NeutralTile(), NeutralTile()),
                arrayListOf(VictoryTile(), NeutralTile(), NeutralTile()),
                arrayListOf(NeutralTile(), ObstacleTile(), NeutralTile())))

        testLevels.add(level1)

        game = Game(testLevels, testRobots)

        val readSensorInstruction = ReadSensorInstruction(game.mainTopic)
        readSensorInstruction.parameter = distanceSensor
        robotPlayer1.attachInstruction(readSensorInstruction)

        val instruction = ConditionalWithList()
        instruction.parameter = TopicEqualsComparison(game.mainTopic, 2)
        val turnInstruction = TurnInstruction()
        turnInstruction.parameter = RobotRotation.COUNTERCLOCKWISE
        instruction.addToList(turnInstruction)
        instruction.addToList(MoveInstruction())
        robotPlayer1.attachInstruction(instruction)

        game.attachEventListener { won = true }

        robotPlayer1.runInstructions()

        assertEquals(won, false)
    }
}