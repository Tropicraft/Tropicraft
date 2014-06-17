package net.tropicraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.tropicraft.entity.projectile.EntityDart;

public class TCDamageSource extends DamageSource {

    public TCDamageSource(String par1Str)
    {
        super(par1Str);
    }

    public static DamageSource causeDartDamage(EntityDart dart, Entity shooter) {
        return (new DartDamage("dart", dart, shooter)).setProjectile();
    }

}
