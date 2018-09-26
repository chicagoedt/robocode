import org.openqa.selenium.*
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

lateinit var driver : WebDriver

fun getBlockFromDrawer(className : String) : WebElement{
    val drawer = driver.findElement(By.id("drawer"))
    val block = drawer.findElement(By.className(className))
    return block
}

fun getPanelWithNumber(number : Int) : WebElement{
    val panels = driver.findElements(By.className("panel"))
    val panel = panels[number]

    return panel
}

fun dragActionBlockToElement(block : WebElement, element : WebElement) : WebElement {
    var blocksInPanel = element.findElements(By.className("actionBlock"))
    blocksInPanel = getOnlyDirectChildren(blocksInPanel, element)

    blocksInPanel.remove(block)

    return dragActionBlockToElement(block, element, blocksInPanel.size)
}

fun dragActionBlockToElement(block : WebElement, element : WebElement, position : Int) : WebElement {
    var blocksInPanel = element.findElements(By.className("actionBlock"))
    blocksInPanel = getOnlyDirectChildren(blocksInPanel, element)

    blocksInPanel.remove(block)

    var insertPosition = position
    if (position > blocksInPanel.size) insertPosition = blocksInPanel.size

    var elementToDropTo = element
    if (element.getAttribute("class").contains("actionBlockMacro"))
        elementToDropTo = element.findElement(By.className("macroHeader"))
    else if (element.getAttribute("class").contains("panel"))
        elementToDropTo = element.findElement(By.className("panelHeader"))

    if (insertPosition == blocksInPanel.size && insertPosition != 0) elementToDropTo = blocksInPanel[blocksInPanel.size - 1]
    else if (insertPosition > 0) elementToDropTo = blocksInPanel[insertPosition - 1]

    if (elementToDropTo.getAttribute("class").contains("actionBlockMacro")){
        elementToDropTo = elementToDropTo.findElement(By.className("macroFooter"))
    }

    val actions = Actions(driver)
    actions.dragAndDrop(block, elementToDropTo).build().perform()

    return block
}

/**
 * Drags a block to an element without calculating position or headers
 */
fun dragBlockToElement(block : WebElement, element : WebElement) : WebElement {
    val actions = Actions(driver)
    actions.dragAndDrop(block, element).build().perform()

    return block
}

/**
 * @return the sensor configurator webelement
 */
fun toggleSensorConfigForRobot(robotNum: Int) : WebElement{
    val grid = driver.findElement(By.id("grid"))
    val players = grid.findElements(By.className("gridPlayer"))
    val player = players[0]

    player.click()

    val sensorConfigs = driver.findElements(By.className("sensorConfigurator"))
    return sensorConfigs[robotNum]
}

fun getSensorFromDrawerAtPosition(robotNum: Int, position: Int) : WebElement{
    val sensorConfigs = driver.findElements(By.className("sensorConfigurator"))
    val sensorConfig = sensorConfigs[robotNum]
    val drawer = sensorConfig.findElement(By.className("sensorDrawer"))
    val sensorBlocks = drawer.findElements(By.className("sensorBlock"))
    val sensorBlock = sensorBlocks[position]

    return sensorBlock
}

fun getSensorPanelAtDirection(robotNum: Int, direction : String) : WebElement{
    val sensorConfigs = driver.findElements(By.className("sensorConfigurator"))
    val sensorConfig = sensorConfigs[0]
    var panel : WebElement? = null
    if (direction.equals("front", ignoreCase = true)){
        panel = sensorConfig.findElement(By.className("frontSensorList"))
    }
    else if (direction.equals("back", ignoreCase = true)){
        panel = sensorConfig.findElement(By.className("backSensorList"))
    }
    else if (direction.equals("left", ignoreCase = true)){
        panel = sensorConfig.findElement(By.className("leftSensorList"))
    }
    else if (direction.equals("right", ignoreCase = true)){
        panel = sensorConfig.findElement(By.className("rightSensorList"))
    }

    return panel!!
}

/**
 * @return The sensor element in the panel
 */
fun dragSensorToSensorPanel(robotNum: Int, drawerPosition: Int, panelDirection: String) : WebElement{
    val sensorBlock = getSensorFromDrawerAtPosition(0, drawerPosition)
    val sensorPanel = getSensorPanelAtDirection(0, panelDirection)
    dragBlockToElement(sensorBlock, sensorPanel)

    val sensorsInPanel = sensorPanel.findElements(By.className("sensorBlock"))
    return sensorsInPanel.last()
}

/**
 * @return The sensor element in the block
 */
