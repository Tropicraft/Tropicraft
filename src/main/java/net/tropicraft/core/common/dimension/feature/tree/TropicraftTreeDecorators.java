package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.PneumatophoresTreeDecorator;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.ReplaceInSoilDecorator;

public final class TropicraftTreeDecorators {
    public static final DeferredRegister<TreeDecoratorType<?>> REGISTER = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, Constants.MODID);

    public static DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> REPLACE_IN_SOIL = register("replace_in_soil", ReplaceInSoilDecorator.CODEC);
    public static DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> PNEUMATOPHORES = register("pneumatophores", PneumatophoresTreeDecorator.CODEC);
    public static DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> PAPAYA = register("papaya", PapayaTreeDecorator.CODEC);

    private static <T extends TreeDecorator> DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> register(String name, MapCodec<T> codec) {
        return REGISTER.register(name, () -> new TreeDecoratorType<>(codec));
    }
}
