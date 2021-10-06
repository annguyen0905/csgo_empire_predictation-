package com.company;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.bytebuddy.implementation.bytecode.Throw;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;

public class CheckScenario {

    public CheckScenario() throws IOException {
    }

    public static void main(String[] args) throws Exception {

        double originalBalance = 5000;
        double balance = 5000;
        double profit = 0;
        double baseBetValue = 1;
        double betValue = 1;
        int playRound = 1;
        int roundWon = 0;
        int roundLost = 0;
        String result = "";
        int winningStreak = 0;
        int losingStreak = 0;
        int longestWinningStreak = 0;
        int longestLosingStreak = 0;
        ArrayList allCoins = new ArrayList();
        ArrayList playingCoinList = new ArrayList();
        getAllCoins(allCoins);
        sleep(5000);
        for (int i = 0; i < 800; i++) {
            playingCoinList.add(allCoins.get(i).toString());
        }

        while (true) {

            if (balance < betValue) {
                throw new Exception("No money left to bet");
            }
            if (true) {
                System.out.println("-----------------------------");
                System.out.println("Balance updated :" + balance);
                System.out.println("Round Won: " + roundWon);
                System.out.println("Round Lost: " + roundLost);
                int size = playingCoinList.size();
                int seed = 2;
                boolean isFound = false;
                boolean isSkip = false;
                do {
                    String prePattern = (playingCoinList.get(size - 1).toString() + playingCoinList.get(size - 2).toString() + playingCoinList.get(size - 3).toString());

                    if (prePattern.contains("bonus")) {
                        isSkip = true;
                    }
                    String comparePattern = "";
                    try {
                        comparePattern = playingCoinList.get(size - seed).toString() + playingCoinList.get(size - (seed + 1)).toString() + playingCoinList.get(size - (seed + 2)).toString();
                    } catch (IndexOutOfBoundsException e) {
                        isSkip = true;
                    }
                    if (!isSkip) {
                        if (prePattern.equals(comparePattern)) {
                            isFound = true;
                            result = playingCoinList.get(size - (seed - 1)).toString();
                            if (result.equals("t")) {
                                System.out.println("Playround " + playRound);
                                playRound = playRound + 1;
                            } else if (result.equals("ct")) {
                                System.out.println("Playround " + playRound);

                                playRound = playRound + 1;
                            } else {
                                // NO BET ON BONUS
                                System.out.println("Result is bonus but we do not bet on BONUS");
                            }
                        } else {
                            seed = seed + 1;
                        }
                    } else {
                        playingCoinList.add(allCoins.get(playingCoinList.size() - 1).toString());
                    }
                } while (!isFound && !isSkip);

                if (isFound) {
//                    inputBalance.clear();
                    System.out.println("Bet on : " + result + " with value : " + betValue);
                    System.out.println("Found at position: " + (size - (seed - 1)));
                    if (!result.equals("bonus")) {
                        balance = balance - betValue;
                    }
                    if (playingCoinList.get(size - 1).toString().trim().equals(result.trim())) {
                        if (result.equals("bonus")) {
                            // Do nothing
                        } else {
                            winningStreak = winningStreak + 1;
                            if (losingStreak >= longestLosingStreak) {
                                longestLosingStreak = losingStreak;
                            }
                            losingStreak = 0;
                            balance = balance + betValue * 2;
                            betValue = baseBetValue;
                            roundWon = roundWon + 1;
                        }
                        profit = balance - originalBalance;
                        System.out.println("Profit: " + profit);

                    } else {
                        System.out.println("Bet on: " + result.toUpperCase() + " but hit " + playingCoinList.get(size - 1).toString().trim().toUpperCase());
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
                        }
                    }
                    playingCoinList.add(allCoins.get(playingCoinList.size() - 1).toString());
                }
            }
        }
    }

    public static void getAllCoins(ArrayList list) throws IOException {
        String sURL = "https://csgoempire.com/api/v2/metadata/roulette/history?seed=2432";
        URL url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject rootobj = root.getAsJsonObject();
        JsonElement jsonEle = rootobj.get("rolls");
        JsonArray jsonArray = jsonEle.getAsJsonArray();
        list.clear();
        for (int i = 1200; i <= jsonArray.size() - 1; i++) {
            list.add(jsonArray.get(i).getAsJsonObject().get("coin").getAsString());
        }
        Collections.reverse(list);
    }


}