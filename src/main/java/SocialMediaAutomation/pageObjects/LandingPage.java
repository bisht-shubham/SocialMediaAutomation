package SocialMediaAutomation.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import SocialMediaAutomation.AbstractComponents.AbstractComponents;

public class LandingPage extends AbstractComponents {
	WebDriver driver;

	public LandingPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void goTo() {
		driver.get("https://www.youtube.com/");
	}

	public void OpenYouTube() throws InterruptedException {
		goTo();
		String firstTabTitle = getCurrentPageTitle();
		System.out.println(firstTabTitle);
		String firstTabUrl = getCurrentPageUrl();
		System.out.println(firstTabUrl);
		Thread.sleep(5000);
	}

	public void SearchTextOnYt(String channelName) throws InterruptedException {
		OpenYouTube();
		EnterSearchtext(channelName);
		ClickSearchButton();
	}
}
