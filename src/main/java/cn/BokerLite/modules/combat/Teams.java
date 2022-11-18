package cn.BokerLite.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Mode;

public class Teams extends Module {
    private static final Mode<Enum<teamsMode>> mode = new Mode<>("Mode", "Mode", teamsMode.values(), teamsMode.ByName);
    public Teams() {
        super("Teams","队友检查" ,Keyboard.KEY_NONE, ModuleType.Combat, "Not attack player you team",ModuleType.SubCategory.COMBAT_LEGIT);
    }

    public static boolean isOnSameTeam(EntityLivingBase target) {

        if (!ModuleManager.getModule("Teams").getState()){
            return false;
        }else if(!(target instanceof EntityPlayer)){
            return false;
        }else if (mode.getValue()==teamsMode.ByName) {
            return checkEnemyNameColor((EntityPlayer) target);
        }else if (mode.getValue()==teamsMode.ByColor){
            return isWearingTeam((EntityPlayer) target);
        }else if (mode.getValue()==teamsMode.Hypixel){
            return isOnSameTeam1((EntityPlayer) target);
        }else {
            return false;
        }


    }
    public static boolean isWearingTeam(EntityPlayer entity){
        if (Minecraft.getMinecraft().thePlayer.inventory.armorInventory[3] != null && entity.inventory.armorInventory[3] != null) {
            ItemStack myHead = Minecraft.getMinecraft().thePlayer.inventory.armorInventory[3];
            ItemArmor myItemArmor = (ItemArmor)myHead.getItem();

            ItemStack entityHead = entity.inventory.armorInventory[3];
            ItemArmor entityItemArmor = (ItemArmor) myHead.getItem();

            return myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead);
        }else {
            return false;
        }
    }
    public static boolean isOnSameTeam1(EntityPlayer entity) {
        if (!ModuleManager.getModule(Teams.class).getState()) return false;
        if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().startsWith("\247")) {
            if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().length() <= 2
                    || entity.getDisplayName().getUnformattedText().length() <= 2) {
                return false;
            }
            return Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(entity.getDisplayName().getUnformattedText().substring(0, 2));
        }
        return false;
    }

    private static boolean checkEnemyNameColor(EntityPlayer entity) {
        return getEntityNameColor(mc.thePlayer).equals(getEntityNameColor(entity));
    }

    private static String getEntityNameColor(EntityPlayer entity) {
        String name = entity.getDisplayName().getFormattedText();
        if (name.contains("§")) {
            if (name.contains("§1")) {
                return "§1";
            }
            if (name.contains("§2")) {
                return "§2";
            }
            if (name.contains("§3")) {
                return "§3";
            }
            if (name.contains("§4")) {
                return "§4";
            }
            if (name.contains("§5")) {
                return "§5";
            }
            if (name.contains("§6")) {
                return "§6";
            }
            if (name.contains("§7")) {
                return "§7";
            }
            if (name.contains("§8")) {
                return "§8";
            }
            if (name.contains("§9")) {
                return "§9";
            }
            if (name.contains("§0")) {
                return "§0";
            }
            if (name.contains("§e")) {
                return "§e";
            }
            if (name.contains("§d")) {
                return "§d";
            }
            if (name.contains("§a")) {
                return "§a";
            }
            if (name.contains("§b")) {
                return "§b";
            }
            if (name.contains("§c")) {
                return "§c";
            }
            if (name.contains("§f")) {
                return "§f";
            }
        }
        return "null";
    }
    enum teamsMode{
        ByName,
        ByColor,
       Hypixel
    }
}

