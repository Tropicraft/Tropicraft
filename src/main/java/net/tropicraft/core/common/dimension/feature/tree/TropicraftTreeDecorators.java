package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;

public final class TropicraftTreeDecorators {
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, Constants.MODID);
    public static RegistryObject<TreeDecoratorType<PiangasTreeDecorator>> PIANGUAS = register("pianguas", PiangasTreeDecorator.CODEC);

    private static <T extends TreeDecorator> RegistryObject<TreeDecoratorType<T>> register(String name, Codec<T> codec) {
        return TREE_DECORATORS.register(name, () -> new TreeDecoratorType<>(codec));
    }
}
