package cn.BokerLite.utils.movement;


import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.*;
import cn.BokerLite.utils.packet.PacketUtil;

import java.util.ArrayList;

public class Teleport {

    private static final Minecraft mc = Minecraft.getMinecraft();

    static double x;
    static double y;
    static double z;
    static double xPreEn;
    static double yPreEn;
    static double zPreEn;
    static double xPre;
    static double yPre;
    static double zPre;

    public static void tpToLocation(final double range, final double maxXZTP, final double maxYTP,
                                    final ArrayList<Vec3> positionsBack, final ArrayList<Vec3> positions, final BlockPos targetBlockPos) {
        positions.clear();
        positionsBack.clear();
        final double step = maxXZTP / range;
        int steps = 0;
        for (int i = 0; i < range; i++) {
            steps++;
            if (maxXZTP * steps > range) {
                break;
            }
        }

        final double posX = ((double) targetBlockPos.getX()) + 0.5;
        final double posY = ((double) targetBlockPos.getY()) + 1.0;
        final double posZ = ((double) targetBlockPos.getZ()) + 0.5;
        xPreEn = posX;
        yPreEn = posY;
        zPreEn = posZ;
        xPre = mc.thePlayer.posX;
        yPre = mc.thePlayer.posY;
        zPre = mc.thePlayer.posZ;
        boolean up = false;

        final MovingObjectPosition rayTrace;
        MovingObjectPosition rayTrace1 = null;
        if ((rayTraceWide(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                new Vec3(posX, posY, posZ), false, false, true))
                || (rayTrace1 = rayTracePos(
                new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
                new Vec3(posX, posY + mc.thePlayer.getEyeHeight(), posZ), false, false,
                true)) != null) {
            if ((rayTrace = rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                    new Vec3(posX, mc.thePlayer.posY, posZ), false, false, true)) != null
                    || (rayTrace1 = rayTracePos(
                    new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
                            mc.thePlayer.posZ),
                    new Vec3(posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), posZ), false, false,
                    true)) != null) {
                MovingObjectPosition trace = null;
                if (rayTrace == null) {
                    trace = rayTrace1;
                }
                if (rayTrace1 == null) {
                    trace = rayTrace;
                }
                if (trace != null) {
                    if (trace.getBlockPos() != null) {
                        final boolean fence;
                        final BlockPos target = trace.getBlockPos();

                        up = true;
                        y = target.up().getY();
                        yPreEn = target.up().getY();
                        Block lastBlock = null;
                        boolean found = false;
                        for (int i = 0; i < maxYTP; i++) {
                            final MovingObjectPosition tr = rayTracePos(
                                    new Vec3(mc.thePlayer.posX, target.getY() + i, mc.thePlayer.posZ),
                                    new Vec3(posX, target.getY() + i, posZ), false, false, true);
                            if (tr == null) {
                                continue;
                            }
                            if (tr.getBlockPos() == null) {
                                continue;
                            }

                            final BlockPos blockPos = tr.getBlockPos();
                            final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                            if (block.getMaterial() != Material.air) {
                                lastBlock = block;
                                continue;
                            }
                            fence = lastBlock instanceof BlockFence;
                            y = target.getY() + i;
                            yPreEn = target.getY() + i;
                            if (fence) {
                                y += 1;
                                yPreEn += 1;
                                if (i + 1 > maxYTP) {
                                    break;
                                }
                            }
                            found = true;
                            break;
                        }
                        if (!found) {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            } else {
                final MovingObjectPosition ent = rayTracePos(
                        new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                        new Vec3(posX, posY, posZ), false, false, false);
                if (ent != null && ent.entityHit == null) {
                    y = mc.thePlayer.posY;
                    yPreEn = mc.thePlayer.posY;
                } else {
                    y = mc.thePlayer.posY;
                    yPreEn = posY;
                }

            }
        }

        for (int i = 0; i < steps; i++) {
            if (i == 1 && up) {
                x = mc.thePlayer.posX;
                y = yPreEn;
                z = mc.thePlayer.posZ;
                sendPacket(false, positionsBack, positions);
            }
            if (i != steps - 1) {
                {
                    final double difX = mc.thePlayer.posX - xPreEn;
                    final double difY = mc.thePlayer.posY - yPreEn;
                    final double difZ = mc.thePlayer.posZ - zPreEn;
                    final double divider = step * i;
                    x = mc.thePlayer.posX - difX * divider;
                    y = mc.thePlayer.posY - difY * (up ? 1 : divider);
                    z = mc.thePlayer.posZ - difZ * divider;
                }
                sendPacket(false, positionsBack, positions);
            } else {
                // if last teleport
                {
                    final double difX = mc.thePlayer.posX - xPreEn;
                    final double difY = mc.thePlayer.posY - yPreEn;
                    final double difZ = mc.thePlayer.posZ - zPreEn;
                    final double divider = step * i;
                    x = mc.thePlayer.posX - difX * divider;
                    y = mc.thePlayer.posY - difY * (up ? 1 : divider);
                    z = mc.thePlayer.posZ - difZ * divider;
                }
                sendPacket(false, positionsBack, positions);
                final double xDist = x - xPreEn;
                final double zDist = z - zPreEn;
                final double yDist = y - posY;
                final double dist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (dist > 4) {
                    x = xPreEn;
                    y = yPreEn;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                } else if (dist > 0.05 && up) {
                    x = xPreEn;
                    y = yPreEn;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                }
                if (Math.abs(yDist) < maxYTP && mc.thePlayer.getDistance(posX, posY, posZ) >= 4) {
                    x = xPreEn;
                    y = posY;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                    mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, targetBlockPos, EnumFacing.UP));
                    mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, targetBlockPos, EnumFacing.UP));
                }
            }
        }
    }

    public static void sendPacket(final boolean goingBack, final ArrayList<Vec3> positionsBack, final ArrayList<Vec3> positions) {
        final C03PacketPlayer.C04PacketPlayerPosition playerPacket = new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false);
        PacketUtil.sendPacketWithoutEvent(playerPacket);
        if (goingBack) {
            positionsBack.add(new Vec3(x, y, z));
            return;
        }
        positions.add(new Vec3(x, y, z));
    }

    public static MovingObjectPosition rayTracePos(final Vec3 vec31, final Vec3 vec32, final boolean stopOnLiquid,
                                                   final boolean ignoreBlockWithoutBoundingBox, final boolean returnLastUncollidableBlock) {
        final float[] rots = getFacePosRemote(vec32, vec31);
        final float yaw = rots[0];
        final double angleA = Math.toRadians(normalizeAngle(yaw));
        final double angleB = Math.toRadians(normalizeAngle(yaw) + 180);
        final double size = 2.1;
        final double size2 = 2.1;
        final Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord,
                vec31.zCoord + Math.sin(angleA) * size);

        final Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord,
                vec31.zCoord + Math.sin(angleB) * size);

        final Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord,
                vec32.zCoord + Math.sin(angleA) * size);

        final Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord,
                vec32.zCoord + Math.sin(angleB) * size);

        new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord,
                vec31.zCoord + Math.sin(angleA) * size2);

        new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord,
                vec31.zCoord + Math.sin(angleB) * size2);

        new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord,
                vec32.zCoord + Math.sin(angleA) * size2);

        new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord,
                vec32.zCoord + Math.sin(angleB) * size2);

        final MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);

        final MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);

        final MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);

        if (trace2 != null || trace1 != null || trace3 != null) {
            if (returnLastUncollidableBlock) {
                if (trace3 != null && (getBlock(trace3.getBlockPos()).getMaterial() != Material.air
                        || trace3.entityHit != null)) {
                    return trace3;
                }
                if (trace1 != null && (getBlock(trace1.getBlockPos()).getMaterial() != Material.air
                        || trace1.entityHit != null)) {
                    return trace1;
                }
                if (trace2 != null && (getBlock(trace2.getBlockPos()).getMaterial() != Material.air
                        || trace2.entityHit != null)) {
                    return trace2;
                }
            } else {
                if (trace3 != null) {
                    return trace3;
                }
                if (trace1 != null) {
                    return trace1;
                }
                return trace2;
            }
        }
        if (trace2 == null) {
            if (trace3 == null) {
                return trace1;
            }
            return trace3;
        }
        return trace2;
    }

    public static boolean rayTraceWide(final Vec3 vec31, final Vec3 vec32, final boolean stopOnLiquid, final boolean ignoreBlockWithoutBoundingBox,
                                       final boolean returnLastUncollidableBlock) {
        float yaw = getFacePosRemote(vec32, vec31)[0];
        yaw = normalizeAngle(yaw);
        yaw += 180;
        yaw = MathHelper.wrapAngleTo180_float(yaw);
        final double angleA = Math.toRadians(yaw);
        final double angleB = Math.toRadians(yaw + 180);
        final double size = 2.1;
        final Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord,
                vec31.zCoord + Math.sin(angleA) * size);
        final Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord,
                vec31.zCoord + Math.sin(angleB) * size);
        final Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord,
                vec32.zCoord + Math.sin(angleA) * size);
        final Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord,
                vec32.zCoord + Math.sin(angleB) * size);

        final MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        final MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        final MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);

        if (returnLastUncollidableBlock) {
            return (trace1 != null && getBlock(trace1.getBlockPos()).getMaterial() != Material.air)
                    || (trace2 != null && getBlock(trace2.getBlockPos()).getMaterial() != Material.air)
                    || (trace3 != null && getBlock(trace3.getBlockPos()).getMaterial() != Material.air);
        } else {
            return trace1 != null || trace2 != null || trace3 != null;
        }

    }


    public static float[] getFacePosRemote(final Vec3 src, final Vec3 dest) {
        final double diffX = dest.xCoord - src.xCoord;
        final double diffY = dest.yCoord - (src.yCoord);
        final double diffZ = dest.zCoord - src.zCoord;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        final float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        return new float[]{MathHelper.wrapAngleTo180_float(yaw),
                MathHelper.wrapAngleTo180_float(pitch)};
    }


    public static float normalizeAngle(final float angle) {
        return (angle + 360) % 360;
    }

    public static Block getBlock(final BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

}
