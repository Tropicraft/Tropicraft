package net.tropicraft.core.common.entity.underdasea.atlantoku;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntitySeahorse extends EntityTropicraftWaterBase {

	public EntitySeahorse(World world) {
		super(world);
		this.setSize(0.75F, 1.25F);
		this.setSwimSpeeds(0.2f, 0.4f, 0.5f);
		this.setMaxHealth(4);
	}
	
	@Override
	public void entityInit() {
		super.entityInit();	
	}

	@Override
	public String[] getTexturePool() {
		return new String[]{"razz", "blue", "cyan", "yellow", "green", "orange"};
	}

	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        return false;
    }

}
