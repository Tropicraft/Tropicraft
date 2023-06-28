package net.tropicraft.core.common.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;

public class TropiBeeEntity extends Bee {
    public TropiBeeEntity(EntityType<? extends Bee> type, Level world) {
        super(type, world);
    }

    @Override
    public Bee getBreedOffspring(ServerLevel world, AgeableMob partner) {
        return TropicraftEntities.TROPI_BEE.get().create(this.level());
    }
}
