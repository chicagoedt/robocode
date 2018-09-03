import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.*
import org.junit.Test
import org.junit.experimental.categories.Category
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
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
        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), getPanelWithNumber(0))

        runProcedure(0)

        assertRobotPosition(0, 0, 0, "left")
    }

    @Test
    fun runButtonsDisabledDuringRun(){
        val firstPanel = getPanelWithNumber(0)

        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), firstPanel)
        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), firstPanel)
        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), firstPanel)
        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), firstPanel)

        val panels = driver.findElements(By.className("panel"))
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

    //<editor-fold desc="Generic list tests">
    /**
     * Inserts a block into an empty procedure
     */
    fun insertBlockIntoEmptyList(element : WebElement, panelToRun : Int){
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)

        assertBlockAtPosition(element, 0, "turnActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 1, 0, "up")
    }

    /**
     * The same as insertBlockIntoEmptyList, except dropping the block into the list instead of into the header
     */
    fun insertBlockIntoEmptyListFromList(element : WebElement, panelToRun : Int){
        val moveBlock = getBlockFromDrawer("moveActionBlock")

        val actions = Actions(driver)
        actions.dragAndDrop(moveBlock, element).build().perform()

        assertBlockAtPosition(element, 0, "moveActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 0, 0, "left")
    }

    /**
     * Inserts a block at the end of a list containing one block
     */
    fun insertBlockAtEndOfOneBlockList(element : WebElement, panelToRun : Int){
        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), element)

        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element, 1)

        assertBlockAtPosition(element, 0, "moveActionBlock")
        assertBlockAtPosition(element, 1, "turnActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 0, 0, "up")
    }

    /**
     * Inserts a block at the end of a list containing multiple blocks
     */
    fun insertBlockAtEndOfMultiBlockList(element : WebElement, panelToRun: Int){
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), element)

        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element, 2)

        assertBlockAtPosition(element, 0, "turnActionBlock")
        assertBlockAtPosition(element, 1, "moveActionBlock")
        assertBlockAtPosition(element, 2, "turnActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 1, 1, "right")
    }

    /**
     * Inserts a block at the start of a list containing one block
     */
    fun insertBlockAtStartOfOneBlockList(element: WebElement, panelToRun: Int){
        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), element)

        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element, 0)

        assertBlockAtPosition(element, 0, "turnActionBlock")
        assertBlockAtPosition(element, 1, "moveActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 1, 1, "up")
    }

    /**
     * Inserts a block at the start of a list containing multiple blocks
     */
    fun insertBlockAtStartOfMultiBlockList(element: WebElement, panelToRun: Int){
        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), element)
        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), element)

        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element, 0)

        assertBlockAtPosition(element, 0, "turnActionBlock")
        assertBlockAtPosition(element, 1, "moveActionBlock")
        assertBlockAtPosition(element, 2, "moveActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 1, 2, "up")
    }

    /**
     * Inserts a block in the middle of a list containing multiple blocks
     */
    fun insertBlockInMiddleOfMultiBlockList(element: WebElement, panelToRun: Int){
        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), element)
        dragBlockToElement(getBlockFromDrawer("moveActionBlock"), element)

        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element, 1)

        assertBlockAtPosition(element, 0, "moveActionBlock")
        assertBlockAtPosition(element, 1, "turnActionBlock")
        assertBlockAtPosition(element, 2, "moveActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 0, 1, "up")
    }

    /**
     * Moves a block from the start of a list to the end of a list. The list contains only two blocks
     */
    fun moveBlockFromStartToEndWithTwoInList(element: WebElement, panelToRun: Int){
        val startBlock = getBlockFromDrawer("moveActionBlock")
        dragBlockToElement(startBlock, element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)

        dragBlockToElement(startBlock, element, 1)

        assertBlockAtPosition(element, 0, "turnActionBlock")
        assertBlockAtPosition(element, 1, "moveActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 1, 1, "up")
    }

    /**
     * Moves a block from the start of a list to the end of a list. The list contains more than two blocks
     */
    fun moveBlockFromStartToEndWithMultipleInList(element: WebElement, panelToRun: Int){
        val startBlock = getBlockFromDrawer("moveActionBlock")
        dragBlockToElement(startBlock, element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)

        dragBlockToElement(startBlock, element, 2)

        assertBlockAtPosition(element, 0, "turnActionBlock")
        assertBlockAtPosition(element, 1, "turnActionBlock")
        assertBlockAtPosition(element, 2, "moveActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 2, 0, "right")
    }

    /**
     * Moves a block from the end of a list to the start of a list. The list contains only two blocks
     */
    fun moveBlockFromEndToStartWithTwoInList(element: WebElement, panelToRun: Int){
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        val endBlock = getBlockFromDrawer("moveActionBlock")
        dragBlockToElement(endBlock, element)

        dragBlockToElement(endBlock, element, 0)

        assertBlockAtPosition(element, 0, "moveActionBlock")
        assertBlockAtPosition(element, 1, "turnActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 0, 0, "up")
    }

    /**
     * Moves a block from the end of a list to the start of a list. The list contains more than two blocks
     */
    fun moveBlockFromEndToStartWithMultipleInList(element: WebElement, panelToRun: Int){
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        val endBlock = getBlockFromDrawer("moveActionBlock")
        dragBlockToElement(endBlock, element)

        dragBlockToElement(endBlock, element, 0)

        assertBlockAtPosition(element, 0, "moveActionBlock")
        assertBlockAtPosition(element, 1, "turnActionBlock")
        assertBlockAtPosition(element, 2, "turnActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 0, 0, "right")
    }

    /**
     * Moves a block from the start of a list to the middle of a list. The list contains only three blocks
     */
    fun moveBlockFromStartToMiddleWithThreeInList(element: WebElement, panelToRun: Int){
        val startBlock = getBlockFromDrawer("moveActionBlock")
        dragBlockToElement(startBlock, element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)

        dragBlockToElement(startBlock, element, 1)

        assertBlockAtPosition(element, 0, "turnActionBlock")
        assertBlockAtPosition(element, 1, "moveActionBlock")
        assertBlockAtPosition(element, 2, "turnActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 1, 1, "right")
    }

    /**
     * Moves a block from the start of a list to the middle of a list. The list contains more than three blocks
     */
    fun moveBlockFromStartToMiddleWithMultipleInList(element: WebElement, panelToRun: Int){
        val startBlock = getBlockFromDrawer("moveActionBlock")
        dragBlockToElement(startBlock, element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)

        dragBlockToElement(startBlock, element, 1)

        assertBlockAtPosition(element, 0, "turnActionBlock")
        assertBlockAtPosition(element, 1, "moveActionBlock")
        assertBlockAtPosition(element, 2, "turnActionBlock")
        assertBlockAtPosition(element, 3, "turnActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 1, 1, "down")
    }

    /**
     * Moves a block from the end of a list to the middle of a list. The list contains only three blocks
     */
    fun moveBlockFromEndToMiddleWithThreeInList(element: WebElement, panelToRun: Int){
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        val endBlock = getBlockFromDrawer("moveActionBlock")
        dragBlockToElement(endBlock, element)

        dragBlockToElement(endBlock, element, 1)

        assertBlockAtPosition(element, 0, "turnActionBlock")
        assertBlockAtPosition(element, 1, "moveActionBlock")
        assertBlockAtPosition(element, 2, "turnActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 1, 1, "right")
    }

    /**
     * Moves a block from the end of a list to the middle of a list. The list contains more than three blocks
     */
    fun moveBlockFromEndToMiddleWithMultipleInList(element: WebElement, panelToRun: Int){
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        dragBlockToElement(getBlockFromDrawer("turnActionBlock"), element)
        val endBlock = getBlockFromDrawer("moveActionBlock")
        dragBlockToElement(endBlock, element)

        dragBlockToElement(endBlock, element, 1)

        assertBlockAtPosition(element, 0, "turnActionBlock")
        assertBlockAtPosition(element, 1, "moveActionBlock")
        assertBlockAtPosition(element, 2, "turnActionBlock")
        assertBlockAtPosition(element, 3, "turnActionBlock")

        runProcedure(panelToRun)

        assertRobotPosition(0, 1, 1, "down")
    }
    //</editor-fold>

    //<editor-fold desc="Panel tests">
    @Test
    fun insertBlockIntoEmptyPanel(){
        val panel = getPanelWithNumber(0)

        insertBlockIntoEmptyList(panel, 0)
    }

    @Test
    fun insertBlockIntoEmptyPanelFromPanel(){
        val panel = getPanelWithNumber(0)

        insertBlockIntoEmptyListFromList(panel, 0)
    }

    @Test
    fun insertBlockAtEndOfOneBlockPanel(){
        val panel = getPanelWithNumber(0)

        insertBlockAtEndOfOneBlockList(panel, 0)
    }

    @Test
    fun insertBlockAtEndOfMultiBlockPanel(){
        val panel = getPanelWithNumber(0)

        insertBlockAtEndOfMultiBlockList(panel, 0)
    }

    @Test
    fun insertBlockAtStartOfOneBlockPanel(){
        val panel = getPanelWithNumber(0)

        insertBlockAtStartOfOneBlockList(panel, 0)
    }

    @Test
    fun insertBlockAtStartOfMultiBlockPanel(){
        val panel = getPanelWithNumber(0)

        insertBlockAtStartOfMultiBlockList(panel, 0)
    }

    @Test
    fun insertBlockInMiddleOfMultiBlockPanel(){
        val panel = getPanelWithNumber(0)

        insertBlockInMiddleOfMultiBlockList(panel, 0)
    }

    @Test
    fun moveBlockFromStartToEndWithTwoInPanel(){
        val panel = getPanelWithNumber(0)

        moveBlockFromStartToEndWithTwoInList(panel, 0)
    }

    @Test
    fun moveBlockFromStartToEndWithMultipleInPanel(){
        val panel = getPanelWithNumber(0)

        moveBlockFromStartToEndWithMultipleInList(panel, 0)
    }

    @Test
    fun moveBlockFromEndToStartWithTwoInPanel(){
        val panel = getPanelWithNumber(0)

        moveBlockFromEndToStartWithTwoInList(panel, 0)
    }

    @Test
    fun moveBlockFromEndToStartWithMultipleInPanel(){
        val panel = getPanelWithNumber(0)

        moveBlockFromEndToStartWithMultipleInList(panel, 0)
    }

    @Test
    fun moveBlockFromStartToMiddleWithThreeInPanel(){
        val panel = getPanelWithNumber(0)

        moveBlockFromStartToMiddleWithThreeInList(panel, 0)
    }

    @Test
    fun moveBlockFromStartToMiddleWithMultipleInPanel(){
        val panel = getPanelWithNumber(0)

        moveBlockFromStartToMiddleWithMultipleInList(panel, 0)
    }

    @Test
    fun moveBlockFromEndToMiddleWithThreeInPanel(){
        val panel = getPanelWithNumber(0)

        moveBlockFromEndToMiddleWithThreeInList(panel, 0)
    }

    @Test
    fun moveBlockFromEndToMiddleWithMultipleInPanel(){
        val panel = getPanelWithNumber(0)

        moveBlockFromEndToMiddleWithMultipleInList(panel, 0)
    }
    //</editor-fold>

    //<editor-fold desc="Macro Tests">
    @Test
    fun insertBlockIntoEmptyMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        insertBlockIntoEmptyList(forLoopMacro, 0)
    }

    @Test
    fun insertBlockIntoEmptyMacroFromMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        insertBlockIntoEmptyListFromList(forLoopMacro, 0)
    }

    @Test
    fun insertBlockAtEndOfOneBlockMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        insertBlockAtEndOfOneBlockList(forLoopMacro, 0)
    }

    @Test
    fun insertBlockAtEndOfMultiBlockMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        insertBlockAtEndOfMultiBlockList(forLoopMacro, 0)
    }

    @Test
    fun insertBlockAtStartOfOneBlockMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        insertBlockAtStartOfOneBlockList(forLoopMacro, 0)
    }

    @Test
    fun insertBlockAtStartOfMultiBlockMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        insertBlockAtStartOfMultiBlockList(forLoopMacro, 0)
    }

    @Test
    fun insertBlockInMiddleOfMultiBlockMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        insertBlockInMiddleOfMultiBlockList(forLoopMacro, 0)
    }

    @Test
    fun moveBlockFromStartToEndWithTwoInMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        moveBlockFromStartToEndWithTwoInList(forLoopMacro, 0)
    }

    @Test
    fun moveBlockFromStartToEndWithMultipleInMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        moveBlockFromStartToEndWithMultipleInList(forLoopMacro, 0)
    }

    @Test
    fun moveBlockFromEndToStartWithTwoInMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        moveBlockFromEndToStartWithTwoInList(forLoopMacro, 0)
    }

    @Test
    fun moveBlockFromEndToStartWithMultipleInMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        moveBlockFromEndToStartWithMultipleInList(forLoopMacro, 0)
    }

    @Test
    fun moveBlockFromStartToMiddleWithThreeInMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        moveBlockFromStartToMiddleWithThreeInList(forLoopMacro, 0)
    }

    @Test
    fun moveBlockFromStartToMiddleWithMultipleInMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        moveBlockFromStartToMiddleWithMultipleInList(forLoopMacro, 0)
    }

    @Test
    fun moveBlockFromEndToMiddleWithThreeInMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        moveBlockFromEndToMiddleWithThreeInList(forLoopMacro, 0)
    }

    @Test
    fun moveBlockFromEndToMiddleWithMultipleInMacro(){
        val panel = getPanelWithNumber(0)
        val forLoopMacro = getBlockFromDrawer("forLoopActionBlock")
        dragBlockToElement(forLoopMacro, panel)

        moveBlockFromEndToMiddleWithMultipleInList(forLoopMacro, 0)
    }
    //</editor-fold>
}