package net.tropicraft.entity.underdasea;

import static java.lang.Math.PI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityEagleRay extends EntityTropicraftWaterMob {

    /**
     * Number of joints the wings have. End points included.
     */
    public static final int WING_JOINTS = 10;
    /**
     * Number of ticks that one wing animation cycle takes.
     */
    public static final int WING_CYCLE_TICKS = 3*20; // 3 seconds
    /**
     * How many sine function phases to go through. Higher = more wave crests.
     */
    public static final float PHASES = 0.33f;
    
    /**
     * Wave amplitudes at the joints, between -1 and 1.
     */
    private float[] wingAmplitudes = new float[WING_JOINTS];
    /**
     * Wave amplitudes at the joints, between -1 and 1, from previous tick.
     */
    private float[] prevWingAmplitudes = new float[WING_JOINTS];
    
    /**
     * Counter from 0 to WING_CYCLE_TICKS for wing animation progress.
     */
    private int animationTicks;
    
    public EntityEagleRay(World world) {
        super(world);
        this.setSize(1f, 0.25f);
        this.type = WaterMobType.OCEAN_DWELLER;
    }

    public EntityEagleRay(World par1World, WaterMobType type) {
        super(par1World, type);
        this.setSize(1f, 0.25f);
    }

    @Override
    public Entity getEntityToAttack() {
        return null;
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        
        if (worldObj.isRemote) {
            if (animationTicks < WING_CYCLE_TICKS) {
                animationTicks++;
            } else {
                animationTicks = 0;
            }

            updateWingAmplitudes();
        }
    }
    
    private void updateWingAmplitudes() {
        float[] temp = prevWingAmplitudes;
        prevWingAmplitudes = wingAmplitudes;
        wingAmplitudes = temp;
        
        // 1 because amplitude at the wing base is 0
        for (int i = 1; i < WING_JOINTS; i++) {
            wingAmplitudes[i] = amplitudeFunc(i);
        }
    }
    
    private float decayFunc(float n) {
        return n/(WING_JOINTS-1f);
    }
    
    private float amplitudeFunc(float n) {
        double angle = 2*PI*n/(WING_JOINTS-1f);
        return decayFunc(n)*MathHelper.sin((float) (getAnimationProgress()*2*PI + PHASES*angle));
    }

    private float getAnimationProgress() {
        return animationTicks/(float)WING_CYCLE_TICKS;
    }

    public float[] getWingAmplitudes() {
        return wingAmplitudes;
    }

    public float[] getPrevWingAmplitudes() {
        return prevWingAmplitudes;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
    }

    @Override
    protected int attackStrength() {
        return 0;
    }
}
