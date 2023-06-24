package net.tropicraft.core.common.item;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.SharedConstants;
import net.minecraft.sounds.SoundEvent;
import net.tropicraft.core.common.sound.Sounds;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public enum RecordMusic {

    BURIED_TREASURE(Sounds.BURIED_TREASURE, "Punchaface", "https://www.youtube.com/watch?v=lPhHWYnUR_4", 362),
    EASTERN_ISLES(Sounds.EASTERN_ISLES, "Frox", "https://soundcloud.com/froxlab/eastern_isles", 376),
    THE_TRIBE(Sounds.THE_TRIBE, "Emile Van Krieken", "https://www.youtube.com/watch?v=LvxVDhMvwE4", 154),
    LOW_TIDE(Sounds.LOW_TIDE, "Punchaface", "https://youtu.be/VgB2TjwGOuQ", 347),
    TRADE_WINDS(Sounds.TRADE_WINDS, "Frox", "https://soundcloud.com/froxlab/breakset-37-unnamed", 240),
    SUMMERING(Sounds.SUMMERING, "Billy Christiansen", "https://www.youtube.com/watch?v=szO30RmM7b8", 292),
    ;
    
    public final String name;
    private final Supplier<SoundEvent> sound;
    public final String author;
    public final String url;
    private final int lengthInSeconds;
    
    RecordMusic(Supplier<SoundEvent> sound, String author, String url, int lengthInSeconds) {
        this.name = RegistrateLangProvider.toEnglishName(name());
        this.sound = sound;
        this.author = author;
        this.url = url;
        this.lengthInSeconds = lengthInSeconds;
    }
    
    public SoundEvent getSound() {
        return sound.get();
    }

    public List<String> getTooltip() {
        return Arrays.asList(author + " - " + name, url);
    }

    public int lengthInTicks() {
        return lengthInSeconds * SharedConstants.TICKS_PER_SECOND;
    }
}