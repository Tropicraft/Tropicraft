package net.tropicraft.core.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.UnbakedGeometry;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.context.ContextMap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;
import net.tropicraft.Tropicraft;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@EventBusSubscriber(modid = Tropicraft.ID, value = Dist.CLIENT)
public class ItemModelWithBackGenerator implements UnbakedModel {
    private static final ItemModelGenerator PARENT = new ItemModelGenerator();

    @SubscribeEvent
    public static void register(ModelEvent.RegisterLoaders event) {
        event.register(Tropicraft.location("generated_with_back"), new Loader());
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

                Material backMaterial = textureSlots.getMaterial(TextureSlot.BACK.getId());
                if (backMaterial == null) {
                    return parentQuads;
                }

                TextureAtlasSprite backSprite = baker.sprites().get(backMaterial, debugName);

                QuadCollection.Builder result = new QuadCollection.Builder();
                for (BakedQuad quad : parentQuads.getAll()) {
                    if (isBackQuad(quad)) {
                        result.addUnculledFace(remapSprite(quad, backSprite));
                    } else {
                        result.addUnculledFace(quad);
                    }
                }

                return result.build();
            }

            private static boolean isBackQuad(BakedQuad quad) {
                return quad.direction() == Direction.SOUTH;
            }

            private static BakedQuad remapSprite(BakedQuad quad, TextureAtlasSprite newSprite) {
                int[] newVertices = Arrays.copyOf(quad.vertices(), quad.vertices().length);
                for (int vertex = 0; vertex < 4; vertex++) {
                    int uvBase = vertex * 8 + 4;
                    float u = Float.intBitsToFloat(newVertices[uvBase]);
                    float v = Float.intBitsToFloat(newVertices[uvBase + 1]);
                    float localU = quad.sprite().getUOffset(u);
                    float localV = quad.sprite().getVOffset(v);
                    float newU = newSprite.getU(localU);
                    float newV = newSprite.getV(localV);
                    newVertices[uvBase] = Float.floatToRawIntBits(newU);
                    newVertices[uvBase + 1] = Float.floatToRawIntBits(newV);
                }
                return new BakedQuad(newVertices, quad.tintIndex(), quad.direction(), newSprite, quad.shade(), quad.lightEmission(), quad.hasAmbientOcclusion());
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
