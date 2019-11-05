package weather2;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.tropicraft.core.common.minigames.definitions.IslandRoyaleMinigameDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weather2.util.WeatherUtil;

import java.util.Random;

public class MinigameWeatherInstance {

    /**
     *
     *
     * instantiate in IslandRoyaleMinigameDefinition
     * - packet sync what is needed
     * - setup instanced overrides on client
     *
     *
     * phases:
     * - 1: semi peacefull, maybe light rain/wind
     * - 2: heavy wind, acid rain
     * - 3: see doc, "an extreme storm encroaches the map slowly towards the centre"
     * --- assuming can also do same things phase 2 does?
     *
     * phases should be in IslandRoyaleMinigameDefinition for use in other places, and this class listens to them
     *
     * rng that can happen:
     * - wind, can operate independently of other rng events
     *
     * rng that only allows 1 of them at a time:
     * - extreme rain
     * - acid rain
     * - heat wave
     *
     * heat wave:
     * - player movement reduced if player pos can see sky
     *
     * rain:
     * - the usual
     *
     * acid rain:
     * - player damage over time
     * - degrade items and armor over time
     *
     * extreme rain:
     * - fog closes in
     * - pump up weather2 effects
     * - splashing noise while walking
     *
     * - consider design to factor in worn items to negate player effects
     */

    //only one of these can be active at a time
    protected long heavyRainfallTime = 0;
    protected long acidRainTime = 0;
    protected long heatwaveTime = 0;

    //operates independently of other weather events
    //TODO: just modify WindManager to do this?
    protected long highWindTime = 0;

    protected Random random = new Random();

    public static final Logger LOGGER = LogManager.getLogger();

    public static boolean debug = true;

    public MinigameWeatherInstance() {

    }

    public void tick(IslandRoyaleMinigameDefinition minigameDefinition) {

    }

    public void reset() {
        heavyRainfallTime = 0;
        acidRainTime = 0;
        heatwaveTime = 0;
    }

    public boolean heavyRainfallActive() {
        return heavyRainfallTime > 0;
    }

    public boolean acidRainActive() {
        return acidRainTime > 0;
    }

    public boolean heatwaveActive() {
        return heatwaveTime > 0;
    }

    public boolean specialWeatherActive() {
        return heavyRainfallActive() || acidRainActive() || heatwaveActive();
    }

    public void dbg(String str) {
        if (debug) {
            LOGGER.info(str);
        }
    }

    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putLong("heavyRainfallTime", heavyRainfallTime);
        nbt.putLong("acidRainTime", acidRainTime);
        nbt.putLong("heatwaveTime", heatwaveTime);

        return nbt;
    }

    public void deserialize(CompoundNBT nbt) {
        heavyRainfallTime = nbt.getLong("heavyRainfallTime");
        acidRainTime = nbt.getLong("acidRainTime");
        heatwaveTime = nbt.getLong("heatwaveTime");

        dbg("minigame weather deserialize(): " + nbt);
    }

}
