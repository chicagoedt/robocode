import main.Game
import main.Level
import main.LevelProperties
import kotlin.test.*

class BackendTests {
    private lateinit var game : Game

    private lateinit var levelProp1 : LevelProperties
    private lateinit var levelProp2 : LevelProperties

    private lateinit var level1 : Level
    private lateinit var level2 : Level

    @BeforeTest
    fun SetUp(){
        game = Game()
        levelProp1 = LevelProperties(0)
        levelProp2 = LevelProperties(1)
        level1 = Level(levelProp1)
        level2 = Level(levelProp2)

        game.levels.add(level1)
        game.levels.add(level2)
    }

    @Test
    fun GameStart() {
        game.start()

        assertEquals(game.currentLevel, level1)
    }
}