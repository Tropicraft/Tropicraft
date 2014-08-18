package net.tropicraft.curare;

import net.minecraft.potion.Potion;

public class CurareType {
    public static final CurareType[] curareTypeList = new CurareType[128];
    public static final CurareType paralysis = new CurareType(0, Potion.blindness);
    public static final CurareType poison = new CurareType(1, Potion.poison);
    public static final CurareType moveSlowdown = new CurareType(2, Potion.moveSlowdown);
    public static final CurareType harm = new CurareType(3, Potion.harm);
    public static final CurareType confusion = new CurareType(4, Potion.confusion);
    public static final CurareType hunger = new CurareType(5, Potion.hunger);
    public static final CurareType weakness = new CurareType(6, Potion.weakness);
    
    private static final String[] names = new String[]{"Paralysis", "Poison", "Slowdown", "Harm", "Confusion", "Hunger", "Weakness"};
    
    public int curareId;
    
    public Potion potion;
    
    public CurareType(int id, Potion potion) {
        curareId = id;
        this.potion = potion;
        curareTypeList[id] = this;
    }
    
    public Potion getPotion() {
        return potion;
    }
    
    public static CurareType getCurareFromDamage(int damage) {
        return curareTypeList[damage];
    }
    
    @Override
    public String toString() {
        return names[curareId];
    }
}
