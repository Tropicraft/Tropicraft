package net.tropicraft.core.common.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.build.BuildServerTicks;
import net.tropicraft.core.common.build.world.Build;
import net.tropicraft.core.common.build.world.BuildJob;
import net.tropicraft.core.common.capability.WorldDataInstance;
import net.tropicraft.core.common.config.TropicsConfigs;
import net.tropicraft.core.common.dimension.WorldProviderTropicraft;
import net.tropicraft.core.common.donations.DonationData;
import net.tropicraft.core.common.donations.ThreadWorkerDonations;
import net.tropicraft.core.common.donations.TickerDonation;
import net.tropicraft.core.common.worldgen.village.TownKoaVillage;
import net.tropicraft.core.common.worldgen.village.TownKoaVillageGenHelper;

public class CommandTropicsMisc extends CommandBase {
    
    private enum SubCommand {
        
        VILLAGE_COORD((player, args) -> {
            int x = 8452;
            int z = 5921;

            int relX = x - player.getPosition().getX();
            int relZ = z - player.getPosition().getZ();

            //x and z swapped on purpose
            System.out.println("pos: " + relZ + ", " + 0 + ", " + relX);
        }),
        
        VILLAGE_NEW((player, args) -> {
            int x = MathHelper.floor(player.posX);
            int z = MathHelper.floor(player.posZ);
            int y = player.world.getHeight(x, z);

            if (y < WorldProviderTropicraft.MID_HEIGHT) y = WorldProviderTropicraft.MID_HEIGHT + 1;

            WorldDataInstance storage = player.world.getCapability(Tropicraft.WORLD_DATA_INSTANCE, null);
            if (storage != null) {
                TownKoaVillage village = new TownKoaVillage();

                int newID = storage.getAndIncrementKoaIDVillage();
                village.initData(newID, player.world.provider.getDimension(), new BlockPos(x, y, z));

                //TEMP!?
                village.direction = 0;

                village.initFirstTime();
                village.genStructure();
                //wd.addTickingLocation(village);

                storage.addTickingLocation(village);
            }
        }),
        
        VILLAGE_TRY((player, args) -> {
            int x = MathHelper.floor(player.posX);
            int z = MathHelper.floor(player.posZ);
            int y = player.world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
            if (y < WorldProviderTropicraft.MID_HEIGHT) y = WorldProviderTropicraft.MID_HEIGHT+1;
            boolean result = TownKoaVillageGenHelper.hookTryGenVillage(new BlockPos(x, y, z), player.world);
            if (!result) {
                System.out.println("failed to gen village");
            }
        }),
        
        VILLAGE_CUSTOM((player, args) -> {
            int x = MathHelper.floor(player.posX);
            int z = MathHelper.floor(player.posZ);
            int y = MathHelper.floor(player.posY);

            WorldDataInstance storage = player.world.getCapability(Tropicraft.WORLD_DATA_INSTANCE, null);

            if (storage != null) {
                TownKoaVillage village = new TownKoaVillage();

                int newID = storage.getAndIncrementKoaIDVillage();
                village.initData(newID, player.world.provider.getDimension(), new BlockPos(x, y, z));

                //TEMP!?
                village.direction = 0;

                //custom village changes
                village.isCustomVillage = true;

                //no koa regen
                village.minEntitiesToKeepAlive = 2;

                village.initFirstTime();

                //force new spawn coords for the 2 koa
                village.generateSpawnCoords();

                //NO GEN FOR CUSTOM!
                //village.genStructure();


                player.sendMessage(new TextComponentString("spawned a new custom village"));

                storage.addTickingLocation(village);
            } else {
                player.sendMessage(new TextComponentString("couldnt spawn a new custom village"));
            }
        }),
        
