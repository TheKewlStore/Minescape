package com.iandavis.minescape.skills;

import com.iandavis.minescape.gui.utils.Position;
import net.minecraft.util.ResourceLocation;

public class SkillIcon {
    private final ResourceLocation textureLocation;
    private final Position textureOffset;
    private final int texWidth;
    private final int texHeight;

    public SkillIcon(ResourceLocation texture, Position texOffset, int textureWidth, int textureHeight) {
        textureOffset = texOffset;
        textureLocation = texture;
        texWidth = textureWidth;
        texHeight = textureHeight;
    }

    public ResourceLocation getTextureLocation() {
        return textureLocation;
    }

    public Position getTextureOffset() {
        return textureOffset;
    }

    public int getTexWidth() {
        return texWidth;
    }

    public int getTexHeight() {
        return texHeight;
    }
}
