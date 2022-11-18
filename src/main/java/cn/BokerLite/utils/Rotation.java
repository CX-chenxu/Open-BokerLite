package cn.BokerLite.utils;

import net.minecraft.entity.Entity;

public class Rotation {
    private float yaw;
    private static float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        Rotation.pitch = pitch;
    }

    public Rotation(Entity ent) {
        this.yaw = ent.rotationYaw;
        pitch = ent.rotationPitch;
    }

    public void add(float yaw, float pitch) {
        this.yaw += yaw;
        Rotation.pitch += pitch;
    }

    public void remove(float yaw, float pitch) {
        this.yaw -= yaw;
        Rotation.pitch -= pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public static float getPitch() {
        return pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        Rotation.pitch = pitch;
    }
}

