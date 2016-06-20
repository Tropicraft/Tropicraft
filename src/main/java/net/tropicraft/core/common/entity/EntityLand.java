package net.tropicraft.core.common.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;
import net.tropicraft.Info;

public abstract class EntityLand extends EntityCreature {

    public EntityLand(World world) {
        super(world);
        setSize(.7F, 1.95F);
    }
    
    /**
     * Appends a sound name to the necessary mod prefix so the sound file can be located correctly
     * @param postfix Actual file name (sans file type)
     * @return Corrected path to mod sound file
     */
    protected String tcSound(String postfix) {
        return String.format("%s:%s", Info.MODID, postfix);
    }
}
