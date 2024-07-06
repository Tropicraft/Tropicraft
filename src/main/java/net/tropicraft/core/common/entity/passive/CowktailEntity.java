package net.tropicraft.core.common.entity.passive;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.IShearable;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftFlower;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.CocktailItem;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CowktailEntity extends Cow implements IShearable {
    private static final EntityDataAccessor<String> COWKTAIL_TYPE = SynchedEntityData.defineId(CowktailEntity.class, EntityDataSerializers.STRING);

    public CowktailEntity(EntityType<? extends CowktailEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.getBlockState(pos.below()).getBlock() == Blocks.MYCELIUM ? 10.0f : worldIn.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COWKTAIL_TYPE, Type.IRIS.name);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() == TropicraftItems.BAMBOO_MUG.get() && !isBaby()) {
            if (player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            List<RegistryEntry<Item, CocktailItem>> cocktails = new ArrayList<>(TropicraftItems.COCKTAILS.values());
            // Remove generic cocktail from cowktail
            cocktails.removeIf(cocktail -> cocktail.isBound() && CocktailItem.getDrink(new ItemStack(cocktail)) == Drink.COCKTAIL);
            ItemStack cocktailItem = new ItemStack(cocktails.get(random.nextInt(cocktails.size())).get());

            if (itemstack.isEmpty()) {
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Type", getCowktailType().name);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setCowktailType(CowktailEntity.Type.getTypeByName(compound.getString("Type")));
    }

    private void setCowktailType(CowktailEntity.Type typeIn) {
        entityData.set(COWKTAIL_TYPE, typeIn.name);
    }

    public CowktailEntity.Type getCowktailType() {
        return CowktailEntity.Type.getTypeByName(entityData.get(COWKTAIL_TYPE));
    }

    @Override
    public CowktailEntity getBreedOffspring(ServerLevel world, AgeableMob ageable) {
        CowktailEntity child = TropicraftEntities.COWKTAIL.get().create(level());
        child.setCowktailType(getOffspringType((CowktailEntity) ageable));
        return child;
    }

    private CowktailEntity.Type getOffspringType(CowktailEntity cowktail) {
        CowktailEntity.Type CowktailEntity$type = getCowktailType();
        CowktailEntity.Type CowktailEntity$type1 = cowktail.getCowktailType();
        CowktailEntity.Type CowktailEntity$type2;
        if (CowktailEntity$type == CowktailEntity$type1 && random.nextInt(1024) == 0) {
            CowktailEntity$type2 = Type.getRandomType(random);
        } else {
            CowktailEntity$type2 = random.nextBoolean() ? CowktailEntity$type : CowktailEntity$type1;
        }

        return CowktailEntity$type2;
    }

    @Override
    public boolean isShearable(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        return !isBaby();
    }

    @Override
    public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        java.util.List<ItemStack> ret = new java.util.ArrayList<>();
        level().addParticle(ParticleTypes.EXPLOSION, getX(), getY(0.5), getZ(), 0.0, 0.0, 0.0);
        if (!level().isClientSide) {
            remove(RemovalReason.DISCARDED);
            Cow cowentity = EntityType.COW.create(level());
            cowentity.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
            cowentity.setHealth(getHealth());
            cowentity.yBodyRot = yBodyRot;
            if (hasCustomName()) {
                cowentity.setCustomName(getCustomName());
                cowentity.setCustomNameVisible(isCustomNameVisible());
            }
            level().addFreshEntity(cowentity);
            for (int i = 0; i < 5; ++i) {
                ret.add(new ItemStack(getCowktailType().renderState.getBlock()));
            }
            playSound(SoundEvents.MOOSHROOM_SHEAR, 1.0f, 1.0f);
        }
        return ret;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyInstance, MobSpawnType spawnReason, @Nullable SpawnGroupData data) {
        setCowktailType(Type.getRandomType(random));
        return super.finalizeSpawn(world, difficultyInstance, spawnReason, data);
    }

    public enum Type {
        IRIS("iris", TropicraftBlocks.IRIS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)),
        ANEMONE("anemone", TropicraftBlocks.FLOWERS.get(TropicraftFlower.ANEMONE).get().defaultBlockState());

        private final String name;
        private final BlockState renderState;

        Type(String nameIn, BlockState renderStateIn) {
            name = nameIn;
            renderState = renderStateIn;
        }

        public static CowktailEntity.Type getRandomType(RandomSource rand) {
            return Util.getRandom(values(), rand);
        }

        /**
         * A block state that is rendered on the back of the mooshroom.
         */
        @OnlyIn(Dist.CLIENT)
        public BlockState getRenderState() {
            return renderState;
        }

        private static CowktailEntity.Type getTypeByName(String nameIn) {
            for (CowktailEntity.Type CowktailEntity$type : values()) {
                if (CowktailEntity$type.name.equals(nameIn)) {
                    return CowktailEntity$type;
                }
            }

            return IRIS;
        }
    }
}