        SCHEMATIC_SAVE((player, args) -> {
            try {
                String name = args[1];
                //Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
                Vec3d vec = player.getPositionVector();
                int sx = MathHelper.floor(parseCoordinate(vec.x, args[2], false).getResult());//Integer.parseInt(args[2]);
                int sy = MathHelper.floor(parseCoordinate(vec.y, args[3], false).getResult());//Integer.parseInt(args[3]);
                int sz = MathHelper.floor(parseCoordinate(vec.z, args[4], false).getResult());//Integer.parseInt(args[4]);
                int ex = MathHelper.floor(parseCoordinate(vec.x, args[5], false).getResult());//Integer.parseInt(args[5]);
                int ey = MathHelper.floor(parseCoordinate(vec.y, args[6], false).getResult());//Integer.parseInt(args[6]);
                int ez = MathHelper.floor(parseCoordinate(vec.z, args[7], false).getResult());//Integer.parseInt(args[7]);
                Build clipboardData = new Build(0, 0, 0, name, true);
                clipboardData.newFormat = true;
                clipboardData.recalculateLevelSize(sx, sy, sz, ex, ey, ez, true);
                clipboardData.scanLevelToData(player.world);
                clipboardData.writeNBT();
                player.sendMessage(new TextComponentString("schematic saved to " + name + ".schematic"));
            } catch (Exception ex) {
                ex.printStackTrace();
                player.sendMessage(new TextComponentString("command usage: tc_village schematic_save <filename> <start coords> <end coords>"));
                player.sendMessage(new TextComponentString("eg: tc_village schematic_save myfile 0 0 0 5 5 5"));
                player.sendMessage(new TextComponentString("start and end coords are inclusive"));
            }
        }),
        
        SCHEMATIC_PRINT((player, args) -> {
            try {
                Vec3d vec = player.getPositionVector();
                String name = args[1];
                CoordinateArg sx = parseCoordinate(vec.x, args[2], false);
                CoordinateArg sy = parseCoordinate(vec.y, args[3], false);
                CoordinateArg sz = parseCoordinate(vec.z, args[4], false);
                int x = MathHelper.floor(sx.getResult());
                int y = MathHelper.floor(sy.getResult());
                int z = MathHelper.floor(sz.getResult());
                int direction = 0;
                if (args.length > 5) {
                    direction = Integer.parseInt(args[5]);
                }
                Build buildData = new Build(x, y, z, name, false);
                BuildJob bj = new BuildJob(-99, x, y, z, buildData);
                bj.build.dim = player.world.provider.getDimension();
                bj.useFirstPass = false; //skip air setting pass
                bj.useRotationBuild = true;
                bj.build_rate = 10000;
                bj.notifyFlag = 2;
                bj.setDirection(direction);
                //bj.customGenCallback = this;
                //bj.blockIDsNoBuildOver.add(HostileWorlds.blockSourceStructure);

                BuildServerTicks.buildMan.addBuild(bj);

                player.sendMessage(new TextComponentString("printing schematic"));
            } catch (Exception ex) {
                ex.printStackTrace();
                player.sendMessage(new TextComponentString("command usage: tc_village schematic_print <filename> <start coords>"));
                player.sendMessage(new TextComponentString("eg: tc_village schematic_print myfile 5 5 5"));
            }
        }),
        
        ENTITIES((player, args) -> {
            HashMap<ResourceLocation, Integer> lookupCounts = new HashMap<>();

            for (Entity ent : player.world.loadedEntityList) {
                if (ent instanceof EntityLivingBase) {
                    ResourceLocation key = EntityList.getKey(ent.getClass());
                    lookupCounts.merge(key, 1, (a, b) -> a + b);
                }
            }

            player.sendMessage(new TextComponentString("Entity counts: "));

            int count = 0;

            for (Map.Entry<ResourceLocation, Integer> entry : lookupCounts.entrySet()) {
                ResourceLocation name = entry.getKey();
                player.sendMessage(new TextComponentString(name + ": " + entry.getValue()));
                count += entry.getValue();
            }

            player.sendMessage(new TextComponentString("total: " + count));
        }),
        
        MOUNT((player, args) -> {

            float clDist = 99999;
            Entity clEntity = null;
            if (args.length < 2) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + "/tc_misc mount <entity id or player name> [reverse]"));
                return;
            }
            String name = args[1];
            boolean reverse = false;
            boolean playerMode = false;
            Class<? extends Entity> clazz = EntityList.getClass(new ResourceLocation(name));
            if (clazz == null) {
                clazz = EntityPlayer.class;
                playerMode = true;
            }
            if (args.length > 2) {
                reverse = args[2].equals("reverse");
                //no greifing
                if (reverse) {
                    playerMode = false;
                }
            }
            if (clazz != null) {
                List<Entity> listEnts = player.world.getEntitiesWithinAABB(clazz, player.getEntityBoundingBox().grow(15, 15, 15));
                for (Entity ent : listEnts) {
                    float dist = player.getDistance(ent);
                    if (dist < clDist) {
                        if (!playerMode) {
                            clDist = dist;
                            clEntity = ent;
                        } else {
                            if (player.getName().equals(name)) {
                                clEntity = ent;
                                break;
                            }
                        }
                    }
                }
            }

