package net.tropicraft.core.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.registry.ItemRegistry;

import java.util.Random;

public class FireArmorItem extends TropicraftArmorItem {
    public FireArmorItem(EquipmentSlotType slotType, Properties properties) {
        super(ArmorMaterials.FIRE_ARMOR, slotType, properties);
    }

    // TODO waiting on Forge
    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (world.isRemote) {
            clientTick(player);
        } else {
            if (player.isBurning()) player.extinguish();

            // Repair in the sun?
            int factor = (int)(40D / (0.001D + world.getSunBrightness(1.0F)));
            if (world.getGameTime() % (factor) == 0 && world.canBlockSeeSky(new BlockPos(MathHelper.floor(player.posX), MathHelper.floor(player.posY+1), MathHelper.floor(player.posZ)))) {
                //repair!
                stack.damageItem(-1, player, (e) -> {
                    e.sendBreakAnimation(EquipmentSlotType.MAINHAND);
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
    public void clientTick(PlayerEntity player) {
        // Don't show fire particles underwater
        if (player.isInWater()) return;

        float range = 0.2F;
        float speed = 0.08F;

        Random rand = new Random();
        World worldRef = player.world;

        int extraRand = 0;

        final Vec3d motion = player.getMotion();
        double plSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);

        if (plSpeed < 0.1F) {
            extraRand = 7;
        }

        /** 0 for all, 1 for minimal, 2 for off */
        ParticleStatus particles = Minecraft.getInstance().gameSettings.particles;
        if (particles == ParticleStatus.MINIMAL) return;

        if (this == TropicraftItems.FIRE_BOOTS.get()) {
            boolean onLava = false;
            boolean inLava = false;
            //for (int x = -1; x < 2; x++) {
            //for (int z = -1; z < 2; z++) {
            int x = 0;
            int z = 0;
            if (motion.y < 0) {
                BlockState state = player.world.getBlockState(new BlockPos(MathHelper.floor(player.posX + x), MathHelper.floor(player.posY-2), MathHelper.floor(player.posZ + z)));
                if (state.getMaterial() == Material.LAVA) {
                    onLava = true;
                }
            }
            BlockState block2 = player.world.getBlockState(new BlockPos(MathHelper.floor(player.posX + x), MathHelper.floor(player.posY-1), MathHelper.floor(player.posZ + z)));
            if (block2.getMaterial() == Material.LAVA) {
                inLava = true;
            }

            // why do we do this on the client?
            if (onLava && !inLava) {
                player.setMotion(motion.mul(1, 0, 1));
                player.onGround = true;
            }

            // why do we do this on the client???????
            if (inLava) {
                if (plSpeed < 0.4D) {
                    player.setMotion(motion.mul(1.5D, 1.5D, 1.5D));
                }
            }

            float look = player.world.getGameTime() * (10 + (onLava ? 10 : 0));
            double dist = 1F;

            double gatherX = player.posX;
            double gatherY = player.getBoundingBox().minY;
            double gatherZ = player.posZ;

            double motionX = ((rand.nextFloat() * speed) - (speed/2));
            double motionZ = ((rand.nextFloat() * speed) - (speed/2));

            final int numFeetParticles = particles == ParticleStatus.DECREASED ? 2 : 11;

            for (int i = 0; i < numFeetParticles + (onLava ? 5 : 0); i++) {
                motionX = (-Math.sin((look) / 180.0F * 3.1415927F) * Math.cos(0 / 180.0F * 3.1415927F) * (speed + (0.1 * rand.nextDouble())));
                motionZ = (Math.cos((look) / 180.0F * 3.1415927F) * Math.cos(0 / 180.0F * 3.1415927F) * (speed + (0.1 * rand.nextDouble())));

                BasicParticleType particle = ParticleTypes.FLAME;
                if (rand.nextInt(22) == 0) particle = ParticleTypes.LARGE_SMOKE;

                if (onLava || rand.nextInt(1 + extraRand) == 0) {
                    Vec3d motion1 = player.getMotion();
                    player.world.addParticle(particle,
                            gatherX + ((rand.nextFloat() * range) - (range/2)),
                            gatherY + ((rand.nextFloat() * range) - (range/2)),
                            gatherZ + ((rand.nextFloat() * range) - (range/2)),
                            motion1.x + motionX,
                            0.01F,
                            motion1.z + motionZ);

                    player.world.addParticle(particle,
                            (double)gatherX + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherY + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherZ + ((rand.nextFloat() * range) - (range/2)),
                            motion1.x - motionX,
                            0.01F,
                            motion1.z - motionZ);
                }
            }

        } else if (this == TropicraftItems.FIRE_LEGGINGS.get()) {
            BasicParticleType particle = ParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = ParticleTypes.LARGE_SMOKE;

            if (rand.nextInt(3 + extraRand) == 0) {
                player.world.addParticle(particle,
                        player.posX + ((rand.nextFloat() * range) - (range/2)),
                        player.posY - 0.8F + ((rand.nextFloat() * range) - (range/2)),
                        player.posZ + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.05F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }
        } else if (this == TropicraftItems.FIRE_CHESTPLATE.get()) {
            float look = -180F;
            double dist = 0.5F;

            double gatherX = player.posX + (-Math.sin((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F) * dist);
            double gatherZ = player.posZ + (Math.cos((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F) * dist);

            BasicParticleType particle = ParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = ParticleTypes.LARGE_SMOKE;

            if (rand.nextInt(1 + extraRand) == 0) {
                player.world.addParticle(particle,
                        gatherX + ((rand.nextFloat() * range) - (range/2)),
                        player.posY - 0.4F + ((rand.nextFloat() * range) - (range/2)),
                        gatherZ + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.01F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }

        } else if (this == TropicraftItems.FIRE_HELMET.get()) {
            float look = -180F;
            double dist = 0.5F;

            range = 2F;

            double gatherX = player.posX + (-Math.sin((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F) * dist);
            double gatherZ = player.posZ + (Math.cos((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F) * dist);

            BasicParticleType particle = ParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = ParticleTypes.LARGE_SMOKE;

            if (rand.nextInt(2) == 0) {
                player.world.addParticle(particle,
                        gatherX + ((rand.nextFloat() * range) - (range/2)),
                        player.posY + 0.7F,
                        gatherZ + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.01F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }
        }
    }
}
