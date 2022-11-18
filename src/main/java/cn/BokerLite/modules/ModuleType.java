package cn.BokerLite.modules;

import cn.BokerLite.gui.clickgui.components.ModuleWindow;

public enum ModuleType {
    Combat("Combat", "战斗类",SubCategory.COMBAT_RAGE, SubCategory.COMBAT_LEGIT),
    Render("Render", "视觉类",SubCategory.RENDER_MODEL, SubCategory.RENDER_WORLD, SubCategory.RENDER_SELF, SubCategory.RENDER_OVERLAY),
    Movement("Movement","行走类",SubCategory.MOVEMENT_MAIN, SubCategory.MOVEMENT_EXTRAS),
    Player("Player", "玩家类",SubCategory.PLayer_Player,SubCategory.PLayer_assist),
    World("World", "世界类",SubCategory.World_World);
    private final SubCategory[] subCategories;
    public SubCategory[] getSubCategories() {
        return subCategories;
    }


    private final String name;
    private final String chinese;
    
    private final ModuleWindow window;
    
    ModuleType(String name, String chineseName,SubCategory... subCategories) {
        this.name = name;
        this.chinese = chineseName;
        window = new ModuleWindow(name, chineseName);
        this.subCategories = subCategories;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public ModuleWindow getWindow() {
        // TODO Auto-generated method stub
        return window;
    }
    public enum SubCategory {
        // Combat
        COMBAT_RAGE("Rage"),
        COMBAT_LEGIT("Legit"),

        // Movement
        MOVEMENT_MAIN("Main"),
        MOVEMENT_EXTRAS("Extras"),


        // Render
        RENDER_MODEL("Model"),
        RENDER_WORLD("World"),
        RENDER_SELF("Self"),
        RENDER_OVERLAY("Overlay"),

        //Player
        PLayer_Player("Counterattack"),
        PLayer_assist("Assist"),

        //World
        World_World("World"),;
        private final String name;

        SubCategory(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
    public String getChinese() {
        return chinese;
    }
}
