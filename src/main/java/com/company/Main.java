package com.company;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;

public class Main {

    public Main() throws IOException {
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ASUS\\Downloads\\chromedriver_win32\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get("https://csgoempire.com/");
        WebDriverWait wait = new WebDriverWait(driver, 20000);

        WebElement loginBtn = driver.findElement(By.cssSelector("div[class='user-action absolute']"));
        loginBtn.click();
        new WebDriverWait(driver, 5000).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        WebElement txtUsername = driver.findElement(By.cssSelector("input[name='username']"));
        WebElement txtPassword = driver.findElement(By.cssSelector("input[name='password']"));
        WebElement signInBtn = driver.findElement(By.cssSelector("input[id='imageLogin']"));
        // Enter you username & password
        txtUsername.sendKeys("chanhquangbinh");
        txtPassword.sendKeys("Helloan0905@#");
        signInBtn.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[class='bg-transparent w-full h-full relative z-10']")));

        WebElement inputBalance = driver.findElement(By.cssSelector("input[class='bg-transparent w-full h-full relative z-10']"));
        int seedNum = 2433;
        double originalBalance = 126; //Your initial balance
        double balance = 126;
        double profit = 0;
        double baseBetValue = 1; //Your initial bet
        double betValue = 1;
        int playRound = 1;
        int roundWon = 0;
        int roundLost = 0;
        String result = "";
        int winningStreak = 0;
        int losingStreak = 0;
        int longestWinningStreak = 0;
        int longestLosingStreak = 0;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");


        while (true) {
            if (balance < betValue) {
                throw new Exception("No money left to bet");
            }
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[class='text-2xl font-bold font-numeric']")));
            WebElement txtTime = driver.findElement(By.cssSelector("div[class='text-2xl font-bold font-numeric']"));
            if (txtTime.getText().contains("10.")) {
                LocalTime now = LocalTime.now();
                now = LocalTime.parse(dtf.format(now));
                inputBalance.clear();
                inputBalance.sendKeys(betValue + "");
                System.out.println("-----------------------------");
//                System.out.println("Balance updated :" + balance);
                System.out.println("Round Won: " + roundWon);
                System.out.println("Round Lost: " + roundLost);
                System.out.println("Time : " + now);
                ArrayList listCoin = new ArrayList();
                updateCoinList(listCoin,seedNum);
                int size = listCoin.size();
                int seed = 2;
                boolean isFound = false;
                boolean isSkip = false;
                do {
                    String prePattern = (listCoin.get(size - 1).toString() + listCoin.get(size - 2).toString() + listCoin.get(size - 3).toString());
//                    String bonusPattern = (listCoin.get(size - 1).toString() + listCoin.get(size - 2).toString() + listCoin.get(size - 3).toString()+ listCoin.get(size - 4).toString()+ listCoin.get(size - 5).toString());

                    if (prePattern.contains("bonus")) {
                        System.out.println("Waiting for dices train ends");
                        isSkip = true;
                    }
                    String comparePattern = "";
                    try {
                        comparePattern = listCoin.get(size - seed).toString() + listCoin.get(size - (seed + 1)).toString() + listCoin.get(size - (seed + 2)).toString();
                    } catch (IndexOutOfBoundsException e) {
                        isSkip = true;
                    }
                    if (!isSkip) {
                        if (prePattern.equals(comparePattern)) {
                            isFound = true;
                            result = listCoin.get(size - (seed - 1)).toString();
                            if (result.equals("t")) {
                                System.out.println("Playround " + playRound);
                                WebElement tButton = driver.findElement(By.cssSelector("div.bet-buttons.w-full.mb-1.lg\\:mb-0 > div:nth-child(3) > button"));
                                tButton.click();
                                playRound = playRound + 1;
                            } else if (result.equals("ct")) {
                                System.out.println("Playround " + playRound);
                                WebElement ctButton = driver.findElement(By.cssSelector("div.bet-buttons.w-full.mb-1.lg\\:mb-0 > div:nth-child(1) > button"));
                                ctButton.click();
                                playRound = playRound + 1;
                            } else {
                                // NO BET ON BONUS
                                System.out.println("Result is bonus but we do not bet on BONUS");
                            }
                        } else {
                            seed = seed + 1;
                        }
                    }
                } while (!isFound && !isSkip);

                if (isFound) {
                    inputBalance.clear();
                    System.out.println("Bet on : " + result + " with value : " + betValue);
                    System.out.println("Found at position: " + (size - (seed - 1)));
                    if (!result.equals("bonus")) {
                        balance = balance - betValue;
                    }
                    sleep(1000 * 17);
                    updateCoinList(listCoin,seedNum);
                    if (listCoin.get(size - 1).toString().trim().equals(result.trim())) {
                        if (result.equals("bonus")) {
                            // Do nothing
                        } else {
                            winningStreak = winningStreak + 1;
                            if (losingStreak >= longestLosingStreak) {
                                longestLosingStreak = losingStreak;
                            }
                            losingStreak = 0;
                            balance = balance + betValue * 2;
                            betValue = 1;
                            roundWon = roundWon + 1;
                        }
                        profit = balance - originalBalance;
                        System.out.println("Profit: " + profit);

                    } else {
                        System.out.println("Bet on: " + result.toUpperCase() + " but hit " + listCoin.get(size - 1).toString().trim().toUpperCase());
                        if (result.trim().equals("bonus")) {
                            System.out.println("Result is BONUS but skip for safe");
                        } else {
                            if (winningStreak >= longestWinningStreak) {
                                longestWinningStreak = winningStreak;
                            }
                            winningStreak = 0;
                            losingStreak = losingStreak + 1;
                            roundLost = roundLost + 1;
                            betValue = betValue * 2;
                            profit = balance - originalBalance;
                            System.out.println("Profit: " + profit);


                            if (betValue >= 16) {
                                sleep(180 * 1000);
                                System.out.println("Take a break for 180s to find a better winning chance");
                            }

                            if (betValue >= 16 && profit >= 20) {
                                betValue = baseBetValue;
                            }

                            if (profit >= 100) {
                                throw new Exception("Withdraw please");
                            }
                        }
                    }
                }
            }
        }
    }

    public static void updateCoinList(ArrayList list, int seed) throws IOException {
        String sURL = "https://csgoempire.com/api/v2/metadata/roulette/history?seed=" + seed;
        URL url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject rootobj = root.getAsJsonObject();
        JsonElement jsonEle = rootobj.get("rolls");
        JsonArray jsonArray = jsonEle.getAsJsonArray();
        list.clear();
        for (int i = 0; i < 200; i++) {
            list.add(jsonArray.get(i).getAsJsonObject().get("coin").getAsString());
        }
        Collections.reverse(list);
    }
}