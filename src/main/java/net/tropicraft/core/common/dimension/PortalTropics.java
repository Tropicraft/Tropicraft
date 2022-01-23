package net.tropicraft.core.common.dimension;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.BambooChestBlockEntity;
import net.tropicraft.core.common.item.TropicraftItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class PortalTropics implements ITeleporter {

    private static final Logger LOGGER = LogManager.getLogger("tropicraft");
    private static final Block PORTAL_BLOCK = TropicraftBlocks.PORTAL_WATER.get();
    private static final Block TELEPORTER_BLOCK = TropicraftBlocks.TELEPORT_PORTAL_WATER.get();

    private final ServerLevel world;

    public static final int SEARCH_AREA = 128;

    public PortalTropics(ServerLevel world) {
        this.world = world;
    }

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        long startTime = System.currentTimeMillis();
        PortalInfo portalInfo = findPortalInfoPoi(entity, destWorld);

        if (portalInfo == null) {
            placeNewPortal(world, entity);

            portalInfo = findPortalInfoPoi(entity, destWorld);
        }

        long finishTime = System.currentTimeMillis();
        LOGGER.info(String.format("[Tropicraft] Portal Time: It took %f seconds for TeleporterTropics.placeInPortal to complete", (finishTime - startTime) / 1000.0F));

        return portalInfo;
    }

    public PortalInfo findPortalInfoPoi(Entity entity, ServerLevel destWorld) {
        BlockPos portalPostion = searchForPortalPoi(this.world, entity.getOnPos());

        if (portalPostion != null) {
            double newLocX = portalPostion.getX() + 0.5D;
            double newLocY = portalPostion.getY() + 0.5D;
            double newLocZ = portalPostion.getZ() + 0.5D;

            if (world.getBlockState(portalPostion.west()).getBlock() == PORTAL_BLOCK) newLocX -= 0.5D;
            if (world.getBlockState(portalPostion.east()).getBlock() == PORTAL_BLOCK) newLocX += 0.5D;
            if (world.getBlockState(portalPostion.north()).getBlock() == PORTAL_BLOCK) newLocZ -= 0.5D;
            if (world.getBlockState(portalPostion.south()).getBlock() == PORTAL_BLOCK) newLocZ += 0.5D;

            //entity.setLocationAndAngles();
            int worldSpawnX = Mth.floor(newLocX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
            int worldSpawnZ = Mth.floor(newLocZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
            int worldSpawnY = portalPostion.getY() + 5; // Move to top of portal

            entity.setDeltaMovement(0, 0, 0);

            //If the player is entering the tropics, spawn an Encyclopedia Tropica
            //in the spawn portal chest (if they don't already have one AND one isn't
            //already in the chest)

            if (this.world.dimension() == TropicraftDimension.WORLD && entity instanceof Player player && !player.getInventory().contains(new ItemStack(TropicraftItems.NIGEL_STACHE.get()))) { //TODO [1.17]: Replace Nigel Stache item with encyclopedia when reimplemented
                //TODO improve this logical check to an NBT tag or something?

                // Search for the spawn chest
                BambooChestBlockEntity chest = null;

                BlockPos chestPosition = portalPostion.north(2).above();

                if(world.getBlockState(chestPosition).getBlock() == TropicraftBlocks.BAMBOO_CHEST.get() && world.getBlockEntity(chestPosition) instanceof BambooChestBlockEntity bambooChestBlockEntity){
                    chest = bambooChestBlockEntity;
                }

                // Make sure chest doesn't have the encyclopedia
                ///TODO
                if (chest == null) {
                    world.setBlock(chestPosition, TropicraftBlocks.BAMBOO_CHEST.get().defaultBlockState(), 3);

                    chest = (BambooChestBlockEntity)world.getBlockEntity(chestPosition);
                }

                boolean hasEncyclopedia = chest.hasAnyOf(ImmutableSet.of(TropicraftItems.NIGEL_STACHE.get()));

                // Give out a new encyclopedia
                if (!hasEncyclopedia) {
                    for (int inv = 0; inv < chest.getContainerSize(); inv++) {
                        ItemStack stack = chest.getItem(inv);
                        if (stack.isEmpty()) {
                            chest.setItem(inv, new ItemStack(TropicraftItems.NIGEL_STACHE.get(), 1));
                            break;
                        }
                    }
                }
            }


            LOGGER.info(String.format("[Tropicraft] Portal: Portal Information given to the player [x: %f, y: %f, z: %f]", newLocX, newLocY, newLocZ));
            return new PortalInfo(new Vec3(newLocX, newLocY, newLocZ), Vec3.ZERO, entity.getYRot(), entity.getXRot());
        }
        else{
            LOGGER.info("[Tropicraft] Portal: No Portal was found within the search radius");
            return null;
        }
    }

    public static BlockPos searchForPortalPoi(ServerLevel world, BlockPos blockPos){
        PoiManager poimanager = world.getPoiManager();
        poimanager.ensureLoadedAndValid(world, blockPos, SEARCH_AREA);

        List<PoiRecord> poiRecords = poimanager.getInSquare((p_77654_) -> p_77654_ == TropicraftPoiTypes.TROPICRAFT_PORTAL.get(), blockPos, SEARCH_AREA, PoiManager.Occupancy.ANY).toList();

        if (poiRecords.isEmpty()) return null;

        for (PoiRecord poiRecord : poiRecords) {
            BlockPos portalPoi = poiRecord.getPos();

            boolean fl1 = world.getBlockState(portalPoi.north()).getBlock() == PORTAL_BLOCK
                    && world.getBlockState(portalPoi.south()).getBlock() == PORTAL_BLOCK
                    && world.getBlockState(portalPoi.east()).getBlock() == PORTAL_BLOCK
                    && world.getBlockState(portalPoi.west()).getBlock() == PORTAL_BLOCK
                    && world.getBlockState(portalPoi.below()).getBlock() == PORTAL_BLOCK;


            if (world.getBlockState(portalPoi).getBlock() == PORTAL_BLOCK && fl1) {
//                int y = portalPoi.getY();
//                portalPoi = portalPoi.below();
//
//                while (world.getBlockState(portalPoi).getBlock() == PORTAL_BLOCK)
//                {
//                    --y;
//                    portalPoi = portalPoi.below();
//                }

                LOGGER.info("[Tropicraft] Portal: Found a portal close to the player!");

//                portalPoi.above();

                LOGGER.info("[Tropicraft] Portal: Current block Pos Values that was found using poi finder [" + poiRecord.getPos() + "]");

                return poiRecord.getPos();
            }
            else{
                //Removes the poiRecord as it dosn't seem there is any portal water at that position

                poimanager.remove(poiRecord.getPos());
                LOGGER.info("[Tropicraft] Portal: Removing POI Record at: [" + poiRecord.getPos() + "]");
            }
        }

        return null;
    }

    public static boolean placeNewPortal(ServerLevel world, Entity entity) {
        LOGGER.info("[Tropicraft] Portal: Start make portal");
        int searchArea = 16;
        double closestSpot = -1D;
        int entityX = Mth.floor(entity.getX());
        int entityY = Mth.floor(entity.getY());
        int entityZ = Mth.floor(entity.getZ());
        int foundX = entityX;
        int foundY = entityY;
        int foundZ = entityZ;
        int seaLevel = getSeaLevel(world);

        for (int x = entityX - searchArea; x <= entityX + searchArea; x++) {
            double distX = (x + 0.5D) - entity.getX();
            nextCoords:
            for (int z = entityZ - searchArea; z <= entityZ + searchArea; z++) {
                double distZ = (z + 0.5D) - entity.getZ();

                // Find topmost solid block at this x,z location
                LevelChunk chunk = world.getChunk(entityX >> 4, entityZ >> 4);
                int y = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, entityX & 15, entityZ & 15);

                BlockPos pos = new BlockPos(x, y, z);
                while(y >= seaLevel - 1 && (world.getBlockState(pos).getBlock() == Blocks.AIR || !isValidBuildBlocks(world.getBlockState(pos))) ){
                    y = pos.getY();
                    pos = pos.below();
                }

                for (; y >= seaLevel - 1 && (world.getBlockState(pos).getBlock() == Blocks.AIR ||
                        !isValidBuildBlocks(world.getBlockState(pos))); pos = pos.below()) {
                    y = pos.getY();
                }
                // Only generate portal between sea level and sea level + 20
                if (y > seaLevel + 20 || y < seaLevel) {
                    continue;
                }

                BlockPos tryPos = new BlockPos(x, y - 1, z);
                if (isValidBuildBlocks(world.getBlockState(tryPos))) {
                    for (int xOffset = -2; xOffset <= 2; xOffset++) {
                        for (int zOffset = -2; zOffset <= 2; zOffset++) {
                            int otherY = world.getHeight() - 1;
                            BlockPos pos1 = new BlockPos(x + xOffset, otherY, z + zOffset);
//                                BlockPos pos2 = tryPos.toImmutable();
                            for (; otherY >= seaLevel && (world.getBlockState(pos1).getBlock() == Blocks.AIR ||
                                    !world.getBlockState(tryPos).isAir()); pos1 = pos1.below()) {
                                otherY = pos1.getY();
                            }
                            if (Math.abs(y - otherY) >= 3) {
                                continue nextCoords;
                            }
                        }
                    }

                    double distY = (y + 0.5D) - entity.getY();
                    double distance = distX * distX + distY * distY + distZ * distZ;
                    if (closestSpot < 0.0D || distance < closestSpot)
                    {
                        closestSpot = distance;
                        foundX = x;
                        foundY = y;
                        foundZ = z;

                    }
                }
            }
        }

        int worldSpawnX = (int) Math.floor(foundX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
        int worldSpawnZ = (int) Math.floor(foundZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);

        int worldSpawnY = getTerrainHeightAt(world, worldSpawnX, worldSpawnZ);//world.getHeightValue(worldSpawnX, worldSpawnZ) - 2;

        BlockPos worldPos = new BlockPos(worldSpawnX, worldSpawnY, worldSpawnZ);

        // Max distance to search in every direction for the nearest landmass to build a bridge to
        int SEARCH_FOR_LAND_DISTANCE_MAX = 200;

        // If we can't find a spot (e.g. we're in the middle of the ocean),
        // just put the portal at sea level

        return placePortalStructure(world, (ServerPlayer) entity, worldPos);
    }

    public static boolean placePortalStructure(ServerLevel world, ServerPlayer player, BlockPos pos) {
        LOGGER.info("[Tropicraft] Portal: Start make portal");

        /* Taken from https://github.com/TelepathicGrunt/CommandStructures-Forge/blob/526b54ecba5a1f3c0460e15767328ae7156a6313/src/main/java/com/telepathicgrunt/commandstructures/commands/SpawnPiecesCommand.java#L219
         *
         * With permission from TelepathicGrunt
         */

        BlockPos portalGenerateOrigin = new BlockPos(pos.getX() - 4, pos.getY() - 8, pos.getZ() - 4);
        LOGGER.info("[Tropicraft] Portal: Creating portal from: [" + portalGenerateOrigin + "]");

        BlockState prevBlock = world.getBlockState(portalGenerateOrigin);

        world.setBlock(portalGenerateOrigin, Blocks.STRUCTURE_BLOCK.defaultBlockState().setValue(StructureBlock.MODE, StructureMode.LOAD), 3);
        BlockEntity be = world.getBlockEntity(portalGenerateOrigin);
        if(be instanceof StructureBlockEntity structureBlockTileEntity) {
            structureBlockTileEntity.setStructureName(new ResourceLocation(Constants.MODID, "portal/tropicraft_portal")); // set identifier

            structureBlockTileEntity.setMode(StructureMode.LOAD);
            structureBlockTileEntity.setIgnoreEntities(false);

            manuallyCauseBlockChange(world, new ResourceLocation(Constants.MODID, "portal/tropicraft_portal"), portalGenerateOrigin);
            structureBlockTileEntity.loadStructure(world,false); // load structure

            structureBlockTileEntity.setIgnoreEntities(false);
        }

        world.setBlock(portalGenerateOrigin, prevBlock, 3);

        //------------------------------------------------------------------------------------------

//        BlockPos portalGenerateOrigin = new BlockPos(pos.getX(), pos.getY() - 8, pos.getZ());
//        Optional<StructureTemplate> structure = world.getStructureManager().get(new ResourceLocation(Constants.MODID, "portal/tropicraft_portal"));
//
//        //Optional<StructureTemplate> structure = world.getStructureManager().get(new ResourceLocation("badlands"));
//
//        if(structure.isEmpty()){
//            LOGGER.info("[Tropicraft Portal]: The portal structure could not be found");
//            return false;
//        }
//
//        structure.ifPresent((structureTemplate) -> {
//            StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(false);
//
//            structureplacesettings.clearProcessors().addProcessor(new BlockRotProcessor((float) Mth.clamp(0.5, 0.0F, 1.0F))).setRandom(world.getRandom());
//
//            LOGGER.info("[Tropicraft Portal]: Creating portal from: [" + portalGenerateOrigin + "]");
//
//            fillStructureVoidSpace(world, new ResourceLocation(Constants.MODID, "portal/tropicraft_portal"), portalGenerateOrigin);
//
//            if(structureTemplate.placeInWorld(world, portalGenerateOrigin, portalGenerateOrigin.above(), structureplacesettings, world.getRandom(), Block.UPDATE_CLIENTS)){
//                LOGGER.info("[Tropicraft Portal]: The portal structure has generated");
//            }else{
//                LOGGER.info("[Tropicraft Portal]: The portal structure had issues generating");
//            }
//
//            //TODO: UN COMMET WHEN TIME IS CORRECT
//            //world.getPoiManager().add(new BlockPos(pos.getX() + 4, pos.getY(), pos.getZ() + 4), TropicraftPoiTypes.TROPICRAFT_PORTAL.get());
//            LOGGER.info("[Tropicraft Portal]: Setting Poi postion at: [" + new BlockPos(pos.getX() + 4, pos.getY(), pos.getZ() + 4) + "]");
//        });

        //TODO: UN COMMET WHEN TIME IS CORRECT
        world.getPoiManager().add(pos, TropicraftPoiTypes.TROPICRAFT_PORTAL.get());
        LOGGER.info("[Tropicraft] Portal: Setting Poi postion at: [" + pos + "]");

        LOGGER.info("[Tropicraft] Portal: End makePortal");
        return true;
    }

    /**
     * Gets the terrain height at the specified coordinates
     * @param x The x coordinate
     * @param z The z coordinate
     * @return The terrain height at the specified coordinates
     */

    public static int getTerrainHeightAt(ServerLevel world, int x, int z) {
        LevelChunk chunk2 = world.getChunk(x >> 4, z >> 4);
        int worldSpawnY = chunk2.getHeight(Heightmap.Types.WORLD_SURFACE, x & 15, z & 15);

        for(int y = worldSpawnY; y > 0; y--) {
            BlockState state = world.getBlockState(new BlockPos(x, y, z));

            //TODO [1.17]: Confirm that these tags are going to work with modded blocks
            if(state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(Blocks.WATER) || state.is(BlockTags.BASE_STONE_OVERWORLD)) {
                return y;
            }
        }
        return 0;
    }

    /**
     * TODO why in the world is this a thing?
     *
     * A method to check if the block a valid place to put a portal
     *
     * @param state A block state of the block being tested
     * @return A boolean value if the blockstate is a valid build block
     */

    public static Boolean isValidBuildBlocks(BlockState state) {
        if(state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(Blocks.WATER) || state.is(BlockTags.BASE_STONE_OVERWORLD)) {
            return true;
        }
        else {
            return false;
        }
    }

    /* Taken from https://github.com/TelepathicGrunt/CommandStructures-Forge/blob/526b54ecba5a1f3c0460e15767328ae7156a6313/src/main/java/com/telepathicgrunt/commandstructures/commands/SpawnPiecesCommand.java#L257
     *
     * With permission from TelepathicGrunt
     */

    private static void manuallyCauseBlockChange(ServerLevel world, ResourceLocation resourceLocation, BlockPos startSpot) {
        StructureManager structuremanager = world.getStructureManager();
        Optional<StructureTemplate> optional = structuremanager.get(resourceLocation);
        optional.ifPresent(template -> {
            BlockPos.MutableBlockPos mutable = startSpot.mutable();
            ChunkAccess chunk = world.getChunk(mutable);
            for(int x = 0; x < template.getSize().getX(); x++) {
                for (int z = 0; z < template.getSize().getZ(); z++) {
                    for(int y = 0; y < template.getSize().getY(); y++) {
                        mutable.set(startSpot).move(x, y + 1, z);
                        if(chunk.getPos().x != mutable.getX() >> 4 || chunk.getPos().z != mutable.getZ() >> 4) {
                            chunk = world.getChunk(mutable);
                        }

                        world.getChunkSource().blockChanged(mutable);
                        world.getChunkSource().getLightEngine().checkBlock(mutable);
                    }
                }
            }
        });
    }

    private static int getSeaLevel(ServerLevel world){
        return world.getSeaLevel(); //OG Value: 63
    }
}
