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
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftFlower;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.CocktailItem;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CowktailEntity extends CowEntity implements net.minecraftforge.common.IShearable {
	private static final DataParameter<String> COWKTAIL_TYPE = EntityDataManager.createKey(CowktailEntity.class, DataSerializers.STRING);

	public CowktailEntity(EntityType<? extends CowktailEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
		return worldIn.getBlockState(pos.down()).getBlock() == Blocks.MYCELIUM ? 10.0F : worldIn.getBrightness(pos) - 0.5F;
	}

	protected void registerData() {
		super.registerData();
		this.dataManager.register(COWKTAIL_TYPE, Type.IRIS.name);
	}

	public boolean processInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (itemstack.getItem() == TropicraftItems.BAMBOO_MUG.get() && !this.isChild()) {
			if (player.abilities.isCreativeMode) {
				itemstack.shrink(1);
			}

			final List<RegistryObject<CocktailItem>> cocktails = new ArrayList<>(TropicraftItems.COCKTAILS.values());
			// Remove generic cocktail from cowktail
			cocktails.removeIf(cocktail -> cocktail.isPresent() && cocktail.get().getDrink() == Drink.COCKTAIL);
			final ItemStack cocktailItem = new ItemStack(cocktails.get(rand.nextInt(cocktails.size())).get());

			if (itemstack.isEmpty()) {
				player.setHeldItem(hand, cocktailItem);
			} else if (!player.inventory.addItemStackToInventory(cocktailItem)) {
				player.dropItem(cocktailItem, false);
			}

			this.playSound(SoundEvents.ENTITY_MOOSHROOM_SUSPICIOUS_MILK, 1.0F, 1.0F);
			return true;
		}

		return super.processInteract(player, hand);
	}

	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putString("Type", this.getCowktailType().name);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setCowktailType(CowktailEntity.Type.getTypeByName(compound.getString("Type")));
	}

	private void setCowktailType(CowktailEntity.Type typeIn) {
		this.dataManager.set(COWKTAIL_TYPE, typeIn.name);
	}

	public CowktailEntity.Type getCowktailType() {
		return CowktailEntity.Type.getTypeByName(this.dataManager.get(COWKTAIL_TYPE));
	}

	public CowktailEntity createChild(AgeableEntity ageable) {
		CowktailEntity CowktailEntity = TropicraftEntities.COWKTAIL.get().create(this.world);
		CowktailEntity.setCowktailType(this.func_213445_a((CowktailEntity)ageable));
		return CowktailEntity;
	}

	private CowktailEntity.Type func_213445_a(CowktailEntity p_213445_1_) {
		CowktailEntity.Type CowktailEntity$type = this.getCowktailType();
		CowktailEntity.Type CowktailEntity$type1 = p_213445_1_.getCowktailType();
		CowktailEntity.Type CowktailEntity$type2;
		if (CowktailEntity$type == CowktailEntity$type1 && this.rand.nextInt(1024) == 0) {
			CowktailEntity$type2 = Type.getRandomType(rand);
		} else {
			CowktailEntity$type2 = this.rand.nextBoolean() ? CowktailEntity$type : CowktailEntity$type1;
		}

		return CowktailEntity$type2;
	}

	@Override
	public boolean isShearable(ItemStack item, net.minecraft.world.IWorldReader world, net.minecraft.util.math.BlockPos pos) {
		return !this.isChild();
	}

	@Override
	public java.util.List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IWorld world, net.minecraft.util.math.BlockPos pos, int fortune) {
		java.util.List<ItemStack> ret = new java.util.ArrayList<>();
		this.world.addParticle(ParticleTypes.EXPLOSION, this.getPosX(), this.getPosYHeight(0.5D), this.getPosZ(), 0.0D, 0.0D, 0.0D);
		if (!this.world.isRemote) {
			this.remove();
			CowEntity cowentity = EntityType.COW.create(this.world);
			cowentity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
			cowentity.setHealth(this.getHealth());
			cowentity.renderYawOffset = this.renderYawOffset;
			if (this.hasCustomName()) {
				cowentity.setCustomName(this.getCustomName());
				cowentity.setCustomNameVisible(this.isCustomNameVisible());
			}
			this.world.addEntity(cowentity);
			for(int i = 0; i < 5; ++i) {
				ret.add(new ItemStack(this.getCowktailType().renderState.getBlock()));
			}
			this.playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1.0F, 1.0F);
		}
		return ret;
	}

	@Nullable
	public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
		setCowktailType(Type.getRandomType(rand));
		return super.onInitialSpawn(world, difficultyInstance, spawnReason, data, nbt);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(TropicraftItems.COWKTAIL_SPAWN_EGG.get());
	}

	public enum Type {
		IRIS("iris", TropicraftBlocks.IRIS.get().getDefaultState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)),
		ANEMONE("anemone", TropicraftBlocks.FLOWERS.get(TropicraftFlower.ANEMONE).get().getDefaultState());

		private final String name;
		private final BlockState renderState;

		private Type(String nameIn, BlockState renderStateIn) {
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