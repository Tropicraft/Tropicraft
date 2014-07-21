package net.tropicraft.item.armor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFireArmor extends ItemTropicraftArmor {

	public ItemFireArmor(ArmorMaterial material, int renderIndex, int armorType) {
		super(material, renderIndex, armorType);
		
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

            //System.out.println(10D / (0.001D+player.worldObj.getSunBrightness(1F)));

            int factor = (int)(40D / (0.001D+this.getSunBrightness(world, 1F)));
            //System.out.println(factor);
            if (world.getWorldTime() % (factor) == 0 && world.canBlockSeeTheSky(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY+1), MathHelper.floor_double(player.posZ))) {

                //repair!
                itemStack.damageItem(-1, player);
                //System.out.println("repair! " + itemStack.getItemDamage());
            }
        }

    }

    private float getSunBrightness(World world, float par1)
    {
        float f1 = world.getCelestialAngle(par1);
        float f2 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.2F);

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        f2 = 1.0F - f2;
        f2 = (float)((double)f2 * (1.0D - (double)(world.getRainStrength(par1) * 5.0F) / 16.0D));
        f2 = (float)((double)f2 * (1.0D - (double)(world.getWeightedThunderStrength(par1) * 5.0F) / 16.0D));
        return f2 * 0.8F + 0.2F;
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
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor,
            DamageSource source, double damage, int slot) {

        //System.out.println(source.damageType);

        if(source == DamageSource.inFire || source == DamageSource.lava)
            return new ArmorProperties(10, Integer.MAX_VALUE, Integer.MAX_VALUE);
        else
            return new ArmorProperties(10, 0.15, Integer.MAX_VALUE);
    }

    @SideOnly(Side.CLIENT)
    public static void getFXLayers() {
        //fxLayers
        Field field = null;

        try {
            field = (EffectRenderer.class).getDeclaredField("field_78876_b");
            field.setAccessible(true);
            fxLayers = (List[])field.get(FMLClientHandler.instance().getClient().effectRenderer);
        } catch (Exception ex) {
            try {
                field = (EffectRenderer.class).getDeclaredField("fxLayers");
                field.setAccessible(true);
                fxLayers = (List[])field.get(FMLClientHandler.instance().getClient().effectRenderer);
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void clientTick(EntityPlayer player) {
        if (fxLayers == null) getFXLayers();

        float range = 0.2F;
        float speed = 0.08F;

        Random rand = new Random();
        World worldRef = player.worldObj;

        int extraRand = 0;

        double plSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);

        if (plSpeed < 0.1F) {
            extraRand = 7;
        }

        if (this == TCItemRegistry.fireBoots) {

            boolean onLava = false;
            boolean inLava = false;
            //for (int x = -1; x < 2; x++) {
                //for (int z = -1; z < 2; z++) {
                    int x = 0;
                    int z = 0;
                    if (player.motionY < 0) {
                        Block block = player.worldObj.getBlock(MathHelper.floor_double(player.posX + x), MathHelper.floor_double(player.posY-2), MathHelper.floor_double(player.posZ + z));
                        if (block != null && block.getMaterial() == Material.lava) {
                            onLava = true;
                            //break;
                        }
                    }
                    Block block2 = player.worldObj.getBlock(MathHelper.floor_double(player.posX + x), MathHelper.floor_double(player.posY-1), MathHelper.floor_double(player.posZ + z));
                    if (block2 != null && block2.getMaterial() == Material.lava) {
                        inLava = true;
                        //break;
                    }
                //}
            //}
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

            float look = player.worldObj.getWorldTime() * (10 + (onLava ? 10 : 0));
            double dist = 1F;

            double gatherX = player.posX;
            double gatherY = player.boundingBox.minY;
            double gatherZ = player.posZ;

            double motionX = ((rand.nextFloat() * speed) - (speed/2));
            double motionZ = ((rand.nextFloat() * speed) - (speed/2));



            for (int i = 0; i < 1 + (onLava ? 5 : 0); i++) {

                motionX = ((double)(-Math.sin((look) / 180.0F * 3.1415927F) * Math.cos(0 / 180.0F * 3.1415927F)) * (speed + (0.1 * rand.nextDouble())));
                motionZ = ((double)(Math.cos((look) / 180.0F * 3.1415927F) * Math.cos(0 / 180.0F * 3.1415927F)) * (speed + (0.1 * rand.nextDouble())));

                String particle = "flame";
                if (rand.nextInt(2) == 0) particle = "smoke";

                if (onLava || rand.nextInt(1 + extraRand) == 0) {
                    player.worldObj.spawnParticle(particle,
                            (double)gatherX + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherY + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherZ + ((rand.nextFloat() * range) - (range/2)),
                            player.motionX + motionX,
                            0.01F,
                            player.motionZ + motionZ);

                    player.worldObj.spawnParticle(particle,
                            (double)gatherX + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherY + ((rand.nextFloat() * range) - (range/2)),
                            (double)gatherZ + ((rand.nextFloat() * range) - (range/2)),
                            player.motionX - motionX,
                            0.01F,
                            player.motionZ - motionZ);
                }
            }


        } else if (this == TCItemRegistry.fireLeggings) {

            String particle = "flame";
            if (rand.nextInt(2) == 0) particle = "smoke";

            if (rand.nextInt(3 + extraRand) == 0) {
                player.worldObj.spawnParticle(particle,
                        (double)player.posX + ((rand.nextFloat() * range) - (range/2)),
                        (double)player.posY - 0.8F + ((rand.nextFloat() * range) - (range/2)),
                        (double)player.posZ + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.05F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }

        } else if (this == TCItemRegistry.fireChestplate) {

            float look = -180F;
            double dist = 0.5F;

            double gatherX = player.posX + ((double)(-Math.sin((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F)) * dist);
            //double gatherY = player.posY-0.5 + (double)(-MathHelper.sin(player.rotationPitch / 180.0F * 3.1415927F) * dist) - 0D; //center.posY - 0D;
            double gatherZ = player.posZ + ((double)(Math.cos((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F)) * dist);

            String particle = "flame";
            if (rand.nextInt(2) == 0) particle = "smoke";

            if (rand.nextInt(3 + extraRand) == 0) {
                player.worldObj.spawnParticle(particle,
                        (double)gatherX + ((rand.nextFloat() * range) - (range/2)),
                        (double)player.posY - 0.4F + ((rand.nextFloat() * range) - (range/2)),
                        (double)gatherZ + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.01F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }

        } else if (this == TCItemRegistry.fireHelmet) {

            float look = -180F;
            double dist = 0.5F;

            range = 2F;

            double gatherX = player.posX + ((double)(-Math.sin((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F)) * dist);
            //double gatherY = player.posY + 0.5 + (double)(-MathHelper.sin(player.rotationPitch / 180.0F * 3.1415927F) * dist) - 0D; //center.posY - 0D;
            double gatherZ = player.posZ + ((double)(Math.cos((player.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(player.rotationPitch / 180.0F * 3.1415927F)) * dist);

            String particle = "flame";
            if (rand.nextInt(2) == 0) particle = "smoke";

            if (rand.nextInt(2) == 0) {
                player.worldObj.spawnParticle(particle,
                        (double)gatherX + ((rand.nextFloat() * range) - (range/2)),
                        (double)player.posY + 0.7F/* + ((rand.nextFloat() * range) - (range/2))*/,
                        (double)gatherZ + ((rand.nextFloat() * range) - (range/2)),
                        ((rand.nextFloat() * speed) - (speed/2)),
                        -0.01F,
                        ((rand.nextFloat() * speed) - (speed/2)));
            }

            if (fxLayers != null)
            {
                //Build in particles
                for (int layer = 0; layer < 4; layer++)
                {
                    for (int i = 0; i < fxLayers[layer].size(); i++)
                    {
                        EntityFX entity1 = (EntityFX)fxLayers[layer].get(i);



                        if (entity1 instanceof EntityFlameFX && player.getDistanceToEntity(entity1) < 4F && entity1.posY > player.posY) {
                            if (player.getDistanceToEntity(entity1) < 2F) {
                               //TODO if (player.worldObj.getWorldTime() % 4 == 0) WeatherUtil.setParticleAge(entity1, WeatherUtil.getParticleAge(entity1) - 3);
                            }
                            if (player.getDistanceToEntity(entity1) < 2F || rand.nextInt(5) == 0) moveEnt(entity1, player, false);
                        }
                    }
                }
            }

        }
    }

    public void moveEnt(Entity ent, Entity center, boolean shield) {
        //get velocity?
        //to get "relative" velocity: Vec3D.createVector(motX, mot, motZ); speed.rotateAroundY(yaw); speed.rotateAroundX(pitch); <- is this right?
        //or??
        //Vec3D.create(motX, motY, motZ); .rotX(yaw) .rotY(pitch) ... Model.rotPointX = v.x; .rotPointY = v.y; .rotPointZ = v.z;
        float look = 0F;
        //int height = 10;
        double dist = 0.1F;
        double gatherX = center.posX + ((double)(-Math.sin((center.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(center.rotationPitch / 180.0F * 3.1415927F)) * dist);
        double gatherY = center.posY+0.8 + (double)(-MathHelper.sin(center.rotationPitch / 180.0F * 3.1415927F) * dist) - 0D; //center.posY - 0D;
        double gatherZ = center.posZ + ((double)(Math.cos((center.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(center.rotationPitch / 180.0F * 3.1415927F)) * dist);
        double entDist = ent.getDistance(gatherX, gatherY, gatherZ);

        if (entDist > -1) {
            double vecX = gatherX - ent.posX;
            double vecY = gatherY - ent.posY;// + (double)(tNode.nextNode.bodyPiece.height / 2.0F) - (tNode.bodyPiece.posY + (double)(tNode.bodyPiece.height / 2.0F));
            double vecZ = gatherZ - ent.posZ;
            /*Vec3D vec = Vec3D.createVector(vecX, vecY, vecZ);
            vec.rotateAroundX(??);
            double a = vec.xCoord;*/
            //vecX = ent.posX - gatherX;
            //vecY = ent.posY - gatherY;
            //vecZ = ent.posZ - gatherZ;
            double var1 = 1.0D;
            /*double var2 = ent.prevRotationPitch + (ent.rotationPitch - ent.prevRotationPitch) * var1;
            double var3 = ent.prevRotationYaw + (ent.rotationYaw - ent.prevRotationYaw) * var1;
            double var4 = Math.cos(-var3 * 0.017453292F - 3.1415927F);
            double var5 = Math.sin(-var3 * 0.017453292F - 3.1415927F);*/
            //return Vec3D.createVector((double)(var5 * var6), (double)var7, (double)(var4 * var6));
            //vecX = center.posX - ent.posX;
            //vecY = center.posY - ent.posY;// + (double)(tNode.nextNode.bodyPiece.height / 2.0F) - (tNode.bodyPiece.posY + (double)(tNode.bodyPiece.height / 2.0F));
            //vecZ = center.posZ - ent.posZ;
            //mod_MovePlus.displayMessage(new StringBuilder().append("vecX: " + vecX).toString());
            //tNode.bodyPiece.vecX = vecX;//tNode.bodyPiece.posX - tNode.nextNode.bodyPiece.posX;
            //tNode.bodyPiece.vecY = vecY;//tNode.bodyPiece.posY - tNode.nextNode.bodyPiece.posY;
            //tNode.bodyPiece.vecZ = vecZ;//tNode.bodyPiece.posZ - tNode.nextNode.bodyPiece.posZ;
            double var9 = (double)MathHelper.sqrt_double(vecX * vecX + vecY * vecY + vecZ * vecZ);
            int maxDist = 10;
            int adjDist = ((int)entDist-maxDist);

            if (adjDist < 0) {
                adjDist = 10;
            }

            double speed = 0.02D;// * adjDist / maxDist;//* (Math.sqrt(dist)/10.0F);
            //double speed = 0.02D;
            double newspeed = speed;// * (Math.sqrt(entDist)/1.0F);
            //tNode.bodyPiece.posX += vecX / var9 * newspeed;
            //tNode.bodyPiece.posY += vecY / var9 * newspeed;
            //tNode.bodyPiece.posZ += vecZ / var9 * newspeed;
            //tNode.bodyPiece.setPosition(tNode.bodyPiece.posX, tNode.bodyPiece.posY, tNode.bodyPiece.posZ);
            float pitch = (float)((Math.atan2(vecX, vecZ) * 180D) / 3.1415927410125732D);
            float f = (float)((Math.atan2(vecZ, vecX) * 180D) / 3.1415927410125732D);
            float angle = f;
            angle += 180;
            //angle += 15;
            //pitch += 180;

            /*for(angle = f; angle < -180F; angle += 360F) { }
            for(; angle >= 180F; angle -= 360F) { }
            for(pitch = pitch; pitch < -180F; pitch += 360F) { }
            for(; pitch >= 180F; pitch -= 360F) { }*/

            for(angle = f; angle < 0F; angle += 360F) { }

            for(; angle >= 360F; angle -= 360F) { }

            for(pitch = pitch; pitch < 0F; pitch += 180F) { }

            for(; pitch >= 180F; pitch -= 180F) { }

            /*System.out.println(angle);
            System.out.println(pitch);
            System.out.println("-");*/
            //enable once 3d trig works
            if (!shield) {
                angle = angle - 15;
            } else if (shield/* || activeMode == 1*/) {
                /*if (shockWaveCharging) {
                    angle = angle - 5;
                } else {*/
                    angle = angle - 40;
                    speed = 0.03D;
                    if (ent.worldObj.rand.nextInt(5) == 0) {
                        angle += 20;
                        speed = 0.06F;
                    }
                //}
            }
            //pitch = pitch - 10;
            //float rad_angle =
            float rad_angle = angle * 0.01745329F;// * 3.1415927410125732F * 2F;
            float rad_pitch = pitch * 0.01745329F * 2F;// * 3.1415927410125732F * 2F;
            float uhh = 1.0F;
            float newY = uhh * (float)Math.sin(rad_pitch);
            float projection = uhh * (float)Math.cos(rad_pitch);
            projection = 1.0F;
            //System.out.println(projection);
            float newX = projection * (float)Math.cos(rad_angle);
            float newZ = projection * (float)Math.sin(rad_angle);
            float newVecX = newX / uhh;
            float newVecY = newY / uhh;
            float newVecZ = newZ / uhh;
            //float var6 = 1.0F;//(float)-Math.cos(-pitch * 0.017453292F);
            //float var7 = (float)Math.sin(-pitch * 0.017453292F);
            //ent.motionX -= f3 * var6 * speed;//vecX / var9 * newspeed;
            //ent.motionY -= var7 * speed;//vecY / var9 * newspeed;
            //ent.motionZ += f4 * var6 * speed;// / var9 * newspeed;
            ent.motionY += vecY / var9 * speed;
            ent.motionX += newVecX * speed;
            //ent.motionY += vecY * speed;
            ent.motionZ += newVecZ * speed;
            //ent.posX += ent.motionX;
            //ent.posZ += ent.motionZ;


            if (!shield) {
                //angle = angle - 5;
            } else if (shield/* || activeMode == 1*/) {
                if (ent.worldObj.rand.nextInt(10) == 0) {
                    ent.motionY+= 0.05F;

                }
            }



        } else {

            if (true) return;

            look = 40F;
            //int height = 10;
            dist = 1F;
            gatherX = center.posX + ((double)(-Math.sin((center.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(center.rotationPitch / 180.0F * 3.1415927F)) * dist);
            gatherY = center.posY-0.5 + (double)(-MathHelper.sin(center.rotationPitch / 180.0F * 3.1415927F) * dist) - 0D; //center.posY - 0D;
            gatherZ = center.posZ + ((double)(Math.cos((center.rotationYaw+look) / 180.0F * 3.1415927F) * Math.cos(center.rotationPitch / 180.0F * 3.1415927F)) * dist);
            double vecX = ent.posX - gatherX;
            double vecY = ent.posY - gatherY;// + (double)(tNode.nextNode.bodyPiece.height / 2.0F) - (tNode.bodyPiece.posY + (double)(tNode.bodyPiece.height / 2.0F));
            double vecZ = ent.posZ - gatherZ;
            dist = 2F;
            ent.posX = gatherX + vecX * dist;
            ent.posY = gatherY + vecY * dist;
            ent.posZ = gatherZ + vecZ * dist;
            ent.setPosition(ent.posX, ent.posY, ent.posZ);
            //entDist = ent.getDistance(gatherX, gatherY, gatherZ);
        }
    }
}
