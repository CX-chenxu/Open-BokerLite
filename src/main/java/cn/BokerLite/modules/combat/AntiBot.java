package cn.BokerLite.modules.combat;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Mode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class AntiBot extends Module {
    private static final Mode<Enum<AntiBotMode>> mode = new Mode<>("Mode", "mode", AntiBotMode.values(), AntiBotMode.Hypixel);
    private static final ArrayList<Integer> ground = new ArrayList<Integer>();

    public AntiBot() {
        super("AntiBot","机器人检测", Keyboard.KEY_NONE, ModuleType.Combat, "Make target exclude the bots",ModuleType.SubCategory.COMBAT_RAGE);
    }
    @Override
    public void enable() {
        super.enable();
        AntiBot.ground.clear();
    }

    @Override
    public void disable() {
        super.disable();
        AntiBot.ground.clear();
    }

    @SubscribeEvent
    public void onUpdate(final TickEvent.PlayerTickEvent e) {

        if (mc.thePlayer.ticksExisted <= 1) {
            AntiBot.ground.clear();
        }
        for (final EntityPlayer entity : mc.theWorld.playerEntities) {
            this.removeHypixelBot(entity);
        }
        ground.addAll(this.getLivingPlayers().stream().filter(entityPlayer -> entityPlayer.onGround && !ground.contains(entityPlayer.getEntityId())).map(Entity::getEntityId).collect(Collectors.toList()));
    }
    private ArrayList<EntityPlayer> getLivingPlayers() {
        return (ArrayList<EntityPlayer>) Arrays.asList(mc.theWorld.loadedEntityList.stream().filter(entity -> entity instanceof EntityPlayer).filter(entity -> entity != mc.thePlayer).map(entity -> entity).toArray(EntityPlayer[]::new));
    }

    private boolean removeHypixelBot(final EntityLivingBase entity) {
        if (entity instanceof EntityWither && entity.isInvisible()) {
            return true;
        }
        if (!this.inTab(entity) && !isHypixelNPC(entity) && entity.isEntityAlive() && entity != mc.thePlayer) {
            mc.theWorld.removeEntity(entity);
            return true;
        }
        return false;
    }
    private boolean inTab(final EntityLivingBase entity) {
        for (final Object item : mc.getNetHandler().getPlayerInfoMap()) {
            final NetworkPlayerInfo playerInfo = (NetworkPlayerInfo)item;
            if (playerInfo != null && playerInfo.getGameProfile() != null && playerInfo.getGameProfile().getName().contains(entity.getName())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isHypixelNPC(final EntityLivingBase entity) {
        final String formatted = entity.getDisplayName().getFormattedText();
        return (!formatted.startsWith("§") && formatted.endsWith("§r")) || AntiBot.ground.contains(entity.getEntityId()) || formatted.contains("§8[NPC]");
    }

    private static boolean isHypixelNPC(final Entity entity) {
        final String formatted = entity.getDisplayName().getFormattedText();
        return (!formatted.startsWith("§") && formatted.endsWith("§r")) || AntiBot.ground.contains(entity.getEntityId()) || formatted.contains("§8[NPC]");
    }

    public static double getEntitySpeed(Entity entity) {
        double xDif = entity.posX - entity.prevPosX;
        double zDif = entity.posZ - entity.prevPosZ;
        return (Math.sqrt(xDif * xDif + zDif * zDif) * 20.0);
    }

    public static boolean isServerBot(Entity entity) {
        if (ModuleManager.getModule("AntiBot").getState()) {
            if (AntiBot.mode.getValue() == AntiBotMode.Hypixel) {
                if (entity instanceof EntityMob) {
                    return false;
                }

                if (entity instanceof EntityAnimal) {
                    return false;
                }
                return !entity.getDisplayName().getFormattedText().startsWith("\u00a7") ||
                        entity.isInvisible() ||
                        entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")&& isHypixelNPC(entity);
            } else if (AntiBot.mode.getValue() == AntiBotMode.Mineplex) {
                for (EntityPlayer object : mc.theWorld.playerEntities) {
                    if (object == null || object == mc.thePlayer || !object.getName().startsWith("Body #") && object.getMaxHealth() != 20.0f)
                        continue;
                    return true;
                }
            } else if (AntiBot.mode.getValue() == AntiBotMode.Syuu) {
            	if (entity == mc.thePlayer) return false;
                if (entity instanceof EntityPlayer) {
                    final EntityPlayer entityPlayer = (EntityPlayer) entity;
                    return entityPlayer.isInvisible() && entityPlayer.getHealth() > 1000.0f && getEntitySpeed(entityPlayer) > 20;
                }
            } else if (AntiBot.mode.getValue() == AntiBotMode.Vanilla) {
                if(entity.getName().contains("商店"))
                    return true;
                if(entity.getName().contains("商人"))
                    return true;
                if(entity.getName().contains("升级"))
                    return true;
                if(entity.getName().length() > 13)
                    return true;
                return !entity.getDisplayName().getFormattedText().startsWith("\u00a7") ||
                        entity.isInvisible() ||
                        entity.getDisplayName().getFormattedText().toLowerCase().contains("npc");

            } else if (AntiBot.mode.getValue() == AntiBotMode.PaimonPVP) {
                return entity.getName().contains("prc") || entity.getName().contains("smux");
            } else return AntiBot.mode.getValue() == AntiBotMode.DomcerMM && entity.getName().contains("啊") && entity.getName().contains("我死了");
        }
        return false;
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        this.setSuffix(mode.getValue().name());
    }

    enum AntiBotMode {
        Hypixel,
        DomcerMM,
        Mineplex,
        Syuu,
        PaimonPVP,
        Vanilla
    }
}

