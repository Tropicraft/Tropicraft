package net.tropicraft.entity;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityLand.EnumTropiMobType;
import net.tropicraft.info.TCInfo;

public abstract class EntityTropicraftAnimal extends EntityAnimal {

    public EntityTropicraftAnimal(World world) {
        super(world);
    }
    
    /**
     * Appends a sound name to the necessary mod prefix so the sound file can be located correctly
     * @param postfix Actual file name (sans file type)
     * @return Corrected path to mod sound file
     */
    protected String tcSound(String postfix) {
        return String.format("%s:%s", TCInfo.MODID, postfix);
    }
    
    public abstract EnumTropiMobType getMobType();

}
