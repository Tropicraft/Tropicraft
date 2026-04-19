package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.egg.EggEntity;
import net.tropicraft.core.common.entity.egg.SeaUrchinEggEntity;

public class SeaUrchinEntity extends EchinodermEntity {
    /**
     * Bounding box length/width/height of a freshly hatched sea urchin.
     */
    public static final float BABY_SIZE = 0.25f;

    /**
     * Bounding box length/width/height of a mature sea urchin.
     */
    public static final float ADULT_SIZE = 0.5f;

    /**
     * Rendered Y offset of a freshly hatched sea urchin.
     */
    public static final float BABY_YOFFSET = 0.125f;

    /**
     * Rendered Y offset of a mature sea urchin.
     */
    public static final float ADULT_YOFFSET = 0.25f;

    public SeaUrchinEntity(EntityType<? extends EchinodermEntity> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amt) {
        if (source.is(DamageTypes.PLAYER_ATTACK)) {
            if (source.getEntity() instanceof Player player && player.getMainHandItem().isEmpty()) {
                player.hurtServer(level, damageSources().mobAttack(this), 2);
            }
        }

        return super.hurtServer(level, source, amt);
    }

    @Override
    public void push(Entity ent) {
        super.push(ent);

        if (level() instanceof ServerLevel serverLevel) {
            if (ent instanceof LivingEntity && !(ent instanceof SeaUrchinEntity) && !(ent instanceof SeaUrchinEggEntity)) {
                ent.hurtServer(serverLevel, damageSources().mobAttack(this), 2);
            }
        }
    }

    @Override
    public EggEntity createEgg() {
        return new SeaUrchinEggEntity(TropicraftEntities.SEA_URCHIN_EGG_ENTITY.get(), level());
    }

    @Override
    public float getBabyWidth() {
        return BABY_SIZE;
    }

    @Override
    public float getAdultWidth() {
        return ADULT_SIZE;
    }

    @Override
    public float getBabyHeight() {
        return BABY_SIZE;
    }

    @Override
    public float getAdultHeight() {
        return ADULT_SIZE;
    }

    @Override
    public float getBabyYOffset() {
        return BABY_YOFFSET;
    }

    @Override
    public float getAdultYOffset() {
        return ADULT_YOFFSET;
    }
}
