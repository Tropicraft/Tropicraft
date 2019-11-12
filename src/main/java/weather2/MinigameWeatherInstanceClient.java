package weather2;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.tropicraft.core.common.minigames.definitions.survive_the_tide.SurviveTheTideMinigameDefinition;

public class MinigameWeatherInstanceClient extends MinigameWeatherInstance {


    //public float rainfallAmount = 0;

    //0 - 1
    public float curOvercastStr = 0F;
    public float curOvercastStrTarget = 0F;

    public MinigameWeatherInstanceClient() {

    }

    @Override
    public void tick(SurviveTheTideMinigameDefinition minigameDefinition) {
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
            //LOGGER.info(curOvercastStr);
        }

        world.getPlayers().forEach(player -> tickPlayer(player));
    }

    @Override
    public void tickPlayer(PlayerEntity player) {
//        if (heatwaveActive() && !player.isCreative() && !player.isSpectator()) {
//            if (player.world.getHeight(Heightmap.Type.MOTION_BLOCKING, player.getPosition()).getY() <= player.getPosition().getY()) {
//                System.out.println("slowing player");
//                player.setMotionMultiplier(player.getBlockState(), new Vec3d(0.95D, 1D, 0.95D));
//                Vec3d v = player.getMotion();
//                player.setMotion(v.x * heatwaveMovementMultiplierClient, v.y, v.z * heatwaveMovementMultiplierClient);
//            }
//        }
    }

    public float getParticleRainfallAmount() {
        return curOvercastStr;
    }

    public float getVanillaRainRenderAmount() {
        //have vanilla rain get to max saturation by the time curOvercastStr hits 0.33, rest of range is used for extra particle effects elsewhere
        return Math.min(curOvercastStr * 3F, 1F);
    }
}
