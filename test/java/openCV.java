import java.io.File;

public class openCV  {
    private final static String VALIDATION_PATH = System.getProperty("user.dir") + "/Creatives";

    private final static String BANNER = "banner";

    private final static double MATCH_THRESHOLD = 0.99;



    @Test
    public void test() throws Exception {

        GeneralMethods generalMethodsObject = GeneralMethodsObjectFactory.get(driver);

        generalMethodsObject.beforeTest();

        doVisualCheck(BANNER);

        generalMethodsObject.pause(5000);
    }


    private void doVisualCheck(String checkName) throws Exception {
        String baselineFilename = VALIDATION_PATH + "/" + checkName + ".png";
        File baselineImg = new File(baselineFilename);

        // If no baseline image exists for this check, we should create a baseline image
        if (!baselineImg.exists()) {
            System.out.println(String.format("No baseline found for '%s' check; capturing baseline instead of checking", checkName));
            File newBaseline = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(newBaseline, new File(baselineFilename));
            return;
        }

        // Otherwise, if we found a baseline, get the image similarity from Appium. In getting the similarity,
        // we also turn on visualization so we can see what went wrong if something did.
        SimilarityMatchingOptions opts = new SimilarityMatchingOptions();
        opts.withEnabledVisualization();
        SimilarityMatchingResult res = driver.getImagesSimilarity(baselineImg, driver.getScreenshotAs(OutputType.FILE), opts);

        // If the similarity is not high enough, consider the check to have failed
        if (res.getScore() < MATCH_THRESHOLD) {
            File failViz = new File(VALIDATION_PATH + "/FAIL_" + checkName + ".png");
            res.storeVisualization(failViz);
            throw new Exception(
                    String.format("Visual check of '%s' failed; similarity match was only %f, and below the threshold of %f. Visualization written to %s.",
                            checkName, res.getScore(), MATCH_THRESHOLD, failViz.getAbsolutePath()));
        }

        // Otherwise, it passed!
        System.out.println(String.format("Visual check of '%s' passed; similarity match was %f",
                checkName, res.getScore()));
    }
} //end class



Описание:
-Тут ничего особо тест не делает, на первый прогон тест проходит сохраняя скриншот как эталон в private final static String VALIDATION_PATH = System.getProperty("user.dir") + "/Creatives"; тут все ок

-на второй прогон при попытке сравнения падает ошибка error: '@u4/opencv4nodejs' module is required to use OpenCV features:

        org.openqa.selenium.WebDriverException: An unknown server-side error occurred while processing the command. Original error: '@u4/opencv4nodejs' module is required to use OpenCV features. Please install it first and restart Appium. Read https://github.com/appium/appium-plugins/pull/73#issuecomment-1013683074 and https://github.com/UrielCh/opencv4nodejs#fork-changes for more details on this topic.
        Build info: version: '3.141.59', revision: 'e82be7d358', time: '2018-11-14T08:17:03'
        System info: host: 'Ivans-MacBook-Pro.local', ip: 'fe80:0:0:0:ce6:9d12:c885:4565%en0', os.name: 'Mac OS X', os.arch: 'x86_64', os.version: '12.6', java.version: '1.8.0_332'
        Driver info: io.appium.java_client.android.AndroidDriver
        Capabilities {app: /Users/ivanlitvinau/IdeaPro..., appActivity: com.appodeal.test.MainActivity, appPackage: com.appodealstack.demo, automationName: Appium, clearSystemFiles: true, databaseEnabled: false, desired: {app: /Users/ivanlitvinau/IdeaPro..., appActivity: com.appodeal.test.MainActivity, appPackage: com.appodealstack.demo, automationName: Appium, clearSystemFiles: true, deviceName: Pixel 3, full-reset: true, platformName: android, platformVersion: 12, unlockKey: 9911, unlockType: pin}, deviceApiLevel: 31, deviceManufacturer: Google, deviceModel: Pixel 3, deviceName: 8AQX0XDTG, deviceScreenDensity: 440, deviceScreenSize: 1080x2160, deviceUDID: 8AQX0XDTG, full-reset: true, javascriptEnabled: true, locationContextEnabled: false, networkConnectionEnabled: true, pixelRatio: 2.75, platform: LINUX, platformName: Android, platformVersion: 12, statBarHeight: 77, takesScreenshot: true, unlockKey: 9911, unlockType: pin, viewportRect: {height: 1951, left: 0, top: 77, width: 1080}, warnings: {}, webStorageEnabled: false}
        Session ID: 60e8be18-4d62-49bc-af66-f18323542ac0

        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
        at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
        at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
        at org.openqa.selenium.remote.http.W3CHttpResponseCodec.createException(W3CHttpResponseCodec.java:187)
        at org.openqa.selenium.remote.http.W3CHttpResponseCodec.decode(W3CHttpResponseCodec.java:122)
        at org.openqa.selenium.remote.http.W3CHttpResponseCodec.decode(W3CHttpResponseCodec.java:49)
        at org.openqa.selenium.remote.HttpCommandExecutor.execute(HttpCommandExecutor.java:158)
        at io.appium.java_client.remote.AppiumCommandExecutor.execute(AppiumCommandExecutor.java:247)
        at org.openqa.selenium.remote.RemoteWebDriver.execute(RemoteWebDriver.java:552)
        at io.appium.java_client.DefaultGenericMobileDriver.execute(DefaultGenericMobileDriver.java:41)
        at io.appium.java_client.AppiumDriver.execute(AppiumDriver.java:1)
        at io.appium.java_client.android.AndroidDriver.execute(AndroidDriver.java:1)
        at io.appium.java_client.CommandExecutionHelper.execute(CommandExecutionHelper.java:27)
        at io.appium.java_client.ComparesImages.getImagesSimilarity(ComparesImages.java:194)
        at io.appium.java_client.ComparesImages.getImagesSimilarity(ComparesImages.java:230)
        at Experiment.doVisualCheck(Experiment.java:134)
        at Experiment.testAppDesign(Experiment.java:112)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at junit.framework.TestCase.runTest(TestCase.java:177)
        at junit.framework.TestCase.runBare(TestCase.java:142)
        at junit.framework.TestResult$1.protect(TestResult.java:122)
        at junit.framework.TestResult.runProtected(TestResult.java:142)
        at junit.framework.TestResult.run(TestResult.java:125)
        at junit.framework.TestCase.run(TestCase.java:130)
        at junit.framework.TestSuite.runTest(TestSuite.java:241)
        at junit.framework.TestSuite.run(TestSuite.java:236)
        at org.junit.internal.runners.JUnit38ClassRunner.run(JUnit38ClassRunner.java:90)
        at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
        at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:69)
        at com.intellij.rt.junit.IdeaTestRunner$Repeater$1.execute(IdeaTestRunner.java:38)
        at com.intellij.rt.execution.junit.TestsRepeater.repeat(TestsRepeater.java:11)
        at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:35)
        at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:235)
        at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:54)


