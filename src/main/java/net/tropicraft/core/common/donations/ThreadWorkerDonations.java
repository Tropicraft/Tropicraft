package net.tropicraft.core.common.donations;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import net.tropicraft.core.common.config.TropicsConfigs;

public class ThreadWorkerDonations implements Runnable {

    private static ThreadWorkerDonations instance;
    private static Thread thread;

    public volatile boolean running = false;

    private DonationData donationData;

    public static ThreadWorkerDonations getInstance() {
        if (instance == null) {
            instance = new ThreadWorkerDonations();
        }
        return instance;
    }

    public void startThread(DonationData donationData) {
        this.donationData = donationData;
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
                Thread.sleep(TimeUnit.SECONDS.toMillis(TropicsConfigs.donationTrackerRefreshRate));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void checkDonations() {
        //http code
        try {

            //check if we decided to shut off donation querying after it started
            if (TropicsConfigs.tiltifyAppToken.isEmpty() || TropicsConfigs.tiltifyCampaign == 0) {
                stopThread();
                return;
            }

            String contents = getData_Real();

            JsonDataDonation json = TickerDonation.GSON.fromJson(contents, JsonDataDonation.class);
            
            TickerDonation.callbackDonations(json);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public String getData_Real() {
        //TODO: get URL from a master json file we can change
        try {
            //URL url = new URL("https://tiltify.com/api/v3/campaigns/love-tropics");
            URL url;
            synchronized (donationData) {
                url = new URL("https://tiltify.com/api/v3/campaigns/" + TropicsConfigs.tiltifyCampaign + "/donations?count=100" + (donationData.getLastSeenId() == 0 ? "" : "&after=" + donationData.getLastSeenId()));
            }
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + TropicsConfigs.tiltifyAppToken);

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
}
