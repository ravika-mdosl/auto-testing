package com.mdo.autotest.support;

import static com.mdo.autotest.support.CaseFormat.toLowerUnderscore;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class SeleniumTestExecutionListener extends AbstractTestExecutionListener
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private WebDriver webDriver;
	

	public int getOrder()
	{
		return Ordered.HIGHEST_PRECEDENCE;
	}

	/**Registers the driver with bean factory*/
	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception
	{
		if (webDriver != null)
		{
			return;
		}
		ApplicationContext context = testContext.getApplicationContext();
		if (context instanceof ConfigurableApplicationContext)
		{

			SeleniumTest annotation = findAnnotation(testContext.getTestClass(), SeleniumTest.class);
			webDriver = BeanUtils.instantiate(annotation.driver());

			ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
			ConfigurableListableBeanFactory bf = configurableApplicationContext.getBeanFactory();
			bf.registerResolvableDependency(WebDriver.class, webDriver);
			
		}
	}

	/**Before each test method base URL of the application will be opened by a WebDriver*/
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception
	{
		if (webDriver != null)
		{
			SeleniumTest annotation = findAnnotation(testContext.getTestClass(), SeleniumTest.class);
			webDriver.get(annotation.appUrl());
		}
	}

	/**After each test the driver will be closed*/
	@Override
	public void afterTestClass(TestContext testContext) throws Exception
	{
		if (webDriver != null)
		{
			webDriver.close();
			
		}
	}

	/**Every failure a screenshot will be generated*/
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception
	{
		if (testContext.getTestException() != null)
		{
			logger.error("Error in testing: {}", testContext.getTestException().getMessage());
			
			this.takeSnapshot(testContext);
		}
	}
	/**Currently save the .png in project root dir*/
	private void takeSnapshot(TestContext testContext) throws IOException
	{
		String testName = toLowerUnderscore(testContext.getTestClass().getSimpleName());
		String methodName = toLowerUnderscore(testContext.getTestMethod().getName());		
		
		File sourceFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		
		String generateFilePath = testName + "_" + methodName + "_" + sourceFile.getName();
		
		File destFile = new File(generateFilePath);
		FileUtils.copyFile(sourceFile, destFile);		
	}

}
