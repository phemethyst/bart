package org.phemethyst.bart.entity.goals;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.phemethyst.bart.entity.custom.BartEntity;

public class TransitionGoal extends Goal {
    protected final PathfinderMob mob;

    public TransitionGoal(PathfinderMob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (!(mob instanceof BartEntity)) {
            return false;
        }
        BartEntity bart = (BartEntity) mob;

        if (bart.transitionTimer <= 0) {
            bart.currentTransitionTime =
                    (int) Mth.randomBetweenInclusive(RandomSource.create(), bart.minTransitionTime, bart.maxTransitionTime);
            bart.transitionTimer = bart.currentTransitionTime;
            return true;
        } else {
            return false;
        }
    }
}
