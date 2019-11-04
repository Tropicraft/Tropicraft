package weather2;

public class WeatherMinigameInstance {

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
     *
     * rng that can happen:
     * - wind, can operate independantly of other rng events
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

}
