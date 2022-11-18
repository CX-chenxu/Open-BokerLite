package cn.BokerLite.modules.move;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import cn.BokerLite.Client;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.clickgui.ClickGui;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.combat.AntiBot;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.math.MathUtils;
import cn.BokerLite.utils.pathfinding.MainPathFinder;
import cn.BokerLite.utils.pathfinding.Vec3;
import cn.BokerLite.utils.reflect.ReflectionUtil;
import cn.BokerLite.utils.render.RenderHelper;
import cn.BokerLite.utils.render.RenderUtil;
import cn.BokerLite.utils.timer.CPSDelay;
import cn.BokerLite.utils.timer.TimerUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TPAura extends Module {
    public EntityLivingBase target = null;

    private final ArrayList<EntityLivingBase> targets = new ArrayList<>();
    private final ArrayList<EntityLivingBase> attackedTargets = new ArrayList<>();
    private final Mode<Enum<Priority>> priority = new Mode<>("Priority", "Priority", Priority.values(), Priority.Range);
    private final Mode<Enum<AuraMode>> mode = new Mode<>("AuraMode", "AuraMode", AuraMode.values(), AuraMode.Switch);
    private final Numbers Switchdelay = new Numbers("Switchdelay", "目标切换间隔", "switchdelay", 11.0, 0.0, 50.0, 1.0);
    private final Numbers maxCps = new Numbers("MaxAPS", "最大CPS", "MaxCPS", 14.0, 1.0, 20.0, 0.1);
    private Numbers minCps = new Numbers("MinAPS", "最小CPS", "MinCPS", 10.0, 1.0, 20.0, 0.1);
    private final Option<Boolean> esp = new Option<>("DrawESP", "ESP", "DrawESP", true);
    private final Option<Boolean> players = new Option<>("Players", "攻击玩家", "Players", true);
    private final Option<Boolean> animals = new Option<>("Animals", "攻击动物", "Animals", true);
    private final Option<Boolean> mobs = new Option<>("Mobs", "攻击生物", "Mobs", true);
    private final Option<Boolean> invis = new Option<>("Invisibles", "攻击隐身", "Invisibles", false);
    public Option<Boolean> guicheck = new Option<>("GuiCheck", "GUI检查", "GuiCheck", true);
    private final TimerUtil timer = new TimerUtil();
    
    private ArrayList<Vec3> paths;

    TimerUtil timerUtil = new TimerUtil();
    boolean allowCrits;
    double yPos;
    boolean direction = true;
    private final CPSDelay cpsDelay = new CPSDelay();
    private int index;
    double[] rotation = new double[2];
    public static float x = 1F;
    public static float y = 1F;

    public TPAura() {
        super("TPAura", "传送光环", Keyboard.KEY_NONE, ModuleType.Combat, "",ModuleType.SubCategory.COMBAT_RAGE);
    }

    public static net.minecraft.util.Timer getTimer() {
        return ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "timer", "field_71428_T");
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if(ClickGui.thw.isHidden())
            return;
        if(mc.currentScreen instanceof ClickGui)
            return;
        if (target != null) {
            int additionalWidth = Math.max(FontRenderer.F16.getStringWidth(target.getName()), 75);
            RenderUtil.drawRoundedRect(x + 0.0F, y + 0.0F, x + 45.0F + (float) additionalWidth, y + 40.0F, 4, new Color(33, 33, 33).getRGB());
            GL11.glColor4f(1F, 1F, 1F, 1F);
            try {
                mc.getTextureManager().bindTexture(TPAura.getskin(target));
            } catch (Exception ep) {
                mc.getTextureManager().bindTexture(TPAura.getskin(mc.thePlayer));
            }
            RenderUtil.drawScaledCustomSizeModalCircle((int) (x + 5), (int) (y + 5), 8f, 8f, 8, 8, 30, 30, 64f, 64f);
            RenderUtil.drawScaledCustomSizeModalCircle((int) (x + 5), (int) (y + 5), 40f, 8f, 8, 8, 30, 30, 64f, 64f);
            ClickGui.drawCenteredStrings(target.getName(), (int) (x + 40 + (additionalWidth / 2f)), (int) (y + 5f), -1);
            ClickGui.drawCenteredStrings("Health: " + (int) target.getHealth(), (int) (x + 40 + (additionalWidth / 2f)), (int) (y + 6f) + FontRenderer.F16.getFontHeight(), -1);
            RenderUtil.drawRoundedRect(x + 40f, y + 28f, x + 40f + additionalWidth, y + 33f, 1, new Color(0, 0, 0).getRGB());
            RenderUtil.drawRoundedRect(x + 40f, y + 28f, x + 40f + (mc.thePlayer.getHealth() / mc.thePlayer.getMaxHealth()) * additionalWidth, y + 33f, 1, Client.THEME_RGB_COLOR);
        }
    }

    public static ResourceLocation getskin(EntityLivingBase entity) {
        ResourceLocation var2;
        try {
            if (entity instanceof EntityPlayer) {
                NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(entity.getUniqueID());
                var2 = playerInfo.getLocationSkin();
            } else {
                var2 = DefaultPlayerSkin.getDefaultSkinLegacy();
            }
        }catch (NullPointerException e){
            var2 = DefaultPlayerSkin.getDefaultSkinLegacy();
            e.printStackTrace();
        }
        return var2;
    }

    public void color(int color) {
        float f = (float) (color >> 24 & 255) / 254.0f;
        float f2 = (float) (color >> 16 & 255) / 255.0f;
        float f3 = (float) (color >> 8 & 255) / 255.0f;
        float f4 = (float) (color & 255) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
    }

    public double random(double min, double max) {
        Random random = new Random();
        return min + (int) (random.nextDouble() * (max - min));
    }

    private boolean shouldAttack() {
        Random random=new Random();
        int delay=minCps.getValue().intValue()==maxCps.getValue().intValue()?maxCps.getValue().intValue():random.nextInt(MathUtils.getRandomInRange(minCps.getValue().intValue(),maxCps.getValue().intValue()));
         return this.timer.hasReached(1000.0 / delay);
    }
    @SubscribeEvent
    public void onPreventCPSError(TickEvent e){
        if (minCps.getValue()>maxCps.getValue()){
            minCps=maxCps;
        }
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent e) {
        if (timerUtil.delay(10)) {
            if (direction) {
                yPos += 0.03;
                if (2 - yPos < 0.02) {
                    direction = false;
                }
            } else {
                yPos -= 0.03;
                if (yPos < 0.02) {
                    direction = true;
                }
            }
            timerUtil.reset();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onUpdate(final TickEvent.PlayerTickEvent event) {
        if (!this.state)
            return;
        if(mc.thePlayer.isDead) toggle();
        if(guicheck.getValue() && mc.currentScreen !=null)
            return;
        if (event.phase == Phase.END || event.side == Side.SERVER)
            return;
        //
        this.setSuffix(this.mode.getValue().toString());
        if (mc.thePlayer.ticksExisted % Switchdelay.getValue() == 0 && targets.size() > 1) {
            ++this.index;
        }
        if (!targets.isEmpty() && this.index >= targets.size()) {
            this.index = 0;
        }
        this.findTargets();
        this.sortList(targets);
        target = targets.isEmpty() ? null : targets.get(0);
//        this.settarget();
        if (target != null) {
            if (Client.nullCheck())
                return;
            try {
                if (target != null && this.shouldAttack()) {
                    this.doAttack();
                    this.newAttack();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            targets.clear();
            this.attackedTargets.clear();
        }
    }

    public void settarget() {
        if (Client.nullCheck())
            return;
        for (Entity object : Objects.requireNonNull(getEntityList())) {
            EntityLivingBase entity;
            if (!(object instanceof EntityLivingBase) || !this.check(entity = (EntityLivingBase) object)) continue;
            target = entity;
        }
    }

    public boolean check(EntityLivingBase entity) {
        if (Client.nullCheck())
            return false;
        if (entity instanceof EntityArmorStand) {
            return false;
        }
        if (entity == mc.thePlayer) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (entity.getHealth() == 0) {
            return false;
        }
        return !AntiBot.isServerBot(entity);
    }

    public Entity[] getEntityList() {
        if (Client.nullCheck())
            return null;
        return mc.theWorld != null ? mc.theWorld.getLoadedEntityList().toArray(new Entity[0]) : null;
    }

    private void doAttack() {
        try {
            if (cpsDelay.shouldAttack(minCps.getValue().intValue() == maxCps.getValue().intValue() ? maxCps.getValue().intValue() : ThreadLocalRandom.current().nextInt(minCps.getValue().intValue(), maxCps.getValue().intValue()))) {
                this.attack();
                this.attackedTargets.add(target);
            }
        } catch (Exception ex) {
            //
        }
    }

    @SubscribeEvent
    public void onPost(TickEvent.PlayerTickEvent event) {
        if (event.phase == Phase.END || event.side == Side.SERVER)
            return;
        //PostEvent
        
    }

    //ESP
    @SubscribeEvent
    public void onRenderWqorldLast(RenderWorldLastEvent event) {
    	if (target == null || target.isDead) return;
        if (esp.getValue()) RenderHelper.drawESP(target, new Color(255, 0, 0).getRGB(), true, 3);
    	if (paths == null) return;
    	GL11.glLineWidth(4F);
    	GL11.glDisable(GL11.GL_TEXTURE_2D);
    	RenderManager renderManager = mc.getRenderManager();
    	double x = (double) ReflectionUtil.getFieldValue(renderManager, "renderPosX", "field_78725_b");
        double y = (double) ReflectionUtil.getFieldValue(renderManager, "renderPosY", "field_78726_c");
        double z = (double) ReflectionUtil.getFieldValue(renderManager, "renderPosZ","field_78728_n");
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_LINE_STRIP);
    	double lastX, lastY, lastZ, dist;
    	lastX = lastY = lastZ = dist = 0;
    	for (Vec3 p : paths) {
    		int color = Color.HSBtoRGB((float) (dist / 5D) % 1F, 1F, 1F);
	        float r = (color >> 16 & 0xFF) / 255.0f,
	        	  g = (color >> 8 & 0xFF) / 255.0f,
	        	  b = (color & 0xFF) / 255.0f;
	        double diffX = p.getX() - lastX,
	        	   diffY = p.getY() - lastY,
	        	   diffZ = p.getZ() - lastZ;
	        dist += Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
	        lastX = p.getX();
	        lastY = p.getY();
	        lastZ = p.getZ();
	        GL11.glColor4f(r, g, b, 1F);
			GL11.glVertex3d(lastX - x, lastY - y, lastZ - z);
    	}
    	GL11.glEnd();
    	GL11.glShadeModel(GL11.GL_FLAT);
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glLineWidth(1F);
    }

    private void attack() {
//    	System.out.println(target);
    	paths = MainPathFinder.computePath(Vec3.from(mc.thePlayer), Vec3.from(target));
    	if(paths == null) return;
    	for(int i=0;i<paths.size()-1;i++) {
    		Vec3 v = paths.get(i);
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(v.getX(), v.getY(), v.getZ(), true));
    	}
    	Vec3 v = paths.get(paths.size()-1);
    	float[] rot = this.getRotationToEntity(target);
    	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(v.getX(), v.getY(), v.getZ(), rot[0], rot[1], true));
    	mc.thePlayer.swingItem();
        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        for(int i=paths.size()-1;i>1;i--) {
    		v = paths.get(i);
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(v.getX(), v.getY(), v.getZ(), true));
    	}
    	v = paths.get(0);
    	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(v.getX(), v.getY(), v.getZ(), mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
    }

    private void newAttack() {
        try {
            if (mc.thePlayer.isBlocking()) {
                for (int i = 0; i <= 2; i++) {
                    if (new Random().nextBoolean())
                        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
                                mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                }
            }
            if (mc.thePlayer.isBlocking()) {
                for (int i = 0; i <= 2; i++) {
                    if (new Random().nextBoolean())
                        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
                                mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                }
            }


            if (mc.thePlayer.isBlocking() && this.timer.delay(100)) {
                for (int i = 0; i <= 2; i++) {
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN
                    ));

                }
            }
        } catch (Exception ex) {
            //
        }
    }

    private void clear() {
        target = null;
        targets.clear();
        for (EntityLivingBase ent : this.targets) {
            if (this.isValidEntity(ent))
                continue;
            targets.remove(ent);
            if (!this.attackedTargets.contains(ent))
                continue;
            this.attackedTargets.remove(ent);
        }
    }

    private void findTargets() {
        try {
            int maxSize = this.mode.getValue() == AuraMode.Switch ? 4 : 1;
            for (Entity o3 : getEntityList()) {
                EntityLivingBase curEnt;
                if (o3 instanceof EntityLivingBase && this.isValidEntity(curEnt = (EntityLivingBase) o3)
                        && !targets.contains(curEnt)) {
                    targets.add(curEnt);
                }
                if (targets.size() >= maxSize)
                    break;
            }
            targets.sort((o1, o2) -> (int) (o1.getDistanceToEntity(o2) - o2.getDistanceToEntity(o1)));
        } catch (Exception e) {
            //
        }
    }

    private boolean isValidEntity(EntityLivingBase ent) {
//        if (Teams.isOnSameTeam(ent))
//            return false;
    	if (ent == null) return false;
    	return ent.getName().contains("_1");
    }

    @Override
    public void enable() {
        index = 0;
    }

    public float[] getRotationToEntity(Entity target) {
        double xDiff = target.posX - mc.thePlayer.posX;
        double yDiff = target.posY - mc.thePlayer.posY;
        double zDiff = target.posZ - mc.thePlayer.posZ;
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) (-Math.atan2(
                target.posY + (double) 0
                        - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight()),
                Math.hypot(xDiff, zDiff)) * 180.0 / Math.PI);
        if (yDiff > -0.2 && yDiff < 0.2) {
            pitch = (float) (-Math.atan2(
                    target.posY + (double) target.getEyeHeight() / HitLocation.CHEST.getOffset()
                            - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight()),
                    Math.hypot(xDiff, zDiff)) * 180.0 / Math.PI);
        } else if (yDiff > -0.2) {
            pitch = (float) (-Math.atan2(
                    target.posY + (double) target.getEyeHeight() / HitLocation.FEET.getOffset()
                            - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight()),
                    Math.hypot(xDiff, zDiff)) * 180.0 / Math.PI);
        } else if (yDiff < 0.3) {
            pitch = (float) (-Math.atan2(
                    target.posY + (double) target.getEyeHeight() / HitLocation.HEAD.getOffset()
                            - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight()),
                    Math.hypot(xDiff, zDiff)) * 180.0 / Math.PI);
        }
        return new float[]{yaw, pitch};
    }

    @Override
    public void disable() {
        targets.clear();
        this.attackedTargets.clear();
        target = null;
        mc.thePlayer.setItemInUse(mc.thePlayer.getItemInUse(), 0);
        allowCrits = true;
        mc.thePlayer.renderYawOffset = mc.thePlayer.rotationYaw;
    }

    public float angle(float angle1, float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 0.0f;
        }
        return angle3;
    }

    private void sortList(ArrayList<EntityLivingBase> weed) {
        if (this.priority.getValue() == Priority.Range) {
            weed.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
        }
        if (this.priority.getValue() == Priority.Fov) {
            weed.sort(Comparator.comparingDouble(o -> angle(mc.thePlayer.rotationPitch, getRotationToEntity(o)[0])));
        }
        if (this.priority.getValue() == Priority.Angle) {
            weed.sort((o1, o2) -> {
                float[] rot1 = getRotationToEntity(o1);
                float[] rot2 = getRotationToEntity(o2);
                return (int) (mc.thePlayer.rotationYaw - rot1[0] - (mc.thePlayer.rotationYaw - rot2[0]));
            });
        }
        if (this.priority.getValue() == Priority.Health) {
            weed.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
        }
    }

    enum Priority {
        Range, Fov, Angle, Health
    }

    enum AuraMode {
        Switch, Single
    }

    enum HitLocation {
        AUTO(0.0), HEAD(1.0), CHEST(1.5), FEET(3.5);

        private final double offset;

        HitLocation(double offset) {
            this.offset = offset;
        }

        public double getOffset() {
            return this.offset;
        }
    }

}