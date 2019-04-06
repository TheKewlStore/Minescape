package com.iandavis.minescape.gui.toast;

import com.iandavis.minescape.api.skills.SkillIcon;
import net.minecraft.client.gui.toasts.GuiToast;

public class LevelUpToast extends TimedToastMessage {
    private SkillIcon skillIcon;
    private String skillName;
    private int newLevel;

    public LevelUpToast(SkillIcon skillIcon, String skillName, int newLevel) {
        this.skillIcon = skillIcon;
        this.skillName = skillName;
        this.newLevel = newLevel;
    }

    public LevelUpToast(int timeToLive, SkillIcon skillIcon, String skillName, int newLevel) {
        super(timeToLive);
        this.skillIcon = skillIcon;
        this.skillName = skillName;
        this.newLevel = newLevel;
    }

    @Override
    protected void drawToast(GuiToast toastGui, long delta) {
        ToastUtils.drawToastBackground(toastGui);

        String title = String.format(
                "Gained %s level!",
                skillName);
        String body = String.format(
                "Current level: %d",
                newLevel);

        ToastUtils.renderTitleAndBody(toastGui, title, body);
        ToastUtils.renderSkillIcon(toastGui, skillIcon);
    }
}
