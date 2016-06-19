package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.entity.EntityLand;

public class EntityVMonkey extends EntityLand implements IMob {

	public boolean isClimbing = false;
	public boolean isSitting = false;
	
    public EntityVMonkey(World world) {
        super(world);
        setSize(0.8F, 0.8F);
        this.experienceValue = 4;
    }
}
