package org.phemethyst.bart.entity.custom;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BartEntity extends Monster {
    public static final AnimationState idleAnimState = new AnimationState();
    private int idleAnimTimeout = 0;

    // tutorial timestamp 7:05
    // kaupenjoe my goat
    // and if anyone has an issue, this is for bap and i kinda want something, right?
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 3.0, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public BartEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
}
