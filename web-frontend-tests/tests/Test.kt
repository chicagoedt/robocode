import com.thoughtworks.selenium.Selenium
import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.*
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import kotlin.test.assertEquals

private lateinit var driver : WebDriver

class Test {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setupDriver(){
            WebDriverManager.chromedriver().setup()
            driver = ChromeDriver()

            driver.get("localhost:8080")
        }

        @AfterClass
        @JvmStatic
        fun quitDriver(){
            driver.quit()
        }
    }

    @Test
    fun startGame(){
        driver.findElement(By.id("play")).click()

        assertEquals("Robocode", driver.title)
    }
}