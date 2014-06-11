package net.tropicraft.item.scuba;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.tropicraft.item.armor.ItemTropicraftArmor;
import net.tropicraft.registry.TCCreativeTabRegistry;

public abstract class ItemScubaGear extends ItemTropicraftArmor implements ISpecialArmor {

	protected ScubaMaterial scubaMaterial;
	
	public ItemScubaGear(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, int armorType) {
		super(material, renderIndex, armorType);
		this.scubaMaterial = scubaMaterial;
		this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
	}
	
	/**
	 * Retrives an existing nbt tag compound or creates a new one if it is null
	 * @param stack
	 * @return
	 */
	public NBTTagCompound getTagCompound(ItemStack stack) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		return stack.getTagCompound();
	}
	
	@Override
	public abstract void onArmorTick(World world, EntityPlayer player, ItemStack itemStack);
	
	public static enum ScubaMaterial {
		
		DRY(0, "dry", "Dry"),
		WET(35, "wet", "Wet");
		
		/** The y-level that a player can safely dive to while wearing this gear material */
		private int maxDepth;
		
		/** The image prefix of this material type */
		private String imagePrefix;
		
		/** The name to be displayed when figuring out what type of gear this is */
		private String displayName;
		
		private ScubaMaterial(int maxDepth, String imagePrefix, String displayName) {
			this.maxDepth = maxDepth;
			this.imagePrefix = imagePrefix;
			this.displayName = displayName;
		}
		
		public int getMaxDepth() {
			return this.maxDepth;
		}
		
		public String getImagePrefix() {
			return this.imagePrefix;
		}
		
		public String getDisplayName() {
			return this.displayName;
		}
	}
	
	public static enum AirType {
		
		REGULAR(3200, 0.005F, "Regular"),
		TRIMIX(3200, 0.0005F, "Trimix");
		
		/** The max amount of psi one tank of this air type can hold */
		private int maxCapacity;
		
		/** The average amount of air that escapes one tank of this air per tick */
		private float usageRate;
		
		/** The name that shows up in the GUI when this air type is used */
		private String displayName;
		
		private AirType(int maxCapacity, float usageRate, String displayName) {
			this.maxCapacity = maxCapacity;
			this.usageRate = usageRate;
			this.displayName = displayName;
		}
		
		public int getMaxCapacity() {
			return this.maxCapacity;
		}
		
		public float getUsageRate() {
			return this.usageRate;
		}
		
		public String getDisplayName() {
			return this.displayName;
		}
	}

}
