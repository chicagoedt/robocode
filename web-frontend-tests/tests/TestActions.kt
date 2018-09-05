import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.test.assertEquals

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

fun dragBlockToElement(block : WebElement, element : WebElement) : WebElement {
    var blocksInPanel = element.findElements(By.className("actionBlock"))
    blocksInPanel = getOnlyDirectChildren(blocksInPanel, element)

    blocksInPanel.remove(block)

    return dragBlockToElement(block, element, blocksInPanel.size)
}

fun dragBlockToElement(block : WebElement, element : WebElement, position : Int) : WebElement {
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
fun dragBlockToElementDirectly(block : WebElement, element : WebElement) : WebElement {
    val actions = Actions(driver)
    actions.dragAndDrop(block, element).build().perform()

    return block
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

fun getOnlyDirectChildren(children : List<WebElement>, parent : WebElement) : ArrayList<WebElement>{
    val directChildren : ArrayList<WebElement> = arrayListOf()

    for (child in children){
        if (child.findElement(By.xpath("..")).equals(parent)){
            directChildren.add(child)
        }
    }

    return directChildren
}