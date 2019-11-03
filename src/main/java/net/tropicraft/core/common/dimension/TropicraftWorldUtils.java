package net.tropicraft.core.common.dimension;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
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

    public static void teleportPlayer(ServerPlayerEntity player, DimensionType dimensionType) {
        long time = System.currentTimeMillis();
        if (player.dimension == dimensionType) {
            teleportPlayerNoPortal(player, DimensionType.OVERWORLD);
        } else {
            teleportPlayerNoPortal(player, dimensionType);
        }

        long time2 = System.currentTimeMillis();

        System.out.printf("It took %f seconds to teleport\n", (time2 - time) / 1000.0F);
    }

    /**
     * Finds the top Y position relative to the dimension the player is teleporting to and places
     * the entity at that position. Avoids portal generation by using player.teleport() instead of
     * player.changeDimension()
     *
     * @param player The player that will be teleported
     * @param destination The target dimension to teleport to
     */
    public static void teleportPlayerNoPortal(ServerPlayerEntity player, DimensionType destination) {
        if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(player, destination)) return;

        ServerWorld serverworld = player.server.getWorld(destination);

        int posX = MathHelper.floor(player.posX);
        int posZ = MathHelper.floor(player.posZ);

        Chunk chunk = serverworld.getChunk(posX >> 4, posZ >> 4);
        int topY = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, posX & 15, posZ & 15);
        player.teleport(serverworld, posX + 0.5D, topY + 1.0D, posZ + 0.5D, player.rotationYaw, player.rotationPitch);

        net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerChangedDimensionEvent(player, destination, destination);
    }
}
