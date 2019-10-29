package net.tropicraft.core.common.dimension;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;

public class TropicraftWorldUtils {
    
    public static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, Constants.MODID);

	public static DimensionType TROPICS_DIMENSION;
	public static final RegistryObject<ModDimension> TROPICRAFT_MOD_DIMENSION = register(
	        "tropics", TropicraftWorldUtils::dimFactory); 

	private static ModDimension dimFactory() {
		return new ModDimension() {
			@Override
			public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
				return TropicraftDimension::new;
			}
		};
	}
	
	private static RegistryObject<ModDimension> register(final String name, final Supplier<ModDimension> sup) {
	    return DIMENSIONS.register(name, sup);
	}

	@Mod.EventBusSubscriber(modid = Constants.MODID)
	public static class EventDimensionType {
		@SubscribeEvent
		public static void onModDimensionRegister(final RegisterDimensionsEvent event) {
			ResourceLocation id = new ResourceLocation(Constants.MODID, "tropics");
			if (DimensionType.byName(id) == null) {
				TROPICS_DIMENSION = DimensionManager.registerDimension(id, TROPICRAFT_MOD_DIMENSION.get(), new PacketBuffer(Unpooled.buffer()), true);
				//TROPICS_DIMENSION.setRegistryName(new ResourceLocation(Constants.MODID, "tropics"));
				DimensionManager.keepLoaded(TROPICS_DIMENSION, false);
			} else {
				TROPICS_DIMENSION = DimensionType.byName(id);
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
