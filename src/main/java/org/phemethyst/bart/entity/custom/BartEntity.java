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
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.entity.goals.TelefraggerGoal;

public class BartEntity extends Monster {
    public final AnimationState wakeupeepyheadAnimState = new AnimationState();
    public final AnimationState downedAnimState = new AnimationState();
    public final AnimationState wersobackAnimState = new AnimationState();
    public final AnimationState dashAnimState = new AnimationState();
    public final AnimationState blueteleportalAnimState = new AnimationState();
    public final AnimationState orangeteleportalAnimState = new AnimationState();
    public final AnimationState idle1AnimState = new AnimationState();
    public final AnimationState idle2AnimState = new AnimationState();
    public final AnimationState toxicbitchAnimState = new AnimationState();

    public final WalkAnimationState walkAnimation = new WalkAnimationState();

    private int wakeupeepyheadTimeout = 0;
    private int downedTimeout = 0;
    private int wersobackTimeout = 0;
    private int dashTimeout = 0;
    private int blueteleportalTimeout = 0;
    private int orangeteleportalTimeout = 0;
    private int walkTimeout = 0;
    private int idle2Timeout = 0;
    private int toxicbitchTimeout = 0;

    public final int minTelefraggerTime = 150;
    public final int maxTelefraggerTime = 200;
    public int currentTelefraggerTime;
    public int telefraggerTimer = 300;

    public boolean isTelefragging = false;
    public boolean isDashing = false;

    public boolean passive = true;

    public int telefragTimer = 0;

    public int wakeupeepyheadTimer = -1;

    public final int minTransitionTime = 80;
    public final int maxTransitionTime = 140;
    public int currentTransitionTime;
    public int transitionTimer = 100;

    // kaupenjoe my goat
    // and if anyone has an issue, this is for bap and i kinda want something, right?
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, true));
        this.goalSelector.addGoal(2, new TelefraggerGoal(this));
        // dash
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 60)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.ATTACK_DAMAGE, 4)
                .add(Attributes.ATTACK_SPEED, 2)
                .add(Attributes.KNOCKBACK_RESISTANCE, Bart.MAXINT);
    }

    // might have to revisit tbh
    // ofc i WILL have to revisit but i highly doubt walking will just work

    // TODO: fix orangeteleportal animation
    private void setupAnimationStates() {
        if (this.passive) {
            this.idle2AnimState.start(tickCount);
        } else {
            this.idle1AnimState.start(tickCount);
        }
    }

    @Override
    public void tick() {
        super.tick();

        telefraggerTimer--;
        telefragTimer--;

        transitionTimer--;

        if (wakeupeepyheadTimer > 0) {
            wakeupeepyheadTimer--;
        } else if (wakeupeepyheadTimer != -1) {
            if (passive) {
                passive = false;
            }
        }

        if (telefragTimer <= 0) {
            isTelefragging = false;
        }

        if (isTelefragging || isDashing) {
            this.setDeltaMovement(0, 0, 0);
        }

        if (passive || wakeupeepyheadTimer > 0) {
            this.setDeltaMovement(0, 0, 0);
        }

        if (this.level().isClientSide()) {
            this.setupAnimationStates();

            if (this.passive) {
                this.idle2AnimState.start(tickCount);
            }
        }
    }

    public BartEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);

        if (this.level().isClientSide() && this.passive) {
            this.idle2AnimState.start(this.tickCount);
            return;
        }

        if (id == 1 && this.level().isClientSide()) {
            this.orangeteleportalAnimState.start(this.tickCount);
        } else if (id == 2 && this.level().isClientSide()) {
            this.wakeupeepyheadAnimState.start(this.tickCount);
        }
    }
}
