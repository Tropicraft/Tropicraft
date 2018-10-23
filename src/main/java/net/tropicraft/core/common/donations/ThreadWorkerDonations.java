package net.tropicraft.core.common.donations;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ThreadWorkerDonations implements Runnable {

    private static ThreadWorkerDonations instance;
    private static Thread thread;

    public int donationCheckRateInMilliseconds = 5000;
    public boolean running = false;

    public static ThreadWorkerDonations getInstance() {
        if (instance == null) {
            instance = new ThreadWorkerDonations();
        }
        return instance;
    }

    public void startThread() {
        running = true;

        if (thread == null || thread.getState() == Thread.State.TERMINATED) {
            thread = new Thread(instance, "Donations lookup thread");
            thread.start();
        }
    }

    public void stopThread() {
        running = false;
    }

    @Override
    public void run() {

        try {
            while (running) {
                checkDonations();
                Thread.sleep(donationCheckRateInMilliseconds);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void checkDonations() {
        //http code
        try {
            String contents = getData_Test();

            JsonDataDonation json = TickerDonation.GSON.fromJson(contents, JsonDataDonation.class);

            TickerDonation.callbackDonations(json);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public static String getData_Real() {
        //TODO: get URL from a master json file we can change
        try {
            //URL url = new URL("https://tiltify.com/api/v3/campaigns/love-tropics");
            URL url = new URL("https://tiltify.com/api/v3/campaigns/10218/donations?count=100");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer f921e5eccf25c37404de5c096b4b097e0a57b5b8a82f525b8de8973174d37d32");

            int status = con.getResponseCode();

            //System.out.println("response code: " + status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            con.disconnect();

            return content.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "ERROR";
    }

    public static String getData_Test() {
        try {
            URL url = new URL("http://share.coros.us/files/donation_check_new.json");
            InputStream is = url.openStream();

            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, StandardCharsets.UTF_8);
            return writer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "ERROR";
    }
}
