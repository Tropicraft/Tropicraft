package net.tropicraft.core.common.donations;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;

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
    
    private static final Set<TileEntityDonation> callbacks = new HashSet<>();
    
    private static DonationData donationData;

    public static void tick(World world) {

        if (!ThreadWorkerDonations.getInstance().running 
                && !TropicsConfigs.tiltifyAppToken.isEmpty() && TropicsConfigs.tiltifyCampaign != 0 
                && FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getCurrentPlayerCount() > 0) {
            donationData = getSavedData(world);
            ThreadWorkerDonations.getInstance().startThread(donationData);
        }

        /*long time = world.getTotalWorldTime();

        if (time % (20 * 5) == 0) {
            //check for new data
            String name = "";
            int amount = 0;

            try {
                String contents = FileUtils.readFileToString(new File("/mnt/ntfs_data/dev/windows/donation_check.json"), StandardCharsets.UTF_8);

                JsonDataDonationOld json = GSON.fromJson(contents, JsonDataDonationOld.class);

                System.out.println(json);

                for (JsonDataDonationEntryOld entry : json.new_donations) {
                    for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                        player.sendMessage(new TextComponentString(TextFormatting.GREEN.toString() + entry.name + TextFormatting.RESET.toString() + " donated " + TextFormatting.RED.toString() + entry.amount + "!!!"));
                    }
                    //FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessageToTeamOrAllPlayers(null, new TextComponentString(entry.name + " donated " + entry.amount));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }*/

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

        //sort and filter list
       //int lastIDReported = donationData.lastIDReported;

        //System.out.println("max pre: " + finalDonationData.lastDateReported);
        data.new_donations.stream()
                .sorted(Comparator.comparingLong(JsonDataDonationEntry::getDate))
                .map(donation -> new TextComponentTranslation("tropicraft.donations.donation", TextFormatting.AQUA + donation.name + TextFormatting.RESET.toString(), TextFormatting.GREEN.toString() + NumberFormat.getCurrencyInstance(Locale.US).format(donation.amount) + TextFormatting.RESET))
                .forEach(msg -> {
                    server.getPlayerList().getPlayers().stream()
                            .forEach(p -> p.sendMessage(msg));

                    callbacks.forEach(TileEntityDonation::triggerDonation); 
                });

                /*.forEach(entryDonation -> server.getPlayerList().getPlayers().stream()
                        .forEach(player -> { player.sendMessage(
                                new TextComponentString(TextFormatting.GREEN.toString() + entryDonation.name + TextFormatting.RESET.toString() + " donated " + TextFormatting.RED.toString() + entryDonation.amount + "!!!")
                        );
                        //if (entryDonation.id > highestID) { highestID = entryDonation.id; }
                        } )*/

        //);

        if (data.new_donations.size() > 0) {
            if (!TropicsConfigs.tiltifyCommandRun.equals("")) {
                server.getCommandManager().executeCommand(new CommandUser(world), TropicsConfigs.tiltifyCommandRun);
            }
        }

        //Optional<JsonDataDonationEntryOld> max = data.new_donations.stream().max(Comparator.comparingInt(JsonDataDonationEntryOld::getId));
        int lastSeenId = data.new_donations.stream()
                .mapToInt(d -> d.id)
                .max()
                .orElse(0);
        
        synchronized (donationData) {
            donationData.setLastSeenId(Math.max(donationData.getLastSeenId(), lastSeenId));
        }

        /*for (JsonDataDonationEntryOld entry : data.new_donations) {
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                //player.sendMessage(new TextComponentString(TextFormatting.GREEN.toString() + entry.name + TextFormatting.RESET.toString() + " donated " + TextFormatting.RED.toString() + entry.amount + "!!!"));
            }
            //FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessageToTeamOrAllPlayers(null, new TextComponentString(entry.name + " donated " + entry.amount));
        }*/
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

        if (!TropicsConfigs.tiltifyCommandRun.equals("")) server.getCommandManager().executeCommand(new CommandUser(world), TropicsConfigs.tiltifyCommandRun);
        //server.getCommandManager().executeCommand(new CommandUser(world), "scoreboard players set @r mTrack 1");
    }

    public static void addCallback(TileEntityDonation tile) {
        callbacks.add(tile);
    }
    
    public static void removeCallback(TileEntityDonation tile) {
        callbacks.remove(tile);
    }

}
