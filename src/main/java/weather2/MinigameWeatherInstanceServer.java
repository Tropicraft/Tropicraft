package weather2;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.network.PacketDistributor;
import net.tropicraft.core.common.config.ConfigLT;
import net.tropicraft.core.common.minigames.definitions.IslandRoyaleMinigameDefinition;
import weather2.util.CachedNBTTagCompound;
import weather2.util.WeatherUtil;

public class MinigameWeatherInstanceServer extends MinigameWeatherInstance {

    public MinigameWeatherInstanceServer() {
        super();
    }

    public void tick(IslandRoyaleMinigameDefinition minigameDefinition) {
        super.tick(minigameDefinition);
        World world = WeatherUtil.getWorld(minigameDefinition.getDimension());
        if (world != null) {
            int tickRate = 20;
            if (world.getGameTime() % tickRate == 0) {

                if (minigameDefinition.getPhase() == IslandRoyaleMinigameDefinition.MinigamePhase.PHASE2 ||
                        minigameDefinition.getPhase() == IslandRoyaleMinigameDefinition.MinigamePhase.PHASE3) {
                    if (!specialWeatherActive()) {

                        //favors first come first serve but if the rates are low enough its negligable probably
                        if (random.nextFloat() <= ConfigLT.MINIGAME_ISLAND_ROYALE.rainHeavyChance.get()) {
                            heavyRainfallStart();
                        } else if (random.nextFloat() <= ConfigLT.MINIGAME_ISLAND_ROYALE.rainAcidChance.get()) {
                            acidRainStart();
                        } else if (random.nextFloat() <= ConfigLT.MINIGAME_ISLAND_ROYALE.heatwaveChance.get()) {
                            heatwaveStart();
                        }
                    }
                }

                //dbg("" + minigameDefinition.getDimension() + minigameDefinition.getPhaseTime());

                tickSync(minigameDefinition);
            }

            //heavyRainfallTime = 0;
            //acidRainTime = 999;
            //acidRainStart();
            //heatwaveStart();

            if (heavyRainfallTime > 0) {
                heavyRainfallTime--;

                //if (heavyRainfallTime == 0) dbg("heavyRainfallTime ended");
            }

            if (acidRainTime > 0) {
                acidRainTime--;

                //if (acidRainTime == 0) dbg("acidRainTime ended");
            }

            if (heatwaveTime > 0) {
                heatwaveTime--;

                //if (heatwaveTime == 0) dbg("heatwaveTime ended");
            }

            //need to set for overworld due to vanilla bug
            World overworld = WeatherUtil.getWorld(0);
            if (overworld != null) {
                if (specialWeatherActive() && !heatwaveActive()) {
                    overworld.getWorldInfo().setRaining(true);
                    overworld.getWorldInfo().setThundering(true);
                } else {
                    overworld.getWorldInfo().setRaining(false);
                    overworld.getWorldInfo().setThundering(false);
                }
            }
        }

        world.getPlayers().forEach(player -> tickPlayer(player));
    }

    @Override
    public void tickPlayer(PlayerEntity player) {
        if (player.isCreative()) return;
        if (acidRainActive()) {
            if (player.world.getHeight(Heightmap.Type.MOTION_BLOCKING, player.getPosition()).getY() <= player.getPosition().getY()) {
                if (player.world.getGameTime() % ConfigLT.MINIGAME_ISLAND_ROYALE.acidRainDamageRate.get() == 0) {
                    player.attackEntityFrom(DamageSource.GENERIC, ConfigLT.MINIGAME_ISLAND_ROYALE.acidRainDamage.get());
                }
            }
        } else if (heatwaveActive()) {

        }
    }

    public void tickSync(IslandRoyaleMinigameDefinition minigameDefinition) {
        CompoundNBT data = new CompoundNBT();
        data.putString("packetCommand", WeatherNetworking.NBT_PACKET_COMMAND_MINIGAME);

        data.put(WeatherNetworking.NBT_PACKET_DATA_MINIGAME, serialize());

        WeatherNetworking.HANDLER.send(PacketDistributor.DIMENSION.with(() -> minigameDefinition.getDimension()), new PacketNBTFromServer(data));
    }

    public void heavyRainfallStart() {
        heavyRainfallTime = ConfigLT.MINIGAME_ISLAND_ROYALE.rainHeavyMinTime.get() + random.nextInt(ConfigLT.MINIGAME_ISLAND_ROYALE.rainHeavyExtraRandTime.get());
        lastRainWasAcid = false;
        dbg("heavyRainfallStart: " + heavyRainfallTime);
    }

    public void acidRainStart() {
        acidRainTime = ConfigLT.MINIGAME_ISLAND_ROYALE.rainAcidMinTime.get() + random.nextInt(ConfigLT.MINIGAME_ISLAND_ROYALE.rainAcidExtraRandTime.get());
        lastRainWasAcid = true;
        dbg("acidRainStart: " + acidRainTime);
    }

    public void heatwaveStart() {
        heatwaveTime = ConfigLT.MINIGAME_ISLAND_ROYALE.heatwaveMinTime.get() + random.nextInt(ConfigLT.MINIGAME_ISLAND_ROYALE.heatwaveExtraRandTime.get());
        dbg("heatwaveStart: " + heatwaveTime);
    }

}
