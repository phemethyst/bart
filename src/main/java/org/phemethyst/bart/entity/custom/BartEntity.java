package org.phemethyst.bart.entity.custom;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class BartEntity extends Mob {
    public static final AnimationState idleAnimState = new AnimationState();
    private int idleAnimTimeout = 0;

    public BartEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }
}
