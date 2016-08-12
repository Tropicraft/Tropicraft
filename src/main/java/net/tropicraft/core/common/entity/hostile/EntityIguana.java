package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.EntityLand;

public class EntityIguana extends EntityLand implements IMob {

    public EntityIguana(World world) {
        super(world);
        setSize(1.0F, 0.4F);
        
    }
}
