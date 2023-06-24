package net.tropicraft.core.common.item;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class DaggerItem extends Item {

    private final Supplier<Multimap<Attribute, AttributeModifier>> defaultModifiers;

    public DaggerItem(Tier tier, Properties properties) {
        super(properties.defaultDurability(tier.getUses()));

        this.defaultModifiers = Suppliers.memoize(() -> ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .putAll(super.getAttributeModifiers(EquipmentSlot.MAINHAND, new ItemStack(this)))
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double) tier.getAttackDamageBonus() + 2.5D, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", 0, AttributeModifier.Operation.ADDITION))
                .build());
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Block block = state.getBlock();

        if (block == Blocks.COBWEB) {
            return 15.0F;
        } else {
            Material material = state.getMaterial();
            return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.STONE && material != Material.LEAVES && material != Material.VEGETABLE ? 1.0F : 1.5F;
        }
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity attacker, LivingEntity target) {
        itemStack.hurtAndBreak(1, target, (e) -> {
            e.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemstack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 60 * SharedConstants.TICKS_PER_MINUTE;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return state.getBlock() == Blocks.COBWEB;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            return defaultModifiers.get();
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }
}
