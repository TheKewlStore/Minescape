package com.iandavis.minescape.utils;

import java.util.Arrays;

public class XPBinaryTreeNode {
    private XPBinaryTreeNode left;
    private XPBinaryTreeNode right;

    private int xpValue;
    private int level;

    public XPBinaryTreeNode(int[] xpLevels) {
        this(xpLevels, xpLevels.length / 2);
    }

    public XPBinaryTreeNode(int[] xpLevels, int level) {
        int middleIndex = (int) (xpLevels.length / 2.0f + 0.5f);
        this.level = level;
        xpValue = xpLevels[middleIndex];

        int[] leftArray = Arrays.copyOfRange(xpLevels, 0, middleIndex);
        int[] rightArray = Arrays.copyOfRange(xpLevels, middleIndex + 1, xpLevels.length);

        if (leftArray.length >= 2) {
            this.left = new XPBinaryTreeNode(leftArray, level - (int) (middleIndex / 2.0f + 0.5f));
        } else if (leftArray.length == 1) {
            this.left = new XPBinaryTreeNode(null, null, leftArray[0], level - 1);
        } else {
            this.left = null;
        }

        if (rightArray.length >= 2) {
            this.right = new XPBinaryTreeNode(rightArray, level + (int) (middleIndex / 2.0f + 0.5f));
        } else if (rightArray.length == 1) {
            this.right = new XPBinaryTreeNode(null, null, rightArray[0], level + 1);
        } else {
            this.right = null;
        }
    }

    public XPBinaryTreeNode(XPBinaryTreeNode left, XPBinaryTreeNode right, int xpValue, int level) {
        this.left = left;
        this.right = right;
        this.xpValue = xpValue;
        this.level = level;
    }

    public XPBinaryTreeNode getLeft() {
        return left;
    }

    public XPBinaryTreeNode getRight() {
        return right;
    }

    public int getXpValue() {
        return xpValue;
    }

    public int getLevel() {
        return level;
    }

    public int getXPForLevel(int level) {
        if (level > this.level) {
            if (this.right == null) {
                return this.xpValue;
            } else if (this.right.level < level) {
                return this.xpValue;
            } else {
                return this.right.getXPForLevel(level);
            }
        } else if (level < this.level) {
            if (this.left == null) {
                return this.xpValue;
            } else if (this.left.level > level) {
                return this.level;
            } else {
                return this.left.getXPForLevel(level);
            }
        } else {
            return this.level;
        }
    }

    public int getLevelForXP(int xpValue) {
        if (xpValue > this.xpValue) {
            if (this.right == null) {
                return this.level;
            } else if (this.right.xpValue < xpValue) {
                return this.level;
            } else {
                return this.right.getLevelForXP(xpValue);
            }
        } else if (xpValue < this.xpValue) {
            if (this.left == null) {
                return this.level;
            } else if (this.left.xpValue > xpValue) {
                return this.level;
            } else {
                return this.left.getLevelForXP(xpValue);
            }
        } else {
            return this.level;
        }
    }

    public void setLeft(XPBinaryTreeNode left) {
        this.left = left;
    }

    public void setRight(XPBinaryTreeNode right) {
        this.right = right;
    }
}
