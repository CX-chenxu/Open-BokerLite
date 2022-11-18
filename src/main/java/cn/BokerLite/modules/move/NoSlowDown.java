package cn.BokerLite.modules.move;

import cn.BokerLite.Client;
import cn.BokerLite.api.EventHandler;
import cn.BokerLite.api.event.EventPacketRecieve;
import cn.BokerLite.api.event.PacketEvent;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.combat.LegitAura;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.utils.MovementUtils;
import cn.BokerLite.utils.NoSlowInput;
import cn.BokerLite.utils.PlayerUtils;
import cn.BokerLite.utils.movement.noslow.NoSlowDownUtil;
import cn.BokerLite.utils.packet.PacketUtil;
import cn.BokerLite.utils.timer.MSTimer;
import cn.BokerLite.utils.timer.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;


import java.util.LinkedList;

import static cn.BokerLite.utils.movement.MoveUtils.isMoving;


public class NoSlowDown extends Module {
    private final Mode<Enum<SlowMode>> mode = new Mode<>("Mode", "Mode",SlowMode.values(), SlowMode.AAC5);
    public NoSlowDown() {
        super("NoSlowdown", "无减速", Keyboard.KEY_NONE, ModuleType.Movement, "Make you move noslow down",ModuleType.SubCategory.MOVEMENT_MAIN);
        // this.addValues(mode);
    }
    public static int ticks = 0;
    public TimerUtil timer = new TimerUtil();

    private final MSTimer msTimer = new MSTimer();
    private boolean pendingFlagApplyPacket = false;
    private final LinkedList<Packet<?>> packetBuf = new LinkedList<>();
    private boolean nextTemp = false;
    private boolean waitC03 = false;
    private boolean lastBlockingStat = false;
    public static int ticks2 = 0;

    // private final Option<Boolean> vanillakickbypass = new Option<>("vanillakickbypass", "vanillakickbypass", false);
    // public static Option<Boolean> consume = new Option<Boolean>("consume", "consume", true);



    MovementInput origmi;
    @Override
    public void enable(){
        origmi=mc.thePlayer.movementInput;
        if(!(mc.thePlayer.movementInput instanceof NoSlowDownUtil)) {
            mc.thePlayer.movementInput = new NoSlowDownUtil(mc.gameSettings);
        }
    }

