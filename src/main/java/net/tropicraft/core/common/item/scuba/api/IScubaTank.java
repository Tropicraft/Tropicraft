package net.tropicraft.core.common.item.scuba.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.tropicraft.core.common.item.scuba.api.IAirType.AirType;

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
    default void deserializeNBT(NBTTagCompound nbt) {
        this.setPressure(nbt.getFloat("pressure"));
        this.setAirType(AirTypeRegistry.INSTANCE.getType(nbt.getString("airType")));
    }
    
    public static class ScubaTank implements IScubaTank {
        
        private float pressure;
        private IAirType type = AirType.REGULAR;
        
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
            this.type = type;
        }
    }
}
