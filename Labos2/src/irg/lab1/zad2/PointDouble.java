package irg.lab1.zad2;

public class PointDouble {

    private double x;
    private double y;
    private double z;

    public PointDouble() {

    }

    public PointDouble(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "PointDouble{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}