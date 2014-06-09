package net.tropicraft.block.tileentity;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.particle.ParticleRegistry;
import extendedrenderer.particle.behavior.ParticleBehaviors;
import extendedrenderer.particle.entity.EntityRotFX;

public class TileEntityFirePit extends TileEntity {

	@SideOnly(Side.CLIENT)
	public ParticleBehaviors pm;
	
	public TileEntityFirePit() {
		
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
    }
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if (!this.worldObj.isRemote) {
	    	
    	} else {
    		if (pm == null) pm = new ParticleBehaviors(Vec3.createVectorHelper(xCoord+0.5F, yCoord+0.5F, zCoord+0.5F));
    		tickAnimate();
    	}
	}
	
	@SideOnly(Side.CLIENT)
	public void tickAnimate() {
		
		//debug
		int amount = 5 / (Minecraft.getMinecraft().gameSettings.particleSetting+1);
		
		//System.out.println(amount);
		Random rand = new Random();
		for (int i = 0; i < amount; i++)
        {
        	double speed = 0.15D;
        	
        	EntityRotFX entityfx = pm.spawnNewParticleIconFX(worldObj, ParticleRegistry.smoke, xCoord + rand.nextDouble(), yCoord + 0.2D + rand.nextDouble() * 0.2D, zCoord + rand.nextDouble(), (rand.nextDouble() - rand.nextDouble()) * speed, 0.03D, (rand.nextDouble() - rand.nextDouble()) * speed);
        	pm.setParticleRandoms(entityfx, true, true);
        	pm.setParticleFire(entityfx);
        	entityfx.setMaxAge(100+rand.nextInt(300));
			entityfx.spawnAsWeatherEffect();
			pm.particles.add(entityfx);
			
        }
	}

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
    }

    @Override
    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    	this.readFromNBT(pkt.func_148857_g());
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 5, nbttagcompound);
    }

}
