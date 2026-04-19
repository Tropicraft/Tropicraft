package net.tropicraft.core.common.attribute;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.attribute.AttributeRange;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Tropicraft;

@EventBusSubscriber(modid = Tropicraft.ID)
public class TropicraftEnvironmentAttributes {
    public static final DeferredRegister<EnvironmentAttribute<?>> REGISTER = DeferredRegister.create(Registries.ENVIRONMENT_ATTRIBUTE, Tropicraft.ID);

    public static final DeferredHolder<EnvironmentAttribute<?>, EnvironmentAttribute<Boolean>> CAN_TELEPORT_TO_TROPICS = REGISTER.register(
            "can_teleport_to_tropics",
            () -> EnvironmentAttribute.builder(AttributeTypes.BOOLEAN)
                    .defaultValue(false)
                    .build()
    );
    public static final DeferredHolder<EnvironmentAttribute<?>, EnvironmentAttribute<Float>> KOA_PARTY_CHANCE = REGISTER.register(
            "koa_party_chance",
            () -> EnvironmentAttribute.builder(AttributeTypes.FLOAT)
                    .defaultValue(0.0f)
                    .valueRange(AttributeRange.UNIT_FLOAT)
                    .build()
    );
    public static final DeferredHolder<EnvironmentAttribute<?>, EnvironmentAttribute<Integer>> KOA_PARTY_AMP = REGISTER.register(
            "koa_party_amp",
            () -> EnvironmentAttribute.builder(AttributeTypes.INTEGER)
                    .defaultValue(1)
                    .build()
    );
}
