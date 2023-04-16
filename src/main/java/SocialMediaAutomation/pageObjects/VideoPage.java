package SocialMediaAutomation.pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import SocialMediaAutomation.AbstractComponents.AbstractComponents;

public class VideoPage extends AbstractComponents {
	WebDriver driver;

	public VideoPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//h1[@class='style-scope ytd-watch-metadata']")
	WebElement currentPlayingVideoTitle;

	@FindBy(xpath = "//button//div[contains(text(),'Skip Ad')]")
	WebElement skipAdButton;

	@FindBy(xpath = "(//span[text()='Share'])[1]/../..")
	WebElement shareButton;

	@FindBy(xpath = "//span[text()='Copy']/parent::div/parent::button")
	WebElement copyUrlButton;

	@FindBy(xpath = "//yt-formatted-string[text()='Link copied to clipboard']")
	WebElement copiedToClipboardToast;

	@FindBy(xpath = "//button[@title='Play (k)']")
	List<WebElement> playButton;

	@FindBy(xpath = "//span[@class='ytp-volume-area']")
	WebElement volumeButtonSection;

	@FindBy(xpath = "//div[contains(@class,'ytp-volume-panel')]")
	WebElement volumePannel;

	@FindBy(xpath = "//button//div[contains(text(),'Skip Ad')]")
	List<WebElement> skipAdsButtons;

	@FindBy(xpath = "//button//div[contains(text(),'Skip Ad')]")
	WebElement skipAdsButton;
	
	@FindBy(xpath = "//span[@class='ytp-ad-duration-remaining']")
	List<WebElement> remainingAdDuration;
	
	

	String SHARE_SOCIAL_MEDIA_LOGO = "//div[@id='title'][text()='socialMedia']";

	public String getCurrentVideoTitle() {
		String videoTitle = getElementText(currentPlayingVideoTitle);
		return videoTitle;
	}

	public int PauseVideo() {
		PerformActionSendKeysSpace();
		int playButtonSize = playButton.size();
		return playButtonSize;
	}

	public void AdjustVolume() throws InterruptedException {
		WebElement volumeButtonArea = volumeButtonSection;
		Thread.sleep(1000);
		PerformActionMoveToElement(volumeButtonArea);
		WebElement volumeSlider = volumePannel;
		PerformActionDragDrop(volumeSlider);
	}

	public int SkipVideoAd() throws InterruptedException {
		int skipAdsCount = skipAdsButtons.size();
		if (skipAdsCount > 0) {
			waitForElementToBeClickable(skipAdsButton);
			skipAdsButton.click();
		} else if (skipAdsCount == 0) {
			System.out.println("No Ads");
		}
		int adDurationCount = remainingAdDuration.size();
		return adDurationCount;
	}

	public String ClickShareButtonCopyUrl() {
		waitForElementToBeClickable(shareButton);
		shareButton.click();
		waitForElementToBeClickable(copyUrlButton);
		copyUrlButton.click();
		String linkCopiedText = getElementText(copiedToClipboardToast);
		return linkCopiedText;
	}

	public List<WebElement> prepareWebElementWithDynamicXpath(String substitutionValue) {
		return driver.findElements(By.xpath(SHARE_SOCIAL_MEDIA_LOGO.replace("socialMedia", substitutionValue)));
	}

	public int VerifyLogo() {
		String[] shareLogoText = { "WhatsApp", "Facebook", "Twitter" };
		int totalElementCount = 0;
		for (int i = 0; i < shareLogoText.length; i++) {
			int elementCount = prepareWebElementWithDynamicXpath(shareLogoText[i]).size();
			totalElementCount += elementCount;
		}
		return totalElementCount;
	}

	public void ClickShareButton(String channelName) throws InterruptedException {
		SearchResultsPage searchresultsPage = new SearchResultsPage(driver);
		searchresultsPage.ClickOnFirstResult(channelName);
		ClickShareButtonCopyUrl();
	}
}