    @Override
    public void disable(){
        msTimer.reset();
        pendingFlagApplyPacket = false;
        packetBuf.clear();
        nextTemp = false;
        waitC03 = false;
        mc.thePlayer.movementInput=origmi;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.thePlayer == null) return;
        if (mc.gameSettings.keyBindSneak.isPressed())
            return;
        if (!(mc.thePlayer.movementInput instanceof NoSlowInput)) {
            origmi = mc.thePlayer.movementInput;
            mc.thePlayer.movementInput = new NoSlowInput(mc.gameSettings);
        }
        if ((mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) || !mc.gameSettings.keyBindSneak.isKeyDown()) {
            NoSlowInput move = (NoSlowInput) mc.thePlayer.movementInput;
            move.setNSD(true);
        }
    }


    private void sendPacket(TickEvent.PlayerTickEvent event,
                            boolean sendC07,
                            boolean sendC08,
                            boolean delay,
                            long delayValue,
                            boolean onGround) {
        boolean Hypixel = false;

        // C08PacketPlayerBlockPlacement blockPlace = new C08PacketPlayerBlockPlacement(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
        //  C08PacketPlayerBlockPlacement blockMent = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f);
        if (onGround && !Minecraft.getMinecraft().thePlayer.onGround) {
            return;
        }
        if (sendC07 && event.phase== TickEvent.Phase.START) {
            //Helper.sendMessage("sendc07");
            if (delay && msTimer.hasTimePassed(delayValue)) {
                PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
            } else if (!delay) {
                PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
            }
        }

    }
    private void sendPacket(TickEvent.PlayerTickEvent event) {


        // C08PacketPlayerBlockPlacement blockPlace = new C08PacketPlayerBlockPlacement(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
        //  C08PacketPlayerBlockPlacement blockMent = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f);
        if (!Minecraft.getMinecraft().thePlayer.onGround) {
            return;
        }
        if (!(event.phase == TickEvent.Phase.START)) {
            // Helper.sendMessage("sendc08");
            PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
        }
    }
    @EventHandler
    public void onPacketReceive(PacketEvent e) {

            if ((mode.getValue()==SlowMode.Flux) && e.getPacket() instanceof S30PacketWindowItems && (mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking())) {
                ticks2 = mc.thePlayer.ticksExisted;
                e.setCancelled(true);
            }


    }
    @SubscribeEvent
    public void onMotion(TickEvent.PlayerTickEvent event) {
        if (!isMoving()) {
            return;
        }
        if (mode.getValue() == SlowMode.AAC5) {
            if ((event.phase== TickEvent.Phase.END)&&(Minecraft.getMinecraft().thePlayer.isUsingItem() || Minecraft.getMinecraft().thePlayer.isBlocking() || (mc.thePlayer.isBlocking()))) {
                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
            }
            return;
        }
        if (mode.getValue() != SlowMode.AAC5) {
            if (!Minecraft.getMinecraft().thePlayer.isBlocking()) {
                return;
            }
            if (mode.getValue() == SlowMode.LiquidBounce) {
                sendPacket(event, true, true, false, 0, false);
            } else if (mode.getValue() == SlowMode.AAC) {
                if (Minecraft.getMinecraft().thePlayer.ticksExisted % 3 == 0) {
                    sendPacket(event, true, false, false, 0, false);
                } else if (Minecraft.getMinecraft().thePlayer.ticksExisted % 3 == 1) {
                    sendPacket(event, false, true, false, 0, false);
                }
            } else if (mode.getValue() == SlowMode.NCP) {
                sendPacket(event, true, true, false, 0, false);
                // ClientUtil.sendClientMessage(this.getName() + " Enabled", ClientNotification.Type.SUCCESS);
            }else if (mode.getValue() == SlowMode.Hypixel3) {
                if (event.phase== TickEvent.Phase.START) {
                    PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                } else {
                    PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));
                }
            }else if (mode.getValue() == SlowMode.Hypixel2) {
                if (Minecraft.getMinecraft().thePlayer.ticksExisted % 2 == 0) {
                    sendPacket(event, true, false, true, 50, true);
                } else {
                    sendPacket(event);
                }
            }
            if ((!(event.phase== TickEvent.Phase.START))&&mode.getValue()==SlowMode.Flux){
                if (mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking()) {
                    Math.min(Math.round(((mc.thePlayer.ticksExisted - ticks2) / 30f * 100)), 100);
                    // ChatUtils.debug("Eating: " + process + "%");
                    PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
                            mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
                } else {
                    ticks2 = mc.thePlayer.ticksExisted;
                }
            }
        }
    }
  //  @SubscribeEvent
 //   public void onLoop(TickEvent.PlayerTickEvent eventPostUpdate) {
   //     if (mode.getValue() == SlowMode.Hypixel) {
 //           if (!(LegitAura.target == null && this.mc.thePlayer.isBlocking() && PlayerUtils.isMoving() && MovementUtils.isOnGround((double) 0.42))) {
          //      return;
  //          }
   //         if (LegitAura.target != null && !this.mc.thePlayer.isBlocking() || !PlayerUtils.isMoving() || !MovementUtils.isOnGround((double) 0.42)) {
    //            if (timer.delay(200)) {
   //                 this.mc.thePlayer.sendQueue.addToSendQueue((Packet) new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
     //               timer.reset();
    //            }

    //        }
    //    }

   // }
    private void sendPacket(Boolean sendC07  ,  Boolean sendC08 , Boolean delay ,Long delayValue ,Boolean onGround ,Boolean watchDog ) {
        C07PacketPlayerDigging  digging = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1,-1,-1), EnumFacing.DOWN);
        C08PacketPlayerBlockPlacement blockPlace = new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem());
        C08PacketPlayerBlockPlacement blockMent = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f);
        if(onGround && !mc.thePlayer.onGround) {
            return;
        }
        if(sendC07 ) {
            if(delay && msTimer.hasTimePassed(delayValue)) {
                if (timer.delay(100)) {
                    System.out.println("C07 1");
                    mc.thePlayer.sendQueue.addToSendQueue(digging);
                    timer.reset();
                }
            } else if(!delay) {
                if (timer.delay(100)) {
                    System.out.println("C07 2");
                    mc.thePlayer.sendQueue.addToSendQueue(digging);
                    timer.reset();
                }
            }
        }
        if(sendC08 ) {
            if(delay && msTimer.hasTimePassed(delayValue) && !watchDog) {
                if (timer.delay(100)) {
                    System.out.println("C08 1");
                    mc.thePlayer.sendQueue.addToSendQueue(blockPlace);
                    msTimer.reset();
                    timer.reset();
                }
            } else if(!delay && !watchDog) {
                if (timer.delay(100)) {
                    System.out.println("C08 2");
                    mc.thePlayer.sendQueue.addToSendQueue(blockPlace);
                    timer.reset();
                }
            } else if(watchDog) {
                if (timer.delay(100)) {
                    System.out.println("C08 3");
                    mc.thePlayer.sendQueue.addToSendQueue(blockMent);
                    timer.reset();
                }
            }
        }
    }
    @EventHandler
    public void onPacketRec(EventPacketRecieve e) {
        if (mode.getValue() == SlowMode.Hypixel && e.getPacket() instanceof S30PacketWindowItems && (mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking())) {
            ticks = mc.thePlayer.ticksExisted;
            e.setCancelled(true);
        }
    }
    @SubscribeEvent
    public void onPost(TickEvent.PlayerTickEvent event) {
        if (mode.getValue() == SlowMode.Hypixel) {
            Item item = mc.thePlayer.getItemInUse().getItem();
            if (!mc.thePlayer.isBlocking())
                return;


                 if (mc.thePlayer.ticksExisted % 2 == 0) {
                        sendPacket(true, false, false, 50L, true, false);
                 } else {
                        sendPacket(false, true, false, 0L, true, true);
                 }

            }

            // mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem < 8 ? mc.thePlayer.inventory.currentItem - 1 : mc.thePlayer.inventory.currentItem + 1));

            //   }
        }



    @SubscribeEvent
    public void onLoop(TickEvent eventPreUpdate) {
        if (mode.getValue() == SlowMode.Hypixel && mc.thePlayer.isUsingItem() && (mc.thePlayer.getItemInUse().getItem() instanceof ItemFood || mc.thePlayer.getItemInUse().getItem() instanceof ItemBucketMilk || mc.thePlayer.getItemInUse().getItem() instanceof ItemPotion) && mc.thePlayer.getItemInUseDuration() >= 1) {
            if (timer.delay(250)) {
           //     System.out.println("C09");
              //  PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem < 8 ? mc.thePlayer.inventory.currentItem - 1 : mc.thePlayer.inventory.currentItem + 1));
                PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                timer.reset();
            }

        }
    }


    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event){

        if((mode.getValue()==SlowMode.Matrix || mode.getValue()==SlowMode.Vulcan) && (lastBlockingStat)){
            if(msTimer.hasTimePassed(230) && nextTemp) {
                nextTemp = false;
                PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                if(!packetBuf.isEmpty()) {
                    boolean canAttack = false;
                    for(int i=0;packetBuf.isEmpty();i++) {
                        Packet<?> packet=packetBuf.get(i);
                        if(packet instanceof C03PacketPlayer) {
                            canAttack = true;
                        }
                        if(!((packet instanceof C02PacketUseEntity || packet instanceof C0APacketAnimation) && !canAttack)) {
                            PacketUtil.sendPacketWithoutEvent(packet);
                        }
                    }
                    packetBuf.clear();
                }
            }
            if(!nextTemp) {
                lastBlockingStat = mc.thePlayer.isBlocking();
                if (!mc.thePlayer.isBlocking()) {
                    return;
                }
                PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
                nextTemp = true;
                waitC03 = mode.getValue()==SlowMode.Vulcan;
                msTimer.reset();
            }
        }
    }
    @EventHandler
    public void onPacket(PacketEvent event){

            Packet<?> packet = event.getPacket();
            if (mode.getValue()==SlowMode.Medusa) {
                if (mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking()) {
                    PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer,C0BPacketEntityAction.Action.STOP_SPRINTING));
                }
            }
            if((mode.getValue()==SlowMode.Matrix || mode.getValue()==SlowMode.Vulcan) && nextTemp) {
                if((packet instanceof C07PacketPlayerDigging || packet instanceof C08PacketPlayerBlockPlacement) && mc.thePlayer.isBlocking()) {
                    event.setCancelled(true);
                }else if (packet instanceof C03PacketPlayer || packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity || packet instanceof C07PacketPlayerDigging || packet instanceof C08PacketPlayerBlockPlacement) {
                    if (mode.getValue()==SlowMode.Vulcan && waitC03 && packet instanceof C03PacketPlayer) {
                        waitC03 = false;
                        return;
                    }
                    packetBuf.add(packet);
                    event.setCancelled(true);
                }
            } else if (pendingFlagApplyPacket && packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                pendingFlagApplyPacket = false;

            }

    }

    public boolean isUsingFood() {
        if (mc.thePlayer.getItemInUse() == null)
            return false;
        Item usingItem = mc.thePlayer.getItemInUse().getItem();
        return mc.thePlayer.isUsingItem() && (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion);
    }




    public enum SlowMode {
        AAC5, AAC, LiquidBounce, Matrix, NCP, Vanilla, Vulcan, Hypixel, Hypixel2, Hypixel3, Flux,Medusa
    }

    public static boolean isHoldingSword() {
        return Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

    public static boolean isOnGround(double height) {
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        return !Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
    }
}
