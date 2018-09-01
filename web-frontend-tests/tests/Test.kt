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
import kotlin.test.assertEquals

private lateinit var driver : WebDriver

class Test {
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
        driver.findElement(By.id("play")).click()

        assertEquals("Robocode", driver.title)

    }

    fun dragBlockFromDrawerToPanel(blockClass : String, panelNumber : Int) : WebElement{
        val drawer = driver.findElement(By.id("drawer"))
        val block = drawer.findElement(By.className(blockClass))

        val panels = driver.findElements(By.className("panel"))
        val firstPanel = panels[panelNumber]

        val actions = Actions(driver)
        actions.dragAndDrop(block, firstPanel).build().perform()

        return block
    }

    fun setNumberParameterOnBlock(block : WebElement, number : Int){
        val parameter = block.findElement(By.className("actionBlockNumberInput"))
        parameter.sendKeys(Keys.CONTROL, "a")
        parameter.sendKeys(Keys.BACK_SPACE)
        parameter.sendKeys(number.toString())
    }

    @Test
    fun winLevel(){
        dragBlockFromDrawerToPanel("turnActionBlock", 0)

        val moveBlock = dragBlockFromDrawerToPanel("moveActionBlock", 0)
        setNumberParameterOnBlock(moveBlock, 3)

        dragBlockFromDrawerToPanel("turnActionBlock", 0)

        val moveBlock2 = dragBlockFromDrawerToPanel("moveActionBlock", 0)
        setNumberParameterOnBlock(moveBlock2, 2)

        dragBlockFromDrawerToPanel("turnActionBlock", 0)

        val moveBlock3 = dragBlockFromDrawerToPanel("moveActionBlock", 0)
        setNumberParameterOnBlock(moveBlock3, 2)



        val runButton = driver.findElement(By.className("panelHeaderButton"))
        runButton.click()

        WebDriverWait(driver, 30).until(ExpectedConditions.or(
                ExpectedConditions.elementToBeClickable(runButton),
                ExpectedConditions.numberOfElementsToBeMoreThan(By.className("popupButton"), 0)
        ))

        if (driver.findElements(By.className("popupButton")).size > 0){
            driver.findElements(By.className("popupButton"))[0].click()
        }
    }
}