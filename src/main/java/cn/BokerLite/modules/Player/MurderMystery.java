package cn.BokerLite.modules.Player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.Client;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.combat.AntiBot;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.misc.KillerData;
import cn.BokerLite.utils.mod.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MurderMystery extends Module {

    public static boolean a1;
    public static int a2;
    private static List<EntityPlayer> m;
    private static List<EntityPlayer> bw;

    private final Mode<Enum<mdetectmode>> mode = new Mode<>("Mode", "Mode", mdetectmode.values(), mdetectmode.advanced);
    private final Option<Boolean> Murder = new Option<>("Tell Everyone Murder", "告诉所有人杀手", "Tell Everyone Murder", false);
    private final Option<Boolean> Bow = new Option<>("Tell Everyone Bow", "告诉所有人侦探", "Tell Everyone Bow", false);

    public MurderMystery() {
        super("MurderMystery", "密室杀手", Keyboard.KEY_NONE, ModuleType.Player, "Detection Murders in Murder game",ModuleType.SubCategory.PLayer_Player);
    }


    @Override
    public void disable() {
        if (mode.getValue() == mdetectmode.advanced) {
            return;
        }
        this.c();
        MurderMystery.a1 = false;
        MurderMystery.a2 = 0;
    }

    @SubscribeEvent
    public void o(final RenderWorldLastEvent ev) {
        if (mode.getValue()==mdetectmode.advanced){
            return;
        }
        if (!Client.nullCheck()) {
            for (final EntityPlayer en : MurderMystery.mc.theWorld.playerEntities) {
                if (en != MurderMystery.mc.thePlayer && !en.isInvisible() && !AntiBot.isServerBot(en)) {
                    if (en.getHeldItem() != null && en.getHeldItem().hasDisplayName()) {
                        final Item i = en.getHeldItem().getItem();
                        if (i instanceof ItemSword || i instanceof ItemAxe || en.getHeldItem().getDisplayName().replaceAll("§", "").equals("aKnife")) {
                            if (!MurderMystery.m.contains(en)) {
                                MurderMystery.m.add(en);
                                MurderMystery.mc.thePlayer.playSound("note.pling", 1.0f, 1.0f);
                                Helper.sendMessage(en.getName() + " is the murderer!");
                                if (this.Murder.getValue()) {
                                    mc.thePlayer.sendChatMessage(en.getName() + " is the murderer!");
                                }
                            }
                        } else if (i instanceof ItemBow && !MurderMystery.bw.contains(en)) {
                            MurderMystery.bw.add(en);
                            Helper.sendMessage("[WARNING]" + en.getName() + " have bow! he maybe will kill you.");
                            if (this.Bow.getValue()) {
                                mc.thePlayer.sendChatMessage(en.getName() + " have bow.");
                            }
                        }
                    }
                }
            }
        }
        else {
            this.c();
        }
    }
    @SubscribeEvent
    public void onUpdateMdetect(TickEvent e) {
        if (mode.getValue() == mdetectmode.advanced) {
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                    if (entityLivingBase instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) entityLivingBase;
                        if (player.inventory.getCurrentItem() != null) {
                            // MurderDetector murderDetector=new MurderDetector();
                            if (killerData.get(player) == null) {
                                if (isWeapon(player.inventory.getCurrentItem().getItem())) {
                                    Helper.sendMessageWithoutPrefix("§a[MurderMystery]§c" + player.getName() + " is Killer!!!");
                                    //call
                                    if (Murder.getValue()){
                                        mc.thePlayer.sendChatMessage(player.getName() + " is the murderer!");
                                    }
                                    if (killerData.get(player) == null) killerData.put(player, new KillerData(player));
                                }
                            }else if (player.inventory.getCurrentItem().getItem() instanceof ItemBow&& !MurderMystery.bw.contains(player)){
                                MurderMystery.bw.add(player);
                                Helper.sendMessage("[WARNING]" + player.getName() + " have bow! he maybe will kill you.");
                                if (this.Bow.getValue()) {
                                    mc.thePlayer.sendChatMessage(player.getName() + " have bow.");
                                }
                            } else {
                                if (!isWeapon(player.inventory.getCurrentItem().getItem())) {
                                    killerData.remove(player);
                                }
                            }

                        }
                    }
                }
            }
        }
    }
    private void c() {
        if (mode.getValue()==mdetectmode.advanced){
            return;
        }
        if (MurderMystery.m.size() > 0) {
            MurderMystery.m.clear();
        }
        if (MurderMystery.bw.size() > 0) {
            MurderMystery.bw.clear();
        }
    }

    static {

        MurderMystery.a1 = false;
        MurderMystery.a2 = 0;
        MurderMystery.m = new ArrayList<>();
        MurderMystery.bw = new ArrayList<>();
    }

    enum mdetectmode{
        old,
        advanced
    }
    public static Item[] itemTypes=new Item[] {
            Items.iron_sword,
            ItemBlock.getItemFromBlock(Blocks.ender_chest),
            Items.stone_sword,
            Items.iron_shovel,
            Items.stick,
            Items.wooden_axe,
            Items.wooden_sword,
            ItemBlock.getItemFromBlock(Blocks.deadbush),
            Items.stone_shovel,
            Items.blaze_rod,
            Items.diamond_shovel,
            Items.feather,
            Items.pumpkin_pie,
            Items.golden_pickaxe,
            Items.golden_apple,
            Items.name_tag,
            Items.carrot_on_a_stick,
            ItemBlock.getItemFromBlock(Blocks.sponge),
            Items.bone,
            Items.carrot,
            Items.golden_carrot,
            Items.cookie,
            Items.diamond_axe,
            ItemBlock.getItemFromBlock(Blocks.double_plant),
            Items.prismarine_shard,
            Items.cooked_beef,
            Items.golden_sword,
            Items.diamond_sword,
            Items.diamond_hoe,
            Items.shears,
            Items.cooked_fish,
            ItemBlock.getItemFromBlock(Blocks.redstone_torch),
            Items.boat,
            Items.speckled_melon
    };
    public static HashMap<EntityPlayer, KillerData> killerData = new HashMap<>();
    public boolean isWeapon(Item item){
        for(Item id:itemTypes){
            if(item==id){
                return true;
            }
        }
        return false;
    }
}
