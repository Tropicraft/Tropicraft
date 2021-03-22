package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnMobPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.egg.EggEntity;
import net.tropicraft.core.common.entity.egg.SeaUrchinEggEntity;
import net.tropicraft.core.common.item.TropicraftItems;

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

    public SeaUrchinEntity(EntityType<? extends EchinodermEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return WaterMobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amt) {
        if (source.getDamageType().equals("player")) {
            Entity ent = source.getTrueSource();

            if (ent instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) ent;

                if (player.getHeldItemMainhand().isEmpty()) {
                    player.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
                }
            }
        }

        return super.attackEntityFrom(source, amt);
    }

    @Override
    public void applyEntityCollision(Entity ent) {
        super.applyEntityCollision(ent);

        if (!world.isRemote) {
            if (ent instanceof LivingEntity && !(ent instanceof SeaUrchinEntity) && !(ent instanceof SeaUrchinEggEntity)) {
                ent.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
            }
        }
    }

    @Override
    public EggEntity createEgg() {
        return new SeaUrchinEggEntity(TropicraftEntities.SEA_URCHIN_EGG_ENTITY.get(), world);
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

    @Override
    public IPacket<?> createSpawnPacket() {
        return new SSpawnMobPacket(this);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.SEA_URCHIN_SPAWN_EGG.get());
    }
}
