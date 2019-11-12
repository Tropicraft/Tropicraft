package net.tropicraft.core.common.donations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;

import net.tropicraft.core.common.config.ConfigLT;

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
                Thread.sleep(TimeUnit.SECONDS.toMillis(ConfigLT.TILTIFY.donationTrackerRefreshRate.get()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void checkDonations() {
        //http code
        try {

            //check if we decided to shut off donation querying after it started
            if (ConfigLT.TILTIFY.appToken.get().isEmpty() || ConfigLT.TILTIFY.campaignId.get() == 0) {
                stopThread();
                return;
            }

            String contents = getData_Real();

            String contentsTotal = getData_TotalDonations();

            JsonDataDonation json = TickerDonation.GSON.fromJson(contents, JsonDataDonation.class);

            //store into temp object to scrap later once we take the total from it
            JsonDataDonation jsonTotal = TickerDonation.GSON_TOTAL.fromJson(contentsTotal, JsonDataDonation.class);

            //dont judge me
            json.totalDonated = jsonTotal.totalDonated;
            
            TickerDonation.callbackDonations(json);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private HttpURLConnection getAuthorizedConnection(String method, String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", "Tropicraft 1.0 (tropicraft.net)");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + ConfigLT.TILTIFY.appToken.get());
        return con;
    }
    
    private String readInput(InputStream is, boolean newlines) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
            if (newlines) {
                content.append("\n");
            }
        }
        in.close();
        return content.toString();
    }

    public String getData_Real() {
        try {
            String uri;
            synchronized (donationData) {
                uri = "https://tiltify.com/api/v3/campaigns/" + ConfigLT.TILTIFY.campaignId.get() + "/donations?count=100" + (donationData.getLastSeenId() == 0 ? "" : "&after=" + donationData.getLastSeenId());
            }
            
            HttpURLConnection con = getAuthorizedConnection("GET", uri);
            try {
                return readInput(con.getInputStream(), false);
            } catch (IOException ex) {
                LogManager.getLogger().error(readInput(con.getErrorStream(), true));
            } finally {
                con.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "ERROR";
    }

    public String getData_TotalDonations() {
        try {
            String uri = "https://tiltify.com/api/v3/campaigns/" + ConfigLT.TILTIFY.campaignId.get();
            HttpURLConnection con = getAuthorizedConnection("GET", uri);
            try {
                return readInput(con.getInputStream(), false);
            } catch (IOException ex) {
                LogManager.getLogger().error(readInput(con.getErrorStream(), true));
            } finally {
                con.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "ERROR";
    }
}
