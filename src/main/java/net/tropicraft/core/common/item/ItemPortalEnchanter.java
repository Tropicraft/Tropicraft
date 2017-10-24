package net.tropicraft.core.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.dimension.TeleporterTropics;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.registry.BlockRegistry;

public class ItemPortalEnchanter extends ItemTropicraft {

	public ItemPortalEnchanter() {
		super();
		maxStackSize = 1;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer ent, List list, boolean wat) {
		list.add(I18n.translateToLocal("portalenchanter.type_" + itemstack.getItemDamage()));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer, EnumHand hand) {
		if (!world.isRemote && (itemstack.getItemDamage() == 1 || entityplayer.capabilities.isCreativeMode)) {
			int destination = entityplayer.dimension == 0 ? TropicraftWorldUtils.TROPICS_DIMENSION_ID : 0;
			TropicraftWorldUtils.teleportPlayer((EntityPlayerMP) entityplayer);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		}
		RayTraceResult raytraceresult = this.rayTrace(world, entityplayer, false);

		if (raytraceresult == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        } else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        } else {
            BlockPos blockpos = raytraceresult.getBlockPos();

            if (!world.isBlockModifiable(entityplayer, blockpos)) {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
            }
            
            if (!entityplayer.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
            }
            
            int x = blockpos.getX(), y = blockpos.getY(), z = blockpos.getZ();
			boolean found = false;
			for (int searchZ = -4; searchZ <= 4 && !found; searchZ++) {
				for (int searchX = -4; searchX <= 4 && !found; searchX++) {
					BlockPos pos = new BlockPos(x + searchX, y, z + searchZ);
					if (canGen(world, pos)) {
						found = true;
						entityplayer.swingArm(EnumHand.MAIN_HAND);
						(new TeleporterTropics(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(entityplayer.dimension))).buildTeleporterAt(x + searchX, y, z + searchZ, entityplayer);
						//ModLoader.getMinecraftInstance().effectRenderer.addEffect(new EntitySplashFX(ModLoader.getMinecraftInstance().world, playerX, playerY, playerZ, 0D, 0D, 0D));
						itemstack.damageItem(1, entityplayer);
					}
					pos = null;
				}
			}
        }
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
	}

	public boolean canGen(World world, BlockPos pos) {
		if (pos.getY() < 9)
			return false;

		for (int offsetZ = -2; offsetZ < 3; offsetZ++) {
			for (int offsetX = -2; offsetX < 3; offsetX++) {
				BlockPos pos2 = new BlockPos(pos.getX() + offsetX, pos.getY(), pos.getZ() + offsetZ);
				if (offsetX == -2 || offsetX == 2 || offsetZ == -2 || offsetZ == 2) {
					Block block = world.getBlockState(pos2).getBlock();
					if (block != Blocks.SANDSTONE && block != BlockRegistry.portalWall) {
						return false;
					}
				} else {
					if (world.getBlockState(pos2).getMaterial() != Material.WATER) {
						return false;
					}
					if (!world.isAirBlock(pos2.up())) {
						return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
