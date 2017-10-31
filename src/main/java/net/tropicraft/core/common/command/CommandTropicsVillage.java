package net.tropicraft.core.common.command;

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

public class CommandTropicsVillage extends CommandBase {

    @Override
    public String getName() {
        return "tc_village";
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
            }

                /* else if (args[0].equals("village_clear")) {
				WorldDirector wd = WorldDirectorManager.instance().getCoroUtilWorldDirector(player.world);
				
				for (Entry<Integer, ISimulationTickable> entry : wd.lookupTickingManagedLocations.entrySet()) {
					entry.getValue().cleanup();
					wd.removeTickingLocation(entry.getValue());
				}
			} else if (args[0].equals("village_regen")) {
				WorldDirector wd = WorldDirectorManager.instance().getCoroUtilWorldDirector(player.world);
				Iterator it = wd.lookupTickingManagedLocations.values().iterator();
				while (it.hasNext()) {
					ManagedLocation ml = (ManagedLocation) it.next();
					ml.initFirstTime();
				}
			} else if (args[0].equals("village_repopulate")) {
				WorldDirector wd = WorldDirectorManager.instance().getCoroUtilWorldDirector(player.world);
				Iterator it = wd.lookupTickingManagedLocations.values().iterator();
				while (it.hasNext()) {
					
					ManagedLocation ml = (ManagedLocation) it.next();
					if (ml instanceof TownKoaVillage) {
						//((TownKoaVillage) ml).spawnEntities();
					}
				}
			}*/
        }
    }

}
