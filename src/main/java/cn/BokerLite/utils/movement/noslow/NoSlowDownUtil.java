package cn.BokerLite.utils.movement.noslow;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;

public class NoSlowDownUtil extends MovementInput {
    boolean NoSlowBoolean = true;
    private final GameSettings gameSettings;

    public NoSlowDownUtil(GameSettings par1GameSettings) {
        this.gameSettings = par1GameSettings;
    }

    public void updatePlayerMoveState() {
        super.moveStrafe = 0.0F;
        super.moveForward = 0.0F;
        if (this.gameSettings.keyBindForward.isKeyDown()) {
            ++super.moveForward;
        }

        if (this.gameSettings.keyBindBack.isKeyDown()) {
            --super.moveForward;
        }

        if(this.gameSettings.keyBindLeft.isKeyDown()) {
            ++super.moveStrafe;
        }

        if(this.gameSettings.keyBindRight.isKeyDown()) {
            --super.moveStrafe;
        }

        super.jump = this.gameSettings.keyBindJump.isKeyDown();
        super.sneak = this.gameSettings.keyBindSneak.isKeyDown();

        if (super.sneak) {
            super.moveStrafe = (float) ((double) super.moveStrafe * 0.3D);
            super.moveForward = (float) ((double) super.moveForward * 0.3D);
        }
        if (this.NoSlowBoolean) {
            super.moveStrafe *= 5.0F;
            super.moveForward *= 5.0F;

        }


    }

    public void setNSD(boolean a) {
        this.NoSlowBoolean = a;
    }
}
