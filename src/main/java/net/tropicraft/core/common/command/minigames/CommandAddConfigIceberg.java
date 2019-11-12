package net.tropicraft.core.common.command.minigames;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.tropicraft.core.common.config.ConfigLT;

import static net.minecraft.command.Commands.literal;

public class CommandAddConfigIceberg {
	public static void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			literal("minigame")
			.then(literal("addConfigIceberg").requires(s -> s.hasPermissionLevel(2))
			.executes(c -> {
			    Entity entity = c.getSource().getEntity();
			    if (entity != null) {
                    CompoundNBT data = entity.getPersistentData();
			        if (data.contains("lastIcebergConfigPos")) {
			            BlockPos startPos = NBTUtil.readBlockPos(data.getCompound("lastIcebergConfigPos"));
			            BlockPos endPos = entity.getPosition();

                        ForgeConfigSpec.ConfigValue<String> val = ConfigLT.MINIGAME_SURVIVE_THE_TIDE.icebergLines;

                        val.set(val.get() + ConfigLT.icebergLineString(startPos, endPos));
                        val.save();

                        data.remove("lastIcebergConfigPos");

                        c.getSource().sendFeedback(new StringTextComponent("Saved iceberg line to config!"), true);

                        return 1;
                    } else {
                        BlockPos pos = entity.getPosition();
                        data.put("lastIcebergConfigPos", NBTUtil.writeBlockPos(pos));

                        c.getSource().sendFeedback(new StringTextComponent("Cached first block pos. Use the command again to save the iceberg line."), true);

                        return 1;
                    }
                }

                return 0;
            }))
		);
	}
}
