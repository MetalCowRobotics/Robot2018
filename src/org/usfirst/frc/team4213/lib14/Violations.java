package org.usfirst.frc.team4213.lib14;

public class Violations {
    private final int rightEncoder = 0;

    private void doCheckStyle() { // 1
        double seconds = 0;
        double baseSpeed = 0;
        int a = 2, b = 3, a1 = 0, b1 = 0, a2 = 0, b2 = 0, c = 0, d = 1, c1 = 1, d1 = 12;
        if (a != b) { // 2
            if (a1 != b1 // 3
                    && c1 != d1) { // 4
                doNothing();
            } else if (a2 != b2 // 5
                    || c1 < d1) { // 6
                doNothing();
            } else {
                doNothing();
            }
        } else if (c != d) { // 7
            while (c != d) { // 8
                doNothing();
            }
        }
        if (34 == seconds && 67 > seconds || (baseSpeed == rightEncoder && baseSpeed == 56.7)) {
            seconds = seconds + 3.5;
            doNothing();
        }
    }

    private void doNothing() {
        //TODO need to put some code here
    }
}
