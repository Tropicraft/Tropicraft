package net.tropicraft.core.common.donations;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
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
            URL url = new URL("http://share.coros.us/files/donation_check.json");
            InputStream is = url.openStream();

            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, StandardCharsets.UTF_8);
            String contents = writer.toString();

            JsonDataDonationOld json = TickerDonation.GSON.fromJson(contents, JsonDataDonationOld.class);

            TickerDonation.callbackDonations(json);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
