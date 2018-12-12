package com.mdo.autotest.support;

import static com.mdo.autotest.support.CaseFormat.toLowerUnderscore;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class SeleniumTestExecutionListener extends AbstractTestExecutionListener
{

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
			File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
			String testName = toLowerUnderscore(testContext.getTestClass().getSimpleName());
			String methodName = toLowerUnderscore(testContext.getTestMethod().getName());
			Files.copy(screenshot.toPath(), Paths.get("screenshots", testName + "_" + methodName + "_" + screenshot.getName()));
		}
	}

}
