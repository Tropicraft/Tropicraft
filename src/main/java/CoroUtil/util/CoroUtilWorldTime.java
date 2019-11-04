package CoroUtil.util;

import net.minecraft.world.World;

public class CoroUtilWorldTime {

    /**
     * Use instead of hardcoded so we can reroute to any day length changing mods for compat
     *
     * @return
     */
    public static int getDayLength() {
        return 24000;
    }

    /**
     * Use instead of vanilla methods, as they are light based, which thunder affects and cause it to count as night
     *
     * - night time first tick = 12542
     * - day time first tick = 23460
     *
     * @return
     */
    public static boolean isNight(World world) {
        long timeMod = world.getDayTime() % getDayLength();
        return timeMod >= getNightFirstTick() && timeMod <= getDayFirstTick();
    }

    public static boolean isNightPadded(World world) {
        return isNightPadded(world, 5);
    }

    public static boolean isNightPadded(World world, int padding) {
        long timeMod = world.getDayTime() % getDayLength();
        return timeMod >= getNightFirstTick() + padding && timeMod <= getDayFirstTick() - padding;
    }

    public static int getNightFirstTick() {
        return 12542;
    }

    public static int getDayFirstTick() {
        return 23460;
    }

}