            if (clEntity != null) {
                if (reverse) {
                    clEntity.startRiding(player);
                } else {
                    player.startRiding(clEntity);
                }
            }
        }),

        DONATION((player, args) -> {
            try {
                String content = ThreadWorkerDonations.getInstance().getData_Real();

                System.out.println(content);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }),

        //reset the date, will reprocess all donations
        DONATION_RESET((player, args) -> {
            DonationData donationData = (DonationData)player.world.getMapStorage().getOrLoadData(DonationData.class, "donationData");
            if (donationData != null) {
                synchronized(donationData) {
                    donationData.resetData();
                }
                player.sendMessage(new TextComponentString("Resetting donation data"));
            }
        }),

        DONATION_SETID((player, args) -> {
            DonationData donationData = (DonationData)player.world.getMapStorage().getOrLoadData(DonationData.class, "donationData");
            if (donationData != null) {
                int id = Integer.valueOf(args[1]);
                synchronized (donationData) {
                    donationData.setLastSeenId(id);
                }
                player.sendMessage(new TextComponentString("Reset last seen donation ID to " + id));
            }
        }),

        DONATION_SIM((player, args) -> {
            String name = String.valueOf(args[1]);
            int amount = Integer.valueOf(args[2]);
            player.sendMessage(new TextComponentString("Simulating donation for name " + name + " and amount " + amount));
            TickerDonation.simulateDonation(name, amount);
        }),

        DONATION_SIM_FIREWORKS((player, args) -> {
            TickerDonation.simulateDonation("", 0);
        }),

        DONATION_SETUP((player, args) -> {
            int id = Integer.valueOf(args[1]);
            String token = String.valueOf(args[2]);
            player.sendMessage(new TextComponentString("Setting campaign id and token"));
            TropicsConfigs.tiltifyCampaign = id;
            TropicsConfigs.tiltifyAppToken = token;
            TropicsConfigs.getConfig().get(TropicsConfigs.C_DONATIONS, "tiltifyCampaign", TropicsConfigs.tiltifyCampaign).set(id);
            TropicsConfigs.getConfig().get(TropicsConfigs.C_DONATIONS, "tiltifyAppToken", TropicsConfigs.tiltifyAppToken).set(token);
            TropicsConfigs.getConfig().save();
        }),

        DONATION_RATE((player, args) -> {
            int rate = Integer.valueOf(args[1]);
            player.sendMessage(new TextComponentString("Setting campaign query rate"));
            TropicsConfigs.donationTrackerRefreshRate = rate;
            TropicsConfigs.getConfig().get(TropicsConfigs.C_DONATIONS, "donationTrackerRefreshRate", TropicsConfigs.donationTrackerRefreshRate).set(rate);
            TropicsConfigs.getConfig().save();
        }),
        
        ;
        
        private final BiConsumer<EntityPlayer, String[]> function;
        
        private SubCommand(BiConsumer<EntityPlayer, String[]> function) {
            this.function = function;
        }
    }

    @Override
    public String getName() {
        return "tc_misc";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return "";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender,
                        String[] args) throws CommandException {
        EntityPlayerMP player = this.getCommandSenderAsPlayer(commandSender);

        if (args.length > 0) {
            String subcmd = args[0].toUpperCase(Locale.ROOT);
            try {
                SubCommand cmd = SubCommand.valueOf(subcmd);
                cmd.function.accept(player, args);
            } catch (IllegalArgumentException e) {
                throw new CommandException("Invalid subcommand");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, Arrays.stream(SubCommand.values()).map(Enum::name).map(s -> s.toLowerCase(Locale.ROOT)).toArray(String[]::new));
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
