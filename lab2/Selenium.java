package selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Selenium {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	String info=new String();
	String address=new String();
	  
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver","E:/大三下/软件测试/chromedriver.exe"); // 此处PATH替换为你的chromedriver所在路径
		driver = new ChromeDriver();
		baseUrl = "https://psych.liebes.top/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	  
	@Test
	public void test1() throws Exception {   
		//读取excel文件
	    File file = new File("E:/大三下/软件测试/input.xls");  
	    
	    try { 
	    	// 创建输入流，读取Excel  
	        InputStream is = new FileInputStream(file.getAbsolutePath());  
	        // jxl提供的Workbook类  
	        Workbook wb = Workbook.getWorkbook(is);
	        // 为页签sheet 1创建一个Sheet对象  
	        Sheet sheet = wb.getSheet(0);
	        
	        for (int i = 0; i < sheet.getRows(); i++) {  
	        	//按列名读取这条记录的值
	   	        String number = sheet.getCell(0, i).getContents();  //读取学号
	   	        String pwd = number.substring(number.length()-6,number.length()); //读取密码
	   	        address = sheet.getCell(1, i).getContents().trim();  //读取github地址,记得用trim去掉首尾空格
	   	        
	   	        //访问给定网址
	   	        driver.get(baseUrl + "/st");
	   	        //输入用户名
	   	        driver.findElement(By.id("username")).clear();
	   	        driver.findElement(By.id("username")).sendKeys(number);
	   	        //输入密码
	   	        driver.findElement(By.id("password")).clear();
	   	        driver.findElement(By.id("password")).sendKeys(pwd);
	   	        //点击登录按钮
	   	        driver.findElement(By.id("submitButton")).click();
	   	        //登录成功之后，获得当前页面的用户信息
	   	        info = driver.findElement(By.tagName("p")).getText().trim();
	   	        
	   	        //excel表格中有的github地址后面带有"/",建议都去掉"/"再比较
	   	        if(info.endsWith("/")) {
	   	        	info = info.substring(0, info.length()-1);
	   	        }
	   	        if(address.endsWith("/")) {
	   	        	address = address.substring(0, address.length()-1);
	   	        }
	   	        
	   	        //比较查询信息            
	   	        if(info.equals(address)) {
	   	        	assertEquals(info,address);
	   	            System.out.println(number+"的信息一致.");
	   	        }
	   	        else {
	   	        	System.out.println(number+"的信息不一致.");
	   	        }
	        }
	        driver.close();
	        
	    } catch (FileNotFoundException e) { 
	    	e.printStackTrace();  
	    } catch (BiffException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

	}

	@After
	public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	    	fail(verificationErrorString);
	    }
	}

	private boolean isElementPresent(By by) {
	    try {
	    	driver.findElement(by);
	        return true;
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	}

	private boolean isAlertPresent() {
	    try {
	    	driver.switchTo().alert();
	        return true;
	    } catch (NoAlertPresentException e) {
	        return false;
	    }
	}

	private String closeAlertAndGetItsText() {
	    try {
	    	Alert alert = driver.switchTo().alert();
	        String alertText = alert.getText();
	        if (acceptNextAlert) {
	        	alert.accept();
	        } else {
	        	alert.dismiss();
	        }
	        return alertText;
	    } finally {
	        acceptNextAlert = true;
	    }
	}

}
