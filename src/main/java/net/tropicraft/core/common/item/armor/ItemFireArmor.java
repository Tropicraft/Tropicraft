package net.tropicraft.core.common.item.armor;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.registry.ItemRegistry;

public class ItemFireArmor extends ItemTropicraftArmor {

    public ItemFireArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot) {
        super(material, renderIndex, slot);

        this.setMaxDamage(300);
    }

    /**
     * Called to tick armor in the armor slot. Override to do something
     *
     * @param world
     * @param player
     * @param itemStack
     */
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        if (world.isRemote) {
            clientTick(player);
        } else {
            if (player.isBurning()) player.extinguish();

            int factor = (int)(40D / (0.001D + world.getSunBrightnessFactor(1.0F)));
            if (world.getWorldTime() % (factor) == 0 && world.canBlockSeeSky(new BlockPos(MathHelper.floor(player.posX), MathHelper.floor(player.posY+1), MathHelper.floor(player.posZ)))) {

                //repair!
                itemStack.damageItem(-1, player);
                //System.out.println("repair! " + itemStack.getItemDamage());
            }
        }

    }

    @Override
    public void damageArmor(EntityLivingBase player, ItemStack stack,
            DamageSource source, int damage, int slot) {
        if(source == DamageSource.inFire || source == DamageSource.lava) {
            //cheap way to slow the damage
            //if (player.worldObj.getWorldTime() % 2 == 0) {
            stack.damageItem(damage, player);
        } else {

        }
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        if (source == DamageSource.inFire || source == DamageSource.lava) {
            // Invincible to fire damage
            return new ArmorProperties(10, 1.0, Integer.MAX_VALUE);
        } else {
            return super.getProperties(player, armor, source, damage, slot);
        }
    }

    @SideOnly(Side.CLIENT)
    public void clientTick(EntityPlayer player) {

        // Don't show fire particles underwater
        if (player.isInWater()) return;        

        float range = 0.2F;
        float speed = 0.08F;

        Random rand = new Random();
        World worldRef = player.world;

        int extraRand = 0;

        double plSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);

        if (plSpeed < 0.1F) {
            extraRand = 7;
        }

        /** 0 for all, 1 for minimal, 2 for off */
        int particleSetting = Minecraft.getMinecraft().gameSettings.particleSetting;

        if (particleSetting == 2) return;

        if (this == ItemRegistry.fireBoots) {

            boolean onLava = false;
            boolean inLava = false;
            //for (int x = -1; x < 2; x++) {
            //for (int z = -1; z < 2; z++) {
            int x = 0;
            int z = 0;
            if (player.motionY < 0) {
                IBlockState state = player.world.getBlockState(new BlockPos(MathHelper.floor(player.posX + x), MathHelper.floor(player.posY-2), MathHelper.floor(player.posZ + z)));
                if (state != null && state.getMaterial() == Material.LAVA) {
                    onLava = true;
                    //break;
                }
            }
            IBlockState block2 = player.world.getBlockState(new BlockPos(MathHelper.floor(player.posX + x), MathHelper.floor(player.posY-1), MathHelper.floor(player.posZ + z)));
            if (block2 != null && block2.getMaterial() == Material.LAVA) {
                inLava = true;
            }
            if (onLava && !inLava) {
                player.motionY = 0F;
                player.onGround = true;
            }

            if (inLava) {
                if (plSpeed < 0.4D) {
                    player.motionX *= 1.5D;
                    player.motionY *= 1.5D;
                    player.motionZ *= 1.5D;
                }
            }

            float look = player.world.getWorldTime() * (10 + (onLava ? 10 : 0));
            double dist = 1F;

            double gatherX = player.posX;
            double gatherY = player.getEntityBoundingBox().minY;
            double gatherZ = player.posZ;

            double motionX = ((rand.nextFloat() * speed) - (speed/2));
            double motionZ = ((rand.nextFloat() * speed) - (speed/2));

            int numFeetParticles = particleSetting == 1 ? 2 : 11;

            for (int i = 0; i < numFeetParticles + (onLava ? 5 : 0); i++) {
                motionX = ((double)(-Math.sin((look) / 180.0F * 3.1415927F) * Math.cos(0 / 180.0F * 3.1415927F)) * (speed + (0.1 * rand.nextDouble())));
                motionZ = ((double)(Math.cos((look) / 180.0F * 3.1415927F) * Math.cos(0 / 180.0F * 3.1415927F)) * (speed + (0.1 * rand.nextDouble())));

                EnumParticleTypes particle = EnumParticleTypes.FLAME;
                if (rand.nextInt(22) == 0) particle = EnumParticleTypes.SMOKE_LARGE;

                if (onLava || rand.nextInt(1 + extraRand) == 0) {
                    player.world.spawnParticle(particle,
                            (double)gatherX + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherY + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherZ + ((rand.nextFloat() * range) - (range/2)),
                            player.motionX + motionX,
                            0.01F,
                            player.motionZ + motionZ);

                    player.world.spawnParticle(particle,
                            (double)gatherX + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherY + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherZ + ((rand.nextFloat() * range) - (range/2)),
                            player.motionX - motionX,
                            0.01F,
                            player.motionZ - motionZ);
                }
            }

        } else if (this == ItemRegistry.fireLeggings) {
            EnumParticleTypes particle = EnumParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = EnumParticleTypes.SMOKE_LARGE;

            if (rand.nextInt(3 + extraRand) == 0) {
                player.world.spawnParticle(particle,
                        (double)player.posX + ((rand.nextFloat() * range) - (range/2)),
                        (double)player.posY - 0.8F + ((rand.nextFloat() * range) - (range/2)),
                        (double)player.posZ + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.05F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }

        } else if (this == ItemRegistry.fireChestplate) {

            float look = -180F;
            double dist = 0.5F;

            double gatherX = player.posX + ((double)(-Math.sin((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F)) * dist);
            double gatherZ = player.posZ + ((double)(Math.cos((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F)) * dist);

            EnumParticleTypes particle = EnumParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = EnumParticleTypes.SMOKE_LARGE;

            if (rand.nextInt(1 + extraRand) == 0) {
                player.world.spawnParticle(particle,
                        (double)gatherX + ((rand.nextFloat() * range) - (range/2)),
                        (double)player.posY - 0.4F + ((rand.nextFloat() * range) - (range/2)),
                        (double)gatherZ + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.01F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }

        } else if (this == ItemRegistry.fireHelmet) {
            float look = -180F;
            double dist = 0.5F;

            range = 2F;

            double gatherX = player.posX + ((double)(-Math.sin((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F)) * dist);
            double gatherZ = player.posZ + ((double)(Math.cos((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F)) * dist);

            EnumParticleTypes particle = EnumParticleTypes.FLAME;
            if (rand.nextInt(2) == 0) particle = EnumParticleTypes.SMOKE_LARGE;

            if (rand.nextInt(2) == 0) {
                player.world.spawnParticle(particle,
                        (double)gatherX + ((rand.nextFloat() * range) - (range/2)),
                        (double)player.posY + 0.7F,
                        (double)gatherZ + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.01F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }
        }
    }
}
