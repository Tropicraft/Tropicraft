package net.tropicraft.core.common.dimension;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;

import java.util.function.BiFunction;

public class TropicraftWorldUtils {

	public static DimensionType TROPICS_DIMENSION;
	public static ModDimension TROPICRAFT_MOD_DIMENSION;

	@Mod.EventBusSubscriber(modid = Constants.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
	public static class EventModDimension {
		@SubscribeEvent
		public static void onDimensionRegistry(final RegistryEvent.Register<ModDimension> event) {
			TROPICRAFT_MOD_DIMENSION = dimFactory();
			event.getRegistry().register(TROPICRAFT_MOD_DIMENSION.setRegistryName(new ResourceLocation(Constants.MODID, "tropics")));
		}

		static ModDimension dimFactory() {
			return new ModDimension() {
				@Override
				public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
					return TropicraftDimension::new;
				}
			};
		}
	}

	@Mod.EventBusSubscriber
	public static class EventDimensionType {
		@SubscribeEvent
		public static void onModDimensionRegister(final RegisterDimensionsEvent event) {
			if (DimensionType.byName(new ResourceLocation(Constants.MODID, "tropics")) == null) {
				TROPICS_DIMENSION = DimensionManager.registerDimension(new ResourceLocation(Constants.MODID, "tropics"), TROPICRAFT_MOD_DIMENSION, new PacketBuffer(Unpooled.buffer()), true);
				//TROPICS_DIMENSION.setRegistryName(new ResourceLocation(Constants.MODID, "tropics"));
				DimensionManager.keepLoaded(TROPICS_DIMENSION, false);
			}
		}
	}

//
//	public static void initializeDimension() {
//		PacketBuffer pbj = new PacketBuffer(Unpooled.buffer());
//		pbj.writeInt(TROPICS_DIMENSION_ID);
//		pbj.writeString("tropics");
//
//	//	DimensionManager.registerDimension(new ResourceLocation("tropics"), new TropicraftModDimension(), pbj, true);
////
////		CompoundNBT n = new CompoundNBT();
////		n.setInteger("dim_id", tropicsDimension.getId());
////		n.setString("dim_name", tropicsDimension.getName());
//		//TODO FMLInterModComms.sendMessage(ForgeModContainer.getInstance().getModId(), "loaderFarewellSkip", n);
//	}

	public static void teleportPlayer(ServerPlayerEntity player) {
		long time = System.currentTimeMillis();
		if (player.dimension == TROPICS_DIMENSION) {
			player.changeDimension(DimensionType.OVERWORLD);
		} else {
			player.changeDimension(TROPICS_DIMENSION);
		}

		long time2 = System.currentTimeMillis();

		System.out.printf("It took %f seconds to teleport\n", (time2 - time) / 1000.0F);
	}

}
