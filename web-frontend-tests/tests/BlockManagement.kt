import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.*
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.interactions.Action
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.math.roundToInt
import kotlin.test.assertEquals

class BlockManagement {
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
    fun moveForward(){
        addProcedure(arrayOf(
                Pair("moveActionBlock", {it -> setNumberParameterOnBlock(it, 1)})
        ), 0)

        runProcedure(0)

        assertRobotPosition(0, 0, 0, "left")
    }

    @Test
    fun runButtonsDisabledDuringRun(){
        addProcedure(arrayOf(
                Pair("moveActionBlock", {it -> setNumberParameterOnBlock(it, 1)}),
                Pair("moveActionBlock", {it -> setNumberParameterOnBlock(it, 1)}),
                Pair("moveActionBlock", {it -> setNumberParameterOnBlock(it, 1)}),
                Pair("moveActionBlock", {it -> setNumberParameterOnBlock(it, 1)})
        ), 0)

        val panels = driver.findElements(By.className("panel"))
        val firstPanel = panels[0]
        val firstRunButton = firstPanel.findElement(By.className("panelHeaderButton"))
        firstRunButton.click()

        for (panel in panels){
            val runButton = panel.findElement(By.className("panelHeaderButton"))

            assertEquals(false, runButton.isEnabled)
        }

        WebDriverWait(driver, 30).until(
                ExpectedConditions.elementToBeClickable(firstRunButton))

        for (panel in panels){
            val runButton = panel.findElement(By.className("panelHeaderButton"))

            assertEquals(true, runButton.isEnabled)
        }
    }

    @Test
    fun runButtonsDisabledDuringRunWithNoBlocks(){
        val panels = driver.findElements(By.className("panel"))
        val firstPanel = panels[0]
        val firstRunButton = firstPanel.findElement(By.className("panelHeaderButton"))
        firstRunButton.click()

        WebDriverWait(driver, 30).until(
                ExpectedConditions.elementToBeClickable(firstRunButton))

        for (panel in panels){
            val runButton = panel.findElement(By.className("panelHeaderButton"))

            assertEquals(true, runButton.isEnabled)
        }
    }

    @Test
    fun insertBlockAtPosition0WithBlocks(){
        addProcedure(arrayOf(
                Pair("moveActionBlock", {it -> setNumberParameterOnBlock(it, 1)}),
                Pair("turnActionBlock", {it -> })
        ), 0)

        dragBlockFromDrawerToPanelToPosition("turnActionBlock", 0, 1)

        runProcedure(0)

        assertRobotPosition(0, 1, 1, "right")
    }

    @Test
    fun insertBlockAtLastPositionWithBlocks(){
        addProcedure(arrayOf(
                Pair("turnActionBlock", {it -> }),
                Pair("moveActionBlock", {it -> setNumberParameterOnBlock(it, 1)})
        ), 0)

        dragBlockFromDrawerToPanelToPosition("turnActionBlock", 0, 3)

        runProcedure(0)

        assertRobotPosition(0, 1, 1, "right")
    }

    @Test
    fun insertBlockAtPosition0WithoutBlocks(){
        dragBlockFromDrawerToPanelToPosition("turnActionBlock", 0, 1)

        runProcedure(0)

        assertRobotPosition(0, 1, 0, "up")
    }

    @Test
    fun insertBlockInPanelWithoutBlocks(){
        dragBlockFromDrawerToPanel("turnActionBlock", 0)

        runProcedure(0)

        assertRobotPosition(0, 1, 0, "up")
    }

    @Test
    fun insertBlockInPanelWithBlocks(){
        addProcedure(arrayOf(
                Pair("turnActionBlock", {it -> }),
                Pair("moveActionBlock", {it -> setNumberParameterOnBlock(it, 1)})
        ), 0)

        dragBlockFromDrawerToPanel("turnActionBlock", 0)

        runProcedure(0)

        assertRobotPosition(0, 1, 1, "right")
    }
}