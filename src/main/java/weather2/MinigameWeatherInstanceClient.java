package weather2;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.tropicraft.core.common.minigames.definitions.IslandRoyaleMinigameDefinition;

public class MinigameWeatherInstanceClient extends MinigameWeatherInstance {


    //public float rainfallAmount = 0;

    //0 - 1
    public float curOvercastStr = 0F;
    public float curOvercastStrTarget = 0F;

    public MinigameWeatherInstanceClient() {

    }

    @Override
    public void tick(IslandRoyaleMinigameDefinition minigameDefinition) {
        super.tick(minigameDefinition);

        World world = Minecraft.getInstance().world;
        if (world == null) return;

        if (heavyRainfallActive() || acidRainActive()) {
            curOvercastStrTarget = 1F;
        } else {
            curOvercastStrTarget = 0F;
        }

        float rateChange = 0.001F;

        if (curOvercastStr > curOvercastStrTarget) {
            curOvercastStr -= rateChange;
        } else if (curOvercastStr < curOvercastStrTarget) {
            curOvercastStr += rateChange;
        }

        if (world.getGameTime() % 60 == 0) {
            LOGGER.info(curOvercastStr);
        }
    }

    public float getParticleRainfallAmount() {
        return curOvercastStr;
    }

    public float getVanillaRainRenderAmount() {
        //have vanilla rain get to max saturation by the time curOvercastStr hits 0.33, rest of range is used for extra particle effects elsewhere
        return Math.min(curOvercastStr * 3F, 1F);
    }
}
