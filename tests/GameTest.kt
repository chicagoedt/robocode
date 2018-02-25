import kotlin.test.*
import org.chicagoedt.rosette.*


class BackendTests {
    private lateinit var game : Game
    private val levels = getLevels()
    private val robots = getRobots()
    private val levelOrder = getLevelOrder()

    @BeforeTest
    fun SetUp(){
        game = Game(levels, robots, levelOrder)
    }

    @Test
    fun GameRobots() {
        for(levelName : String in levelOrder){
            for(robotName : String in levels[levelName]!!.playerOrder){
                val robot = levels[levelName]!!.players[robotName]!!

                assertEquals(game.currentLevel.players[robot.name]!!.name, robot.name)
                assertEquals(game.currentLevel.players[robot.name]!!.x, robot.x)
                assertEquals(game.currentLevel.players[robot.name]!!.y, robot.y)
            }
        }
    }

    @Test
    fun GameLevels() {
        for(levelName : String in levelOrder){
            assertEquals(game.currentLevel, levels[levelName])
            game.nextLevel()
        }
    }

    @Test
    fun GameGrid() {
        for(levelName : String in levelOrder){
            assertEquals(game.currentLevel.properties.width, levels[levelName]!!.properties.width)
            assertEquals(game.currentLevel.properties.height, levels[levelName]!!.properties.height)
            game.nextLevel()
        }
    }

    @Test
    fun RobotInstructionAttach() {
        for(levelName : String in levelOrder){
            for(robotName : String in levels[levelName]!!.playerOrder){
                for (inst: Pair<Int, String> in availableInstructions){
                    val instruction = Instruction(inst.first)
                    game.attachInstruction(robotName, instruction)
                }
                val list = game.getInstructions(robotName)
                for (inst: Pair<Int, String> in availableInstructions){
                    assertEquals(list[0].type, INSTRUCTION_MOVE)
                }
            }
        }
    }

    @Test
    fun RobotInstructionTurn() {
        val instruction = Instruction(INSTRUCTION_TURN)
        for(levelName : String in levelOrder){
            for(robotName : String in levels[levelName]!!.playerOrder){
                for (i in 0..3) {
                    turn(robotName, RobotRotation.CLOCKWISE, true)
                }
            }
        }
    }

    private fun turn(name: String, rotation: RobotRotation, assert: Boolean){
        val next = nextDirection(game.currentLevel.players[name]!!.direction, rotation)
        val turn = Instruction(INSTRUCTION_TURN)
        turn.parameter = rotation
        game.attachInstruction(name, turn)
        game.runInstructionsFor(name)
        game.removeInstruction(name, turn)
        if (assert) assertEquals(game.currentLevel.players[name]!!.direction, next)
    }

    private fun move(name: String, orientation: RobotOrientation, parameter: Int, assert: Boolean) {
        val instruction = Instruction(INSTRUCTION_MOVE)
        instruction.parameter = parameter
        val y = game.currentLevel.players[name]!!.y
        val x = game.currentLevel.players[name]!!.x
        game.attachInstruction(name, instruction)
        game.runInstructionsFor(name)

        val difference : Int
        if (orientation == RobotOrientation.DIRECTION_UP) {
            if (y + parameter >= game.currentLevel.properties.height) difference = game.currentLevel.properties.height - y - 1
            else difference = parameter
            if (assert)assertEquals(game.currentLevel.players[name]!!.y, y + difference)
            if (assert)assertEquals(game.currentLevel.players[name]!!.x, x)
        }
        else if (orientation == RobotOrientation.DIRECTION_DOWN) {
            if (y - parameter < 0) difference =  y
            else difference = parameter
            if (assert)assertEquals(game.currentLevel.players[name]!!.y, y - difference)
            if (assert)assertEquals(game.currentLevel.players[name]!!.x, x)
        }
        else if (orientation == RobotOrientation.DIRECTION_LEFT) {
            if (x - parameter < 0) difference =  x
            else difference = parameter
            if (assert)assertEquals(game.currentLevel.players[name]!!.y, y)
            if (assert)assertEquals(game.currentLevel.players[name]!!.x, x - difference)
        }
        else if (orientation == RobotOrientation.DIRECTION_RIGHT) {
            if (x + parameter >= game.currentLevel.properties.width) difference = game.currentLevel.properties.width - x - 1
            else difference = parameter
            if (assert)assertEquals(game.currentLevel.players[name]!!.y, y)
            if (assert)assertEquals(game.currentLevel.players[name]!!.x, x + difference)
        }
        game.removeInstruction(name, instruction)
    }

    @Test
    fun RobotInstructionMove(){
        for(levelName : String in levelOrder){
            for(robotName : String in levels[levelName]!!.playerOrder){
                move(robotName, game.currentLevel.players[robotName]!!.direction, 2, true)
                turn(robotName, RobotRotation.CLOCKWISE, false)
                move(robotName, game.currentLevel.players[robotName]!!.direction, 4, true)
                turn(robotName, RobotRotation.CLOCKWISE, false)
                move(robotName, game.currentLevel.players[robotName]!!.direction, 5, true)
                turn(robotName, RobotRotation.CLOCKWISE, false)
                move(robotName, game.currentLevel.players[robotName]!!.direction, 3, true)
            }
        }
    }
}