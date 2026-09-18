package org.phemethyst.bart.entity.custom;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BartEntity extends Monster {
    public final AnimationState wakeupeepyheadAnimState = new AnimationState();
    public final AnimationState downedAnimState = new AnimationState();
    public final AnimationState wersobackAnimState = new AnimationState();
    public final AnimationState dashAnimState = new AnimationState();
    public final AnimationState blueteleportalAnimState = new AnimationState();
    public final AnimationState orangeteleportalAnimState = new AnimationState();
    public final AnimationState idle1AnimState = new AnimationState();
    public final AnimationState idle2AnimState = new AnimationState();

    public final WalkAnimationState walkAnimation = new WalkAnimationState();

    private int wakeupeepyheadTimeout = 0;
    private int downedTimeout = 0;
    private int wersobackTimeout = 0;
    private int dashTimeout = 0;
    private int blueteleportalTimeout = 0;
    private int orangeteleportalTimeout = 0;
    private int walkTimeout = 0;
    private int idle2Timeout = 0;

    // tutorial timestamp 17:38
    // (im like hey whats up hello)
    // kaupenjoe my goat
    // and if anyone has an issue, this is for bap and i kinda want something, right?
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 3.0, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.MOVEMENT_SPEED, 2)
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.ATTACK_DAMAGE, 4)
                .add(Attributes.ATTACK_SPEED, 1);
    }

    // might have to revisit tbh
    // ofc i WILL have to revisit but i highly doubt walking will just work
    private void setupAnimationStates() {
        if (this.walkAnimation.isMoving()) {
            return;
        }

        // all other anims go here btw

        this.idle1AnimState.start(0);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    public BartEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
}
