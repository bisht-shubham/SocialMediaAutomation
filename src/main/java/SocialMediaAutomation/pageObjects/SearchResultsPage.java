package SocialMediaAutomation.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import SocialMediaAutomation.AbstractComponents.AbstractComponents;

public class SearchResultsPage extends AbstractComponents {
	WebDriver driver;

	public SearchResultsPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//ytd-channel-name[@id='channel-title']//yt-formatted-string[@class='style-scope ytd-channel-name']")
	WebElement channelNameElement;

	@FindBy(xpath = "//span[contains(text(),'views')]/../../../..//a[@id='video-title']")
	WebElement firstVideoitle;

	@FindBy(xpath = "//div[@id='channel-info']/..//span[contains(text(),'views')]")
	WebElement firstVideoResult;

	public String getChannelName() {
		String channelName = getElementText(channelNameElement);
		return channelName;
	}

	public String getFirstVideoResultText() {
		waitForWebElementToAppear(firstVideoitle);
		String firstVideoResult = getElementText(firstVideoitle);
		return firstVideoResult;
	}

	public void clickFirstVideoResult() {
		waitForElementToBeClickable(firstVideoResult);
		firstVideoResult.click();
	}

	public void ClickOnFirstResult(String channelName) throws InterruptedException {
		LandingPage landingPage = new LandingPage(driver);
		landingPage.SearchTextOnYt(channelName);
		clickFirstVideoResult();
	}
}
