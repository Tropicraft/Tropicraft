package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.world.World;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class EntitySeaTurtle extends EntityTropicraftWaterBase {
    
    public EntitySeaTurtle(World par1World) {
        super(par1World);
        setSize(0.9f, 0.4f);
        this.setSwimSpeeds(1f, 1f, 0.5f);
        this.setApproachesPlayers(true);
    }

    public EntitySeaTurtle(World world, int age) {
        super(world);
        setSize(0.3f, 0.3f);
    }
    
    @Override
    public double getMountedYOffset() {
        return (double)height * 0.75D - 1F + 0.7F;
    }
    
}
