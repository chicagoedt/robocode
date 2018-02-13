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
        assertEquals(game.currentLevel.properties.players[0].name, "Surus")
        assertEquals(game.currentLevel.properties.players[0].x, 0)
        assertEquals(game.currentLevel.properties.players[0].y, 0)

        assertEquals(game.currentLevel.properties.players[1].name, "Hushpuppy")
        assertEquals(game.currentLevel.properties.players[1].x, 0)
        assertEquals(game.currentLevel.properties.players[1].y, 1)

        game.nextLevel()

        assertEquals(game.currentLevel.properties.players[0].name, "Hushpuppy")
        assertEquals(game.currentLevel.properties.players[0].x, 0)
        assertEquals(game.currentLevel.properties.players[0].y, 1)
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
    fun RobotInstructionTurn() {
        val instruction = Instruction(INSTRUCTION_TURN)
        game.attachInstruction("Surus", instruction)

    }
}