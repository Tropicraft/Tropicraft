package net.tropicraft.core.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.tropicraft.Tropicraft;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

// We could (and should!) use SeparateTransformsModel, but it forces isGui3d on which has incorrect lighting for our item model in the inventory
@EventBusSubscriber(modid = Tropicraft.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FaceItemGeometryLoader implements IGeometryLoader<FaceItemGeometryLoader.Geometry> {
    public static final FaceItemGeometryLoader INSTANCE = new FaceItemGeometryLoader();
    public static final ResourceLocation ID = Tropicraft.location("face_item");

    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(ID, INSTANCE);
    }

    @Override
    public Geometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        BlockModel itemModel = deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "item"), BlockModel.class);
        BlockModel equippedModel = deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "equipped"), BlockModel.class);
        return new Geometry(itemModel, equippedModel);
    }

    public record Geometry(
            BlockModel itemModel,
            BlockModel equippedModel
    ) implements IUnbakedGeometry<Geometry> {
        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
            return new Baked(
                    itemModel.bake(baker, itemModel, spriteGetter, modelState, context.useBlockLight()),
                    equippedModel.bake(baker, equippedModel, spriteGetter, modelState, context.useBlockLight())
            );
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
            itemModel.resolveParents(modelGetter);
            equippedModel.resolveParents(modelGetter);
        }
    }

    private record Baked(
            BakedModel itemModel,
            BakedModel equippedModel
    ) implements IDynamicBakedModel {
        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
            return itemModel.getQuads(state, side, rand, extraData, renderType);
        }

        @Override
        public boolean useAmbientOcclusion() {
            return itemModel.useAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return itemModel.isGui3d();
        }

        @Override
        public boolean usesBlockLight() {
            return itemModel.usesBlockLight();
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return itemModel.getParticleIcon();
        }

        @Override
        public ItemOverrides getOverrides() {
            return itemModel.getOverrides();
        }

        @Override
        public ItemTransforms getTransforms() {
            return ItemTransforms.NO_TRANSFORMS;
        }

        @Override
        public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform) {
            BakedModel model = cameraTransformType == ItemDisplayContext.HEAD ? equippedModel : itemModel;
            return model.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
        }

        @Override
        public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
            return itemModel.getRenderTypes(state, rand, data);
        }
    }
}
