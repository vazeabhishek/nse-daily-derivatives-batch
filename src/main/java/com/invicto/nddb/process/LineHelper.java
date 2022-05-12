package com.invicto.nddb.process;

import static java.lang.Math.pow;

public class LineHelper {

    static double bestApproximateSlope(double x[], double y[])
    {
        int n = x.length;
        double m,sum_x = 0, sum_y = 0,
                     sum_xy = 0, sum_x2 = 0;
        for (int i = 0; i < n; i++) {
            sum_x += x[i];
            sum_y += y[i];
            sum_xy += x[i] * y[i];
            sum_x2 += pow(x[i], 2);
        }
 
        m = (n * sum_xy - sum_x * sum_y) / (n * sum_x2 - pow(sum_x, 2));
      return m;
    }

    public static void main(String[] args) {
        double[] x = {1	,
            2	,
            3	,
            4	,
            5	,
            6	,
            7	,
            8	,
            9	,
            10	,
            11	,
            12	,
            13	,
            14	,
            15	,
            16	,
            17	,
            18	,
            19	,
            20	,
            21	,
            22	};

        double[] y = {3.03030303030303		,
            2.94117647058823		,
            19.3650793650793		,
            40.6914893617021		,
            7.37240075614366		,
            5.6338028169014		,
            18.3333333333333		,
            31.2676056338028		,
            50.2145922746781		,
            17.0714285714285		,
            17.0835875533862		,
            94.7889525794684		,
            38.9245585874799		,
            141.286347005584		,
            35.2114924181963		,
            25.1387085350017		,
            26.843073439932		,
            0.673062620853785		,
            0.472795774387766		,
            -0.819822800632329		,
            -0.426273259693083		,
            -1.41830770948888		,
            -0.03398534853863		,

        };

        System.out.println("Slop is "+bestApproximateSlope(x, y));
    }
}
