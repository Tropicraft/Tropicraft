package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.ReplaceInSoilDecorator;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.PneumatophoresTreeDecorator;

public final class TropicraftTreeDecorators {
    public static final DeferredRegister<TreeDecoratorType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, Constants.MODID);

    public static RegistryObject<TreeDecoratorType<?>> REPLACE_IN_SOIL = register("replace_in_soil", ReplaceInSoilDecorator.CODEC);
    public static RegistryObject<TreeDecoratorType<?>> PNEUMATOPHORES = register("pneumatophores", PneumatophoresTreeDecorator.CODEC);
    public static RegistryObject<TreeDecoratorType<?>> PAPAYA = register("papaya", PapayaTreeDecorator.CODEC);

    private static <T extends TreeDecorator> RegistryObject<TreeDecoratorType<?>> register(String name, Codec<T> codec) {
        return REGISTER.register(name, () -> new TreeDecoratorType<>(codec));
    }
}
