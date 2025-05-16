package org.example;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static void main(String[] args)  throws Exception{
       //take advantage of chrome driver to access the challenge page
        WebDriver driver = new ChromeDriver();
        driver.get("https://ml-recruitment-challenge.meqprobe.com");

        //count the number of elements which of src value is cow_01 or sheep_01
        List<WebElement> cows = driver.findElements(By.cssSelector(
                "img[src='assets/cow_01.png'], img[src='assets/sheep_01.png']"));
        int count = cows.size();
        System.out.println("Found " + count + " cow images.");

        //post the result to the server
        URL url = new URL("https://ml-recruitment-challenge.meqprobe.com/submit");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        //edit the post body
        String postData = "count=" + URLEncoder.encode(String.valueOf(count), StandardCharsets.UTF_8)
                + "&name=" + URLEncoder.encode("Bo Xu", StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode("394427975@qq.com", StandardCharsets.UTF_8);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(postData.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        //display the response code and body
        int responseCode = conn.getResponseCode();
        System.out.println("POST Response Code: " + responseCode);

        InputStream is = (responseCode >= 200 && responseCode < 400) ?
                conn.getInputStream() : conn.getErrorStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            System.out.println("Response body:");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        driver.quit();
    }
}