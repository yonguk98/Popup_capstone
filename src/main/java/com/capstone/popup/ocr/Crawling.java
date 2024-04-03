package com.capstone.popup.ocr;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Crawling {

    public List<String> run(String accountName) throws InterruptedException {

        // 크롬 드라이버 로드
        String driverPath = "/Users/yonguk/Desktop/capstone/chromedriver";
        String chromePath = "/Users/yonguk/Desktop/capstone/ChromeTesting.app";

        System.setProperty("webdriver.chrome.driver", driverPath);

        ChromeOptions options = new ChromeOptions();
//        options.setBinary(chromePath);
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);

        // 인스타그램 첫화면으로 이동
        String targetUrl = "https://www.instagram.com";
        driver.get(targetUrl);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));

        System.out.println("접속 페이지 명: " + driver.getTitle());
        System.out.println("접속완료");

        // 이메일, 비밀번호 텍스트 박스 찾기
        List<WebElement> emailBox = driver.findElements(By.tagName("input"));
        System.out.println(emailBox.get(0).getText());

        // 이메일, 비밀번호 입력
        String email = "01099129827";
        emailBox.get(0).clear();
        emailBox.get(0).sendKeys(email);

        String pwd = "Toji1801";
        emailBox.get(1).clear();
        emailBox.get(1).sendKeys(pwd);

        // 로그인 버튼 클릭
        List<WebElement> loginButton = driver.findElements(By.tagName("button"));
        System.out.println(loginButton.get(1).getAccessibleName() + "찾음");
        loginButton.get(1).click();
        System.out.println(loginButton.get(1).getAccessibleName() + "클릭 완료");

        sleep(5000); // 혹시 다른 프로세스들도 멈추는 건지 확인 필요

        // 검색 후 이동
        String searchWord = "https://www.instagram.com/" + accountName;
        String searchHashtag = "성수팝업";
        driver.get("https://www.instagram.com/explore/tags/" + searchWord);
        sleep(10000);
        System.out.println("접속 페이지 명: " + driver.getTitle());

        // 검색 결과 페이지의 게시글 링크 저장
        List<String> links = findAllArticleLink(driver);
        List<String> imgLinks = new ArrayList<>();

        // 게시글로 이동
        for (int i = 0; i < 6; i++) {
            System.out.println(i);
            driver.get(links.get(i));
            sleep(4000);

            // 게시글의 이미지 주소 저장
            imgLinks = saveSrcLink(driver);
        }

        return imgLinks;
    }

    // 검색 결과 페이지에서 a 태그의 href값을 추출하는 함수
    // 게시글의 주소값만 추출
    private static List<String> findAllArticleLink(WebDriver driver) {
        List<WebElement> findLinks = driver.findElements(By.tagName("a"));
        List<String> list = new ArrayList<>();
        for (WebElement link : findLinks) {
            String linkString = link.getAttribute("href");
            if (linkString.contains("https://www.instagram.com/p/")) {
                list.add(link.getAttribute("href"));
            }
        }
        return list;
    }

    // 특정 게시글의 img태그에서 src값을 추출하는 함수
    // TODO: 사용자 계정 이미지 같은 불필요한 값 필터링 필요
    private List<String> saveSrcLink(WebDriver driver) {
        List<WebElement> posts = driver.findElements(By.tagName("img"));
        List<String> imgLinks = new ArrayList<>();
        for (WebElement post : posts) {
            String imgLink = post.getAttribute("src");
            System.out.println(imgLink + "\n");
            imgLinks.add(imgLink);
        }
        return imgLinks;
    }
}
