package CoroUtil.util;

import CoroUtil.forge.CULog;
import CoroUtil.forge.CoroUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CoroUtilCompatibility {

    private static boolean tanInstalled = false;
    private static boolean checkTAN = true;

    private static boolean sereneSeasonsInstalled = false;
    private static boolean checksereneSeasons = true;

    private static boolean lycanitesMobsInstalled = false;
    private static boolean checkLycanitesMobs = true;

    private static Class class_TAN_ASMHelper = null;
    private static Method method_TAN_getFloatTemperature = null;

    private static Class class_SereneSeasons_ASMHelper = null;
    private static Method method_sereneSeasons_getFloatTemperature = null;

    /**
     * flying lycanites mob support
     * - we should also account for LOS, we cant just blind set end node for flyers
     * -- if we do this we could add support for other flyers, ghasts etc
     * - how do we know if the mob isnt already pathing for LYC MOBS?
     * -- if !directNavigator.atTargetPosition()
     */
    private static boolean enableFlyingSupport = false;
    private static Class class_LycanitesMobs_Entity = null;
    private static Class class_LycanitesMobs_DirectNavigator = null;
    private static Field field_LycanitesMobs_directNavigator = null;
    private static Method method_LycanitesMobs_useDirectNavigator = null;
    private static Method method_LycanitesMobs_setTargetPosition = null;

    public static boolean shouldSnowAt(World world, BlockPos pos) {
        /**
         * ISeasonData data = SeasonHelper.getSeasonData(world);
         * boolean canSnow = SeasonASMHelper.canSnowAtInSeason(world, pos, false, data.getSeason());
         *
         * or:
         *
         * float temp = SeasonASMHelper.getFloatTemperature(world, pos);
         * boolean canSnow = temp <= 0;
         */
        return false;
    }

    public static float getAdjustedTemperature(World world, Biome biome, BlockPos pos) {

        //TODO: consider caching results in a blockpos,float hashmap for a second or 2
        if (isTANInstalled()) {
            try {
                if (method_TAN_getFloatTemperature == null) {
                    method_TAN_getFloatTemperature = class_TAN_ASMHelper.getDeclaredMethod("getFloatTemperature", Biome.class, BlockPos.class);
                }
                return (float) method_TAN_getFloatTemperature.invoke(null, biome, pos);
            } catch (Exception ex) {
                ex.printStackTrace();
                //prevent error spam
                tanInstalled = false;
                return biome.getTemperature(pos);
            }
        } else if (isSereneSeasonsInstalled()) {
            try {
                if (method_sereneSeasons_getFloatTemperature == null) {
                    method_sereneSeasons_getFloatTemperature = class_SereneSeasons_ASMHelper.getDeclaredMethod("getFloatTemperature", World.class, Biome.class, BlockPos.class);
                }
                return (float) method_sereneSeasons_getFloatTemperature.invoke(null, world, biome, pos);
            } catch (Exception ex) {
                ex.printStackTrace();
                //prevent error spam
                sereneSeasonsInstalled = false;
                return biome.getTemperature(pos);
            }
        } else {
            return biome.getTemperature(pos);
        }
    }

    /**
     * Check if tough as nails is installed
     *
     * @return
     */
    public static boolean isTANInstalled() {
        if (checkTAN) {
            try {
                checkTAN = false;
                class_TAN_ASMHelper = Class.forName("toughasnails.season.SeasonASMHelper");
                if (class_TAN_ASMHelper != null) {
                    tanInstalled = true;
                }
            } catch (Exception ex) {
                //not installed
                //ex.printStackTrace();
            }

            CULog.log("CoroUtil detected Tough As Nails Seasons " + (tanInstalled ? "Installed" : "Not Installed") + " for use");
        }

        return tanInstalled;
    }

    /**
     * Check if Serene Seasons is installed
     *
     * @return
     */
    public static boolean isSereneSeasonsInstalled() {
        if (checksereneSeasons) {
            try {
                checksereneSeasons = false;
                class_SereneSeasons_ASMHelper = Class.forName("sereneseasons.api.season.BiomeHooks");
                if (class_SereneSeasons_ASMHelper != null) {
                    sereneSeasonsInstalled = true;
                }
            } catch (Exception ex) {
                //not installed
                //ex.printStackTrace();
            }

            CULog.log("CoroUtil detected Serene Seasons " + (sereneSeasonsInstalled ? "Installed" : "Not Installed") + " for use");
        }

        return sereneSeasonsInstalled;
    }

    /**
     * Check if Serene Seasons is installed
     *
     * @return
     */
    public static boolean isLycanitesMobsInstalled() {
        if (checkLycanitesMobs) {
            try {
                checkLycanitesMobs = false;
                class_LycanitesMobs_Entity = Class.forName("com.lycanitesmobs.core.entity.EntityCreatureBase");
                if (class_LycanitesMobs_Entity != null) {
                    lycanitesMobsInstalled = true;
                }
            } catch (Exception ex) {
                //not installed
                //ex.printStackTrace();
            }

            CULog.log("CoroUtil detected Lycanites Mobs " + (lycanitesMobsInstalled ? "Installed" : "Not Installed") + " for use");
        }

        return lycanitesMobsInstalled;
    }

    /**
     * power in / out with simulates:
     * EIO:
     * - ILegacyPowerReceiver extends ILegacyPoweredTile
     * -- int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate);
     *
     * CoFH:
     * - IEnergyStorage
     * -- int receiveEnergy(int maxReceive, boolean simulate);
     * -- int extractEnergy(int maxExtract, boolean simulate);
     *
     * storage:
     *
     * Forge:
     * net.minecraftforge.energy.IEnergyStorage
     * - the cap, also uses that
     * EIO uses it, cofh too:
     * - https://github.com/CoFH/ThermalExpansion/blob/5340e2df6e328a1cc140a8b94692c8d09c5f06a7/src/main/java/cofh/thermalexpansion/block/TilePowered.java#L194
     *
     *
     * EIO:
     * - crazypants.enderio.base.power.ILegacyPoweredTile
     * -- int getEnergyStored();
     * - crazypants.enderio.base.machine.gui.IPowerBarData
     * -- int getEnergyStored();
     *
     * CoFH:
     * - cofh.redstoneflux.api.IEnergyStorage
     * -- int getEnergyStored();
     *
     *
     * Best idea: track relative power storage changes
     * - while EIO and CoFH/RF has a simulate boolean for extractEnergy (CoFH)
     *
     */

    public static int lastPowerVal = -1;

    public static void testPowerInfo(PlayerEntity player, BlockPos pos) {
        TileEntity tEnt = player.world.getTileEntity(pos);
        if (tEnt != null) {

            int value = -1;

            //TODO: 1.14 uncomment
            IEnergyStorage cap = null;//tEnt.getCapability(CapabilityEnergy.ENERGY, null);

            if (cap != null) {
                value = cap.getEnergyStored();
                player.sendMessage(new StringTextComponent("cap power stored: " + value));
                if (lastPowerVal != -1) {
                    player.sendMessage(new StringTextComponent("relative power change: " + (value - lastPowerVal)));
                }
                lastPowerVal = value;
            } else {
                try {
                    boolean success = true;
                    Class classTry = Class.forName("crazypants.enderio.base.power.ILegacyPoweredTile");
                    if (classTry == null) {
                        player.sendMessage(new StringTextComponent("EIO class not found, trying cofh"));
                        classTry = Class.forName("cofh.redstoneflux.api.IEnergyStorage");
                        if (classTry == null) {
                            player.sendMessage(new StringTextComponent("cofh class not found"));
                        }
                    }

                    if (classTry != null) {
                        Method method = classTry.getDeclaredMethod("getEnergyStored");
                        if (method != null) {
                            value = (int) method.invoke(tEnt);
                        } else {
                            player.sendMessage(new StringTextComponent("method not found"));
                            success = false;
                        }
                    } else {
                        success = false;
                    }

                    if (success) {
                        player.sendMessage(new StringTextComponent("non cap power stored: " + value));
                        if (lastPowerVal != -1) {
                            player.sendMessage(new StringTextComponent("relative power change: " + (value - lastPowerVal)));
                        }
                        lastPowerVal = value;
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


        }
    }

    public static boolean tryPathToXYZModCompat(MobEntity ent, int x, int y, int z, double speed) {
        /***
         *
         * com.lycanitesmobs.core.entity.EntityCreatureBase extends EntityLiving
         * if .useDirectNavigator()
         * run .directNavigator.setTargetPosition(new BlockPos(x, y, z), this.speed);
         *
         */

        boolean invokedCustom = false;
        boolean successCustom = false;

        if (enableFlyingSupport && isLycanitesMobsInstalled()) {
            try {
                if (class_LycanitesMobs_DirectNavigator == null) {
                    class_LycanitesMobs_DirectNavigator = Class.forName("com.lycanitesmobs.core.entity.ai.DirectNavigator");
                    /*if (class_LycanitesMobs_DirectNavigator == null) {
                        CULog.log("failed to find " + "com.lycanitesmobs.core.entity.ai.DirectNavigator");
                    } else {
                        CULog.log("found " + "com.lycanitesmobs.core.entity.ai.DirectNavigator");
                    }*/
                }
                if (method_LycanitesMobs_useDirectNavigator == null) {
                    method_LycanitesMobs_useDirectNavigator = class_LycanitesMobs_Entity.getDeclaredMethod("useDirectNavigator");
                }
                if (method_LycanitesMobs_setTargetPosition == null) {
                    method_LycanitesMobs_setTargetPosition =
                            class_LycanitesMobs_DirectNavigator.getDeclaredMethod("setTargetPosition", BlockPos.class, double.class);
                }
                if (field_LycanitesMobs_directNavigator == null) {
                    field_LycanitesMobs_directNavigator = class_LycanitesMobs_Entity.getDeclaredField("directNavigator");
                }

                if (ent.getClass().isAssignableFrom(class_LycanitesMobs_Entity)) {
                    if ((boolean)method_LycanitesMobs_useDirectNavigator.invoke(ent)) {
                        Object directNavigator = field_LycanitesMobs_directNavigator.get(ent);
                        invokedCustom = true;
                        successCustom = (boolean)method_LycanitesMobs_setTargetPosition.invoke(directNavigator, new BlockPos(x, y, z), speed);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                //prevent error spam
                lycanitesMobsInstalled = false;
                return false;
            }
        }

        if (!invokedCustom) {
            return tryPathToXYZVanilla(ent, x, y, z, speed);
        } else {
            return successCustom;
        }
    }

    public static boolean tryPathToXYZVanilla(MobEntity ent, int x, int y, int z, double speed) {
        return ent.getNavigator().tryMoveToXYZ(x, y, z, speed);
    }

    /*public static boolean tryPathToXYZLycanitesFlying(World world, EntityLiving ent, int x, int y, int z, double speed) {

    }*/

    public static boolean isHWMonstersInstalled() {
        return ModList.get().isLoaded(CoroUtil.modID_HWMonsters);
    }

    public static boolean isHWInvasionsInstalled() {
        return ModList.get().isLoaded(CoroUtil.modID_HWInvasions);
    }

    public static boolean canTornadoGrabBlockRefinedRules(BlockState state) {
        ResourceLocation registeredName = state.getBlock().getRegistryName();
        if (registeredName.getNamespace().equals("dynamictrees")) {
            if (registeredName.getPath().contains("rooty") || registeredName.getPath().contains("branch")) {
                return false;
            }
        }
        return true;
    }

}
