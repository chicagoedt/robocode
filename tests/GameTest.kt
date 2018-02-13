import kotlin.test.*
import org.chicagoedt.rosette.*


class BackendTests {
    private lateinit var game : Game

    @BeforeTest
    fun SetUp(){
        game = Game(getLevels(), getRobots())
    }

    @Test
    fun GameRobots() {
        assertEquals(game.currentLevel.players["Surus"]!!.name, "Surus")
        assertEquals(game.currentLevel.players["Surus"]!!.x, 0)
        assertEquals(game.currentLevel.players["Surus"]!!.y, 0)

        assertEquals(game.currentLevel.players["Hushpuppy"]!!.name, "Hushpuppy")
        assertEquals(game.currentLevel.players["Hushpuppy"]!!.x, 0)
        assertEquals(game.currentLevel.players["Hushpuppy"]!!.y, 1)

        game.nextLevel()

        assertEquals(game.currentLevel.players["Hushpuppy"]!!.name, "Hushpuppy")
        assertEquals(game.currentLevel.players["Hushpuppy"]!!.x, 0)
        assertEquals(game.currentLevel.players["Hushpuppy"]!!.y, 1)
    }

    @Test
    fun GameLevels() {
        assertEquals(game.currentLevel.properties.name, "Level 1")

        game.nextLevel()

        assertEquals(game.currentLevel.properties.name, "Level 2")
    }

    @Test
    fun RobotInstructionAttach() {
        val instruction1 = Instruction(INSTRUCTION_MOVE)
        val instruction2 = Instruction(INSTRUCTION_TURN)
        game.attachInstruction("Surus", instruction1)
        game.attachInstruction("Surus", instruction2)
        val list = game.getInstructionsFor("Surus")
        assertEquals(list[0].type, INSTRUCTION_MOVE)
        assertEquals(list[1].type, INSTRUCTION_TURN)
    }
    fun RobotInstructionMove() {
        val instruction = Instruction(INSTRUCTION_MOVE)
        game.attachInstruction("Surus", instruction)
        game.runInstructionsFor("Surus")

    }
}