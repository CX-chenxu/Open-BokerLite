package cn.BokerLite.modules.render;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.combat.AntiBot;
import cn.BokerLite.modules.combat.Teams;
import cn.BokerLite.modules.value.Colors;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.friend.FriendManager;
import cn.BokerLite.utils.render.RenderHelper;

import java.awt.*;

public class Tracers extends Module {

    private final Numbers line = new Numbers("Tracersline", "线粗细程度", "Tracersline", 0.5D, 0.1D, 5.0, 0.1D);
    private final Option<Boolean> invisible = new Option<>("Invisible", "隐身实体", "Invisible", false);
    private boolean states;

    public Tracers() {
        super("Tracers", "人物天线", Keyboard.KEY_NONE, ModuleType.Render, "Tracers entitles location",ModuleType.SubCategory.RENDER_OVERLAY);
    }

    @Override
    public void enable() {
        this.states = Tracers.mc.gameSettings.viewBobbing;
        if (this.states) {
            Tracers.mc.gameSettings.viewBobbing = false;
        }
    }

    @SubscribeEvent
    public void update(TickEvent.PlayerTickEvent event) {
        if (Tracers.mc.gameSettings.viewBobbing) {
            Tracers.mc.gameSettings.viewBobbing = false;
        }
    }

    @Override
    public void disable() {
        Tracers.mc.gameSettings.viewBobbing = this.states;
    }


    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent ev) {
        for (final EntityPlayer TargetEntity : Tracers.mc.theWorld.playerEntities) {
            if (TargetEntity != Tracers.mc.thePlayer && !AntiBot.isServerBot(TargetEntity)) {
                if (!this.invisible.getValue() && TargetEntity.isInvisible()) {
                    return;
                }
                int rgb;
                if (Teams.isOnSameTeam(TargetEntity)) {
                    rgb = new Color(255, 255, 255).getRGB();
                } else if (FriendManager.isFriend(TargetEntity.getName())) {
                    rgb = new Color(34, 255, 0, 255).getRGB();
                } else {
                    rgb = (new Color(0,125,227)).getRGB();
                }
                RenderHelper.drawTracers(TargetEntity, rgb, this.line.getValue().floatValue());
            }
        }
    }

}
