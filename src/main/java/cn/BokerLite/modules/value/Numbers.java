package cn.BokerLite.modules.value;

public class Numbers extends Value<Double> {
    public Double min;
    public Double max;
    public Double inc;
    private String name;
    public String ch;

    public Numbers(String displayName, String ch, String name, Double value, Double min, Double max, Double inc) {
        super(displayName, name);
        setValue(value);
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.ch = ch;
    }

    public Double getMinimum() {
        return this.min;
    }

    public Double getMaximum() {
        return this.max;
    }

    public Double getIncrement() {
        return this.inc;
    }

    public String getId() {
        return this.name;
    }

}
