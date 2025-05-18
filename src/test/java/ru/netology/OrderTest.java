package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void formValidData() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван Иванович");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    // Тесты для второго задания невалидные данные
    @Test
    void formDataNotValidNameLatin() { //Поле Имя Латинские буквы
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivanov Ivan Ivanovich");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void formDataNotValidNameSymbol() { //Поле Имя символы
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivanov !@45 Ivanovich");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void formDataNotValidNameEmpty() { //Поле Имя пустое
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void formDataNotValidPhoneEmpty() { //Поле телефон пустое
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван Иванович");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void formDataNotValidPhoneSymbol() { //Поле телефон символы
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван иванович");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("GGGПППпа112");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void formDataNotValidPhone12Number() { //Поле телефон более 11 цифр
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван иванович");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+712345678901");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void formDataNotValidPhone9Number() { //Поле телефон менее 11 цифр
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван иванович");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7123456789");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void formDataNotClickCheckbox() { //Не установлена галочка согласия
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван Иванович");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']"));
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text.trim());
    }
}
