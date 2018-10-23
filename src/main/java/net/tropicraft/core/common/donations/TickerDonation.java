package net.tropicraft.core.common.donations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Comparator;
import java.util.Optional;

public class TickerDonation {

    public static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(JsonDataDonation.class, new JsonDeserializerDonation()).create();

    public static void tick(World world) {

        if (!ThreadWorkerDonations.getInstance().running) {
            ThreadWorkerDonations.getInstance().startThread();
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
        }
    }

    /** called once thread checked for new data, and made sure server is still running **/
    public static void processDonationsServer(JsonDataDonation data) {

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World world = DimensionManager.getWorld(0);

        if (world == null) return;

        //sort and filter list
        DonationData donationData = (DonationData)world.getMapStorage().getOrLoadData(DonationData.class, "donationData");
        if (donationData == null) {
            donationData = new DonationData("donationData");
        }
        //int lastIDReported = donationData.lastIDReported;

        int highestID = -1;


        DonationData finalDonationData = donationData;

        //System.out.println("max pre: " + finalDonationData.lastDateReported);
        data.new_donations.stream()
                .filter(entry -> entry.getDate() > finalDonationData.lastDateReported)
                .map(donation -> TextFormatting.GREEN.toString() + donation.name + TextFormatting.RESET.toString() + " donated " + TextFormatting.RED.toString() + donation.amount + "!!!")
                .map(TextComponentString::new)
                .forEach(msg -> server.getPlayerList().getPlayers().stream()
                .forEach(p -> p.sendMessage(msg)));
                /*.forEach(entryDonation -> server.getPlayerList().getPlayers().stream()
                        .forEach(player -> { player.sendMessage(
                                new TextComponentString(TextFormatting.GREEN.toString() + entryDonation.name + TextFormatting.RESET.toString() + " donated " + TextFormatting.RED.toString() + entryDonation.amount + "!!!")
                        );
                        //if (entryDonation.id > highestID) { highestID = entryDonation.id; }
                        } )*/

        //);

        //Optional<JsonDataDonationEntryOld> max = data.new_donations.stream().max(Comparator.comparingInt(JsonDataDonationEntryOld::getId));
        Optional<JsonDataDonationEntry> max = data.new_donations.stream().max(Comparator.comparingLong(JsonDataDonationEntry::getDate));

        System.out.println("max: " + max.get().getDate());

        donationData.lastDateReported = max.get().getDate();

        //donationData.lastDateReported = -1;

        world.setData("donationData", donationData);

        /*for (JsonDataDonationEntryOld entry : data.new_donations) {
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                //player.sendMessage(new TextComponentString(TextFormatting.GREEN.toString() + entry.name + TextFormatting.RESET.toString() + " donated " + TextFormatting.RED.toString() + entry.amount + "!!!"));
            }
            //FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessageToTeamOrAllPlayers(null, new TextComponentString(entry.name + " donated " + entry.amount));
        }*/
    }

}
