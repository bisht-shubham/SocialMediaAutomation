package SocialMediaAutomation.AbstractComponents;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class AbstractComponents {

	Actions actions;
	File file;
	FileWriter writer;
	Clipboard clipboard;
	Transferable transferable;

	WebDriver driver;

	public AbstractComponents(WebDriver driver) {
		this.driver = driver;
		this.actions = new Actions(driver); // Initialize the actions object
		PageFactory.initElements(driver, this);
	}

	@FindBy(name = "search_query")
	WebElement searchTextBox;

	@FindBy(id = "search-icon-legacy")
	WebElement searchButton;

	public void EnterSearchtext(String channelName) {
		searchTextBox.sendKeys(channelName);
	}

	public void ClickSearchButton() {
		waitForElementToBeClickable(searchButton);
		searchButton.click();
	}

	public String getCurrentPageTitle() {
		String currentTabTitle = driver.getTitle();
		return currentTabTitle;
	}

	public String getCurrentPageUrl() {
		String currentTabUrl = driver.getCurrentUrl();
		return currentTabUrl;
	}

	public void waitForElementToAppear(By findBy) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
	}

	public void waitForWebElementToAppear(WebElement findBy) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(ExpectedConditions.visibilityOf(findBy));
	}

	public void waitForElementToDisappear(WebElement ele) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(ExpectedConditions.invisibilityOf(ele));
	}

	public void waitForElementToBeClickable(WebElement ele) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(ExpectedConditions.elementToBeClickable(ele));
	}

	public String getElementText(WebElement element) {
		waitForWebElementToAppear(element);
		String extractedText = element.getText();
		return extractedText;
	}

	public void PerformActionSendKeysSpace() {
		actions.sendKeys(Keys.SPACE).build().perform();
	}

	public void PerformActionMoveToElement(WebElement volumeButtonArea) {
		actions.moveToElement(volumeButtonArea).perform();
	}

	public void PerformActionDragDrop(WebElement volumeSlider) {
		actions.dragAndDropBy(volumeSlider, -5, 0).build().perform();
	}

	public void SaveClipBoardTextInFile() throws UnsupportedFlavorException, IOException {
		if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			String clipboardContents = (String) transferable.getTransferData(DataFlavor.stringFlavor);
			Assert.assertNotNull(clipboardContents, "Clipboard contents are not of type DataFlavor.stringFlavor");
			Assert.assertFalse(clipboardContents.isEmpty(),
					"Clipboard contents are not of type DataFlavor.stringFlavor");
			writer.write(clipboardContents);
			writer.close();
		} else {
			Assert.fail("Clipboard contents are not of type DataFlavor.stringFlavor");
		}
	}
}
