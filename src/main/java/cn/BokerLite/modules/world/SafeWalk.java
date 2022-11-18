package cn.BokerLite.modules.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Numbers;

public class SafeWalk extends Module {

	public SafeWalk() {
		super("SafeWalk", "安全行走", ModuleType.World, "Safe walk",ModuleType.SubCategory.World_World);
	}

	private final Numbers height = new Numbers("Height", "高度", "height", 1D, 0D, 16D, 1D);

	@SubscribeEvent
	public void onMove(TickEvent.PlayerTickEvent e) {
		if (e.side == Side.CLIENT && e.phase == Phase.START) {
			EntityPlayer p = e.player;
			Module freeCam = ModuleManager.getModule("Freecam");

			if (freeCam != null && freeCam.getState()) {
				return;
			}

			if (p.onGround && !p.noClip) {
				double x = p.motionX, 
					   y = p.motionY, 
					   z = p.motionZ;
				double increment = 0.05D;
				for (; x != 0.0D && isOffsetBBEmpty(x, -this.height.getValue(), 0.0D);) {
					if (x < increment && x >= -increment) {
						x = 0.0D;
					} else if (x > 0.0D) {
						x -= increment;
					} else {
						x += increment;
					}
				}
				for (; z != 0.0D && isOffsetBBEmpty(0.0D, -this.height.getValue(), z);) {
					if (z < increment && z >= -increment) {
						z = 0.0D;
					} else if (z > 0.0D) {
						z -= increment;
					} else {
						z += increment;
					}
				}
				for (; x != 0.0D && z != 0.0D && isOffsetBBEmpty(x, -this.height.getValue(), z);) {
					if (x < increment && x >= -increment) {
						x = 0.0D;
					} else if (x > 0.0D) {
						x -= increment;
					} else {
						x += increment;
					}
					if (z < increment && z >= -increment) {
						z = 0.0D;
					} else if (z > 0.0D) {
						z -= increment;
					} else {
						z += increment;
					}
				}
				if(p.motionX != x || p.motionZ != z) {
					p.setPosition(p.posX - (p.motionX - x) * 2D, p.posY, p.posZ - (p.motionZ - z) * 2D);
					p.motionX = x;
					p.motionY = y;
					p.motionZ = z;
				}
			}
		}
	}

	private boolean isOffsetBBEmpty(double x, double y, double z) {
		return mc.theWorld.getCollisionBoxes(mc.thePlayer.getEntityBoundingBox().offset(x, y, z)).isEmpty();
	}
}
