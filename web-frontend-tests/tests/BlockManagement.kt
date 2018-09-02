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
                Pair("turnActionBlock", {_ -> })
        ), 0)

        dragBlockFromDrawerToPanelToPosition("turnActionBlock", 0, 1)

        runProcedure(0)

        assertRobotPosition(0, 1, 1, "right")
    }

    @Test
    fun insertBlockAtLastPositionWithBlocks(){
        addProcedure(arrayOf(
                Pair("turnActionBlock", {_ -> }),
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
                Pair("turnActionBlock", {_ -> }),
                Pair("moveActionBlock", {it -> setNumberParameterOnBlock(it, 1)})
        ), 0)

        dragBlockFromDrawerToPanel("turnActionBlock", 0)

        runProcedure(0)

        assertRobotPosition(0, 1, 1, "right")
    }

    @Test
    fun insertBlockAfterMacroFromFooter(){
        var forLoopBlock : WebElement? = null
        addProcedure(arrayOf(
                Pair("forLoopActionBlock", {it ->
                    forLoopBlock = it
                    setNumberParameterOnBlock(it, 2)
                })
        ), 0)

        val footer = forLoopBlock!!.findElement(By.className("macroFooter"))

        val drawer = driver.findElement(By.id("drawer"))
        val block = drawer.findElement(By.className("turnActionBlock"))

        val actions = Actions(driver)
        actions.dragAndDrop(block, footer).build().perform()

        assertBlockAtPosition(0, 2, "turnActionBlock")

        runProcedure(0)

        assertRobotPosition(0, 1, 0, "up")
    }

    @Test
    fun insertBlockAfterMacroFromSide(){
        var forLoopBlock : WebElement? = null
        addProcedure(arrayOf(
                Pair("forLoopActionBlock", {it ->
                    forLoopBlock = it
                    setNumberParameterOnBlock(it, 2)
                })
        ), 0)

        val footer = forLoopBlock!!.findElement(By.className("macroSide"))

        val drawer = driver.findElement(By.id("drawer"))
        val block = drawer.findElement(By.className("turnActionBlock"))

        val actions = Actions(driver)
        actions.dragAndDrop(block, footer).build().perform()

        assertBlockAtPosition(0, 2, "turnActionBlock")

        runProcedure(0)

        assertRobotPosition(0, 1, 0, "up")
    }

    @Test
    fun insertBlockIntoEmptyMacro(){
        var forLoopBlock : WebElement? = null
        addProcedure(arrayOf(
                Pair("forLoopActionBlock", {it ->
                    forLoopBlock = it
                    setNumberParameterOnBlock(it, 2)
                })
        ), 0)

        dragBlockFromDrawerToElement("turnActionBlock", forLoopBlock!!)

        assertBlockAtPosition(0, 1, "forLoopActionBlock")

        runProcedure(0)

        assertRobotPosition(0, 1, 0, "right")
    }

    @Test
    fun insertBlockIntoMacroWithBlocks(){
        var forLoopBlock : WebElement? = null
        addProcedure(arrayOf(
                Pair("forLoopActionBlock", {it ->
                    forLoopBlock = it
                    setNumberParameterOnBlock(it, 2)
                })
        ), 0)

        addProcedureToElement(arrayOf(
                Pair("moveActionBlock", {_ ->})
        ), forLoopBlock!!)

        dragBlockFromDrawerToElement("turnActionBlock", forLoopBlock!!)

        assertBlockAtPosition(0, 1, "forLoopActionBlock")

        runProcedure(0)

        assertRobotPosition(0, 0, 1, "right")
    }

    @Test
    fun insertBlockIntoMacroWithBlocksAt0(){
        var forLoopBlock : WebElement? = null
        addProcedure(arrayOf(
                Pair("forLoopActionBlock", {it ->
                    forLoopBlock = it
                    setNumberParameterOnBlock(it, 2)
                })
        ), 0)

        addProcedureToElement(arrayOf(
                Pair("moveActionBlock", {it -> setNumberParameterOnBlock(it, 1)})
        ), forLoopBlock!!)

        dragBlockFromDrawerToElementToPosition("turnActionBlock", forLoopBlock!!, 1)

        assertBlockAtPosition(0, 1, "forLoopActionBlock")

        runProcedure(0)

        assertRobotPosition(0, 2, 1, "right")
    }
}