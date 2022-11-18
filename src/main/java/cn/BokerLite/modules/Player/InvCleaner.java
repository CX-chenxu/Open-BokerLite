package cn.BokerLite.modules.Player;

import cn.BokerLite.api.EventHandler;
import cn.BokerLite.api.event.EventPacketSend;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;

import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.MovementUtils;
import cn.BokerLite.utils.misc.InventoryUtils;
import cn.BokerLite.utils.packet.PacketUtil;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public class InvCleaner extends Module {

    public final Numbers delay = new Numbers("Delay", "Delay","Delay",120d, 300d, 0d, 10d);
    public final Numbers slotWeapon = new Numbers("Weapon Slot", "Weapon Slot","Weapon Slot",1d, 9d, 1d, 1d);
    public final Numbers slotPick = new Numbers("Pickaxe Slot","Pickaxe Slot","Pickaxe Slot", 2d, 9d, 1d, 1d);
    public final Numbers slotAxe = new Numbers("Axe Slot","Axe Slot","Axe Slot", 3d, 9d, 1d, 1d);
    public final Numbers slotShovel = new Numbers("Shovel Slot","Shovel Slot","Shovel Slot", 4d, 9d, 1d, 1d);
    public final Numbers slotBow = new Numbers("Bow Slot","Bow Slot","Bow Slot", 5d, 9d, 1d, 1d);
    public final Numbers slotBlock = new Numbers("Block Slot", "Block Slot","Block Slot",6d, 9d, 1d, 1d);

    public final Option<Boolean> inventoryPackets = new Option<>("Packets", "Packets", "Packets", false);
    public final Option<Boolean> onlyWhileNotMoving = new Option<>("NoMoving","NoMoving","NoMoving", true);
    public final Option<Boolean> inventoryOnly = new Option<>("OpenInv","OpenInv","OpenInv", true);
    public final Option<Boolean> dropArchery = new Option<>("Drop archery","Drop archery", "Drop archery",  false);
    public final Option<Boolean> moveArrows = new Option<>("Move arrows","Move arrows","Move arrows", true);
    public final Option<Boolean> dropFood = new Option<>("Drop food","Drop food","Drop food", false);

    public final String[] shitItems = {"tnt", "stick", "egg", "string", "cake", "mushroom", "flint", "compass", "dyePowder", "feather", "bucket", "chest", "snow", "fish", "enchant", "exp", "shears", "anvil", "torch", "seeds", "leather", "reeds", "skull", "record", "snowball", "piston"};
    public final String[] serverItems = {"selector", "tracking compass", "(right click)", "tienda ", "perfil", "salir", "shop", "collectibles", "game", "profil", "lobby", "show all", "hub", "friends only", "cofre", "(click", "teleport", "play", "exit", "hide all", "jeux", "gadget", " (activ", "emote", "amis", "bountique", "choisir", "choose "};

    public final TimerUtil timer = new TimerUtil();
    public boolean isInvOpen;

    public InvCleaner() {
        super("InvCleaner", "背包清理", Keyboard.KEY_NONE, ModuleType.Player, " Auto Management inv",ModuleType.SubCategory.PLayer_Player);
    }

    public static float getDamageScore(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return 0;

        float damage = 0;
        Item item = stack.getItem();

        if (item instanceof ItemSword) {
            damage += ((ItemSword) item).getDamageVsEntity();
        } else if (item instanceof ItemTool) {
            damage += item.getMaxDamage();
        }

        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F +
                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.1F;

        return damage;
    }

    public static float getProtScore(ItemStack stack) {
        float prot = 0;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();
            prot += armor.damageReduceAmount
                    + ((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 25.F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack) / 100F;
        }
        return prot;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onTick(TickEvent.PlayerTickEvent e) {
        if (stop()) return;
        if (!mc.thePlayer.isUsingItem() && (mc.currentScreen == null || mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiIngameMenu)) {
            long delay = this.delay.getValue().longValue();
            if (timer.hasTimeElapsed(delay)) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(getDesiredSlot(ItemType.WEAPON));
                if (!slot.getHasStack() || !isBestWeapon(slot.getStack())) {
                    getBestWeapon();
                }
            }
            if (timer.hasTimeElapsed(delay)) getBestPickaxe();
            if (timer.hasTimeElapsed(delay)) getBestAxe();
            if (timer.hasTimeElapsed(delay)) getBestShovel();
            if (timer.hasTimeElapsed(delay)) {
                for (int i = 9; i < 45; i++) {
                    if (stop()) return;
                    Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                    ItemStack is = slot.getStack();
                    if (is != null && shouldDrop(is, i)) {
                        InventoryUtils.drop(i);
                        timer.reset();
                        break;
                    }
                }
            }
            if (timer.hasTimeElapsed(delay)) swapBlocks();
            if (timer.hasTimeElapsed(delay)) getBestBow();
            if (timer.hasTimeElapsed(delay)) moveArrows();
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPacket(EventPacketSend e) {
        if (isInvOpen) {
            Packet<?> packet = e.getPacket();
            if ((packet instanceof C16PacketClientStatus && ((C16PacketClientStatus) packet).getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT)
                    || packet instanceof C0DPacketCloseWindow) {
                e.setCancelled(true);
            } else if (packet instanceof C02PacketUseEntity) {
                fakeClose();
            }
        }
    }

    public int getDesiredSlot(ItemType tool) {
        int slot = 36;
        switch (tool) {
            case WEAPON:
                slot = (slotWeapon.getValue().intValue());
                break;
            case PICKAXE:
                slot = (slotPick.getValue().intValue());
                break;
            case AXE:
                slot = (slotAxe.getValue().intValue());
                break;
            case SHOVEL:
                slot = (slotShovel.getValue().intValue());
                break;
            case BLOCK:
                slot = (slotBlock.getValue().intValue());
                break;
            case BOW:
                slot = (slotBow.getValue().intValue());
                break;
        }
        return slot + 35;
    }

    public boolean isBestWeapon(ItemStack is) {
        float damage = getDamageScore(is);
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is2 = slot.getStack();
                if (getDamageScore(is2) > damage && is2.getItem() instanceof ItemSword) {
                    return false;
                }
            }
        }
        return is.getItem() instanceof ItemSword;
    }

    public void getBestWeapon() {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestWeapon(is) && getDamageScore(is) > 0 && is.getItem() instanceof ItemSword) {
                    swap(i, getDesiredSlot(ItemType.WEAPON) - 36);
                    break;
                }
            }
        }
    }

    public boolean shouldDrop(ItemStack stack, int slot) {
        String stackName = stack.getDisplayName().toLowerCase();
        Item item = stack.getItem();
        String ulName = item.getUnlocalizedName();
        if (Arrays.stream(serverItems).anyMatch(stackName::contains)) return false;

        if (item instanceof ItemBlock) {
            return !BlockUtils.isValidBlock(((ItemBlock) item).getBlock(), true);
        }

        int weaponSlot = getDesiredSlot(ItemType.WEAPON);
        int pickaxeSlot = getDesiredSlot(ItemType.PICKAXE);
        int axeSlot = getDesiredSlot(ItemType.AXE);
        int shovelSlot = getDesiredSlot(ItemType.SHOVEL);

        if ((slot != weaponSlot || !isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack()))
                && (slot != pickaxeSlot || !isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack()))
                && (slot != axeSlot || !isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack()))
                && (slot != shovelSlot || !isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack()))) {
            if (item instanceof ItemArmor) {
                for (int type = 1; type < 5; type++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                        ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                        if (isBestArmor(is, type)) {
                            continue;
                        }
                    }
                    if (isBestArmor(stack, type)) {
                        return false;
                    }
                }
            }

            if ((item == Items.wheat) || item == Items.spawn_egg
                    || (item instanceof ItemFood && dropFood.getValue() && !(item instanceof ItemAppleGold))
                    || (item instanceof ItemPotion && isShitPotion(stack))) {
                return true;
            } else if (!(item instanceof ItemSword) && !(item instanceof ItemTool) && !(item instanceof ItemHoe) && !(item instanceof ItemArmor)) {
                if (dropArchery.getValue() && (item instanceof ItemBow || item == Items.arrow)) {
                    return true;
                } else {
                    return item instanceof ItemGlassBottle || Arrays.stream(shitItems).anyMatch(ulName::contains);
                }
            }
            return true;
        }

        return false;
    }

    public void getBestPickaxe() {
        for (int i = 9; i < 45; ++i) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                if (isBestPickaxe(is) && !isBestWeapon(is)) {
                    int desiredSlot = getDesiredSlot(ItemType.PICKAXE);
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !isBestPickaxe(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                    }
                }
            }
        }
    }

    public void getBestAxe() {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                if (isBestAxe(is) && !isBestWeapon(is)) {
                    int desiredSlot = getDesiredSlot(ItemType.AXE);
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !isBestAxe(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                        timer.reset();
                    }
                }
            }
        }
    }

    public void getBestShovel() {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                if (isBestShovel(is) && !isBestWeapon(is)) {
                    int desiredSlot = getDesiredSlot(ItemType.SHOVEL);
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !isBestShovel(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                        timer.reset();
                    }
                }
            }
        }
    }

    public void getBestBow() {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                String stackName = is.getDisplayName().toLowerCase();
                if (Arrays.stream(serverItems).anyMatch(stackName::contains) || !(is.getItem() instanceof ItemBow))
                    continue;
                if (isBestBow(is) && !isBestWeapon(is)) {
                    int desiredSlot = getDesiredSlot(ItemType.BOW);
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !isBestBow(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                    }
                }
            }
        }
    }

    public void moveArrows() {
        if (dropArchery.getValue() || !moveArrows.getValue()) return;
        for (int i = 36; i < 45; i++) {
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (is != null && is.getItem() == Items.arrow) {
                for (int j = 0; j < 36; j++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(j).getStack() == null) {
                        fakeOpen();
                        InventoryUtils.click(i, 0, true);
                        fakeClose();
                        timer.reset();
                        break;
                    }
                }
            }
        }
    }

    public int getMostBlocks() {
        int stack = 0;
        int biggestSlot = -1;
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            ItemStack is = slot.getStack();
            if (is != null && is.getItem() instanceof ItemBlock && is.stackSize > stack && Arrays.stream(serverItems).noneMatch(is.getDisplayName().toLowerCase()::contains)) {
                stack = is.stackSize;
                biggestSlot = i;
            }
        }
        return biggestSlot;
    }

    public void swapBlocks() {
        int mostBlocksSlot = getMostBlocks();
        int desiredSlot = getDesiredSlot(ItemType.BLOCK);
        if (mostBlocksSlot != -1 && mostBlocksSlot != desiredSlot) {
            // only switch if the hotbar slot doesn't already have blocks of the same quantity to prevent an inf loop
            Slot dss = mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
            ItemStack dsis = dss.getStack();
            if (!(dsis != null && dsis.getItem() instanceof ItemBlock && dsis.stackSize >= mc.thePlayer.inventoryContainer.getSlot(mostBlocksSlot).getStack().stackSize && Arrays.stream(serverItems).noneMatch(dsis.getDisplayName().toLowerCase()::contains))) {
                swap(mostBlocksSlot, desiredSlot - 36);
            }
        }
    }

    public boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        } else {
            float value = getToolScore(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (is.getItem() instanceof ItemPickaxe && getToolScore(is) > value) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public boolean isBestShovel(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSpade)) {
            return false;
        } else {
            float score = getToolScore(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (is.getItem() instanceof ItemSpade && getToolScore(is) > score) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public boolean isBestAxe(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemAxe)) {
            return false;
        } else {
            float value = getToolScore(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (getToolScore(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public boolean isBestBow(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBow)) {
            return false;
        } else {
            float value = getPowerLevel(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (getPowerLevel(is) > value && is.getItem() instanceof ItemBow && !isBestWeapon(stack)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public float getPowerLevel(ItemStack stack) {
        float score = 0;
        Item item = stack.getItem();
        if (item instanceof ItemBow) {
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) * 0.5F;
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) * 0.1F;
        }
        return score;
    }

    public float getToolScore(ItemStack stack) {
        float score = 0;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool) item;
            String name = item.getUnlocalizedName().toLowerCase();
            if (item instanceof ItemPickaxe) {
                score = tool.getStrVsBlock(stack, Blocks.stone) - (name.contains("gold") ? 5 : 0);
            } else if (item instanceof ItemSpade) {
                score = tool.getStrVsBlock(stack, Blocks.dirt) - (name.contains("gold") ? 5 : 0);
            } else {
                if (!(item instanceof ItemAxe)) return 1;
                score = tool.getStrVsBlock(stack, Blocks.log) - (name.contains("gold") ? 5 : 0);
            }
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075F;
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100F;
        }
        return score;
    }

    public boolean isShitPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion pot = (ItemPotion) stack.getItem();
            if (pot.getEffects(stack) == null) return true;
            for (PotionEffect effect : pot.getEffects(stack)) {
                if (effect.getPotionID() == Potion.moveSlowdown.getId()
                        || effect.getPotionID() == Potion.weakness.getId()
                        || effect.getPotionID() == Potion.poison.getId()
                        || effect.getPotionID() == Potion.harm.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBestArmor(ItemStack stack, int type) {
        String typeStr = "";
        switch (type) {
            case 1:
                typeStr = "helmet";
                break;
            case 2:
                typeStr = "chestplate";
                break;
            case 3:
                typeStr = "leggings";
                break;
            case 4:
                typeStr = "boots";
                break;
        }
        if (stack.getUnlocalizedName().contains(typeStr)) {
            float prot = getProtScore(stack);
            for (int i = 5; i < 45; i++) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (is.getUnlocalizedName().contains(typeStr) && getProtScore(is) > prot) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void fakeOpen() {
        if (!isInvOpen) {
            timer.reset();
            if (!inventoryOnly.getValue() && inventoryPackets.getValue())
                PacketUtil.sendPacketWithoutEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            isInvOpen = true;
        }
    }

    public void fakeClose() {
        if (isInvOpen) {
            if (!inventoryOnly.getValue() && inventoryPackets.getValue())
                PacketUtil.sendPacketWithoutEvent(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
            isInvOpen = false;
        }
    }

    public void swap(int slot, int hSlot) {
        fakeOpen();
        InventoryUtils.swap(slot, hSlot);
        fakeClose();
        timer.reset();
    }

    public boolean stop() {
        return (inventoryOnly.getValue() && !(mc.currentScreen instanceof GuiInventory)) || (onlyWhileNotMoving.getValue() && MovementUtils.isMoving());
    }

    public enum ItemType {
        WEAPON, PICKAXE, AXE, SHOVEL, BLOCK, BOW
    }

}
