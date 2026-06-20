package net.tropicraft.core.common.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.DamageSourcePredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.tropicraft.Tropicraft;

import java.util.List;

@EventBusSubscriber(modid = Tropicraft.ID)
public record DamageModifier(
        List<Rule> rules
) {
    public static final Codec<DamageModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
            Rule.CODEC.listOf().fieldOf("rules").forGetter(DamageModifier::rules)
    ).apply(i, DamageModifier::new));

    public float apply(ServerLevel level, Vec3 position, DamageSource source, float amount) {
        for (Rule rule : rules) {
            if (rule.predicate.matches(level, position, source)) {
                amount *= rule.factor;
            }
        }
        return amount;
    }

    public record Rule(
            DamageSourcePredicate predicate,
            float factor
    ) {
        public static final Codec<Rule> CODEC = RecordCodecBuilder.create(i -> i.group(
                DamageSourcePredicate.CODEC.fieldOf("predicate").forGetter(Rule::predicate),
                ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("factor").forGetter(Rule::factor)
        ).apply(i, Rule::new));
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        if (!(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        Vec3 position = entity.position();
        DamageSource damageSource = event.getSource();
        for (EquipmentSlot slot : EquipmentSlot.VALUES) {
            DamageModifier damageModifier = entity.getItemBySlot(slot).get(TropicraftDataComponents.INCOMING_DAMAGE_MODIFIER);
            if (damageModifier != null) {
                event.setNewDamage(damageModifier.apply(serverLevel, position, damageSource, event.getNewDamage()));
            }
        }
    }
}
