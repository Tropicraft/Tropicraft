package net.tropicraft.core.common.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.tropicraft.core.common.build.BuildServerTicks;
import net.tropicraft.core.common.build.world.Build;
import net.tropicraft.core.common.build.world.BuildJob;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.capability.WorldDataInstance;
import net.tropicraft.core.common.dimension.WorldProviderTropicraft;
import net.tropicraft.core.common.worldgen.village.TownKoaVillage;
import net.tropicraft.core.common.worldgen.village.TownKoaVillageGenHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandTropicsVillage extends CommandBase {

    @Override
    public String getName() {
        return "tc_misc";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender,
                        String[] args) throws CommandException {
        EntityPlayerMP player = this.getCommandSenderAsPlayer(commandSender);

        if (args.length > 0) {
            if (args[0].equals("village_new")) {
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
                    //wd.addTickingLocation(village);

                    storage.addTickingLocation(village);
                }
            } else if (args[0].equals("village_try")) {

                int x = MathHelper.floor(player.posX);
				int z = MathHelper.floor(player.posZ);
				int y = player.world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
				if (y < WorldProviderTropicraft.MID_HEIGHT) y = WorldProviderTropicraft.MID_HEIGHT+1;
				boolean result = TownKoaVillageGenHelper.hookTryGenVillage(new BlockPos(x, y, z), player.world);
                if (!result) {
                    System.out.println("failed to gen village");
                }
            } else if (args[0].equals("schematic_save")) {
                try {
                    String name = args[1];
                    //Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
                    Vec3d vec = commandSender.getPositionVector();
                    int sx = MathHelper.floor(parseCoordinate(vec.xCoord, args[2], false).getResult());//Integer.parseInt(args[2]);
                    int sy = MathHelper.floor(parseCoordinate(vec.yCoord, args[3], false).getResult());//Integer.parseInt(args[3]);
                    int sz = MathHelper.floor(parseCoordinate(vec.zCoord, args[4], false).getResult());//Integer.parseInt(args[4]);
                    int ex = MathHelper.floor(parseCoordinate(vec.xCoord, args[5], false).getResult());//Integer.parseInt(args[5]);
                    int ey = MathHelper.floor(parseCoordinate(vec.yCoord, args[6], false).getResult());//Integer.parseInt(args[6]);
                    int ez = MathHelper.floor(parseCoordinate(vec.zCoord, args[7], false).getResult());//Integer.parseInt(args[7]);
                    Build clipboardData = new Build(0, 0, 0, name, true);
                    clipboardData.newFormat = true;
                    clipboardData.recalculateLevelSize(sx, sy, sz, ex, ey, ez, true);
                    clipboardData.scanLevelToData(player.world);
                    clipboardData.writeNBT();
                    commandSender.sendMessage(new TextComponentString("schematic saved to " + name + ".schematic"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    commandSender.sendMessage(new TextComponentString("command usage: tc_village schematic_save <filename> <start coords> <end coords>"));
                    commandSender.sendMessage(new TextComponentString("eg: tc_village schematic_save myfile 0 0 0 5 5 5"));
                    commandSender.sendMessage(new TextComponentString("start and end coords are inclusive"));
                }
            } else if (args[0].equals("schematic_print")) {
                try {
                    Vec3d vec = commandSender.getPositionVector();
                    String name = args[1];
                    CoordinateArg sx = parseCoordinate(vec.xCoord, args[2], false);
                    CoordinateArg sy = parseCoordinate(vec.yCoord, args[3], false);
                    CoordinateArg sz = parseCoordinate(vec.zCoord, args[4], false);
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

                    commandSender.sendMessage(new TextComponentString("printing schematic"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    commandSender.sendMessage(new TextComponentString("command usage: tc_village schematic_print <filename> <start coords>"));
                    commandSender.sendMessage(new TextComponentString("eg: tc_village schematic_print myfile 5 5 5"));
                }
            } else if (args[0].equals("entities")) {
                HashMap<String, Integer> lookupCounts = new HashMap<>();

                for (Entity ent : player.world.loadedEntityList) {
                    if (ent instanceof EntityLivingBase) {
                        int count = 0;
                        if (lookupCounts.containsKey(EntityList.CLASS_TO_NAME.get(ent.getClass()))) {
                            count = lookupCounts.get(EntityList.CLASS_TO_NAME.get(ent.getClass()));
                        }
                        lookupCounts.put(EntityList.CLASS_TO_NAME.get(ent.getClass()), count + 1);
                    }
                }

                player.sendMessage(new TextComponentString("Entity counts: "));

                int count = 0;

                for (Map.Entry<String, Integer> entry : lookupCounts.entrySet()) {
                    String name = entry.getKey();
                    player.sendMessage(new TextComponentString(name + ": " + entry.getValue()));
                    count += entry.getValue();
                }

                player.sendMessage(new TextComponentString("total: " + count));
            } else if (args[0].equals("mount")) {
                float clDist = 99999;
                Entity clEntity = null;
                String name = args[1];
                boolean reverse = false;
                boolean playerMode = false;
                Class clazz = EntityList.NAME_TO_CLASS.get(name);
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
                    List<Entity> listEnts = player.world.getEntitiesWithinAABB(clazz, player.getEntityBoundingBox().expand(15, 15, 15));
                    for (Entity ent : listEnts) {
                        float dist = player.getDistanceToEntity(ent);
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
                } else {

                }
                if (clEntity != null) {
                    if (reverse) {
                        clEntity.startRiding(player);
                    } else {
                        player.startRiding(clEntity);
                    }

                }

            }
        }
    }

}
