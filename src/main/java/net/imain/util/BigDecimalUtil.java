package net.imain.util;

import java.math.BigDecimal;

/**
 * @author: uncle
 * @apdateTime: 2017-11-23 11:56
 */
public class BigDecimalUtil {

    private BigDecimalUtil() {}

    public static BigDecimal add(double v1, double v2) {
        BigDecimal bigDecimal_v1 = new BigDecimal(Double.toString(v1));
        BigDecimal bigDecimal_v2 = new BigDecimal(Double.toString(v2));
        return bigDecimal_v1.add(bigDecimal_v2);
    }

    public static BigDecimal subtract(double v1, double v2) {
        BigDecimal bigDecimal_v1 = new BigDecimal(Double.toString(v1));
        BigDecimal bigDecimal_v2 = new BigDecimal(Double.toString(v2));
        return bigDecimal_v1.subtract(bigDecimal_v2);
    }

    public static BigDecimal multiply(double v1, double v2) {
        BigDecimal bigDecimal_v1 = new BigDecimal(Double.toString(v1));
        BigDecimal bigDecimal_v2 = new BigDecimal(Double.toString(v2));
        return bigDecimal_v1.multiply(bigDecimal_v2);
    }

    /**
     * 除法，默认四舍五入保留两位小数
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal divide(double v1, double v2) {
        return divide(v1, v2, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     *
     *
     * @param v1
     * @param v2
     * @param scale 保留小数位几位
     * @param roundingMode mode
     */
    public static BigDecimal divide(double v1, double v2, int scale, int roundingMode) {
        BigDecimal bigDecimal_v1 = new BigDecimal(Double.toString(v1));
        BigDecimal bigDecimal_v2 = new BigDecimal(Double.toString(v2));
        return bigDecimal_v1.divide(bigDecimal_v2, scale, roundingMode);
    }
}
