package net.tropicraft.core.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.tropicraft.Info;
import net.tropicraft.core.common.entity.passive.EntityKoaHunter;

@Mod.EventBusSubscriber(modid = Info.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {

    public static EntityType<EntityKoaHunter> KOA_VILLAGER = null;

    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {

        IForgeRegistry<EntityType<?>> registry = event.getRegistry();

        registry.register((KOA_VILLAGER = EntityType.Builder.create(EntityKoaHunter::new, EntityClassification.MISC)
                .size(0.6F, 1.95F)
                .setTrackingRange(64)
                .setUpdateInterval(3)
                .immuneToFire()
                .setShouldReceiveVelocityUpdates(true)
                .build("koa_villager"))
                .setRegistryName("koa_villager"));

    }

}
