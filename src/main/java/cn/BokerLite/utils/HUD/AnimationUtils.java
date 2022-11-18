package cn.BokerLite.utils.HUD;

import static java.lang.Math.abs;

public final class AnimationUtils {
    public static float animate(float target, float current, float speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0) {
            speed = 0.0F;
        } else if (speed > 1.0) {
            speed = 1.0f;
        }
        float dif = Math.max(target, current) - Math.min(target, current);
        float factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1f;
        }
        current = larger ? (current += factor) : (current -= factor);
        return current;
    }

    public static double animation(double start, double target, double current, double speed, AnimationTypes Type){
        //Creates return value
        double returnValue = 0;

        //Judge plus or minus
        boolean larger = target > start;

        //Puts value
        if(Type == AnimationTypes.faster)
            returnValue = abs((abs(current - start) / abs(target - start)) * speed);
        else if (Type == AnimationTypes.slowDown)
            returnValue = abs((abs(target - current) / abs(target - start)) * speed);

        if(returnValue < 0.1)
            returnValue = 0.1;

        double v = larger ? current + returnValue : current - returnValue;

        if((v < target && !larger) || (v > target && larger))
            return target;

        return v;

    }

    public enum AnimationTypes{
        faster,
        slowDown
    }
}

