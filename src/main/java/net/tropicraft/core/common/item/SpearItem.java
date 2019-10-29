package net.tropicraft.core.common.item;

import com.google.common.collect.Multimap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;

public class SpearItem extends TridentItem {

    private final IItemTier tier;
    private final int attackDamage;
    private final float attackSpeed;

    public SpearItem(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(properties.defaultMaxDamage(tier.getMaxUses()));
        this.tier = tier;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        // TODO
    }
    
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot);
        if (slot == EquipmentSlotType.MAINHAND) {
           multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", attackDamage, AttributeModifier.Operation.ADDITION));
           multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        }

        return multimap;
    }

    public int getItemEnchantability() {
       return this.tier.getEnchantability();
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
       return this.tier.getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
    }
}
