package net.tropicraft.core.common.item;

import javax.annotation.Nullable;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Info;
import net.tropicraft.Names;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrog;
import net.tropicraft.core.registry.CreativeTabRegistry;
import net.tropicraft.core.registry.TropicraftRegistry;

public class ItemMobEgg extends ItemTropicraft {

    public ItemMobEgg() {
        super();
        setMaxStackSize(64);
        setHasSubtypes(true);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int i = MathHelper.clamp(par1ItemStack.getItemDamage(), 0, Names.EGG_NAMES.length - 1);
        return super.getUnlocalizedName() + "." + Names.EGG_NAMES[i];
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;
        for (int var4 = 0; var4 < Names.EGG_NAMES.length; ++var4) {
            list.add(new ItemStack(this, 1, var4));
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack itemstack = player.getHeldItem(hand);
            String s = Names.EGG_NAMES[itemstack.getItemDamage()];

            StringBuilder sb = new StringBuilder();
            sb.append(Info.MODID);
            sb.append(":");
            sb.append(s);
            s = sb.toString();

            pos = pos.offset(facing);
            double ySpawnOffset = 0.0D;

            IBlockState iblockstate = world.getBlockState(pos);

            if (facing == EnumFacing.UP && iblockstate.getBlock() instanceof BlockFence) //Forge: Fix Vanilla bug comparing state instead of block {
                ySpawnOffset = 0.5D;

            spawnCreature(world, s, (double)pos.getX() + 0.5D, (double)pos.getY() + ySpawnOffset, (double)pos.getZ() + 0.5D);
            player.swingArm(EnumHand.MAIN_HAND);
        }
        return EnumActionResult.PASS;
    }

    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * Parameters: world, entityID, x, y, z.
     */
    @Nullable
    public static Entity spawnCreature(World worldIn, @Nullable String entityID, double x, double y, double z) {
        if (entityID != null) {
            Entity entity = null;

            EntityTreeFrog.Type frogType = null;

            if(entityID.endsWith("frog")) {
                for(EntityTreeFrog.Type type : EntityTreeFrog.Type.values()) {
                    if(entityID.equals(TropicraftRegistry.getEntityNamePrefixed(type.getColor()+"frog"))) {
                        entityID = TropicraftRegistry.getEntityNamePrefixed("treefrog");
                        frogType = type;
                    }
                }
            }

            // Wut
            for (int i = 0; i < 1; ++i) {
                entity = EntityList.createEntityByIDFromName(new ResourceLocation(entityID), worldIn);
                if(entity instanceof EntityTreeFrog && frogType != null) {
                    ((EntityTreeFrog)entity).setType(frogType.ordinal());
                    ((EntityTreeFrog)entity).initialSet = true;
                }
                if (entity instanceof EntityLivingBase) {
                    EntityLiving entityliving = (EntityLiving)entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
                    worldIn.spawnEntity(entity);
                    entityliving.playLivingSound();
                }
            }

            return entity;
        }
        else
        {
            return null;
        }
    }
}