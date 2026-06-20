package net.tropicraft.core.common.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Util;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.event.EventHooks;
import net.tropicraft.core.common.TropicraftRegistries;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftFlower;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.CocktailItem;
import net.tropicraft.core.common.item.TropicraftItems;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CowktailEntity extends Cow implements IShearable {
    // TODO: Replace string with network id & custom serializer
    private static final EntityDataAccessor<String> COWKTAIL_TYPE = SynchedEntityData.defineId(CowktailEntity.class, EntityDataSerializers.STRING);

    public CowktailEntity(EntityType<? extends CowktailEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.getBlockState(pos.below()).is(Blocks.MYCELIUM) ? 10.0f : worldIn.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COWKTAIL_TYPE, Type.IRIS.name);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(TropicraftItems.BAMBOO_MUG) && !isBaby()) {
            itemStack.consume(1, player);

            Registry<Drink> drinks = registryAccess().lookupOrThrow(TropicraftRegistries.DRINK);
            ItemStack cocktailItem = drinks.getRandom(random)
                    .map(CocktailItem::makeDrink)
                    .map(ItemStackTemplate::create)
                    .orElse(ItemStack.EMPTY);

            if (itemStack.isEmpty()) {
                player.setItemInHand(hand, cocktailItem);
            } else if (!player.getInventory().add(cocktailItem)) {
                player.drop(cocktailItem, false);
            }

            playSound(SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putString("Type", getCowktailType().name);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setCowktailType(input.read("Type", Type.CODEC).orElse(Type.IRIS));
    }

    private void setCowktailType(CowktailEntity.Type typeIn) {
        entityData.set(COWKTAIL_TYPE, typeIn.name);
    }

    public CowktailEntity.Type getCowktailType() {
        return Type.CODEC.byName(entityData.get(COWKTAIL_TYPE), Type.IRIS);
    }

    @Override
    @Nullable
    public CowktailEntity getBreedOffspring(ServerLevel level, AgeableMob partner) {
        CowktailEntity child = TropicraftEntities.COWKTAIL.get().create(level, EntitySpawnReason.BREEDING);
        if (child != null) {
            child.setCowktailType(getOffspringType((CowktailEntity) partner));
        }
        return child;
    }

    private CowktailEntity.Type getOffspringType(CowktailEntity partner) {
        CowktailEntity.Type type = getCowktailType();
        CowktailEntity.Type parnerType = partner.getCowktailType();
        if (type == parnerType && random.nextInt(1024) == 0) {
            return Type.getRandomType(random);
        } else {
            return random.nextBoolean() ? type : parnerType;
        }
    }

    @Override
    public boolean isShearable(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        return !isBaby();
    }

    @Override
    public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        playSound(SoundEvents.MOOSHROOM_SHEAR, 1.0f, 1.0f);
        List<ItemStack> items = new ArrayList<>();
        if (!EventHooks.canLivingConvert(this, EntityTypes.COW, timer -> {
        })) {
            return items;
        }
        if (!level.isClientSide()) {
            convertTo(EntityTypes.COW, ConversionParams.single(this, false, false), cow -> {
                EventHooks.onLivingConvert(this, cow);
                level.addParticle(ParticleTypes.EXPLOSION, getX(), getY(0.5), getZ(), 0.0, 0.0, 0.0);
                for (int i = 0; i < 5; ++i) {
                    items.add(new ItemStack(getCowktailType().renderState.getBlock()));
                }
            });
        }
        return items;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyInstance, EntitySpawnReason spawnReason, @Nullable SpawnGroupData data) {
        setCowktailType(Type.getRandomType(random));
        return super.finalizeSpawn(world, difficultyInstance, spawnReason, data);
    }

    public enum Type implements StringRepresentable {
        IRIS("iris", TropicraftBlocks.IRIS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)),
        ANEMONE("anemone", TropicraftBlocks.FLOWERS.get(TropicraftFlower.ANEMONE).get().defaultBlockState());

        public static final EnumCodec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        private final String name;
        private final BlockState renderState;

        Type(String nameIn, BlockState renderStateIn) {
            name = nameIn;
            renderState = renderStateIn;
        }

        public static CowktailEntity.Type getRandomType(RandomSource rand) {
            return Util.getRandom(values(), rand);
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        /**
         * A block state that is rendered on the back of the mooshroom.
         */
        public BlockState getRenderState() {
            return renderState;
        }
    }
}
