//package tests;
//
//import com.microsoft.playwright.*;
//import org.testng.annotations.Test;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//public class ERPNetworkTest {
//
//    @Test
//    public void navigateERPAndRecordNetworkCalls() throws IOException {
//
//        List<String[]> networkLogs = new CopyOnWriteArrayList<>();
//
//        try (Playwright playwright = Playwright.create()) {
//
//            Browser browser = playwright.chromium().launch(
//                new BrowserType.LaunchOptions()
//                    .setHeadless(false)
//            );
//
//            BrowserContext context = browser.newContext();
//            Page page = context.newPage();
//
//            // Record outgoing requests
//            page.onRequest(request -> {
//                networkLogs.add(new String[] {
//                    "REQUEST",
//                    request.method(),
//                    request.url(),
//                    request.resourceType(),
//                    ""
//                });
//            });
//
//            // Record server responses
//            page.onResponse(response -> {
//                networkLogs.add(new String[] {
//                    "RESPONSE",
//                    response.request().method(),
//                    response.url(),
//                    response.request().resourceType(),
//                    String.valueOf(response.status())
//                });
//            });
//
//            // Open the ERP website
//            page.navigate(
//                "https://sysadmin.schooladminportal.com/dashboard"
//            );
//
//            System.out.println("ERP website opened.");
//            System.out.println("Log in manually using your email and password.");
//            System.out.println("Navigate through the ERP screens.");
//            System.out.println("The program will record network activity for 5 minutes.");
//
//            long endTime = System.currentTimeMillis() + 5 * 60 * 1000;
//
//            try {
//                while (System.currentTimeMillis() < endTime
//                        && browser.isConnected()
//                        && !page.isClosed()) {
//
//                    page.waitForTimeout(1000);
//                }
//            } catch (PlaywrightException e) {
//                System.out.println(
//                    "Browser closed or Playwright stopped waiting."
//                );
//            } finally {
//
//                // Save logs even if the browser was closed
//                try (FileWriter writer =
//                         new FileWriter("erp-network-logs.csv")) {
//
//                    writer.write(
//                        "Event,Method,URL,ResourceType,Status\n"
//                    );
//
//                    for (String[] log : networkLogs) {
//                        writer.write(
//                            csv(log[0]) + "," +
//                            csv(log[1]) + "," +
//                            csv(log[2]) + "," +
//                            csv(log[3]) + "," +
//                            csv(log[4]) + "\n"
//                        );
//                    }
//                }
//
//                System.out.println(
//                    "Total network events recorded: "
//                        + networkLogs.size()
//                );
//
//                System.out.println(
//                    "Network logs saved to erp-network-logs.csv"
//                );
//            }
//
//            if (browser.isConnected()) {
//                browser.close();
//            }
//        }
//    }
//
//    private String csv(String value) {
//        return "\"" +
//            (value == null ? "" : value.replace("\"", "\"\"")) +
//            "\"";
//    }
//}


package tests;

import com.microsoft.playwright.*;
import org.testng.annotations.Test;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ERPNetworkTest {

    @Test
    public void navigateERPAndRecordNetworkCalls() throws IOException {
        List<String[]> networkLogs = new CopyOnWriteArrayList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
            );
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // Record outgoing requests
            page.onRequest(request -> {
                networkLogs.add(new String[] {
                    "REQUEST", request.method(), request.url(), request.resourceType(), ""
                });
            });

            // Record server responses
            page.onResponse(response -> {
                networkLogs.add(new String[] {
                    "RESPONSE", response.request().method(), response.url(), response.request().resourceType(), String.valueOf(response.status())
                });
            });

            // Open the ERP website
            page.navigate("https://sysadmin.schooladminportal.com/dashboard"); // Ensure your actual URL is here

            System.out.println("ERP website opened successfully.");
            System.out.println("-> Please log in manually using your email and password.");
            System.out.println("-> Navigate through the ERP screens as needed.");
            System.out.println("-> The program will record network activity for 5 minutes...");

            // 5 minutes timer loop (5 * 60 * 1000 ms)
            long endTime = System.currentTimeMillis() + 5 * 60 * 1000;

            try {
                while (System.currentTimeMillis() < endTime
                        && browser.isConnected()
                        && !page.isClosed()) {
                    page.waitForTimeout(1000);
                }
            } catch (PlaywrightException e) {
                System.out.println("Browser closed or Playwright stopped waiting.");
            } finally {
                // Save logs to CSV (Fixed indexing error)
                try (FileWriter writer = new FileWriter("erp-network-logs.csv")) {
                    writer.write("Event,Method,URL,ResourceType,Status\n");

                    for (String[] log : networkLogs) {
                        writer.write(
                            csv(log[0]) + "," +
                            csv(log[1]) + "," +
                            csv(log[2]) + "," +
                            csv(log[3]) + "," +
                            csv(log[4]) + "\n"
                        );
                    }
                }

                System.out.println("Total network events recorded: " + networkLogs.size());
                System.out.println("Network logs saved to erp-network-logs.csv");
            }

            if (browser.isConnected()) {
                browser.close();
            }
        }
    }

    private String csv(String value) {
        return "\"" + (value == null ? "" : value.replace("\"", "\"\"")) + "\"";
    }
}
