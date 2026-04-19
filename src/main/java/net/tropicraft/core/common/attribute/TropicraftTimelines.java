package net.tropicraft.core.common.attribute;

import com.tterrag.registrate.Registrate;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TimelineTags;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.timeline.Timeline;
import net.minecraft.world.timeline.Timelines;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.TropicraftTags;

public interface TropicraftTimelines {
    ResourceKey<Timeline> DAY = createKey("day");
    ResourceKey<Timeline> KOA_PARTY = createKey("koa_party");

    static void bootstrap(BootstrapContext<Timeline> context) {
        Holder.Reference<WorldClock> overworldClock = context.lookup(Registries.WORLD_CLOCK).getOrThrow(WorldClocks.OVERWORLD);

        context.register(DAY, Timeline.builder(overworldClock)
                .setPeriodTicks(SharedConstants.TICKS_PER_GAME_DAY)
                .addTrack(TropicraftEnvironmentAttributes.CAN_TELEPORT_TO_TROPICS.get(), track -> track
                        .addKeyframe(12200, true)
                        .addKeyframe(14000, false)
                )
                .addTrack(TropicraftEnvironmentAttributes.KOA_PARTY_AMP.get(), track -> track
                        .addKeyframe(0, 4)
                        .addKeyframe(15250, 3)
                        .addKeyframe(18000, 2)
                        .addKeyframe(20750, 1)
                )
                .build()
        );

        context.register(KOA_PARTY, Timeline.builder(overworldClock)
                .setPeriodTicks(SharedConstants.TICKS_PER_GAME_DAY * 3)
                .addTrack(TropicraftEnvironmentAttributes.KOA_PARTY_CHANCE.get(), track -> track
                        .addKeyframe(0, 0.9f)
                        .addKeyframe(SharedConstants.TICKS_PER_GAME_DAY, 0.0f)
                )
                .build()
        );
    }

    static void bootstrapTags(Registrate registrate) {
        registrate.addDataGenerator(Tropicraft.TIMELINE_TAGS, prov -> {
            prov.tag(TimelineTags.IN_OVERWORLD).add(DAY);
            prov.tag(TropicraftTags.Timelines.IN_TROPICS).add(Timelines.OVERWORLD_DAY, DAY, KOA_PARTY).addTag(TimelineTags.UNIVERSAL);
        });
    }

    static ResourceKey<Timeline> createKey(String name) {
        return Tropicraft.resourceKey(Registries.TIMELINE, name);
    }
}
