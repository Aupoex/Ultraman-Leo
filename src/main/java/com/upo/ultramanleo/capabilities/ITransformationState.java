package com.upo.ultramanleo.capabilities;

public interface ITransformationState {
    boolean isTransformed();
    int getTransformTicks();
    void setTransformTicks(int ticks);
    int getCooldownTicks();
    void setCooldownTicks(int ticks);
    void startTransform(int durationTicks);
    void endTransform();
    void setSkillZProgress(int i);
    void setUsingSkillZ(boolean b);
    boolean isUsingSkillZ();
    int getSkillZProgress();
    int getSkillZCooldownTicks();
    void setSkillZCooldown(int ticks);
    void tickSkillZCooldown();
    void tick();
    void tickCooldown();
    int getSkillZTimerTicks();
    void tickSkillZTimer();
    void resetSkillZTimer();
}
