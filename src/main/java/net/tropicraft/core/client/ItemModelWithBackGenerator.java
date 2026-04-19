package net.tropicraft.core.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.cuboid.ItemModelGenerator;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextMap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;
import net.tropicraft.Tropicraft;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = Tropicraft.ID, value = Dist.CLIENT)
public class ItemModelWithBackGenerator implements UnbakedModel {
    private static final ItemModelGenerator PARENT = new ItemModelGenerator();

    @SubscribeEvent
    public static void register(ModelEvent.RegisterLoaders event) {
        event.register(Tropicraft.id("generated_with_back"), new Loader());
    }

    @Override
    public TextureSlots.Data textureSlots() {
        return PARENT.textureSlots();
    }

    @Override
    @Nullable
    public GuiLight guiLight() {
        return PARENT.guiLight();
    }

    @Override
    public UnbakedGeometry geometry() {
        UnbakedGeometry parent = PARENT.geometry();
        return new UnbakedGeometry() {
            @Override
            public QuadCollection bake(TextureSlots textureSlots, ModelBaker baker, ModelState state, ModelDebugName debugName, ContextMap additionalProperties) {
                QuadCollection parentQuads = parent.bake(textureSlots, baker, state, debugName, additionalProperties);

                Material unbakedBackMaterial = textureSlots.getMaterial(TextureSlot.BACK.getId());
                if (unbakedBackMaterial == null) {
                    return parentQuads;
                }

                Material.Baked backMaterial = baker.materials().get(unbakedBackMaterial, debugName);
                BakedQuad.MaterialInfo backMaterialInfo = baker.interner().materialInfo(BakedQuad.MaterialInfo.of(
                        backMaterial,
                        backMaterial.sprite().transparency(),
                        -1,
                        true,
                        0,
                        false
                ));

                QuadCollection.Builder result = new QuadCollection.Builder();
                for (BakedQuad quad : parentQuads.getAll()) {
                    if (isBackQuad(quad)) {
                        result.addUnculledFace(remapSprite(quad, backMaterialInfo));
                    } else {
                        result.addUnculledFace(quad);
                    }
                }

                return result.build();
            }

            private static boolean isBackQuad(BakedQuad quad) {
                return quad.direction() == Direction.SOUTH;
            }

            private static BakedQuad remapSprite(BakedQuad quad, BakedQuad.MaterialInfo newMaterial) {
                TextureAtlasSprite oldSprite = quad.materialInfo().sprite();
                TextureAtlasSprite newSprite = newMaterial.sprite();
                return new BakedQuad(
                        quad.position0(),
                        quad.position1(),
                        quad.position2(),
                        quad.position3(),
                        remapUv(quad.packedUV0(), oldSprite, newSprite),
                        remapUv(quad.packedUV1(), oldSprite, newSprite),
                        remapUv(quad.packedUV2(), oldSprite, newSprite),
                        remapUv(quad.packedUV3(), oldSprite, newSprite),
                        quad.direction(),
                        newMaterial,
                        quad.bakedNormals(),
                        quad.bakedColors()
                );
            }

            private static long remapUv(long packedUv, TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite) {
                return UVPair.pack(
                        Mth.map(UVPair.unpackU(packedUv), oldSprite.getU0(), oldSprite.getU1(), newSprite.getU0(), newSprite.getU1()),
                        Mth.map(UVPair.unpackV(packedUv), oldSprite.getV0(), oldSprite.getV1(), newSprite.getV0(), newSprite.getV1())
                );
            }

            @Override
            public QuadCollection bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, ModelDebugName debugName) {
                return bake(textureSlots, baker, modelState, debugName, ContextMap.EMPTY);
            }
        };
    }

    public static class Loader implements UnbakedModelLoader<ItemModelWithBackGenerator> {
        private Loader() {
        }

        @Override
        public ItemModelWithBackGenerator read(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new ItemModelWithBackGenerator();
        }
    }
}
