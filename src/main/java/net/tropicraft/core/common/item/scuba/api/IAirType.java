package net.tropicraft.core.common.item.scuba.api;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.I18n;

@MethodsReturnNonnullByDefault
public interface IAirType {
    
    float getMaxCapacity();
    
    float getUsageRate();
    
    String getID();
    
    String getDisplayName();

    public enum AirType implements IAirType {

        REGULAR(3200, 1.005F, "regular"),
        TRIMIX(3200, 1.185F, "trimix");

        /** The max amount of psi one tank of this air type can hold */
        private float maxCapacity;

        /** The average amount of air that escapes one tank of this air per second */
        private float usageRate;

        /** The name that shows up in the GUI when this air type is used */
        private String id;

        private AirType(float maxCapacity, float usageRate, String id) {
            this.maxCapacity = maxCapacity;
            this.usageRate = usageRate;
            this.id = id;
        }

        @Override
        public float getMaxCapacity() {
            return this.maxCapacity;
        }

        @Override
        public float getUsageRate() {
            return this.usageRate;
        }
        
        @Override
        public String getID() {
            return id;
        }

        @Override
        public String getDisplayName() {
            return I18n.format("tropicraft.gui.air." + id);
        }
    }
}