fun dragSensorToActionBlock(sensor: WebElement, actionBlock : WebElement) : WebElement{
    val sensorDropField = actionBlock.findElement(By.className("actionSensorDrop"))
    dragBlockToElement(sensor, sensorDropField)
    val sensorInBlock = sensorDropField.findElement(By.className("sensorBlock"))
    return sensorInBlock
}

/**
 * @return The action block dragged to the panel
 */
fun dragSensorAndReadSensorBlock(robotNum: Int, drawerPosition: Int, sensorPosition: String) : WebElement{
    toggleSensorConfigForRobot(robotNum)
    val sensorBlock = dragSensorToSensorPanel(robotNum, drawerPosition, sensorPosition)
    val panel = getPanelWithNumber(robotNum)
    val readSensorBlock = dragActionBlockToElement(getBlockFromDrawer("readSensorActionBlock"), panel)
    dragSensorToActionBlock(sensorBlock, readSensorBlock)
    val sensorConfig = toggleSensorConfigForRobot(robotNum)

    WebDriverWait(driver, 30).until(ExpectedConditions.attributeToBe(sensorConfig, "display", "none"))

    return readSensorBlock
}

fun setNumberParameterOnBlock(block : WebElement, number : Int){
    val parameter = block.findElement(By.className("actionBlockNumberInput"))
    parameter.sendKeys(Keys.CONTROL, "a")
    parameter.sendKeys(Keys.BACK_SPACE)
    parameter.sendKeys(number.toString())
}

fun runProcedure(panelNum : Int){
    val panel = getPanelWithNumber(panelNum)

    val runButton = panel.findElement(By.className("panelHeaderButton"))
    runButton.click()

    WebDriverWait(driver, 30).until(ExpectedConditions.or(
            ExpectedConditions.elementToBeClickable(runButton),
            ExpectedConditions.numberOfElementsToBeMoreThan(By.className("popupButton"), 0)
    ))
}

fun runProcedureAndCheckTopic(panelNum: Int, topicVals : Array<Int>){
    val panel = getPanelWithNumber(panelNum)

    val runButton = panel.findElement(By.className("panelHeaderButton"))
    runButton.click()

    val topic = driver.findElement(By.id("topicValue"))

    val wait = FluentWait<WebDriver>(driver)
            .withTimeout(Duration.ofMillis(500))
            .pollingEvery(Duration.ofMillis(50))

    for (value in topicVals){
        wait.until {
            wait.withMessage("Topic with value ${value}")
            topic.text == value.toString()
        }
    }

    WebDriverWait(driver, 30).until(ExpectedConditions.or(
            ExpectedConditions.elementToBeClickable(runButton),
            ExpectedConditions.numberOfElementsToBeMoreThan(By.className("popupButton"), 0)
    ))
}

fun assertRobotPosition(robotNum : Int, x : Int, y : Int, direction : String){
    val robot = driver.findElements(By.className("gridPlayer"))[robotNum]

    assertEquals(x, robot.getAttribute("previousGridX").toInt())
    assertEquals(y, robot.getAttribute("previousGridY").toInt())
    assertEquals(direction, robot.getAttribute("previousDirection"))
}

fun assertBlockAtPosition(panelNum : Int, position : Int, className : String){
    val panels = driver.findElements(By.className("panel"))
    val panel = panels[panelNum]

    var blocks = panel.findElements(By.className("actionBlock"))

    blocks = getOnlyDirectChildren(blocks, panel)

    assertEquals(true, blocks[position].getAttribute("class").contains(className))
}

fun assertBlockAtPosition(element : WebElement, position : Int, className : String){
    var blocks = element.findElements(By.className("actionBlock"))

    blocks = getOnlyDirectChildren(blocks, element)
    blocks.remove(element)

    assertEquals(true, blocks[position].getAttribute("class").contains(className))
}

fun assertSensorBlockAtPosition(element : WebElement, position : Int, sensorBlock : WebElement){
    var blocks = element.findElements(By.className("actionBlock"))

    blocks = getOnlyDirectChildren(blocks, element)
    blocks.remove(element)

    val actualSensorBlock = blocks[position].findElement(By.className("sensorBlock"))

    assertEquals(sensorBlock, actualSensorBlock)
}

fun assertSensorBlockAtActionBlock(actionBlock : WebElement, sensorBlock : WebElement){
    val actualSensorBlock = actionBlock.findElement(By.className("sensorBlock"))

    assertTrue(actualSensorBlock.equals(sensorBlock))
}

fun getOnlyDirectChildren(children : List<WebElement>, parent : WebElement) : ArrayList<WebElement>{
    val directChildren : ArrayList<WebElement> = arrayListOf()

    for (child in children){
        if (child.findElement(By.xpath("..")).equals(parent)){
            directChildren.add(child)
        }
    }

    return directChildren
}