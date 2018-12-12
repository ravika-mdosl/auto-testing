package com.mdo.autotest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mdo.autotest.pageobjects.LoginPage;
import com.mdo.autotest.support.SeleniumTest;

/***Go here for reference:  {@link <a href="https://peterkedemo.wordpress.com/2013/03/30/writing-good-selenium-tests-with-page-objects-and-spring"> Page Objects</a>}*/
@RunWith(SpringRunner.class)
@SpringBootTest
@SeleniumTest(appUrl="BASE APP URL", 
			  driver = FirefoxDriver.class)
public class MYPPageTest
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private WebDriver driver;

	private LoginPage loginPage;
	
	@Before
	public void setUp()
	{
		loginPage = PageFactory.initElements(driver, LoginPage.class);
	}
	
	
	/**Testing Login: Only test login goes here*/
	@Test
	public void testLogin() throws Exception
	{
		logger.info("***************  LOGIN - TESTING START  *************** ");
		
		loginPage.userName("USER NAME").password("PASSWORD").submit();
		
		logger.info("***************  LOGIN - TESTING END  *************** ");
			
		//Assert.assertEquals("[/projects] failure - expected HTTP status", 200, 200);		
	}

}
