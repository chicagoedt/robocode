import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.test.assertEquals

lateinit var driver : WebDriver

fun dragBlockFromDrawerToPanel(blockClass : String, panelNumber : Int) : WebElement {
    val drawer = driver.findElement(By.id("drawer"))
    val block = drawer.findElement(By.className(blockClass))

    val panels = driver.findElements(By.className("panel"))
    val panel = panels[panelNumber]

    val blocksInPanel = panel.findElements(By.className("actionBlock"))
    var elementToDropTo = panel
    if (blocksInPanel.size > 0) elementToDropTo = blocksInPanel[blocksInPanel.size - 1]

    val actions = Actions(driver)
    actions.dragAndDrop(block, elementToDropTo).build().perform()

    return block
}

fun dragBlockFromDrawerToPanelToPosition(blockClass : String, panelNumber : Int, position : Int) : WebElement {
    val drawer = driver.findElement(By.id("drawer"))
    val block = drawer.findElement(By.className(blockClass))

    val panels = driver.findElements(By.className("panel"))
    val panel = panels[panelNumber]

    val blocksInPanel = panel.findElements(By.className("actionBlock"))
    var elementToDropTo = panel.findElement(By.className("panelHeader"))
    if (position > 1) elementToDropTo = blocksInPanel[position - 2]

    val actions = Actions(driver)
    actions.dragAndDrop(block, elementToDropTo).build().perform()

    return block
}

fun setNumberParameterOnBlock(block : WebElement, number : Int){
    val parameter = block.findElement(By.className("actionBlockNumberInput"))
    parameter.sendKeys(Keys.CONTROL, "a")
    parameter.sendKeys(Keys.BACK_SPACE)
    parameter.sendKeys(number.toString())
}

fun addProcedure(list : Array<Pair<String, (WebElement) -> Unit>>, panelNum : Int){
    for (pair in list){
        val block = dragBlockFromDrawerToPanel(pair.first, panelNum)
        pair.second(block)
    }
}

fun runProcedure(panelNum : Int){
    val panels = driver.findElements(By.className("panel"))
    val panel = panels[panelNum]

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