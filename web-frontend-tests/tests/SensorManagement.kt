import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.*
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.test.assertEquals

class SensorManagement{
    companion object {
        @BeforeClass
        @JvmStatic
        fun setupDriver(){
            WebDriverManager.chromedriver().setup()
            driver = ChromeDriver()

            driver.navigate().to("localhost:8080")
        }

        @AfterClass
        @JvmStatic
        fun quitDriver(){
            driver.quit()
        }
    }

    @Before
    fun startGame(){
        WebDriverWait(driver, 30).until(
                ExpectedConditions.numberOfElementsToBeMoreThan(By.id("play"), 0))

        driver.findElement(By.id("play")).click()

        WebDriverWait(driver, 30).until(
                ExpectedConditions.numberOfElementsToBeMoreThan(By.id("drawer"), 0))
    }

    @After
    fun backToTitle(){
        driver.navigate().to("localhost:8080")

        WebDriverWait(driver, 30).until(
                ExpectedConditions.numberOfElementsToBeMoreThan(By.id("play"), 0))
    }

    @Test
    fun startAt0(){
        val moveBlock = getBlockFromDrawer("moveActionBlock")
        dragActionBlockToElement(moveBlock, getPanelWithNumber(0))
        runProcedureAndCheckTopic(0, arrayOf(0))
    }

    @Test
    fun addSensorToBlock(){
        dragSensorAndReadSensorBlock(0, 0, "front")
        runProcedureAndCheckTopic(0, arrayOf(1))
    }

    @Test
    fun startAt0AndReadFromSensor(){
        val moveBlock = getBlockFromDrawer("turnActionBlock")
        dragActionBlockToElement(moveBlock, getPanelWithNumber(0))

        dragSensorAndReadSensorBlock(0, 0, "front")

        runProcedureAndCheckTopic(0, arrayOf(0,3))
    }

    @Test
    fun topicResetAfterRun(){
        val turnBlock = getBlockFromDrawer("turnActionBlock")
        dragActionBlockToElement(turnBlock, getPanelWithNumber(0))

        dragSensorAndReadSensorBlock(0, 0, "front")

        runProcedureAndCheckTopic(0, arrayOf(0,3))

        assertEquals("0", driver.findElement(By.id("topicValue")).text)
    }

    @Test
    fun allSidesAfterTurn(){
        dragSensorAndReadSensorBlock(0, 0, "front")
        dragSensorAndReadSensorBlock(0, 0, "right")
        dragSensorAndReadSensorBlock(0, 0, "left")
        dragSensorAndReadSensorBlock(0, 0, "back")

        val sensorFront = getSensorFromSensorPanelAtDirection(0, "front", 0)
        val sensorRight = getSensorFromSensorPanelAtDirection(0, "right", 0)
        val sensorLeft = getSensorFromSensorPanelAtDirection(0, "left", 0)
        val sensorBack = getSensorFromSensorPanelAtDirection(0, "back", 0)

        val turnBlock = getBlockFromDrawer("turnActionBlock")
        dragActionBlockToElement(turnBlock, getPanelWithNumber(0))

        dragSensorAndReadSensorBlock(0, sensorFront)
        dragSensorAndReadSensorBlock(0, sensorRight)
        dragSensorAndReadSensorBlock(0, sensorLeft)
        dragSensorAndReadSensorBlock(0, sensorBack)


        runProcedureAndCheckTopic(0, arrayOf(1,3,0,2,3,2,1,0))
    }

    @Test
    fun moveSensorBetweenActionBlocks(){
        val firstBlock = dragSensorAndReadSensorBlock(0, 0, "front")
        val frontSensorBlock = getSensorFromActionBlock(firstBlock)

        dragSensorAndReadSensorBlock(0, frontSensorBlock)

        toggleSensorConfigForRobot(0)
        val rightSensorBlock = dragSensorToSensorPanel(0, 0, "right")
        dragSensorToActionBlock(rightSensorBlock, firstBlock)
        toggleSensorConfigForRobot(0)

        runProcedureAndCheckTopic(0, arrayOf(3, 1))
    }

    @Test
    fun moveSensorBetweenActionBlocksWithoutReplacement(){
        val firstBlock = dragSensorAndReadSensorBlock(0, 0, "front")
        val frontSensorBlock = getSensorFromActionBlock(firstBlock)

        dragSensorAndReadSensorBlock(0, frontSensorBlock)

        runProcedureAndCheckTopic(0, arrayOf(0, 1))
    }
}