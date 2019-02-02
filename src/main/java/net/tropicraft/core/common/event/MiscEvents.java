package net.tropicraft.core.common.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.capability.PlayerDataInstance;
import net.tropicraft.core.common.capability.WorldDataInstance;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.donations.TickerDonation;
import net.tropicraft.core.common.entity.placeable.EntityChair;
import net.tropicraft.core.common.worldgen.village.WorldEventListener;

import java.util.HashSet;

public class MiscEvents {

    //make resetting in singleplayer instance changes easier
    public World lastWorldTracked = null;

    //public HashMap<Integer, Boolean> lookupDimIDToRegisteredListener = new HashMap<>();
    public HashSet<Integer> lookupDimIDToRegisteredListener = new HashSet<>();



    @SubscribeEvent
    public void tickServer(ServerTickEvent event) {

        World overworld = DimensionManager.getWorld(0);
        if (lastWorldTracked != overworld) {
            reset();
            lastWorldTracked = overworld;
        }

        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);

        if (world != null && world instanceof WorldServer) {
            for (int ii = 0; ii < world.playerEntities.size(); ii++) {
                EntityPlayer player = world.playerEntities.get(ii);

                if (player instanceof EntityPlayerMP) {
                    // If player is drunk and it is sunset and the player is riding a chair
                    // teleport player!
                    if (((EntityPlayerMP)player).isPotionActive(MobEffects.NAUSEA) && isSunset(world) && player.getRidingEntity() instanceof EntityChair) {
                        player.dismountRidingEntity();
                        TropicraftWorldUtils.teleportPlayer((EntityPlayerMP)player);
                    }
                }

//                // Armor checks to perform shutdown functions
//                PlayerDataInstance storage = player.getCapability(Tropicraft.PLAYER_DATA_INSTANCE, null);
//
//                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
//                ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
//                ItemStack flippers = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
//
//                if (helmet == null || helmet.getItem() == null || !(helmet.getItem() instanceof ItemScubaHelmet)) {
//                    if (storage.scubaHelmet) {
//                        // TODO clean up scuba helmet method for player
//                        storage.scubaHelmet = false;
//                    }
//                }
            }
        }

        if (world != null && event.phase == TickEvent.Phase.END) {
            TickerDonation.tick(world);
        }
    }

    @SubscribeEvent
    public void tickServer(TickEvent.WorldTickEvent event) {
        //tick for all dims now!
        //if (event.world.provider instanceof WorldProviderTropicraft) {
            WorldDataInstance storage = event.world.getCapability(Tropicraft.WORLD_DATA_INSTANCE, null);
            if (storage != null) {
                storage.tick();
            }
        //}

        if (!lookupDimIDToRegisteredListener.contains(event.world.provider.getDimension())) {
            //System.out.println("adding world listener for dim " + event.world.provider.getDimension());
            event.world.addEventListener(new WorldEventListener());
            lookupDimIDToRegisteredListener.add(event.world.provider.getDimension());
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(Info.MODID, "PlayerDataInstance"), new ICapabilitySerializable<NBTTagCompound>() {

                PlayerDataInstance instance = Tropicraft.PLAYER_DATA_INSTANCE.getDefaultInstance().setPlayer((EntityPlayer)event.getObject());

                @Override
                public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                    return capability == Tropicraft.PLAYER_DATA_INSTANCE;
                }

                @Override
                public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                    return capability == Tropicraft.PLAYER_DATA_INSTANCE ? Tropicraft.PLAYER_DATA_INSTANCE.<T>cast(this.instance) : null;
                }

                @Override
                public NBTTagCompound serializeNBT() {
                    return (NBTTagCompound) Tropicraft.PLAYER_DATA_INSTANCE.getStorage().writeNBT(Tropicraft.PLAYER_DATA_INSTANCE, this.instance, null);
                }

                @Override
                public void deserializeNBT(NBTTagCompound nbt) {
                    Tropicraft.PLAYER_DATA_INSTANCE.getStorage().readNBT(Tropicraft.PLAYER_DATA_INSTANCE, this.instance, null, nbt);
                }

            });
        }
    }

    @SubscribeEvent
    public void onAttachCapabilitiesWorld(AttachCapabilitiesEvent<World> event) {
        if (event.getObject() instanceof World) {
            //World world = event.getObject();
            //if (true || !world.isRemote && world.provider instanceof WorldProviderTropicraft) {
                event.addCapability(new ResourceLocation(Info.MODID, "WorldDataInstance"), new ICapabilitySerializable<NBTTagCompound>() {

                    WorldDataInstance instance = Tropicraft.WORLD_DATA_INSTANCE.getDefaultInstance().setWorld((World) event.getObject());

                    @Override
                    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                        return capability == Tropicraft.WORLD_DATA_INSTANCE;
                    }

                    @Override
                    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                        return capability == Tropicraft.WORLD_DATA_INSTANCE ? Tropicraft.WORLD_DATA_INSTANCE.<T>cast(this.instance) : null;
                    }

                    @Override
                    public NBTTagCompound serializeNBT() {
                        return (NBTTagCompound) Tropicraft.WORLD_DATA_INSTANCE.getStorage().writeNBT(Tropicraft.WORLD_DATA_INSTANCE, this.instance, null);
                    }

                    @Override
                    public void deserializeNBT(NBTTagCompound nbt) {
                        Tropicraft.WORLD_DATA_INSTANCE.getStorage().readNBT(Tropicraft.WORLD_DATA_INSTANCE, this.instance, null, nbt);
                    }

                });
            //}
        }
    }

    /**
     * Returns whether it is currently sunset
     * @param world World object
     * @return Is it currently sunset in the world?
     */
    private boolean isSunset(World world) {
        long timeDay = world.getWorldTime() % 24000;
        return timeDay > 12200 && timeDay < 14000;
    }

    private void reset() {
        lookupDimIDToRegisteredListener.clear();
    }
}
