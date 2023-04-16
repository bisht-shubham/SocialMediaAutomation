package SocialMediaAutomation.TestComponents;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import SocialMediaAutomation.pageObjects.LandingPage;

public class BaseTest {
	public WebDriver driver;
	public LandingPage landingPage;
	Actions actions;
	File file;
	FileWriter writer;
	Clipboard clipboard;
	Transferable transferable;
	DataFormatter formatter = new DataFormatter();
	Properties prop = new Properties();

	public void loadProperties() throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")
				+ "\\src\\main\\java\\SocialMediaAutomation\\resources\\GlobalData.properties");
		prop.load(fis);
	}

	public void broswerType() {

		String browserName = System.getProperty("browser") == null ? prop.getProperty("browser")
				: System.getProperty("browser");
		switch (browserName.toLowerCase()) {
		case "chrome":
			ChromeOptions co = new ChromeOptions();
			co.addArguments("--remote-allow-origins=*");
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriver.path"));
			if (prop.getProperty("headless").contains("true"))
				co.addArguments("headless");
			driver = new ChromeDriver(co);
			driver.manage().window().setSize(new Dimension(1440, 900));
			break;
		case "firefox":
			System.setProperty("webdriver.gecko.driver", prop.getProperty("geckodriver.path"));
			driver = new FirefoxDriver();
			break;
		case "edge":
			EdgeOptions options = new EdgeOptions();
			options.setCapability("edgeFlags", "--remote-allow-origins=*");
			System.setProperty("webdriver.edge.driver", prop.getProperty("msedgedriver.path"));
			driver = new EdgeDriver(options);
			break;
		default:
			throw new IllegalArgumentException("Invalid browser name: " + browserName);
		}
        if (driver == null) {
            driver = new ChromeDriver();
        }
	}

	public WebDriver initilizeDriver() throws IOException {
		loadProperties();
		broswerType();
		driver.manage().timeouts()
				.pageLoadTimeout(Duration.ofSeconds(Integer.parseInt(prop.getProperty("pageLoadTimeout"))));
		driver.manage().window().maximize();
		driver.manage().timeouts()
				.implicitlyWait(Duration.ofSeconds(Integer.parseInt(prop.getProperty("implicitlyWait"))));
		actions = new Actions(driver);
		file = new File(System.getProperty("user.dir")
				+ "\\src\\test\\java\\SocialMediaAutomation\\TestData\\WriteFileText.txt");
		writer = new FileWriter(file);
		clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
		transferable = clipboard.getContents(null);
		return driver;
	}

	public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File file = new File(System.getProperty("user.dir") + "\\Reports\\" + testCaseName + ".png");
		FileUtils.copyFile(source, file);
		return System.getProperty("user.dir") + "\\Reports\\" + testCaseName + ".png";
	}
	

	private static final String FILE_PATH = System.getProperty("user.dir")
			+ "//src//test//java//SocialMediaAutomation//TestData//SocialMediaAutomationExcelSheetTestData.xlsx";
	private static final String SHEET_NAME = "SMATestdata";

	
	private int getColumnIndexByName(String columnName, XSSFSheet sheet) {
	    Row firstRow = sheet.getRow(0);
	    int column = -1;
	    for (Cell cell : firstRow) {
	        if (cell.getStringCellValue().equalsIgnoreCase(columnName)) {
	            column = cell.getColumnIndex();
	            break;
	        }
	    }
	    return column;
	}

	@DataProvider(name = "driveTest")
	public Object[] getDataFromExcel(Method m) throws IOException {
	    ArrayList<String> a = new ArrayList<String>();
	    try (FileInputStream fis = new FileInputStream(FILE_PATH); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
	        XSSFSheet sheet = workbook.getSheet(SHEET_NAME);
	        int column = getColumnIndexByName("TestCaseName", sheet);
	        if (column != -1) {
	            for (Row r : sheet) {
	                if (r.getCell(column).getStringCellValue().equalsIgnoreCase(m.getName())) {
	                    for (Cell c : r) {
	                        DataFormatter formatter = new DataFormatter();
	                        String cellValue = formatter.formatCellValue(c);
	                        a.add(cellValue);
	                    }
	                    break;
	                }
	            }
	        }
	    }
	    return new Object[] { a };
	}

	@BeforeMethod(alwaysRun = true)
	public LandingPage launchApplication() throws IOException {
		driver = initilizeDriver();
		landingPage = new LandingPage(driver);
		landingPage.goTo();
		return landingPage;
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		driver.close();
	}
}
