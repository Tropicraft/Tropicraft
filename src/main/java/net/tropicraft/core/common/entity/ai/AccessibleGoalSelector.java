package net.tropicraft.core.common.entity.ai;

import com.google.common.collect.Sets;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.profiler.IProfiler;

import java.util.Set;

public class AccessibleGoalSelector extends GoalSelector {

    public final Set<PrioritizedGoal> goals = Sets.newLinkedHashSet();

    public AccessibleGoalSelector(IProfiler profiler) {
        super(profiler);
    }

    public void addGoal(int prio, Goal goal) {
        super.addGoal(prio, goal);
        goals.add(new PrioritizedGoal(prio, goal));
    }
}
