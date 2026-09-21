package org.phemethyst.bart.entity.goals;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import org.phemethyst.bart.entity.custom.BartEntity;

import java.util.Random;

public class TelefraggerGoal extends Goal {
    protected final PathfinderMob mob;

    public final int minTelefraggerRange = 8;
    public final int maxTelefraggerRange = 14;
    private int currentTelefraggerRange;

    public final int telefraggerAngle = 105;

    public TelefraggerGoal(PathfinderMob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (!(mob instanceof BartEntity)) {
            return false;
        }
        BartEntity bart = (BartEntity) mob;

        if (bart.telefraggerTimer <= 0) {
            bart.currentTelefraggerTime =
                    (int) Mth.randomBetweenInclusive(RandomSource.create(), bart.minTelefraggerTime, bart.maxTelefraggerTime);
            bart.telefraggerTimer = bart.currentTelefraggerTime;
            return true;
        } else {
            return false;
        }
    }

    public void start() {
        if (this.mob.getTarget() == null) {
            return;
        }

        if (!(mob instanceof BartEntity)) {
            return;
        }
        BartEntity bart = (BartEntity) mob;

        if (bart.passive) {
            return;
        }

        bart.isTelefragging = true;

        this.mob.setDeltaMovement(0, 0, 0);

        currentTelefraggerRange = (int)
                Mth.randomBetweenInclusive(RandomSource.create(), minTelefraggerRange, maxTelefraggerRange);

        float bartAngle = 0;
        float bartDist = 0;
        Vec2 selectedPos = Vec2.ZERO;

        Vec2 targetAngle = new Vec2(
                (float) this.mob.getTarget().getLookAngle().x,
                (float) this.mob.getTarget().getLookAngle().z
        );

        Vec2 targetPos = new Vec2((float) this.mob.getTarget().getPosition(0).x,
                (float) this.mob.getTarget().getPosition(0).z);
        Vec2 mobPos = new Vec2((float) this.mob.getPosition(0).x,
                (float) this.mob.getPosition(0).z);

        int i = 0;

        while ((bartAngle <= telefraggerAngle || bartDist > currentTelefraggerRange) && i < 1000) {
            i++;

            selectedPos = new Vec2((float) Mth.randomBetweenInclusive(RandomSource.create(),
                    (int)(targetPos.x - currentTelefraggerRange), (int)(targetPos.x + currentTelefraggerRange)),
                    (float) Mth.randomBetweenInclusive(RandomSource.create(),
                            (int)(targetPos.y - currentTelefraggerRange), (int)(targetPos.y + currentTelefraggerRange)));

            float dx = selectedPos.x - targetPos.x;
            float dz = selectedPos.y - targetPos.y;

            bartDist = Mth.sqrt(dx * dx + dz * dz);

            if (bartDist > currentTelefraggerRange) {
                continue;
            }

            Vec2 diff = new Vec2(
                    selectedPos.x - targetPos.x,
                    selectedPos.y - targetPos.y
            );

            float targetMagnitude = targetAngle.length();
            float diffMagnitude = diff.length();

            float cosAngle = targetAngle.dot(diff) / (targetMagnitude * diffMagnitude);
            bartAngle = (float) Math.toDegrees(Math.acos(
                    Mth.clamp(cosAngle, -1.0F, 1.0F)
            ));

            AABB box = this.mob.getBoundingBox()
                    .move(selectedPos.x - this.mob.getX(),
                            this.mob.getY(),
                            selectedPos.y - this.mob.getZ());

            if (!this.mob.level().noCollision(this.mob, box)) {
                continue;
            }
        }

        this.mob.teleportTo((double) selectedPos.x, this.mob.getY(), (double) selectedPos.y);
        bart.telefragTimer = 20;

        bart.level().broadcastEntityEvent(bart, (byte) 1);
    }
}
