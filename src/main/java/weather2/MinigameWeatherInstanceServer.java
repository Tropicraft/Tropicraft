package weather2;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.network.PacketDistributor;
import net.tropicraft.core.common.config.ConfigLT;
import net.tropicraft.core.common.minigames.definitions.survive_the_tide.SurviveTheTideMinigameDefinition;
import weather2.util.WeatherUtil;

public class MinigameWeatherInstanceServer extends MinigameWeatherInstance {

    public MinigameWeatherInstanceServer() {
        super();
    }

    public void tick(SurviveTheTideMinigameDefinition minigameDefinition) {
        super.tick(minigameDefinition);
        World world = WeatherUtil.getWorld(minigameDefinition.getDimension());
        if (world != null) {
            int tickRate = 20;
            if (world.getGameTime() % tickRate == 0) {

                SurviveTheTideMinigameDefinition.MinigamePhase phase = minigameDefinition.getPhase();

                if (!SurviveTheTideMinigameDefinition.isSafePhase(phase)) {
                    if (!specialWeatherActive()) {

                        //testing vals to maybe make configs
                        float rateAmp = 1F;
                        if (phase == SurviveTheTideMinigameDefinition.MinigamePhase.PHASE2) {
                            rateAmp = 1.5F;
                        } else if (phase == SurviveTheTideMinigameDefinition.MinigamePhase.PHASE3) {
                            rateAmp = 2.0F;
                        } else if (phase == SurviveTheTideMinigameDefinition.MinigamePhase.PHASE4) {
                            rateAmp = 2.5F;
                        }

                        //favors first come first serve but if the rates are low enough its negligable probably
                        if (random.nextFloat() <= ConfigLT.MINIGAME_SURVIVE_THE_TIDE.rainHeavyChance.get() * rateAmp) {
                            heavyRainfallStart(phase);
                        } else if (random.nextFloat() <= ConfigLT.MINIGAME_SURVIVE_THE_TIDE.rainAcidChance.get() * rateAmp) {
                            acidRainStart(phase);
                        } else if (random.nextFloat() <= ConfigLT.MINIGAME_SURVIVE_THE_TIDE.heatwaveChance.get() * rateAmp) {
                            heatwaveStart(phase);
                        }
                    }
                }

                if (SurviveTheTideMinigameDefinition.isSafePhase(phase)) {
                    windSpeed = 0.1F;
                } else if (phase == SurviveTheTideMinigameDefinition.MinigamePhase.PHASE2) {
                    windSpeed = 0.5F;
                } else if (phase == SurviveTheTideMinigameDefinition.MinigamePhase.PHASE3) {
                    windSpeed = 1.0F;
                } else if (phase == SurviveTheTideMinigameDefinition.MinigamePhase.PHASE4) {
                    windSpeed = 1.5F;
                }

                //dbg("" + minigameDefinition.getDimension() + minigameDefinition.getPhaseTime());

                tickSync(minigameDefinition);
            }

            //heavyRainfallTime = 0;
            //acidRainTime = 0;
            //acidRainStart();
            //heatwaveTime = 0;
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
                if (player.world.getGameTime() % ConfigLT.MINIGAME_SURVIVE_THE_TIDE.acidRainDamageRate.get() == 0) {
                    player.attackEntityFrom(DamageSource.GENERIC, ConfigLT.MINIGAME_SURVIVE_THE_TIDE.acidRainDamage.get());
                }
            }
        } else if (heatwaveActive()) {
            if (player.world.getHeight(Heightmap.Type.MOTION_BLOCKING, player.getPosition()).getY() <= player.getPosition().getY()) {
                player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, 1, true, false, true));
            }
        }
    }

    public void tickSync(SurviveTheTideMinigameDefinition minigameDefinition) {
        CompoundNBT data = new CompoundNBT();
        data.putString("packetCommand", WeatherNetworking.NBT_PACKET_COMMAND_MINIGAME);

        data.put(WeatherNetworking.NBT_PACKET_DATA_MINIGAME, serialize());

        WeatherNetworking.HANDLER.send(PacketDistributor.DIMENSION.with(() -> minigameDefinition.getDimension()), new PacketNBTFromServer(data));
    }

    public void heavyRainfallStart(SurviveTheTideMinigameDefinition.MinigamePhase phase) {
        heavyRainfallTime = ConfigLT.MINIGAME_SURVIVE_THE_TIDE.rainHeavyMinTime.get() + random.nextInt(ConfigLT.MINIGAME_SURVIVE_THE_TIDE.rainHeavyExtraRandTime.get());
        if (phase == SurviveTheTideMinigameDefinition.MinigamePhase.PHASE4) {
            heavyRainfallTime /= 2;
        }
        lastRainWasAcid = false;
        //dbg("heavyRainfallStart: " + heavyRainfallTime);
    }

    public void acidRainStart(SurviveTheTideMinigameDefinition.MinigamePhase phase) {
        acidRainTime = ConfigLT.MINIGAME_SURVIVE_THE_TIDE.rainAcidMinTime.get() + random.nextInt(ConfigLT.MINIGAME_SURVIVE_THE_TIDE.rainAcidExtraRandTime.get());
        if (phase == SurviveTheTideMinigameDefinition.MinigamePhase.PHASE4) {
            acidRainTime /= 2;
        }
        lastRainWasAcid = true;
        //dbg("acidRainStart: " + acidRainTime);
    }

    public void heatwaveStart(SurviveTheTideMinigameDefinition.MinigamePhase phase) {
        heatwaveTime = ConfigLT.MINIGAME_SURVIVE_THE_TIDE.heatwaveMinTime.get() + random.nextInt(ConfigLT.MINIGAME_SURVIVE_THE_TIDE.heatwaveExtraRandTime.get());
        if (phase == SurviveTheTideMinigameDefinition.MinigamePhase.PHASE4) {
            heatwaveTime /= 2;
        }
        //dbg("heatwaveStart: " + heatwaveTime);
    }

}
