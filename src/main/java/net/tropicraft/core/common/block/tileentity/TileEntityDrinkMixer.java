package net.tropicraft.core.common.block.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.tropicraft.core.common.block.BlockDrinkMixer;
import net.tropicraft.core.common.block.tileentity.message.MessageMixerInventory;
import net.tropicraft.core.common.block.tileentity.message.MessageMixerStart;
import net.tropicraft.core.common.drinks.Ingredient;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.item.ItemCocktail;
import net.tropicraft.core.common.network.TCPacketHandler;
import net.tropicraft.core.registry.DrinkMixerRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class TileEntityDrinkMixer extends TileEntity implements ITickable {

	/**
	 * Number of ticks the mixer has been mixin'
	 */
	public int ticks;

	/**
	 * Number of ticks to mix
	 */
	public static final int TICKS_TO_MIX = 4*20;

	public ItemStack[] ingredients;

	public boolean mixing;

	public ItemStack result;

	public static final int MAX_NUM_INGREDIENTS = 3;

	public TileEntityDrinkMixer() {
		mixing = false;
		ingredients = new ItemStack[MAX_NUM_INGREDIENTS];
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.ticks = nbt.getInteger("MixTicks");
		this.mixing = nbt.getBoolean("Mixing");

		for (int i = 0; i < MAX_NUM_INGREDIENTS; i++) {
			if (nbt.hasKey("Ingredient0")) {
				this.ingredients[i] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Ingredient" + i));
			} else {
				this.ingredients[i] = null;
			}
		}

		if (nbt.hasKey("Result")) {
			this.result = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Result"));
		} else {
			this.result = null;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("MixTicks", this.ticks);
		nbt.setBoolean("Mixing", mixing);

		for (int i = 0; i < MAX_NUM_INGREDIENTS; i++) {
			if (this.ingredients[i] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				this.ingredients[i].writeToNBT(var4);
				nbt.setTag("Ingredient" + i, var4);
			}
		}

		if (this.result != null) {
			NBTTagCompound var4 = new NBTTagCompound();
			this.result.writeToNBT(var4);
			nbt.setTag("Result", var4);
		}

		return nbt;
	}

	@Override
	public void update() {
		if (ticks < TICKS_TO_MIX && mixing) {
			ticks++;
			if (ticks == TICKS_TO_MIX) {
				finishMixing();
			}
		}
	}

	public boolean isDoneMixing() {
		return result != null;
	}

	public ItemStack[] getIngredients() {
		return this.ingredients;
	}

	public static Ingredient findMatchingIngredient(ItemStack stack) {
		for (Ingredient ingredient: Ingredient.ingredientsList) {
			if (ingredient == null) {
				continue;
			}
			if (ItemStack.areItemStacksEqual(ingredient.getIngredient(), stack)) {
				return ingredient;
			}
		}

		return null;
	}

	public static List<Ingredient> listIngredients(ItemStack stack) {
		List<Ingredient> is = new ArrayList<Ingredient>();

		if (stack != null && stack.getItem() != null && stack.getItem() == ItemRegistry.cocktail) {
			for (Ingredient ingredient: ItemCocktail.getIngredients(stack)) {
				is.add(ingredient);
			}
		} else {
			Ingredient i = findMatchingIngredient(stack);
			is.add(i);
		}

		return is;
	}

	public void startMixing() {
		this.ticks = 0;
		this.mixing = true;
		if (!getWorld().isRemote) {
		    TCPacketHandler.INSTANCE.sendToDimension(new MessageMixerStart(this), getWorld().provider.getDimension());
		}
	}
	
	private void dropItem(@Nonnull ItemStack stack, @Nullable EntityPlayer at) {
        if (at == null) {
            BlockPos pos = getPos().offset(getWorld().getBlockState(getPos()).getValue(BlockDrinkMixer.FACING));
            InventoryHelper.spawnItemStack(getWorld(), pos.getX(), pos.getY(), pos.getZ(), stack);
        } else {
            InventoryHelper.spawnItemStack(getWorld(), at.posX, at.posY, at.posZ, stack);
        }
	}

    public void emptyMixer(@Nullable EntityPlayer at) {
        for (int i = 0; i < MAX_NUM_INGREDIENTS; i++) {
            if (this.ingredients[i] != null) {
                dropItem(this.ingredients[i], at);
                this.ingredients[i] = null;
			}
		}

		this.ticks = TICKS_TO_MIX;
		this.mixing = false;
		this.syncInventory();
	}

	public void retrieveResult(@Nullable EntityPlayer at) {
	    if (result == null) {
	        return;
	    }
	    
		dropItem(result, at);

		for (int i = 0; i < MAX_NUM_INGREDIENTS; i++) {
			// If we're not using one of the ingredient slots, just move along
			if (ingredients[i] == null) continue;

			ItemStack container = ingredients[i].getItem().getContainerItem(ingredients[i]);

			if (container != null) {
				dropItem(container, at);
			}
		}

		this.ingredients[0] = null;
		this.ingredients[1] = null;
		this.ingredients[2] = null;
		this.result = null;
		this.syncInventory();
	}

	public void finishMixing() {
		result = getResult(getIngredients());
		this.mixing = false;
		this.ticks = 0;
		this.syncInventory();
	}

	public boolean addToMixer(ItemStack ingredient) {
		if (this.ingredients[0] == null) {
			if (ingredient.getItem() != ItemRegistry.cocktail) {
				Ingredient i = findMatchingIngredient(ingredient);
				// Ordinarily we check for primary here, but I don't think that feature
				// is as relevant anymore. Will leave it here just in case!
				if (i == null/* || !i.isPrimary()*/) {
					return false;
				}
			}
			this.ingredients[0] = ingredient;
			syncInventory();
			return true;
		} else if (this.ingredients[1] == null) {
			if (ingredient.getItem() == ItemRegistry.cocktail) {
				// prevent mixing multiple primary ingredients
				// all cocktails already contain one
				return false;
			}

			Ingredient ing0 = findMatchingIngredient(this.ingredients[0]);
			Ingredient i = findMatchingIngredient(ingredient);

			// See above comment about isPrimary()
			if (i == null/* || i.isPrimary()*/ || ing0.id == i.id) {
				return false;
			}

			this.ingredients[1] = ingredient;
			syncInventory();
			return true;
		} else if (this.ingredients[2] == null) {
			if (ingredient.getItem() == ItemRegistry.cocktail) {
				// prevent mixing multiple primary ingredients
				// all cocktails already contain one
				return false;
			}

			Ingredient ing0 = findMatchingIngredient(this.ingredients[0]);
			Ingredient ing1 = findMatchingIngredient(this.ingredients[1]);
			Ingredient i = findMatchingIngredient(ingredient);

			// See above comment about isPrimary()
			if (i == null/* || i.isPrimary()*/ || ing0.id == i.id || ing1.id == i.id) {
				return false;
			}

			this.ingredients[2] = ingredient;
			syncInventory();
			return true;
		} else {
			return false;
		}
	}

	public boolean isMixing() {
		return this.mixing;
	}

	private boolean isMixerFull() {
		return MixerRecipes.isValidRecipe(ingredients);
		//return ingredients[0] != null && ingredients[1] != null;
	}

	public boolean canMix() {
		return !mixing && isMixerFull();
	}

	/**
	 * Called when you receive a TileEntityData packet for the location this
	 * TileEntity is currently in. On the client, the NetworkManager will always
	 * be the remote server. On the server, it will be whomever is responsible for
	 * sending the packet.
	 *
	 * @param net The NetworkManager the packet originated from
	 * @param pkt The data packet
	 */
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	protected void syncInventory() {
		TCPacketHandler.INSTANCE.sendToDimension(new MessageMixerInventory(this), getWorld().provider.getDimension());
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
		return nbttagcompound;
	}

	public ItemStack getResult(ItemStack[] ingredients2) {
		return DrinkMixerRegistry.getResult(ingredients2);
	}
}
