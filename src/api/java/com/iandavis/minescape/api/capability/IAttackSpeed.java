package com.iandavis.minescape.api.capability;

import com.iandavis.minescape.api.capability.ICommonCapability;

public interface IAttackSpeed extends ICommonCapability {
    float getBonusAttackSpeed();

    void setBonusAttackSpeed(final float bonusAttackSpeed);

    void addBonusAttackSpeed(final float bonusAttackSpeed);

    void synchronize();
}
