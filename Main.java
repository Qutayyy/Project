// Main.java — Students version
import java.io.*;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
                              "July","August","September","October","November","December"};

    static int[][][] profit = new int[MONTHS][DAYS][COMMS];
    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {
        try {
            for (int m = 0; m < MONTHS; m++) {
                String filename = "Data_Files/" + months[m] + ".txt";
                Scanner scanner = new Scanner(new File(filename));

                scanner.nextLine();

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");

                    int day = Integer.parseInt(parts[0]) - 1;
                    String commodity = parts[1];
                    int profitValue = Integer.parseInt(parts[2]);

                    int commIndex = getCommodityIndex(commodity);
                    profit[m][day][commIndex] = profitValue;
                }

                scanner.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }
        int maxProfit = Integer.MIN_VALUE;
        int bestCommodity = 0;
        for (int c = 0; c < COMMS; c++) {
            int totalProfit = 0;
            for (int d = 0; d < DAYS; d++) {
                totalProfit += profit[month][d][c];
            }
            if (totalProfit > maxProfit) {
                maxProfit = totalProfit;
                bestCommodity = c;
            }
        }

        return commodities[bestCommodity] + " " + maxProfit;
    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 1 || day > DAYS) {
            return -99999;
        }
        int total = 0;
        for (int c = 0; c < COMMS; c++) {
            total += profit[month][day][c];
        }
        return total;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        int commIndex = getCommodityIndex(commodity);

        if (commIndex == -1 || from < 1 || to > DAYS || from > to) {
            return -99999;
        }

        int totalProfit = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = from - 1; d < to; d++) { 
                totalProfit += profit[m][d][commIndex];
            }
        }

        return totalProfit;
    }

    public static int bestDayOfMonth(int month) {
        if (month < 0 || month >= MONTHS) {
            return -1;
        }

        int bestDay = 1;
        int maxProfit = Integer.MIN_VALUE;

        for (int d = 0; d < DAYS; d++) {
            int dailyTotal = 0;
            for (int c = 0; c < COMMS; c++) {
                dailyTotal += profit[month][d][c];
            }

            if (dailyTotal > maxProfit) {
                maxProfit = dailyTotal;
                bestDay = d + 1;
            }
        }

        return bestDay;
    }
    
    public static String bestMonthForCommodity(String comm) {
        int commIndex = getCommodityIndex(comm);

        if (commIndex == -1) {
            return "INVALID_COMMODITY";
        }

        int bestMonth = 0;
        int maxProfit = Integer.MIN_VALUE;

        for (int m = 0; m < MONTHS; m++) {
            int monthlyTotal = 0;
            for (int d = 0; d < DAYS; d++) {
                monthlyTotal += profit[m][d][commIndex];
            }

            if (monthlyTotal > maxProfit) {
                maxProfit = monthlyTotal;
                bestMonth = m;
            }
        }

        return months[bestMonth];
    }

    public static int consecutiveLossDays(String comm) {
        int commIndex = getCommodityIndex(comm);

        if (commIndex == -1) {
            return -1;
        }
        int maxStreak = 0;
        int currentStreak = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profit[m][d][commIndex] < 0) {
                    currentStreak++;
                    if (currentStreak > maxStreak) {
                        maxStreak = currentStreak;
                    }
                } else {
                    currentStreak = 0;
                }
            }
        }

        return maxStreak;
    }
    
    public static int daysAboveThreshold(String comm, int threshold) {
        int commIndex = getCommodityIndex(comm);

        if (commIndex == -1) {
            return -1;
        }

        int count = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profit[m][d][commIndex] > threshold) {
                    count++;
                }
            }
        }

        return count;
    }

    public static int biggestDailySwing(int month) {
        if (month < 0 || month >= MONTHS) {
            return -99999;
        }

        int maxSwing = 0;

        for (int d = 0; d < DAYS - 1; d++) {
            int day1Total = 0;
            int day2Total = 0;

            for (int c = 0; c < COMMS; c++) {
                day1Total += profit[month][d][c];
                day2Total += profit[month][d + 1][c];
            }

            int swing = Math.abs(day2Total - day1Total);

            if (swing > maxSwing) {
                maxSwing = swing;
            }
        }

        return maxSwing;
    }
    
    public static String compareTwoCommodities(String c1, String c2) {
        int c1Index = getCommodityIndex(c1);
        int c2Index = getCommodityIndex(c2);

        if (c1Index == -1 || c2Index == -1) {
            return "INVALID_COMMODITY";
        }

        int c1Total = 0;
        int c2Total = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                c1Total += profit[m][d][c1Index];
                c2Total += profit[m][d][c2Index];
            }
        }

        if (c1Total > c2Total) {
            return c1 + " is better by " + (c1Total - c2Total);
        } else if (c2Total > c1Total) {
            return c2 + " is better by " + (c2Total - c1Total);
        } else {
            return "Equal";
        }
    }
    
    public static String bestWeekOfMonth(int month) {
        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }

        int bestWeek = 1;
        int maxProfit = Integer.MIN_VALUE;

        for (int week = 0; week < 4; week++) {
            int weekTotal = 0;
            int startDay = week * 7;      // 0, 7, 14, 21
            int endDay = startDay + 7;    // 7, 14, 21, 28

            for (int d = startDay; d < endDay; d++) {
                for (int c = 0; c < COMMS; c++) {
                    weekTotal += profit[month][d][c];
                }
            }

            if (weekTotal > maxProfit) {
                maxProfit = weekTotal;
                bestWeek = week + 1;
            }
        }

        return "Week " + bestWeek;
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }

    public static int getCommodityIndex(String name) {
        for (int i = 0; i < commodities.length; i++) {
            if (commodities[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }
}