- Ошибка понятна тк я изначально не могу поставить OpenCV на мак, пастоянно вылетаю проблемы при установке, конфликты с nodejs, cmake и тд, я уже запутался
- Как вижу я, нужно ставить все компоненты с 0 под Вашим руководством)


мой мак:
        MacBook Pro (14‑дюймовый, 2021 г.)
        Apple M1 Max
        Monterey Версия 12.6
            
            
            
            
 при npm i -g opencv4nodejs 
 
 
npm i -g opencv4nodejs
npm ERR! code 1
npm ERR! path /opt/homebrew/lib/node_modules/opencv4nodejs
npm ERR! command failed
npm ERR! command sh -c -- node ./install/install.js
npm ERR! info install using lib dir: /opt/homebrew/lib/node_modules/opencv4nodejs/node_modules/opencv-build/opencv/build/lib
npm ERR! /opt/homebrew/lib/node_modules/opencv4nodejs/install/install.js:37
npm ERR!   throw new Error('library dir does not exist: ' + libDir)
npm ERR!   ^
npm ERR! 
npm ERR! Error: library dir does not exist: /opt/homebrew/lib/node_modules/opencv4nodejs/node_modules/opencv-build/opencv/build/lib
npm ERR!     at Object.<anonymous> (/opt/homebrew/lib/node_modules/opencv4nodejs/install/install.js:37:9)
npm ERR!     at Module._compile (node:internal/modules/cjs/loader:1120:14)
npm ERR!     at Module._extensions..js (node:internal/modules/cjs/loader:1174:10)
npm ERR!     at Module.load (node:internal/modules/cjs/loader:998:32)
npm ERR!     at Module._load (node:internal/modules/cjs/loader:839:12)
npm ERR!     at Function.executeUserEntryPoint [as runMain] (node:internal/modules/run_main:81:12)
npm ERR!     at node:internal/main/run_main_module:17:47
npm ERR! 
npm ERR! Node.js v18.7.0

npm ERR! A complete log of this run can be found in:
npm ERR!     /Users/ivanlitvinau/.npm/_logs/2022-10-11T15_29_05_334Z-debug-0.log
ivanlitvinau@Ivans-MacBook-Pro ~ % 



    
пытался на разных версиях Node.js     
+ лазил по размым issues вроде этого https://github.com/justadudewhohacks/opencv4nodejs/issues/806

    
    
