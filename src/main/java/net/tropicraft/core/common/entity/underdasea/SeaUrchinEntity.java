package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
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

    public SeaUrchinEntity(EntityType<? extends EchinodermEntity> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    public boolean hurt(DamageSource source, float amt) {
        if (source.getMsgId().equals("player")) {
            Entity ent = source.getEntity();

            if (ent instanceof Player) {
                Player player = (Player) ent;

                if (player.getMainHandItem().isEmpty()) {
                    player.hurt(DamageSource.mobAttack(this), 2);
                }
            }
        }

        return super.hurt(source, amt);
    }

    @Override
    public void push(Entity ent) {
        super.push(ent);

        if (!level.isClientSide) {
            if (ent instanceof LivingEntity && !(ent instanceof SeaUrchinEntity) && !(ent instanceof SeaUrchinEggEntity)) {
                ent.hurt(DamageSource.mobAttack(this), 2);
            }
        }
    }

    @Override
    public EggEntity createEgg() {
        return new SeaUrchinEggEntity(TropicraftEntities.SEA_URCHIN_EGG_ENTITY.get(), level);
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
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddMobPacket(this);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.SEA_URCHIN_SPAWN_EGG.get());
    }
}
