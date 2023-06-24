package net.tropicraft.core.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FireArmorItem extends TropicraftArmorItem {
    public FireArmorItem(EquipmentSlot slotType, Properties properties) {
        super(ArmorMaterials.FIRE_ARMOR, slotType, properties);
    }

    // TODO waiting on Forge
    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (world.isClientSide) {
            clientTick(player);
        } else {
            if (player.isOnFire()) player.clearFire();

            // Repair in the sun?
            int factor = (int)(40D / (0.001D + world.getLightLevelDependentMagicValue(player.blockPosition())));
            if (world.getGameTime() % (factor) == 0 && world.canSeeSkyFromBelowWater(new BlockPos(Mth.floor(player.getX()), Mth.floor(player.getY() + 1), Mth.floor(player.getZ())))) {
                //repair!
                stack.hurtAndBreak(-1, player, (e) -> {
                    e.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });
            }
        }
    }

//    @Override
//    public void damageArmor(LivingEntity player, ItemStack stack, DamageSource source, int damage, int slot) {
//        if (source == DamageSource.IN_FIRE || source == DamageSource.LAVA) {
//            //cheap way to slow the damage
//            //if (player.worldObj.getWorldTime() % 2 == 0) {
//            stack.damageItem(damage, player);
//        }
//    }
//
    @OnlyIn(Dist.CLIENT)
    public void clientTick(Player player) {
        // Don't show fire particles underwater
        if (player.isInWater()) return;

        float range = 0.2F;
        float speed = 0.08F;

        RandomSource rand = RandomSource.create();

        int extraRand = 0;

        final Vec3 motion = player.getDeltaMovement();
        double plSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);

        if (plSpeed < 0.1F) {
            extraRand = 7;
        }

        /** 0 for all, 1 for minimal, 2 for off */
        ParticleStatus particles = Minecraft.getInstance().options.particles().get();
        if (particles == ParticleStatus.MINIMAL) return;

        if (this == TropicraftItems.FIRE_BOOTS.get()) {
            boolean onLava = false;
            boolean inLava = false;
            //for (int x = -1; x < 2; x++) {
            //for (int z = -1; z < 2; z++) {
            int x = 0;
            int z = 0;
            if (motion.y < 0) {
                BlockState state = player.level.getBlockState(new BlockPos(Mth.floor(player.getX() + x), Mth.floor(player.getY() - 2), Mth.floor(player.getZ() + z)));
                if (state.getMaterial() == Material.LAVA) {
                    onLava = true;
                }
            }
            BlockState block2 = player.level.getBlockState(new BlockPos(Mth.floor(player.getX() + x), Mth.floor(player.getY() - 1), Mth.floor(player.getZ() + z)));
            if (block2.getMaterial() == Material.LAVA) {
                inLava = true;
            }

            // why do we do this on the client?
            if (onLava && !inLava) {
                player.setDeltaMovement(motion.multiply(1, 0, 1));
                player.setOnGround(true);
            }

            // why do we do this on the client???????
            if (inLava) {
                if (plSpeed < 0.4D) {
                    player.setDeltaMovement(motion.multiply(1.5D, 1.5D, 1.5D));
                }
            }

            float look = player.level.getGameTime() * (10 + (onLava ? 10 : 0));
            double dist = 1F;

            double gatherX = player.getX();
            double gatherY = player.getBoundingBox().minY;
            double gatherZ = player.getZ();

            double motionX = ((rand.nextFloat() * speed) - (speed/2));
            double motionZ = ((rand.nextFloat() * speed) - (speed/2));

            final int numFeetParticles = particles == ParticleStatus.DECREASED ? 2 : 11;

            for (int i = 0; i < numFeetParticles + (onLava ? 5 : 0); i++) {
                motionX = (-Math.sin((look) / 180.0F * 3.1415927F) * Math.cos(0 / 180.0F * 3.1415927F) * (speed + (0.1 * rand.nextDouble())));
                motionZ = (Math.cos((look) / 180.0F * 3.1415927F) * Math.cos(0 / 180.0F * 3.1415927F) * (speed + (0.1 * rand.nextDouble())));

                SimpleParticleType particle = ParticleTypes.FLAME;
                if (rand.nextInt(22) == 0) particle = ParticleTypes.LARGE_SMOKE;

                if (onLava || rand.nextInt(1 + extraRand) == 0) {
                    Vec3 motion1 = player.getDeltaMovement();
                    player.level.addParticle(particle,
                            gatherX + ((rand.nextFloat() * range) - (range/2)),
                            gatherY + ((rand.nextFloat() * range) - (range/2)),
                            gatherZ + ((rand.nextFloat() * range) - (range/2)),
                            motion1.x + motionX,
                            0.01F,
                            motion1.z + motionZ);

                    player.level.addParticle(particle,
                            (double)gatherX + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherY + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherZ + ((rand.nextFloat() * range) - (range/2)),
                            motion1.x - motionX,
                            0.01F,
                            motion1.z - motionZ);
                }
            }

        } else if (this == TropicraftItems.FIRE_LEGGINGS.get()) {
            SimpleParticleType particle = ParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = ParticleTypes.LARGE_SMOKE;

            if (rand.nextInt(3 + extraRand) == 0) {
                player.level.addParticle(particle,
                        player.getX() + ((rand.nextFloat() * range) - (range/2)),
                        player.getY() - 0.8F + ((rand.nextFloat() * range) - (range/2)),
                        player.getZ() + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.05F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }
        } else if (this == TropicraftItems.FIRE_CHESTPLATE.get()) {
            float look = -180F;
            double dist = 0.5F;

            double gatherX = player.getX() + (-Math.sin((player.getYRot() + look) / 180.0F * 3.1415927F) * Math.cos(player.getXRot() / 180.0F * 3.1415927F) * dist);
            double gatherZ = player.getZ() + (Math.cos((player.getYRot() + look) / 180.0F * 3.1415927F) * Math.cos(player.getXRot() / 180.0F * 3.1415927F) * dist);

            SimpleParticleType particle = ParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = ParticleTypes.LARGE_SMOKE;

            if (rand.nextInt(1 + extraRand) == 0) {
                player.level.addParticle(particle,
                        gatherX + ((rand.nextFloat() * range) - (range/2)),
                        player.getY() - 0.4F + ((rand.nextFloat() * range) - (range/2)),
                        gatherZ + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.01F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }

        } else if (this == TropicraftItems.FIRE_HELMET.get()) {
            float look = -180F;
            double dist = 0.5F;

            range = 2F;

            double gatherX = player.getX() + (-Math.sin((player.getYRot() + look) / 180.0F * 3.1415927F) * Math.cos(player.getXRot() / 180.0F * 3.1415927F) * dist);
            double gatherZ = player.getZ() + (Math.cos((player.getYRot() + look) / 180.0F * 3.1415927F) * Math.cos(player.getXRot() / 180.0F * 3.1415927F) * dist);

            SimpleParticleType particle = ParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = ParticleTypes.LARGE_SMOKE;

            if (rand.nextInt(2) == 0) {
                player.level.addParticle(particle,
                        gatherX + ((rand.nextFloat() * range) - (range/2)),
                        player.getY() + 0.7F,
                        gatherZ + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.01F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }
        }
    }
}
