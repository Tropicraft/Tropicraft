package net.tropicraft.core.common.item;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.util.SoundEvent;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.sound.Sounds;

public enum RecordMusic {
    
    BURIED_TREASURE(() -> Sounds.BURIED_TREASURE, "Punchaface", "https://www.youtube.com/watch?v=lPhHWYnUR_4"),
    EASTERN_ISLES(() -> Sounds.EASTERN_ISLES, "Frox", "https://soundcloud.com/froxlab/eastern_isles"),
    THE_TRIBE(() -> Sounds.THE_TRIBE, "Emile Van Krieken", "https://www.youtube.com/watch?v=LvxVDhMvwE4"),
    LOW_TIDE(() -> Sounds.LOW_TIDE, "Punchaface", "https://youtu.be/VgB2TjwGOuQ"),
    TRADE_WINDS(() -> Sounds.TRADE_WINDS, "Frox", "https://soundcloud.com/froxlab/breakset-37-unnamed"),
    SUMMERING(() -> Sounds.SUMMERING, "Billy Christiansen", "https://www.youtube.com/watch?v=szO30RmM7b8"),
    ;
    
    public final String name;
    private final Supplier<SoundEvent> sound;
    public final String author;
    public final String url;
    
    private RecordMusic(Supplier<SoundEvent> sound, String author, String url) {
        this.name = Util.toEnglishName(name());
        this.sound = sound;
        this.author = author;
        this.url = url;
    }
    
    public SoundEvent getSound() {
        return sound.get();
    }

    public List<String> getTooltip() {
        return Arrays.asList(author + " - " + name, url);
    }
}