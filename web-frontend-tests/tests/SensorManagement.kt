import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.*
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

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
    fun addSensorToBlock(){
        dragSensorAndReadSensorBlock(0, 0, "front")
        dragSensorAndReadSensorBlock(0, 0, "right")

        runProcedureAndCheckTopic(0, arrayOf(1,3))
    }
}