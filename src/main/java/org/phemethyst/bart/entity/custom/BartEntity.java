package org.phemethyst.bart.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.MenuProvider;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.entity.goals.TelefraggerGoal;
import org.phemethyst.bart.entity.goals.TransitionGoal;
import org.phemethyst.bart.event.ASingularBoolean;
import org.phemethyst.bart.ui.BartScreen;

public class BartEntity extends Monster implements MenuProvider {
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

    public final int minTelefraggerTime = 150;
    public final int maxTelefraggerTime = 200;
    public int currentTelefraggerTime;
    public int telefraggerTimer = 300;

    public boolean isTelefragging = false;
    public boolean isDashing = false;
    public boolean isMidDash = false;
    public boolean downed = false;

    private Vec3 dashTarget = Vec3.ZERO;
    private final int minDashDistance = 5;

    public int dashTimer = -1;
    public int midDashTimer = -1;

    public boolean passive = true;

    public int telefragTimer = 0;

    public int wakeupeepyheadTimer = -1;

    public final int minTransitionTime = 80;
    public final int maxTransitionTime = 140;
    public int currentTransitionTime;
    public int transitionTimer = 100;

    public boolean canDash = false;
    public boolean canTelefrag = false;
    public boolean canDie = false;

    private final ServerBossEvent bossEvent =
            new ServerBossEvent(Component.literal("Bart, Destroyer of Bartenders"), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.NOTCHED_6);

    // kaupenjoe my goat
    // and if anyone has an issue, this is for bap and i kinda want something, right?
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, true));
        this.goalSelector.addGoal(2, new TelefraggerGoal(this));
        this.goalSelector.addGoal(3, new TransitionGoal(this));
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
        dashTimer--;
        midDashTimer--;

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

        if (isMidDash) {
            this.getNavigation().stop();
            this.setDeltaMovement(dashTarget.x, dashTarget.y, dashTarget.z);
        }

        if (passive || wakeupeepyheadTimer > 0) {
            this.setDeltaMovement(0, 0, 0);
        }

        if (dashTimer == 0) {
            isDashing = false;
            isMidDash = true;

            dash();
        }

        if (midDashTimer == 0) {
            isMidDash = false;
            dashTarget = Vec3.ZERO;
            this.level().broadcastEntityEvent(this, (byte) 31);
        }

        if (this.getHealth() <= 0 && !canDie) {
            this.downed = true;
            this.level().broadcastEntityEvent(this, (byte) 41); // 41 but i got 41 bart
        }

        if (this.downed) {
            this.setHealth(this.getMaxHealth());
            this.level().broadcastEntityEvent(this, (byte) 40);
            this.setNoAi(true);
        } else {
            this.setNoAi(false);
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
        } else if (id == 30 && this.level().isClientSide()) {
            this.isDashing = true;
            this.dashAnimState.start(this.tickCount);
        } else if (id == 31 && this.level().isClientSide()) {
            this.isDashing = false;
        } else if (id == 40 && this.level().isClientSide()) {
            ASingularBoolean.heyDoIDrawTheUpgradesYet = true;
        } else if (id == 41 && this.level().isClientSide()) {
            this.downedAnimState.start(this.tickCount);

            // pick 1 bart buff and 3 player



            Minecraft.getInstance().setScreen(new BartScreen(Component.literal("Bart")));
        }
    }

    private void dash() {
        Vec3 targetPos = this.getTarget().getPosition(0);
        Vec3 bartPos = this.getPosition(0);

        Vec3 diff = new Vec3(targetPos.x - bartPos.x,
                targetPos.y - bartPos.y,
                targetPos.z - bartPos.z);

        Vec3 dir = diff.scale(1/diff.length());

        float distance = Math.min((float)minDashDistance, (float)diff.length());

        dashTarget = dir.scale(distance * 0.2);
        midDashTimer = 10;
    }

    @Override
    public boolean isDeadOrDying() {
        return this.getHealth() <= 0.0F && this.canDie;
    }

    public void enableDash() {
        canDash = true;
    }

    public void enableTelefrag() {
        canTelefrag = true;
    }

    public void enableDeath() {
        canDie = true;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void buffPicked() {
        downed = false;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }
}
