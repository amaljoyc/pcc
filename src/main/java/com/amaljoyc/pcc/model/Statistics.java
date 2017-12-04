package com.amaljoyc.pcc.model;

/**
 * Created by achemparathy on 04.12.17.
 */
public class Statistics {

    private Integer sum;
    private Float average;
    private Integer maximum;
    private Integer minimum;
    private Integer count;

    public Statistics() {
    }

    public Statistics(Integer sum, Float average, Integer maximum, Integer minimum, Integer count) {
        this.sum = sum;
        this.average = average;
        this.maximum = maximum;
        this.minimum = minimum;
        this.count = count;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Statistics that = (Statistics) o;

        if (sum != null ? !sum.equals(that.sum) : that.sum != null) return false;
        if (average != null ? !average.equals(that.average) : that.average != null) return false;
        if (maximum != null ? !maximum.equals(that.maximum) : that.maximum != null) return false;
        if (minimum != null ? !minimum.equals(that.minimum) : that.minimum != null) return false;
        return count != null ? count.equals(that.count) : that.count == null;
    }

    @Override
    public int hashCode() {
        int result = sum != null ? sum.hashCode() : 0;
        result = 31 * result + (average != null ? average.hashCode() : 0);
        result = 31 * result + (maximum != null ? maximum.hashCode() : 0);
        result = 31 * result + (minimum != null ? minimum.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "sum=" + sum +
                ", average=" + average +
                ", maximum=" + maximum +
                ", minimum=" + minimum +
                ", count=" + count +
                '}';
    }
}
