package net.tropicraft.core.common.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftFlower;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.CocktailItem;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CowktailEntity extends CowEntity implements IForgeShearable {
	private static final DataParameter<String> COWKTAIL_TYPE = EntityDataManager.defineId(CowktailEntity.class, DataSerializers.STRING);

	public CowktailEntity(EntityType<? extends CowktailEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getWalkTargetValue(BlockPos pos, IWorldReader worldIn) {
		return worldIn.getBlockState(pos.below()).getBlock() == Blocks.MYCELIUM ? 10.0F : worldIn.getBrightness(pos) - 0.5F;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(COWKTAIL_TYPE, Type.IRIS.name);
	}

	@Override
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem() == TropicraftItems.BAMBOO_MUG.get() && !this.isBaby()) {
			if (player.abilities.instabuild) {
				itemstack.shrink(1);
			}

			final List<RegistryObject<CocktailItem>> cocktails = new ArrayList<>(TropicraftItems.COCKTAILS.values());
			// Remove generic cocktail from cowktail
			cocktails.removeIf(cocktail -> cocktail.isPresent() && cocktail.get().getDrink() == Drink.COCKTAIL);
			final ItemStack cocktailItem = new ItemStack(cocktails.get(random.nextInt(cocktails.size())).get());

			if (itemstack.isEmpty()) {
				player.setItemInHand(hand, cocktailItem);
			} else if (!player.inventory.add(cocktailItem)) {
				player.drop(cocktailItem, false);
			}

			this.playSound(SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY, 1.0F, 1.0F);
			return ActionResultType.SUCCESS;
		}

		return super.mobInteract(player, hand);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("Type", this.getCowktailType().name);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.setCowktailType(CowktailEntity.Type.getTypeByName(compound.getString("Type")));
	}

	private void setCowktailType(CowktailEntity.Type typeIn) {
		this.entityData.set(COWKTAIL_TYPE, typeIn.name);
	}

	public CowktailEntity.Type getCowktailType() {
		return CowktailEntity.Type.getTypeByName(this.entityData.get(COWKTAIL_TYPE));
	}

	@Override
	public CowktailEntity getBreedOffspring(ServerWorld world, AgeableEntity ageable) {
		CowktailEntity child = TropicraftEntities.COWKTAIL.get().create(this.level);
		child.setCowktailType(this.getOffspringType((CowktailEntity)ageable));
		return child;
	}

	private CowktailEntity.Type getOffspringType(CowktailEntity p_213445_1_) {
		CowktailEntity.Type CowktailEntity$type = this.getCowktailType();
		CowktailEntity.Type CowktailEntity$type1 = p_213445_1_.getCowktailType();
		CowktailEntity.Type CowktailEntity$type2;
		if (CowktailEntity$type == CowktailEntity$type1 && this.random.nextInt(1024) == 0) {
			CowktailEntity$type2 = Type.getRandomType(random);
		} else {
			CowktailEntity$type2 = this.random.nextBoolean() ? CowktailEntity$type : CowktailEntity$type1;
		}

		return CowktailEntity$type2;
	}

	@Override
	public boolean isShearable(@Nonnull ItemStack item, World world, BlockPos pos) {
		return !this.isBaby();
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nullable PlayerEntity player, @Nonnull ItemStack item, World world, BlockPos pos, int fortune) {
		java.util.List<ItemStack> ret = new java.util.ArrayList<>();
		this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5D), this.getZ(), 0.0D, 0.0D, 0.0D);
		if (!this.level.isClientSide) {
			this.remove();
			CowEntity cowentity = EntityType.COW.create(this.level);
			cowentity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
			cowentity.setHealth(this.getHealth());
			cowentity.yBodyRot = this.yBodyRot;
			if (this.hasCustomName()) {
				cowentity.setCustomName(this.getCustomName());
				cowentity.setCustomNameVisible(this.isCustomNameVisible());
			}
			this.level.addFreshEntity(cowentity);
			for(int i = 0; i < 5; ++i) {
				ret.add(new ItemStack(this.getCowktailType().renderState.getBlock()));
			}
			this.playSound(SoundEvents.MOOSHROOM_SHEAR, 1.0F, 1.0F);
		}
		return ret;
	}

	@Nullable
	public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
		setCowktailType(Type.getRandomType(random));
		return super.finalizeSpawn(world, difficultyInstance, spawnReason, data, nbt);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(TropicraftItems.COWKTAIL_SPAWN_EGG.get());
	}

	public enum Type {
		IRIS("iris", TropicraftBlocks.IRIS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)),
		ANEMONE("anemone", TropicraftBlocks.FLOWERS.get(TropicraftFlower.ANEMONE).get().defaultBlockState());

		private final String name;
		private final BlockState renderState;

		Type(String nameIn, BlockState renderStateIn) {
			this.name = nameIn;
			this.renderState = renderStateIn;
		}

		public static CowktailEntity.Type getRandomType(final Random rand) {
			return values()[rand.nextInt(values().length)];
		}

		/**
		 * A block state that is rendered on the back of the mooshroom.
		 */
		@OnlyIn(Dist.CLIENT)
		public BlockState getRenderState() {
			return this.renderState;
		}

		private static CowktailEntity.Type getTypeByName(String nameIn) {
			for(CowktailEntity.Type CowktailEntity$type : values()) {
				if (CowktailEntity$type.name.equals(nameIn)) {
					return CowktailEntity$type;
				}
			}

			return IRIS;
		}
	}
}
