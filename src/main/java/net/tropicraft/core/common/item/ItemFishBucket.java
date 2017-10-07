package net.tropicraft.core.common.item;

import java.util.ArrayList;
import java.util.List;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class ItemFishBucket extends ItemBucket {

	public ItemFishBucket() {
		super(BlockRegistry.tropicsWater);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer playerIn, EnumHand hand) {
		ActionResult<ItemStack> ret = super.onItemRightClick(stack, world, playerIn, hand);
		if (ret.getType() == EnumActionResult.SUCCESS && !world.isRemote) {
			RayTraceResult raytraceresult = this.rayTrace(world, playerIn, true);

			if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos pos = raytraceresult.getBlockPos();
				for (EntityTropicalFish fish : loadEntiesFromNBT(stack, world)) {
					fish.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, world.rand.nextFloat() * 360.0F, 0);
					world.spawnEntity(fish);
					fish.playLivingSound();
				}
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.capabilities.isCreativeMode ? new ItemStack(ItemRegistry.tropicsWaterBucket) : new ItemStack(Items.BUCKET));
			}
		}
		return ret;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		NBTTagList fishList = getFishList(stack);
		TObjectIntMap<String> fishCounts = new TObjectIntHashMap<>();
		for (int i = 0; i < fishList.tagCount(); i++) {
			// TODO this is awful
			String color = EntityTropicalFish.names[fishList.getCompoundTagAt(i).getInteger("Color")];
			fishCounts.adjustOrPutValue(color, 1, 1);
		}
		for (String s : fishCounts.keySet()) {
			String line = s;
			int count = fishCounts.get(s);
			if (count > 1) {
				line += " (x" + count + ")";
			}
			tooltip.add(line);
		}
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	private static final String KEY_ENTITIES = "fishies";
	
	private static final NBTTagCompound getTag(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack.getTagCompound();
	}

	private static final NBTTagList getFishList(ItemStack stack) {
		NBTTagCompound tag = getTag(stack);
		if (!tag.hasKey(KEY_ENTITIES)) {
			tag.setTag(KEY_ENTITIES, new NBTTagList());
		}
		return tag.getTagList(KEY_ENTITIES, Constants.NBT.TAG_COMPOUND);
	}

	public static List<EntityTropicalFish> loadEntiesFromNBT(ItemStack stack, World world) {
		List<EntityTropicalFish> ret = new ArrayList<>();
		NBTTagList entityTags = getFishList(stack);
		for (int i = 0; i < entityTags.tagCount(); i++) {
			ret.add((EntityTropicalFish) EntityList.createEntityFromNBT(entityTags.getCompoundTagAt(i), world));
		}
		return ret;
	}

	public static boolean addFish(ItemStack stack, EntityTropicalFish entity) {
		if (entity != null) {
			NBTTagList fishList = getFishList(stack);
			if (fishList.tagCount() < 5) {
				getFishList(stack).appendTag(entity.serializeNBT());
				return true;
			}
		}
		return false;
	}
}
