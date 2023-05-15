package me.xemu.fragment.menu;

public abstract class Paged extends Menu {

    protected int page = 0;
    protected int maxItems = 35;
    protected int index = 0;

    public Paged(MenuUtil menuUtil) {
        super(menuUtil);
    }

    protected int getPage() {
        return page + 1;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void applyLayout() {
        // layout function
    }
}