package cn.BokerLite.modules.combat;


import cn.BokerLite.Client;
import cn.BokerLite.api.EventHandler;
import cn.BokerLite.api.event.EventPacketSend;
import cn.BokerLite.gui.EV0.entity.DefaultPlayerSkin;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.Player.Scaffold;
import cn.BokerLite.modules.render.HUD;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.utils.DelayTimer;
import cn.BokerLite.utils.RotationUtils;
import cn.BokerLite.utils.friend.FriendManager;
import cn.BokerLite.utils.math.MathUtil;
import cn.BokerLite.utils.math.MathUtils;
import cn.BokerLite.utils.render.ColorUtils;
import cn.BokerLite.utils.render.RenderUtil;
import cn.BokerLite.utils.rotation.RotationUtil;
import cn.BokerLite.utils.timer.TimeHelper;
import cn.BokerLite.utils.timer.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class LegitAura extends Module {
    public static final Option<Boolean> autoBlock = new Option<>("AutoBlock","AutoBlock","AutoBlock", true);
    public static float[] facing;
    public static EntityLivingBase target;
    public static EntityLivingBase vip;
    TimeHelper switchTimer = new TimeHelper();
    public static boolean blocking;
    public static boolean attack;
    public final Numbers switchDelayValue= new Numbers("SwitchDelay", "SwitchDelay","SwitchDelay", 15.0d, 0.0d, 20.0d, 0.5d);
    public static float x = 1F;
    private int index;
    private static EntityLivingBase blockTarget;
    public static float y = 1F;
    public float yPos = 0;
    public boolean direction;
    float anim = 100;
    private final Mode<AuraMode> mode = new Mode("Mode","Mode", AuraMode.values(), AuraMode.Switch);
    private final Mode<priority> Priority = new Mode("Priority", "Priority", priority.values(), priority.Health);
    public final Numbers MAXT = new Numbers("MaxTarget", "MaxTarget","MaxTarget",Double.valueOf(1.0), Double.valueOf(1.0), Double.valueOf(50.0), Double.valueOf(1.0));

    public static Numbers fov = new Numbers("FOV", "FOV", "FOV", 90.0, 15.0, 360.0, 1.0);
    public final Numbers max = new Numbers("Max CPS","Max CPS", "Max CPS",  10.0, 1.0, 20.0, 0.5);
    public final Numbers min = new Numbers("Min CPS","Min CPS", "Min CPS",  10.0, 1.0, 20.0, 0.5);
    public static final Numbers reach = new Numbers("Reach","Reach","Reach", 4.5, 1.0, 6.0, 0.1);
    public final Numbers crack = new Numbers("CrackSize", "CrackSize","CrackSize",1.0D, 0.0D, 5.0D, 1.0D);
    public final Option<Boolean> hurtTime = new Option<>("HurtTime","HurtTime","HurtTime", false);
    public final Option<Boolean> esp = new Option<>("DrawESP", "DrawESP","DrawESP",true);
    public static final Option<Boolean> players = new Option<>("Players", "Players","Players",true);
    public static final Option<Boolean> animals = new Option<>("Animals", "Animals","Animals",true);
    public static final Option<Boolean> mobs = new Option<>("Mobs","Mobs","Mobs", true);

    public static final Option<Boolean> invis = new Option<>("Invisibles", "Invisibles", "Invisibles", false);
    public List<EntityLivingBase> targets = new ArrayList<>();
    public Option<Boolean> twall = new Option("Through Wall", "Through Wall","Through Wall",false);
    public static final Option<Boolean> neutralValue = new Option("Neutral","Neutral", "Neutral", false);
    public static final Option<Boolean> multi = new Option("Multi", "Multi", "Multi", false);
    public TimerUtil timer1 = new TimerUtil();
    public TimerUtil timer2 = new TimerUtil();
    public TimerUtil timer3 = new TimerUtil();
    public DelayTimer timer = new DelayTimer();

    double[] rotation1 = new double[2];

    public LegitAura() {
        super("LegitAura", "安全杀戮", Keyboard.KEY_NONE, ModuleType.Combat, "Auto Attack entity near you",ModuleType.SubCategory.COMBAT_RAGE);
    }
    public double[] updateHead(TickEvent.PlayerTickEvent event ,Entity playertarget) {
        rotation1 = RotationUtil.getRotationToEntity(playertarget);
        return rotation1;
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

    @SubscribeEvent
    public void onRenderTargetHUD(RenderGameOverlayEvent.Text e) {
        ScaledResolution sr = new ScaledResolution(mc);
        float scaledWidth = sr.getScaledWidth();
        float scaledHeight = sr.getScaledHeight();
        FontRenderer fr = FontRenderer.F14;

        if (target != null && this.getState()) {
            if (target instanceof EntityLivingBase) {
                if (anim > 0) {
                    anim -= 5;
                }
                float xOffset = Math.max(fr.getStringWidth(target.getName()) + 35.0f, 100);

                float x = scaledWidth / 2.0f + 20.0f;
                float y = scaledHeight / 2.0f + 20.0f + anim;
                float health = target.getHealth();
                double hpPercentage = health / target.getMaxHealth();
                hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                double hpWidth = (xOffset - 35 - 8) * hpPercentage;
                int healthColor = ColorUtils.getHealthColor(target.getHealth(), target.getMaxHealth()).getRGB();
                String healthStr = String.valueOf((float) ((int) target.getHealth()));
                //      RenderUtil.drawRect(x, y, x + xOffset, y + 30.0f, new Color(64,68,75,255).getRGB());
                RenderUtil.drawRect(x + 4, y + 2, x + xOffset - 1, y + 30.0f - 1, new Color(30, 30, 30, 155).getRGB());
                RenderUtil.drawRect((float) (x + 32 - 0.2), (float) (y + 15 - 0.2), (float) (x + 32 + hpWidth + 0.2), (float) (y + 16.0f + 0.2), new Color(64, 68, 75, 255).getRGB());
                RenderUtil.drawGradientSideways(x + 32, y + 15, x + 32 + hpWidth, y + 17.0f, new Color(84, 232, 64, 255).getRGB(), new Color(84, 232, 64, 255).getRGB());
                fr.drawStringWithShadow("Health : " + healthStr, (int) x + (int) 32.0f, (int) y + (int) 21.0f, -1);
                fr.drawStringWithShadow(target.getName(), (int) (x + 32.0f), (int) (y + 7.0f), -1);


                mc.getTextureManager().bindTexture(LegitAura.getskin(target)
                );

                Gui.drawScaledCustomSizeModalRect((int) (x + 4), (int) (y + 3), 8, 8, 8, 8, 26, 26, 64, 64);
            }
        } else {
            anim = 100;
            target = null;
        }
    }
        public static boolean isFovInRange ( final Entity entity, float fov){
            fov *= 0.5;
            final double v = ((mc.thePlayer.rotationYaw - getRotion(entity)) % 360.0 + 540.0) % 360.0 - 180.0;
            return (v > 0.0 && v < fov) || (-fov < v && v < 0.0);
        }


        public static float getRotion ( final Entity ent){
            final double x = ent.posX - mc.thePlayer.posX;
            final double z = ent.posZ - mc.thePlayer.posZ;
            double yaw = Math.atan2(x, z) * 57.2957795;
            yaw = -yaw;
            return (float) yaw;
        }
    public boolean ShouldAttack() {
        int n = ((Number)this.min.getValue()).intValue();
        int n2 = ((Number)this.max.getValue()).intValue();
        int n3 = MathUtil.getRandom(n, n2);

        return this.timer.isDelayComplete((long)(1000 / n3));
    }
        @SubscribeEvent
        @SuppressWarnings("unused")
        public void onPre (TickEvent.PlayerTickEvent e){

            setSuffix("Switch");
            if (ModuleManager.getModule(Scaffold.class).getState() || mc.thePlayer.isDead || mc.thePlayer.isSpectator())
                return;
            if (targets.isEmpty())
                return;

            facing = getRotationsToEnt(target);
            facing[0] += MathUtils.getRandomInRange(1, 5);
            facing[1] += MathUtils.getRandomInRange(1, 5);
            facing[0] = facing[0] + MathUtils.getRandomFloat(1.98f, -1.98f);
            updateHead(e,target);
            mc.thePlayer.setRotationYawHead(facing[0]);



            mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = facing[0];
            attack = true;
            if (this.ShouldAttack()) {
                if (hurtTime.getValue() && target.hurtTime > 0)
                    return;
                if (target.getHealth() > 0) {
                    if (multi.getValue().booleanValue()) {
                        int n = 0;
                        Iterator<EntityLivingBase> iterator = this.targets.iterator();
                        while (iterator.hasNext() && n < ((Number)this.MAXT.getValue()).intValue()) {
                            Entity entity = (Entity)iterator.next();
                            if (!this.isValid((EntityLivingBase)entity)) continue;
                            if(timer1.delay(100)) {
                                mc.thePlayer.swingItem();
                                mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                                timer1.reset();
                            }
                            ++n;
                        }
                    }else {

                            if(timer1.delay(100)) {
                                System.out.println("OK");


                                mc.thePlayer.swingItem();
                                mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                                timer1.reset();
                            }
                        }
                    }
               
            }
        }


        @SubscribeEvent
        @SuppressWarnings("unused")
        public void onPost (TickEvent.PlayerTickEvent e){


        sortTargets();


            if (ModuleManager.getModule(Scaffold.class).getState() || mc.thePlayer.isDead || mc.thePlayer.isSpectator())
                return;
            int crackSize = this.crack.getValue().intValue();
            if (!targets.isEmpty()) {
                if (autoBlock.getValue() && mc.thePlayer.getItemInUse() == null) {
                    if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                        if (target != null) {
                            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                            if (mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem())) {
                                mc.getItemRenderer().resetEquippedProgress2();
                            }
                            blocking = true;
                        } else {
                            blocking = false;
                            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                            mc.playerController.onStoppedUsingItem(mc.thePlayer);
                        }
                    }
                }
                int i2 = 0;
                while (i2 < crackSize && target != null) {
                    mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT);
                    i2++;
                }
            }
            if (targets.isEmpty()) {
                if (blocking) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                }
                attack = false;
                blocking = false;
                target = null;
            }
        }

        @Override
        public void disable () {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            mc.playerController.onStoppedUsingItem(mc.thePlayer);
            targets.clear();

            target = null;
            blocking = false;
            attack = false;
            blockTarget = null;
        }

    public void enable() {
        super.enable();
        this.targets.clear();
        target = null;
        blockTarget = null;

        this.index = 0;
        this.switchTimer.reset();

    }


    public void sortTargets() {
        if (timer2.delay(100)) {
            //   if ( this.mc.thePlayer.getHeldItem().getItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            target = null;
            this.targets.clear();
            final int intValue = ((Number) this.MAXT.getValue()).intValue();
            for (final Entity entity2 : mc.theWorld.getLoadedEntityList()) {
                if (entity2 instanceof EntityLivingBase) {
                    final EntityLivingBase entityLivingBase = (EntityLivingBase) entity2;
                    if (!this.getEntityValid(entityLivingBase)) {
                        continue;
                    }
                    if (vip == entityLivingBase) {
                        this.targets.clear();
                        this.targets.add(entityLivingBase);
                        break;
                    }
                    this.targets.add(entityLivingBase);
                    if (!(boolean) multi.getValue() && this.targets.size() >= intValue) {
                        break;
                    }
                    if (this.Priority.getValue() == priority.Armor) {
                        this.targets.sort(Comparator.comparingInt(this::lambda$onUpdate$1));
                    }
                    if (this.Priority.getValue() == priority.Range) {
                        this.targets.sort(this::lambda$onUpdate$2);
                    }
                    if (this.Priority.getValue() == priority.Fov) {
                        this.targets.sort(Comparator.comparingDouble((ToDoubleFunction<? super EntityLivingBase>) this::lambda$onUpdate$3));
                    }
                    if (this.Priority.getValue() == priority.Angle) {
                        this.targets.sort(this::lambda$onUpdate$4);
                    }
                    if (this.Priority.getValue() != priority.Health) {
                        continue;
                    }
                    this.targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                }
            }

            if (this.switchTimer.isDelayComplete(((Number) this.switchDelayValue.getValue()).intValue() * 100L) && this.targets.size() > 1) {
                this.switchTimer.reset();
                ++this.index;
            }
            if (this.index >= this.targets.size()) {
                this.index = 0;
            }
            if (!this.targets.isEmpty()) {
                target = this.targets.get((this.mode.getValue() == AuraMode.Switch) ? this.index : 0);
            }
            timer2.reset();
        }
        
        }
    public boolean getEntityValid(EntityLivingBase var1) {

        AntiBot var2 = (AntiBot)ModuleManager.getModule(AntiBot.class);

        Teams var3 = (Teams)ModuleManager.getModule(Teams.class);
        if (mc.thePlayer.isEntityAlive() && !mc.thePlayer.isPlayerSleeping() && !mc.thePlayer.isDead && mc.thePlayer.getHealth() > 0.0F && !Teams.isOnSameTeam(var1) && (double) mc.thePlayer.getDistanceToEntity(var1) < ((Number) reach.getValue()).floatValue() && var1.isEntityAlive() && !var1.isDead && var1.getHealth() > 0.0F && !(var1 instanceof EntityArmorStand) && !AntiBot.isServerBot(var1) &&  var1 != mc.thePlayer) {
            if (var1 instanceof EntityPlayer) {
                EntityPlayer var4 = (EntityPlayer)var1;
                if (FriendManager.isFriend(var4.getName())) {
                    return false;
                }



                if (var4.isPlayerSleeping()) {
                    return false;
                }

                if(!canEntityBeSeen(var4) && !twall.getValue())
                    return false;

                if (var4.isPotionActive(Potion.invisibility) && !(Boolean)invis.getValue()) {
                    return false;
                }
            }

            if (var1 instanceof EntityAnimal) {
                return animals.getValue();
            } else if (var1 instanceof EntityMob) {
                return mobs.getValue();
            } else {
                return !(var1 instanceof EntityVillager) && !(var1 instanceof EntityIronGolem) && !(var1 instanceof EntitySnowman) || neutralValue.getValue();
            }
        } else {
            return false;
        }
    }
    private boolean getEntityValidBlock(EntityLivingBase var1) {
        Client.getModuleManager();
        Teams var2 = (Teams)ModuleManager.getModule(Teams.class);
        Client.getModuleManager();
        AntiBot var3 = (AntiBot)ModuleManager.getModule(AntiBot.class);
        if (mc.thePlayer.isEntityAlive() && !mc.thePlayer.isPlayerSleeping() && !mc.thePlayer.isDead && mc.thePlayer.getHealth() > 0.0F && !Teams.isOnSameTeam(var1) && (double) mc.thePlayer.getDistanceToEntity(var1) < (double)((Number) reach.getValue()).floatValue() && var1.isEntityAlive() && !var1.isDead && var1.getHealth() > 0.0F && !(var1 instanceof EntityArmorStand) && !AntiBot.isServerBot(var1)  && var1 != mc.thePlayer) {
            if (var1 instanceof EntityPlayer) {
                EntityPlayer var4 = (EntityPlayer)var1;
                if (FriendManager.isFriend(var4.getName())) {
                    return false;
                }

                if (!(Boolean)players.getValue()) {
                    return false;
                }

                if (var4.isPlayerSleeping()) {
                    return false;
                }

                if (var4.isPotionActive(Potion.invisibility) && !(Boolean)invis.getValue()) {
                    return false;
                }
            }

            if (var1 instanceof EntityAnimal) {
                return animals.getValue();
            } else if (var1 instanceof EntityMob) {
                return mobs.getValue();
            } else {
                return !(var1 instanceof EntityVillager) && !(var1 instanceof EntityIronGolem) && !(var1 instanceof EntitySnowman) || neutralValue.getValue();
            }
        } else {
            return false;
        }
    }
    private int lambda$onUpdate$1(EntityLivingBase entityLivingBase) {
        return entityLivingBase instanceof EntityPlayer ? ((EntityPlayer)entityLivingBase).inventory.getTotalArmorValue() : (int)entityLivingBase.getHealth();
    }
    private int lambda$onUpdate$2(EntityLivingBase entityLivingBase, EntityLivingBase entityLivingBase2) {
        return (int)(entityLivingBase.getDistanceToEntity(mc.thePlayer) - entityLivingBase2.getDistanceToEntity(mc.thePlayer));
    }
    private double lambda$onUpdate$3(EntityLivingBase entityLivingBase) {
        return RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, RotationUtils.getNeededRotations(entityLivingBase)[0]);
    }
    private int lambda$onUpdate$4(EntityLivingBase entityLivingBase, EntityLivingBase entityLivingBase2) {
        float[] fArray = RotationUtils.getNeededRotations(entityLivingBase);
        float[] fArray2 = RotationUtils.getNeededRotations(entityLivingBase2);
        return (int)(mc.thePlayer.rotationYaw - fArray[0] - (mc.thePlayer.rotationYaw - fArray2[0]));
    }
    @SubscribeEvent
    public void onTick(final RenderWorldLastEvent event) {
        if (target != null && esp.getValue()) {
            drawShadow(target, event.partialTicks, yPos, direction);
            drawCircle(target, event.partialTicks, yPos);
        }
    }
    public void drawShadow(Entity entity, float partialTicks, float pos, boolean direction) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(7425);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks - mc.getRenderManager().viewerPosY + pos;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks - mc.getRenderManager().viewerPosZ;
        GL11.glBegin(GL11.GL_QUAD_STRIP);
        Color c = (new Color(0,125,227));
        for (int i = 0; i <= 12; i++) {
            double c1 = i * Math.PI * 2 / 12;
            double c2 = (i + 1) * Math.PI * 2 / 12;
            GL11.glColor4f(c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f, 0.4f);
            GL11.glVertex3d(x + 0.5 * Math.cos(c1), y, z + 0.5 * Math.sin(c1));
            GL11.glVertex3d(x + 0.5 * Math.cos(c2), y, z + 0.5 * Math.sin(c2));
            GL11.glColor4f(c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f, 0f);

            GL11.glVertex3d(x + 0.5 * Math.cos(c1), y + (direction ? -0.3 : 0.3), z + 0.5 * Math.sin(c1));
            GL11.glVertex3d(x + 0.5 * Math.cos(c2), y + (direction ? -0.3 : 0.3), z + 0.5 * Math.sin(c2));


        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(7424);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public void drawCircle(Entity entity, float partialTicks, float pos) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(7425);
        GL11.glLineWidth(2);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks - mc.getRenderManager().viewerPosY + pos;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks - mc.getRenderManager().viewerPosZ;
        GL11.glBegin(GL11.GL_LINE_STRIP);
        Color c = (new Color(HUD.Hudcolor.getValue()));
        for (int i = 0; i <= 12; i++) {
            double c1 = i * Math.PI * 2 / 12;
            GL11.glColor4f(c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f, 1);
            GL11.glVertex3d(x + 0.5 * Math.cos(c1), y, z + 0.5 * Math.sin(c1));


        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(7424);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
    TimerUtil timerUtil = new TimerUtil();
    @SubscribeEvent
    public void onRenderTargetHUD(RenderGameOverlayEvent e){
        if(timerUtil.delay(20)) {
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
    public boolean isValid(EntityLivingBase ent) {
        if (AntiBot.isServerBot(ent))
            return false;
        if (Teams.isOnSameTeam(ent))
            return false;
        if (ent instanceof EntityPlayer && !players.getValue())
            return false;
        if (ent instanceof EntityMob && !mobs.getValue())
            return false;
        if (ent instanceof EntityAnimal && !animals.getValue())
            return false;
        if (ent.isInvisible() && !invis.getValue())
            return false;
        if (!isFovInRange(ent, fov.getValue().floatValue())) {
            return false;
        }
        if(!canEntityBeSeen(ent) && !twall.getValue())
            return false;
        if (ent.getHealth() <= 0)
            return false;
        if (ent.isDead) {
            target = null;
            return false;
        }
        return true;
    }

    public static boolean canEntityBeSeen(Entity e) {
        Vec3 vec1 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        AxisAlignedBB box = e.getEntityBoundingBox();
        Vec3 vec2 = new Vec3(e.posX, e.posY + (e.getEyeHeight() / 1.32F), e.posZ);
        double minx = e.posX - 0.25;
        double maxx = e.posX + 0.25;
        double miny = e.posY;
        double maxy = e.posY + Math.abs(e.posY - box.maxY);
        double minz = e.posZ - 0.25;
        double maxz = e.posZ + 0.25;
        boolean see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see)
            return true;
        vec2 = new Vec3(maxx, miny, minz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see)
            return true;
        vec2 = new Vec3(minx, miny, minz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null;

        if (see)
            return true;
        vec2 = new Vec3(minx, miny, maxz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see)
            return true;
        vec2 = new Vec3(maxx, miny, maxz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see)
            return true;

        vec2 = new Vec3(maxx, maxy, minz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null;

        if (see)
            return true;
        vec2 = new Vec3(minx, maxy, minz);

        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see)
            return true;
        vec2 = new Vec3(minx, maxy, maxz - 0.1);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see)
            return true;
        vec2 = new Vec3(maxx, maxy, maxz);
        see = mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        return see;
    }

    public float[] getRotationsToEnt(Entity ent) {
        final double differenceX = ent.posX - mc.thePlayer.posX;
        final double differenceY = (ent.posY + ent.height) - (mc.thePlayer.posY + mc.thePlayer.height) - 0.5;
        final double differenceZ = ent.posZ - mc.thePlayer.posZ;
        final float rotationYaw = (float) (Math.atan2(differenceZ, differenceX) * 180.0D / Math.PI) - 90.0f;
        final float rotationPitch = (float) (Math.atan2(differenceY, mc.thePlayer.getDistanceToEntity(ent)) * 180.0D
                / Math.PI);
        final float finishedYaw = mc.thePlayer.rotationYaw
                + MathHelper.wrapAngleTo180_float(rotationYaw - mc.thePlayer.rotationYaw);
        final float finishedPitch = mc.thePlayer.rotationPitch
                + MathHelper.wrapAngleTo180_float(rotationPitch - mc.thePlayer.rotationPitch);
        return new float[]{finishedYaw, -MathHelper.clamp_float(finishedPitch, -90, 90)};
    }

    enum priority {
        Range,
        Fov,
        Angle,
        Health,
        Armor

    }
    enum AuraMode {
        Switch,
        Single

    }
}