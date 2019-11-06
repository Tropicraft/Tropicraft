package net.tropicraft.core.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static net.minecraftforge.common.ForgeConfigSpec.*;

@EventBusSubscriber
public class ConfigLT {

    private static final Builder CLIENT_BUILDER = new Builder();

    public static final CategoryGeneral GENERAL = new CategoryGeneral();

    public static final class CategoryGeneral {

        public final DoubleValue Precipitation_Particle_effect_rate;

        public final BooleanValue UseCrouch;

        //public static double Precipitation_Particle_effect_rate = 0.7D;

        private CategoryGeneral() {
            CLIENT_BUILDER.comment("General mod settings").push("general");

            Precipitation_Particle_effect_rate = CLIENT_BUILDER
                    .defineInRange("Precipitation_Particle_effect_rate", 0.7D, 0D, 1D);

            UseCrouch = CLIENT_BUILDER.comment("Enable crawling anywhere by pressing the sprint key while holding down the sneak key")
                    .define("UseCrawl", true);

            CLIENT_BUILDER.pop();
        }
    }
    public static final ForgeConfigSpec CLIENT_CONFIG = CLIENT_BUILDER.build();
}
