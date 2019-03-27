package com.iandavis.runecraft.skills;

import com.iandavis.runecraft.gui.Position;
import net.minecraft.util.ResourceLocation;

public class SkillIcon {
    private ResourceLocation textureLocation;
    private Position textureOffset;
    private int texWidth;
    private int texHeight;

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
