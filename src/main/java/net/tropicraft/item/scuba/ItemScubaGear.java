package net.tropicraft.item.scuba;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.tropicraft.item.armor.ItemTropicraftArmor;

public abstract class ItemScubaGear extends ItemTropicraftArmor implements ISpecialArmor {

	protected ScubaMaterial scubaMaterial;
	
	public ItemScubaGear(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, int armorType) {
		super(material, renderIndex, armorType);
		this.scubaMaterial = scubaMaterial;
	}
	
	@Override
	public abstract void onArmorTick(World world, EntityPlayer player, ItemStack itemStack);
	
	public static enum ScubaMaterial {
		
		DRY(0, "dry"),
		WET(35, "wet");
		
		/** The y-level that a player can safely dive to while wearing this gear material */
		private int maxDepth;
		
		/** The image prefix of this material type */
		private String imagePrefix;
		
		private ScubaMaterial(int maxDepth, String imagePrefix) {
			this.maxDepth = maxDepth;
			this.imagePrefix = imagePrefix;
		}
		
		public int getMaxDepth() {
			return this.maxDepth;
		}
		
		public String getImagePrefix() {
			return this.imagePrefix;
		}
	}
	
	public static enum AirType {
		
		REGULAR(3200, 0.005F),
		TRIMIX(3200, 0.0005F);
		
		/** The max amount of psi one tank of this air type can hold */
		private int maxCapacity;
		
		/** The average amount of air that escapes one tank of this air per tick */
		private float usageRate;
		
		private AirType(int maxCapacity, float usageRate) {
			this.maxCapacity = maxCapacity;
			this.usageRate = usageRate;
		}
		
		public int getMaxCapacity() {
			return this.maxCapacity;
		}
		
		public float getUsageRate() {
			return this.usageRate;
		}
	}

}
