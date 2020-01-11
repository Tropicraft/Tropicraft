package net.tropicraft.core.common.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DaggerItem extends Item {

    private final IItemTier tier;
    public DaggerItem(IItemTier tier, Properties properties) {
        super(properties.defaultMaxDamage(tier.getMaxUses()));
        this.tier = tier;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Block block = state.getBlock();

        if (block == Blocks.COBWEB) {
            return 15.0F;
        } else {
            Material material = state.getMaterial();
            return material != Material.PLANTS && material != Material.TALL_PLANTS && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD ? 1.0F : 1.5F;
        }
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, LivingEntity attacker, LivingEntity target) {
        itemStack.damageItem(1, target, (e) -> {
            e.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    @Override
    public UseAction getUseAction(ItemStack itemstack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 0x11940;
    }

    @Override
    public boolean canHarvestBlock(BlockState state) {
        return state.getBlock() == Blocks.COBWEB;
    }

    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlotType.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) tier.getAttackDamage() + 2.5D, AttributeModifier.Operation.ADDITION));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 0, AttributeModifier.Operation.ADDITION));
        }

        return multimap;
    }
}
