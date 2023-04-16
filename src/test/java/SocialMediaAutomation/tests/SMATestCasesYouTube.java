package SocialMediaAutomation.tests;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.testng.Assert;
import org.testng.annotations.Test;
import SocialMediaAutomation.TestComponents.BaseTest;
import SocialMediaAutomation.pageObjects.LandingPage;
import SocialMediaAutomation.pageObjects.SearchResultsPage;
import SocialMediaAutomation.pageObjects.VideoPage;

public class SMATestCasesYouTube extends BaseTest {
	DataFormatter formatter = 	new DataFormatter();

	@Test(dataProvider = "driveTest", groups = { "SmokeTest" })
	public void YouTube_C100Open_Youtube_and_Verify(ArrayList<String> data) throws InterruptedException, IOException {
	    //driver = driverThreadLocal.get();
		LandingPage landingPage = new LandingPage(driver);
		landingPage.goTo();
		String firstTabTitle = landingPage.getCurrentPageTitle();
		System.out.println(firstTabTitle);
		String firstTabUrl = landingPage.getCurrentPageUrl();
		System.out.println(firstTabUrl);
		Assert.assertEquals(data.get(1), firstTabTitle);
		Assert.assertEquals(data.get(2), firstTabUrl);
		Thread.sleep(5000);
	}

	@Test(dataProvider = "driveTest", groups = { "Regression" })
	public void YouTube_C101Search_Text_on_Youtube_and_Verify_Results(ArrayList<String> data)
			throws InterruptedException {
		SearchResultsPage searchresultsPage = new SearchResultsPage(driver);
		LandingPage landingPage = new LandingPage(driver);
		landingPage.OpenYouTube();
		landingPage.EnterSearchtext(data.get(1));
		landingPage.ClickSearchButton();
		String channelNameResult = searchresultsPage.getChannelName();
		Assert.assertEquals(channelNameResult, data.get(1));
	}

	@Test(dataProvider = "driveTest", groups = { "SmokeTest" })
	public void YouTube_C102Click_on_First_Displayed_Video(ArrayList<String> data) throws InterruptedException {
		VideoPage videoPage = new VideoPage(driver);
		SearchResultsPage searchresultsPage = new SearchResultsPage(driver);
		LandingPage landingPage = new LandingPage(driver);
		landingPage.SearchTextOnYt(data.get(1));
		String firstResultVideoTitle = searchresultsPage.getFirstVideoResultText();
		searchresultsPage.clickFirstVideoResult();
		String videoTitle = videoPage.getCurrentVideoTitle();
		Assert.assertEquals(firstResultVideoTitle, videoTitle);
	}

	@Test(dataProvider = "driveTest", groups = { "Regression" })
	public void YouTube_C103Check_Youtube_Functionalities_Pause(ArrayList<String> data) throws InterruptedException {
		SearchResultsPage searchresultsPage = new SearchResultsPage(driver);
		searchresultsPage.ClickOnFirstResult(data.get(1));
		VideoPage videoPage = new VideoPage(driver);
		int playButtonSize = videoPage.PauseVideo();
		Assert.assertEquals(playButtonSize, 1);
	}

	@Test(dataProvider = "driveTest", groups = { "SmokeTest" })
	public void YouTube_C104Check_Youtube_Functionalities_Volume(ArrayList<String> data) throws InterruptedException {
		SearchResultsPage searchresultsPage = new SearchResultsPage(driver);
		searchresultsPage.ClickOnFirstResult(data.get(1));
		VideoPage videoPage = new VideoPage(driver);
		videoPage.AdjustVolume();
	}

	@Test(dataProvider = "driveTest", groups = { "Regression" })
	public void YouTube_C105Skip_Youtube_Ad_if_skippable(ArrayList<String> data) throws InterruptedException {
		SearchResultsPage searchresultsPage = new SearchResultsPage(driver);
		searchresultsPage.ClickOnFirstResult(data.get(1));
		VideoPage videoPage = new VideoPage(driver);
		int skipAdsCount = videoPage.SkipVideoAd();
		Thread.sleep(3000);
		Assert.assertEquals(skipAdsCount, 0);
	}

	@Test(dataProvider = "driveTest", groups = { "SmokeTest" })
	public void YouTube_C106Copy_Url_and_Verify_if_Copied(ArrayList<String> data) throws InterruptedException {
		SearchResultsPage searchresultsPage = new SearchResultsPage(driver);
		searchresultsPage.ClickOnFirstResult(data.get(1));
		VideoPage videoPage = new VideoPage(driver);
		String linkCopiedText = videoPage.ClickShareButtonCopyUrl();
		Assert.assertFalse(linkCopiedText.isEmpty());
	}

	@Test(groups = { "Regression" }, dependsOnMethods = { "YouTube_C106Copy_Url_and_Verify_if_Copied" })
	public void YouTube_C107Save_Copied_Text_in_a_File() throws IOException, UnsupportedFlavorException {
		VideoPage videoPage = new VideoPage(driver);
		videoPage.SaveClipBoardTextInFile();
	}

	@Test(dataProvider = "driveTest", groups = { "SmokeTest" })
	public void YouTube_C108Verify_Whatspp_Fb_and_Twitter_Logo(ArrayList<String> data) throws InterruptedException {
		VideoPage videoPage = new VideoPage(driver);
		videoPage.ClickShareButton(data.get(1));
		int totalElementCount = videoPage.VerifyLogo();
		Assert.assertEquals(3, totalElementCount);
		System.out.println("WhatsApp, Facebook, and Twitter logos exist");
	}
}
