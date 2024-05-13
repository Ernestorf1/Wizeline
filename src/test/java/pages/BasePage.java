package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import io.github.bonigarcia.wdm.WebDriverManager;
 
public class BasePage {
    /*
     * Declaración de una variable estática 'driver' de tipo WebDriver
     * Esta variable va a ser compartida por todas las instancias de BasePage y sus subclases
     */
    protected static WebDriver driver;
    private Actions action; // Declaración de la variable action
    /*
     * Declaración de una variable de instancia 'wait' de tipo WebDriverWait.
     * Se inicializa inmediatamente con una instancia dew WebDriverWait utilizando el 'driver' estático
     * WebDriverWait se usa para poner esperas explícitas en los elementos web
     */
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    private static final Logger logger = LogManager.getLogger(BasePage.class);
 
    /* 
     * Configura el WebDriver para Chrome usando WebDriverManager.
     * WebDriverManager va a estar descargando y configurando automáticamente el driver del navegador
    */
    static {
        WebDriverManager.chromedriver().setup();
       
 
        //Inicializa la variable estática 'driver' con una instancia de ChromeDriver
        driver = new ChromeDriver();
    }
 
    /*
     * Este es el constructor de BasePage que acepta un objeto WebDriver como argumento.
     */
    public BasePage(WebDriver driver) {
        BasePage.driver = driver;
    }

    public static void navigateTo(String url) {
        // get() carga una pagina web nueva en la ventana del navegador actual
        try {
            driver.get(url);
        } catch (Exception e) {
            logger.error("Error while navigating to URL: " + e.getMessage());
        }
    }

    public static void closeBrowser() {
        try {
            driver.quit();
        } catch (Exception e) {
            logger.error("Error while closing browser: " + e.getMessage());
        }
    }

    // Metodo que devuelve un web element y Selenium puede trabajar con el, se va a
    // crear esta instancia del WebElement y
    // Navegador (con sus metodos), para después a traves de la herencia reutilizar
    // en tod o el proyecto.
    public WebElement Find(String locator, String selectorType) {
        WebElement element = null;
        try {
            switch (selectorType.toLowerCase()) {
                case "xpath":
                    element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
                    break;
                case "cssselector":
                    element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locator)));
                    break;
                case "id":
                    element = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locator)));
                    break;
                default:
                    logger.warn("Unsupported selector type: " + selectorType);
                    break;
            }
            
            // Agregar una comprobación adicional para manejar el caso en que el elemento no se encuentre
            if (element == null) {
                throw new NoSuchElementException("No se encontró ningún elemento con el selector: " + locator);
            }
        } catch (Exception e) {
            logger.error("Error while finding element: " + e.getMessage());
        }
        return element;
    }



    public void clickElement(String locator, String selectorType) {
        try {
            WebElement element = Find(locator, selectorType);
            element.click();
        } catch (Exception e) {
            logger.error("Error while clicking element: " + e.getMessage());
        }
    }

    public void write(String locator, String textToWrite, String selectorType) {
        try { // Limpiar el campo de texto
        Find(locator, selectorType).clear();        
        // Enviar el texto al campo de texto
        Find(locator, selectorType).sendKeys(textToWrite);
        }catch (Exception e) {
            logger.error("Error while writing to element: " + e.getMessage());
        }
    }

    public void selectFromDropdownByValue(String locator, String valueToSelect, String selectorType) {
        // Creamos el dropdown
        Select dropdown = new Select(Find(locator, selectorType));
        // Seleccionar por valor
        dropdown.selectByValue(valueToSelect);
    }

    public void selectFromDropdownByIndex(String locator, int valueToSelect, String selectorType) {
        // Creamos el dropdown
        Select dropdown = new Select(Find(locator, selectorType));
        // Seleccionar por index
        dropdown.selectByIndex(valueToSelect);
    }

    public void selectFromDropdownByText(String locator, String valueToSelect, String selectorType) {
        // Creamos el dropdown
        Select dropdown = new Select(Find(locator, selectorType));
        // Seleccionar por index
        dropdown.selectByVisibleText(valueToSelect);
    }

    public void verTypeFile() {
        String change_visibility = "$(\"#fileField\").css(\"visibility,\"visible\");";
        String change_display = "$(\"#fileField\").css(\"display, \"block\");";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(change_display);
        js.executeScript(change_visibility);

        /*
         * ALGUNOS COMANDOS QUE PUEDEN SERVIR EN LA BUSQUEDA DE MOSTRAR LOS WEB ELEMENTS
         *
         * ("#fileField").style.visibility="visible";
         * ("#fileField").style.display="block";
         * ("#fileField").style.width="200px";
         * ("#fileField").style.height="200px";
         * ("#fileField").style.position="fixed";
         * ("#fileField").style.overflow="visible";
         * ("#fileField").style.zIndex="999999";
         * ("#fileField").style.top="500px";
         * ("#fileField").style.bottom="500px";
         * ("#fileField").style.left="500px";
         * ("#fileField").style.right="500px";
         * ("#fileField").style.marginBottom="100px";
         */
    }

    public void hoverOverElement(String locator, String selectorType) {
        action.moveToElement(Find(locator, selectorType));
    }

    public void doubleClick(String locator, String selectorType) {
        action.doubleClick(Find(locator, selectorType));
    }

    public void rightClick(String locator, String selectorType) {
        action.contextClick(Find(locator, selectorType));
    }

    public String getValueFromTable(String locator, int row, int column,String selectorType) {
        String cellINeed = locator + "/table/tbody/tr[" + row + "]/td[" + column + "]";
        return Find(cellINeed,selectorType).getText();
    }

    public void switchToiFrame(int iFrameIndex) {
        driver.switchTo().frame(iFrameIndex);
    }

    public void switchToParentFrame() {
        driver.switchTo().parentFrame();
    }

    public void dismissAlert() {
        driver.switchTo().alert().dismiss();
    }

    public String textFromElement(String locator, String selectorType) {
        return Find(locator, selectorType).getText();
    }

    public boolean elementIsDisplayed(String locator, String selectorType) {
        // Devuelve un booleano si es mostrado o no
        return Find(locator, selectorType).isDisplayed();
    }

    public boolean elementIsSelected(String locator, String selectorType) {
        // Devuelve un booleano si es seleccionado o no
        return Find(locator, selectorType).isSelected();
    }

    public boolean elementIsEnabled(String locator, String selectorType) {
        // Devuelve un booleano si esta habilitado o no
        return Find(locator, selectorType).isEnabled();
    }

    public List<WebElement> bringMeAllElements(String locator) {
        return driver.findElements(By.className(locator));
    }
    public void maximizeWindow() {
        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            logger.error("Error while maximizing window: " + e.getMessage());
        }
    }

    public void executeJavaScript(String script) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(script);
        } catch (Exception e) {
            logger.error("Error while executing JavaScript: " + e.getMessage());
        }
    }

    public void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            logger.error("Error while accepting alert: " + e.getMessage());
        }
    }

    public void switchToWindowWithTitle(String title) {
        String currentWindowHandle = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();

        for (String windowHandle : windowHandles) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().equals(title)) {
                return;
            }
        }

        driver.switchTo().window(currentWindowHandle);
    }

    public void switchToFrame(String frameNameOrId) {
        driver.switchTo().frame(frameNameOrId);
    }

    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    public void waitForElementToBeVisible(String locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locator)));
    }
}
