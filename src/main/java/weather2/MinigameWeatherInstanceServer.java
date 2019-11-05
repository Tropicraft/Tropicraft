package weather2;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import net.tropicraft.core.common.minigames.definitions.IslandRoyaleMinigameDefinition;
import weather2.util.CachedNBTTagCompound;
import weather2.util.WeatherUtil;

public class MinigameWeatherInstanceServer extends MinigameWeatherInstance {

    //rolls the dice once per second on each special weather event
    //0.1F = 10%
    protected float heavyRainfallChance = 0.1F;
    protected float acidRainChance = 0.1F;
    protected float heatwaveChance = 0.1F;

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
                        if (random.nextFloat() < heavyRainfallChance) {
                            heavyRainfallStart();
                        } else if (random.nextFloat() < acidRainChance) {
                            acidRainStart();
                        } else if (random.nextFloat() < heatwaveChance) {
                            heatwaveStart();
                        }
                    }
                }

                //dbg("" + minigameDefinition.getDimension() + minigameDefinition.getPhaseTime());

                tickSync(minigameDefinition);
            }

            if (heavyRainfallTime > 0) {
                heavyRainfallTime--;

                if (heavyRainfallTime == 0) dbg("heavyRainfallTime ended");
            }

            if (acidRainTime > 0) {
                acidRainTime--;

                if (acidRainTime == 0) dbg("acidRainTime ended");
            }

            if (heatwaveTime > 0) {
                heatwaveTime--;

                if (heatwaveTime == 0) dbg("heatwaveTime ended");
            }
        }
    }

    public void tickSync(IslandRoyaleMinigameDefinition minigameDefinition) {
        CompoundNBT data = new CompoundNBT();
        data.putString("packetCommand", WeatherNetworking.NBT_PACKET_COMMAND_MINIGAME);
        //data.putString("command", "syncStormNew");

        data.put(WeatherNetworking.NBT_PACKET_DATA_MINIGAME, serialize());

        WeatherNetworking.HANDLER.send(PacketDistributor.DIMENSION.with(() -> minigameDefinition.getDimension()), new PacketNBTFromServer(data));
    }

    public void heavyRainfallStart() {
        heavyRainfallTime = (20*60*2) + random.nextInt(20*60*2);
        dbg("heavyRainfallStart: " + heavyRainfallTime);
    }

    public void acidRainStart() {
        acidRainTime = (20*60*2) + random.nextInt(20*60*2);
        dbg("acidRainStart: " + acidRainTime);
    }

    public void heatwaveStart() {
        heatwaveTime = (20*60*2) + random.nextInt(20*60*2);
        dbg("heatwaveStart: " + heatwaveTime);
    }

}
