package net.tropicraft.core.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tropicraft.ColorHelper;
import net.tropicraft.core.common.entity.placeable.EntityBeachFloat;

public class ItemBeachFloat extends ItemTropicraftColored {

	public ItemBeachFloat() {
		super("float");
	}

	@Override
	public int getColor(ItemStack itemstack, int pass) {
		Integer color = ColorHelper.getColorFromDamage(itemstack.getItemDamage());
		return (pass == 0 ? 16777215 : color.intValue());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		float f = 1.0F;
		float f1 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * f;
		float f2 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * f;
		double d0 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * (double)f;
		double d1 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * (double)f + (double)playerIn.getEyeHeight();
		double d2 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * (double)f;
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 5.0D;
		Vec3d vec3d1 = vec3d.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
		RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d1, true);

		if (raytraceresult == null)
		{
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}
		else
		{
			Vec3d vec3d2 = playerIn.getLook(f);
			boolean flag = false;
			List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().expand(vec3d2.x * d3, vec3d2.y * d3, vec3d2.z * d3).grow(1.0D));

			for (int i = 0; i < list.size(); ++i)
			{
				Entity entity = (Entity)list.get(i);

				if (entity.canBeCollidedWith())
				{
					AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow((double)entity.getCollisionBorderSize());

					if (axisalignedbb.contains(vec3d))
					{
						flag = true;
					}
				}
			}

			if (flag)
			{
				return new ActionResult<>(EnumActionResult.PASS, stack);
			}
			else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
			{
				return new ActionResult<>(EnumActionResult.PASS, stack);
			}
			else
			{
				Block block = worldIn.getBlockState(raytraceresult.getBlockPos()).getBlock();
				boolean flag1 = block == Blocks.WATER || block == Blocks.FLOWING_WATER;

				double x = raytraceresult.hitVec.x;
				double y = flag1 ? raytraceresult.hitVec.y - 0.12D : raytraceresult.hitVec.y;
				double z = raytraceresult.hitVec.z;

				int color = ColorHelper.getColorFromDamage(stack.getItemDamage());
				EntityBeachFloat beachFloat = new EntityBeachFloat(worldIn, x, y, z, color, playerIn);

				beachFloat.rotationYaw = playerIn.rotationYaw;

				if (!worldIn.getCollisionBoxes(beachFloat, beachFloat.getEntityBoundingBox().grow(-0.1D)).isEmpty())
				{
					return new ActionResult<>(EnumActionResult.FAIL, stack);
				}
				else
				{
					if (!worldIn.isRemote)
					{
						worldIn.spawnEntity(beachFloat);
					}

					if (!playerIn.capabilities.isCreativeMode)
					{
						stack.shrink(1);
					}

					playerIn.addStat(StatList.getObjectUseStats(this));
					return new ActionResult<>(EnumActionResult.SUCCESS, stack);
				}
			}
		}
	}
}
