package net.tropicraft.core.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.tropicraft.Constants;

@EventBusSubscriber(modid = Constants.MODID)
public class FireArmorItem extends ArmorItem {
    public FireArmorItem(ArmorItem.Type slotType, Properties properties) {
        super(TropicraftArmorMaterials.FIRE_ARMOR, slotType, properties);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        tickArmor(player, EquipmentSlot.HEAD);
        tickArmor(player, EquipmentSlot.CHEST);
        tickArmor(player, EquipmentSlot.LEGS);
        tickArmor(player, EquipmentSlot.FEET);
    }

    private static void tickArmor(Player player, EquipmentSlot slot) {
        ItemStack itemStack = player.getItemBySlot(slot);
        if (itemStack.getItem() instanceof FireArmorItem item) {
            item.onArmorTick(itemStack, player.level(), player, slot);
        }
    }

    private void onArmorTick(ItemStack stack, Level world, Player player, EquipmentSlot slot) {
        if (world.isClientSide) {
            clientTick(player);
        } else {
            if (player.isOnFire()) player.clearFire();

            // Repair in the sun?
            int factor = (int) (40 / (0.001 + world.getLightLevelDependentMagicValue(player.blockPosition())));
            if (world.getGameTime() % (factor) == 0 && world.canSeeSkyFromBelowWater(new BlockPos(Mth.floor(player.getX()), Mth.floor(player.getY() + 1), Mth.floor(player.getZ())))) {
                //repair!
                stack.hurtAndBreak(-1, player, slot);
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

        float range = 0.2f;
        float speed = 0.08f;

        RandomSource rand = RandomSource.create();

        int extraRand = 0;

        Vec3 motion = player.getDeltaMovement();
        double plSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);

        if (plSpeed < 0.1f) {
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
                FluidState fluid = player.level().getFluidState(BlockPos.containing(player.getX() + x, player.getY() - 2, player.getZ() + z));
                onLava = fluid.is(FluidTags.LAVA);
            }
            FluidState fluid2 = player.level().getFluidState(BlockPos.containing(player.getX() + x, player.getY() - 1, player.getZ() + z));
            if (fluid2.is(FluidTags.LAVA)) {
                inLava = true;
            }

            // why do we do this on the client?
            if (onLava && !inLava) {
                player.setDeltaMovement(motion.multiply(1, 0, 1));
                player.setOnGround(true);
            }

            // why do we do this on the client???????
            if (inLava) {
                if (plSpeed < 0.4) {
                    player.setDeltaMovement(motion.multiply(1.5, 1.5, 1.5));
                }
            }

            float look = player.level().getGameTime() * (10 + (onLava ? 10 : 0));
            double dist = 1.0f;

            double gatherX = player.getX();
            double gatherY = player.getBoundingBox().minY;
            double gatherZ = player.getZ();

            double motionX = ((rand.nextFloat() * speed) - (speed / 2));
            double motionZ = ((rand.nextFloat() * speed) - (speed / 2));

            int numFeetParticles = particles == ParticleStatus.DECREASED ? 2 : 11;

            for (int i = 0; i < numFeetParticles + (onLava ? 5 : 0); i++) {
                motionX = (-Math.sin((look) / 180.0f * Mth.PI) * Math.cos(0 / 180.0f * Mth.PI) * (speed + (0.1 * rand.nextDouble())));
                motionZ = (Math.cos((look) / 180.0f * Mth.PI) * Math.cos(0 / 180.0f * Mth.PI) * (speed + (0.1 * rand.nextDouble())));

                SimpleParticleType particle = ParticleTypes.FLAME;
                if (rand.nextInt(22) == 0) particle = ParticleTypes.LARGE_SMOKE;

                if (onLava || rand.nextInt(1 + extraRand) == 0) {
                    Vec3 motion1 = player.getDeltaMovement();
                    player.level().addParticle(particle,
                            gatherX + ((rand.nextFloat() * range) - (range / 2)),
                            gatherY + ((rand.nextFloat() * range) - (range / 2)),
                            gatherZ + ((rand.nextFloat() * range) - (range / 2)),
                            motion1.x + motionX,
                            0.01f,
                            motion1.z + motionZ);

                    player.level().addParticle(particle,
                            (double) gatherX + ((rand.nextFloat() * range) - (range / 2)),
                            (double) gatherY + ((rand.nextFloat() * range) - (range / 2)),
                            (double) gatherZ + ((rand.nextFloat() * range) - (range / 2)),
                            motion1.x - motionX,
                            0.01f,
                            motion1.z - motionZ);
                }
            }
        } else if (this == TropicraftItems.FIRE_LEGGINGS.get()) {
            SimpleParticleType particle = ParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = ParticleTypes.LARGE_SMOKE;

            if (rand.nextInt(3 + extraRand) == 0) {
                player.level().addParticle(particle,
                        player.getX() + ((rand.nextFloat() * range) - (range / 2)),
                        player.getY() - 0.8f + ((rand.nextFloat() * range) - (range / 2)),
                        player.getZ() + ((rand.nextFloat() * range) - (range / 2)),
                        ((rand.nextFloat() * speed) - (speed / 2)),
                        -0.05f,
                        ((rand.nextFloat() * speed) - (speed / 2)));
            }
        } else if (this == TropicraftItems.FIRE_CHESTPLATE.get()) {
            float look = -180.0f;
            double dist = 0.5f;

            double gatherX = player.getX() + (-Math.sin((player.getYRot() + look) / 180.0f * Mth.PI) * Math.cos(player.getXRot() / 180.0f * Mth.PI) * dist);
            double gatherZ = player.getZ() + (Math.cos((player.getYRot() + look) / 180.0f * Mth.PI) * Math.cos(player.getXRot() / 180.0f * Mth.PI) * dist);

            SimpleParticleType particle = ParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = ParticleTypes.LARGE_SMOKE;

            if (rand.nextInt(1 + extraRand) == 0) {
                player.level().addParticle(particle,
                        gatherX + ((rand.nextFloat() * range) - (range / 2)),
                        player.getY() - 0.4f + ((rand.nextFloat() * range) - (range / 2)),
                        gatherZ + ((rand.nextFloat() * range) - (range / 2)),
                        ((rand.nextFloat() * speed) - (speed / 2)),
                        -0.01f,
                        ((rand.nextFloat() * speed) - (speed / 2)));
            }
        } else if (this == TropicraftItems.FIRE_HELMET.get()) {
            float look = -180.0f;
            double dist = 0.5f;

            range = 2.0f;

            double gatherX = player.getX() + (-Math.sin((player.getYRot() + look) / 180.0f * Mth.PI) * Math.cos(player.getXRot() / 180.0f * Mth.PI) * dist);
            double gatherZ = player.getZ() + (Math.cos((player.getYRot() + look) / 180.0f * Mth.PI) * Math.cos(player.getXRot() / 180.0f * Mth.PI) * dist);

            SimpleParticleType particle = ParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = ParticleTypes.LARGE_SMOKE;

            if (rand.nextInt(2) == 0) {
                player.level().addParticle(particle,
                        gatherX + ((rand.nextFloat() * range) - (range / 2)),
                        player.getY() + 0.7f,
                        gatherZ + ((rand.nextFloat() * range) - (range / 2)),
                        ((rand.nextFloat() * speed) - (speed / 2)),
                        -0.01f,
                        ((rand.nextFloat() * speed) - (speed / 2)));
            }
        }
    }
}
