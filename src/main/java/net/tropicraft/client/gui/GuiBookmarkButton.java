package net.tropicraft.client.gui;

import net.tropicraft.core.encyclopedia.Page;

public class GuiBookmarkButton extends GuiClearButton {
    
    private final int targetPage;
    private final Page section;

    public GuiBookmarkButton(Page section, int target, int i, int j, int k, int l, int i1) {
        super(i, j, k, l, i1, section.getLocalizedTitle(), -1, "encyclopedia_tropica_inside", 0x440000);
        this.targetPage = target;
        this.section = section;
    }
    
    public int getTarget() {
        return targetPage;
    }

    public Page getPage() {
        return section;
    }
}
