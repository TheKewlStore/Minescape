package com.iandavis.minescape.utils;

public class XPBinaryTree {
    private XPBinaryTreeNode root;

    public XPBinaryTree(int rootLevel, int rootXP) {
        this.root = new XPBinaryTreeNode(null, null, rootXP, rootLevel);
    }

    public XPBinaryTree(int[] xpLevels) {
        int offset = xpLevels.length / 2;
        this.root = new XPBinaryTreeNode(null, null, xpLevels[offset], offset);
        addAll(xpLevels, offset);
    }

    public void addAll(int[] xpLevels, int offset) {
        if (xpLevels.length == 1) {
            addNode(xpLevels[0], offset + 1);
        } else if (xpLevels.length >= 1){

        }
    }

    public void addNode(int level, int xp) {
        addNode(this.root, level, xp);
    }

    public void addNode(XPBinaryTreeNode node, int level, int xp) {
        if (xp > node.getXpValue()) {
            if (node.getLeft() == null) {
                setLeftNode(node.getRight(), level, xp);
            } else {
                addNode(node.getLeft(), level, xp);
            }
        } else if (xp < node.getXpValue()) {
            if (node.getRight() == null) {
                setRightNode(node.getLeft(), level, xp);
            } else {
                addNode(node.getRight(), level, xp);
            }
        }
    }

    private void setLeftNode(XPBinaryTreeNode node, int level, int xp) {
        node.setLeft(new XPBinaryTreeNode(null, null, level, xp));
    }

    private void setRightNode(XPBinaryTreeNode node, int level, int xp) {
        node.setRight(new XPBinaryTreeNode(null, null, level, xp));
    }

}
