package com.mdo.autotest.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;

/** Page Object for Login Page. WebElements and its functions goes here**/
public class LoginPage
{
	@Autowired
	private WebDriver driver;

	@FindBy(id = "UserName")
	private WebElement userName;

	@FindBy(id = "Password")
	private WebElement password;

	@FindBy(css = "button[type='submit']")
	private WebElement submit;
	

	public LoginPage userName(String usernameInput)
	{
		userName.clear();
		userName.sendKeys(usernameInput);
		return this;
	}
	
	public LoginPage password(String passwordInput)
	{
		password.clear();
		password.sendKeys(passwordInput);
		return this;
	}
	
	public LoginPage submit()
	{
		submit.click();
		return this;
	}

}
