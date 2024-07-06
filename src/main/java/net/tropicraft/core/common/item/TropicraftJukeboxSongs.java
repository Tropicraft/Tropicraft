package net.tropicraft.core.common.item;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.sound.Sounds;

public interface TropicraftJukeboxSongs {
    ResourceKey<JukeboxSong> BURIED_TREASURE = createKey("buried_treasure");
    ResourceKey<JukeboxSong> EASTERN_ISLES = createKey("eastern_isles");
    ResourceKey<JukeboxSong> THE_TRIBE = createKey("the_tribe");
    ResourceKey<JukeboxSong> LOW_TIDE = createKey("low_tide");
    ResourceKey<JukeboxSong> TRADE_WINDS = createKey("trade_winds");
    ResourceKey<JukeboxSong> SUMMERING = createKey("summering");

    static void bootstrap(BootstrapContext<JukeboxSong> context) {
        register(context, BURIED_TREASURE, Sounds.BURIED_TREASURE, "Punchaface", "https://www.youtube.com/watch?v=lPhHWYnUR_4", 362);
        register(context, EASTERN_ISLES, Sounds.EASTERN_ISLES, "Frox", "https://soundcloud.com/froxlab/eastern_isles", 376);
        register(context, THE_TRIBE, Sounds.THE_TRIBE, "Emile Van Krieken", "https://www.youtube.com/watch?v=LvxVDhMvwE4", 154);
        register(context, LOW_TIDE, Sounds.LOW_TIDE, "Punchaface", "https://youtu.be/VgB2TjwGOuQ", 347);
        register(context, TRADE_WINDS, Sounds.TRADE_WINDS, "Frox", "https://soundcloud.com/froxlab/breakset-37-unnamed", 240);
        register(context, SUMMERING, Sounds.SUMMERING, "Billy Christiansen", "https://www.youtube.com/watch?v=szO30RmM7b8", 292);
    }

    private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, Holder<SoundEvent> sound, String author, String url, int lengthInSeconds) {
        String name = RegistrateLangProvider.toEnglishName(key.location().getPath());
        Component description = Component.literal(name + " - " + author);
        context.register(key, new JukeboxSong(sound, description, lengthInSeconds, 13));
    }

    private static ResourceKey<JukeboxSong> createKey(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(Tropicraft.ID, name));
    }
}
