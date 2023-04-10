package com.capstone.smart_white_cane.map.data;

public class JibunAddress {
    public String si;
    public String gu;
    public String dong;
    public String landNum;

    JibunAddress(String si, String gu, String dong, String landNum) {
        this.si = si;
        this.gu = gu;
        this.dong = dong;
        this.landNum = landNum;
    }
    public String toString() {
        return si + " " + gu + " " + dong + " " +landNum;
    }

    public static JibunAddress toJibun(String str) {
        String[] array = str.split(" ");

        if(array.length == 4) {
            return new JibunAddress(array[0], array[1], array[2], array[3]);
        } else {
            return new JibunAddress(array[0], array[1]+" "+array[2], array[3], array[4]);
        }


    }
}
