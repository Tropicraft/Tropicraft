package net.tropicraft.core.common.item.scuba.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.logging.log4j.LogManager;

import com.google.common.base.Preconditions;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.tropicraft.core.common.item.scuba.api.IAirType.AirType;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface IScubaTank extends INBTSerializable<NBTTagCompound> {
    
    float getPressure();
    
    void setPressure(float pressure);
    
    IAirType getAirType();
    
    void setAirType(IAirType type);
    
    @Override
    default NBTTagCompound serializeNBT() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setFloat("pressure", getPressure());
        ret.setString("airType", getAirType().getID());
        return ret;
    }
    
    @Override
    default void deserializeNBT(@Nullable NBTTagCompound nbt) {
        if (nbt == null) return;
        this.setPressure(nbt.getFloat("pressure"));
        IAirType type = AirTypeRegistry.INSTANCE.getType(nbt.getString("airType"));
        if (type == null) {
            LogManager.getLogger().error("Found invalid air type reading scuba tank NBT: {}", nbt.getString("airType"));
            type = AirType.REGULAR;
        }
        this.setAirType(type);
    }
    
    public static class ScubaTank implements IScubaTank {
        
        private float pressure;
        private @Nonnull IAirType type = AirType.REGULAR;
        
        @Override
        public float getPressure() {
            return pressure;
        }
        
        @Override
        public void setPressure(float pressure) {
            this.pressure = pressure;
        }
        
        @Override
        public IAirType getAirType() {
            return type;
        }
        
        @Override
        public void setAirType(IAirType type) {
            Preconditions.checkNotNull(type);
            this.type = type;
        }
    }
}
