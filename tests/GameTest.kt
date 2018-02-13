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
        val instruction1 = Instruction(INSTRUCTION_MOVE)
        val instruction2 = Instruction(INSTRUCTION_TURN)
        game.attachInstruction("Surus", instruction1)
        game.attachInstruction("Surus", instruction2)
        val list = game.getInstructions("Surus")
        assertEquals(list[0].type, INSTRUCTION_MOVE)
        assertEquals(list[1].type, INSTRUCTION_TURN)
    }

    @Test
    fun RobotInstructionTurn() {
        val instruction = Instruction(INSTRUCTION_TURN)
        game.attachInstruction("Surus", instruction)

        game.runInstructionsFor("Surus")
        assertEquals(game.currentLevel.players["Surus"]!!.direction, DIRECTION_RIGHT)

        game.runInstructionsFor("Surus")
        assertEquals(game.currentLevel.players["Surus"]!!.direction, DIRECTION_DOWN)

        game.runInstructionsFor("Surus")
        assertEquals(game.currentLevel.players["Surus"]!!.direction, DIRECTION_LEFT)

        game.runInstructionsFor("Surus")
        assertEquals(game.currentLevel.players["Surus"]!!.direction, DIRECTION_UP)
    }

    fun turn(name: String){
        var turn = Instruction(INSTRUCTION_TURN)
        game.attachInstruction(name, turn)
        game.runInstructionsFor(name)
        game.removeInstruction(name, turn)
    }

//    @Test
//    fun RobotInstructionMove(){
//        move("Surus", DIRECTION_UP)
//        turn("Surus")
//        move("Surus", DIRECTION_LEFT)
//        turn("Surus")
//        move("Surus", DIRECTION_DOWN)
//        turn("Surus")
//        move("Surus", DIRECTION_RIGHT)
//    }


    fun move(name: String, direction: Int) {
        var instruction = Instruction(INSTRUCTION_MOVE)
        val y = game.currentLevel.players[name]!!.y
        val x = game.currentLevel.players[name]!!.x
        game.attachInstruction(name, instruction)
        game.runInstructionsFor(name)

        if (direction == DIRECTION_UP) {
            assertEquals(game.currentLevel.players[name]!!.y, y + 1)
            assertEquals(game.currentLevel.players[name]!!.x, x)
        }
        else if (direction == DIRECTION_DOWN) {
            assertEquals(game.currentLevel.players[name]!!.y, y - 1)
            assertEquals(game.currentLevel.players[name]!!.x, x)
        }
        else if (direction == DIRECTION_LEFT) {
            assertEquals(game.currentLevel.players[name]!!.y, y)
            assertEquals(game.currentLevel.players[name]!!.x, x - 1)
        }
//        else if (direction == DIRECTION_RIGHT) {
//            assertEquals(game.currentLevel.players[name]!!.y, y)
//            assertEquals(game.currentLevel.players[name]!!.x, x + 1)
//        }
        game.removeInstruction(name, instruction)
    }
}