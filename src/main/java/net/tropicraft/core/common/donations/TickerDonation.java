package net.tropicraft.core.common.donations;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.tropicraft.core.common.block.tileentity.TileEntityDonation;
import net.tropicraft.core.common.config.TropicsConfigs;

public class TickerDonation {

    public static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(JsonDataDonation.class, new JsonDeserializerDonation()).create();
    public static final Gson GSON_TOTAL = (new GsonBuilder()).registerTypeAdapter(JsonDataDonation.class, new JsonDeserializerDonationTotal()).create();
    
    private static final Set<TileEntityDonation> callbacks = new HashSet<>();
    
    private static DonationData donationData;

    public static void tick(World world) {

        if (!ThreadWorkerDonations.getInstance().running 
                && !TropicsConfigs.tiltifyAppToken.isEmpty() && TropicsConfigs.tiltifyCampaign != 0 
                && FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getCurrentPlayerCount() > 0) {
            donationData = getSavedData(world);
            ThreadWorkerDonations.getInstance().startThread(donationData);
        }

    }

    public static void callbackDonations(JsonDataDonation data) {
        //make sure server instance didnt end while running thread work
        if (FMLCommonHandler.instance().getMinecraftServerInstance() != null) {
            FMLCommonHandler.instance().getMinecraftServerInstance()
                    .addScheduledTask(() -> processDonationsServer(data));
        } else {
            ThreadWorkerDonations.getInstance().stopThread();
            callbacks.clear();
        }
    }
    
    private static DonationData getSavedData(World world) {
        DonationData data = (DonationData) world.getMapStorage().getOrLoadData(DonationData.class, "donationData");
        if (data == null) {
            data = new DonationData("donationData");
            world.getMapStorage().setData("donationData", data);
        }
        return data;
    }

    /** called once thread checked for new data, and made sure server is still running **/
    public static void processDonationsServer(JsonDataDonation data) {

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World world = DimensionManager.getWorld(0);

        if (world == null) return;

        data.new_donations.stream()
                .sorted(Comparator.comparingLong(JsonDataDonationEntry::getDate))
                .filter(entry -> entry.getDate() > donationData.getLastSeenDate())
                .map(donation -> new TextComponentTranslation("tropicraft.donations.donation", TextFormatting.AQUA + donation.name + TextFormatting.RESET.toString(), TextFormatting.GREEN.toString() + NumberFormat.getCurrencyInstance(Locale.US).format(donation.amount) + TextFormatting.RESET))
                .forEach(msg -> {
                    server.getPlayerList().getPlayers().stream()
                            .forEach(p -> p.sendMessage(msg));

                    callbacks.forEach(TileEntityDonation::triggerDonation); 
                });

        long lastSeenDate = data.new_donations.stream()
                .mapToLong(d -> d.getDate())
                .max()
                .orElse(0);

        int lastSeenId = data.new_donations.stream()
                .mapToInt(d -> d.id)
                .max()
                .orElse(0);
        
        synchronized (donationData) {

            donationData.setLastSeenDate(Math.max(donationData.getLastSeenDate(), lastSeenDate));
            donationData.setLastSeenId(Math.max(donationData.getLastSeenId(), lastSeenId));

            int amountPerMonument = TropicsConfigs.donationAmountPerMonument;
            if (amountPerMonument > 0) {
                while (donationData.getMonumentsPlaced() < data.totalDonated / amountPerMonument) {
                    donationData.setMonumentsPlaced(donationData.getMonumentsPlaced() + 1);
                    server.getCommandManager().executeCommand(new CommandUser(world), TropicsConfigs.tiltifyCommandRun);
                }
            }
        }
    }

    public static void simulateDonation(String name, int amount) {

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World world = DimensionManager.getWorld(0);

        if (world == null) return;

        if (!name.equals("")) {
            server.getPlayerList().getPlayers().stream()
                    .forEach(p -> p.sendMessage(new TextComponentTranslation("tropicraft.donations.donation", TextFormatting.AQUA + name + TextFormatting.RESET.toString(), TextFormatting.GREEN.toString() + NumberFormat.getCurrencyInstance(Locale.US).format(amount) + TextFormatting.RESET)));
        }

        callbacks.forEach(TileEntityDonation::triggerDonation);
    }

    public static void addCallback(TileEntityDonation tile) {
        callbacks.add(tile);
    }
    
    public static void removeCallback(TileEntityDonation tile) {
        callbacks.remove(tile);
    }

}
