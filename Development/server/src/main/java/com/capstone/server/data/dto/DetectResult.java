package com.capstone.server.data.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DetectResult {
    String state;
    ArrayList<detectData> results;


    public DetectResult() {
        this.state = "empty!";
        results = new ArrayList<>();
    }

    public void setResult(String result) {
        if(!result.equals("")) {
            String[] splits = result.split("tensor");
            state =  "tensor found: " +  splits.length;
            System.out.println("original is " + splits[splits.length-1]);

            splits = splits[splits.length-1].split("\\[|\\]|\\(|\\)");

            System.out.println("size is " + splits.length);
            for(int i =3; i<splits.length; i+=2) {
                String str = splits[i];
                str = str.replace("[^0-9]", "");
                str = str.replace(",", "");

                System.out.println(str);
                String[] str2 = str.split(" ");
                ArrayList<String> rs = new ArrayList<>();
                for(String str3: str2) {
                    if(!str3.isEmpty())
                        rs.add(str3);
                }

                detectData dData = new detectData(
                        Float.parseFloat(rs.get(0)),
                        Float.parseFloat(rs.get(1)),
                        Float.parseFloat(rs.get(2)),
                        Float.parseFloat(rs.get(3)),
                        Float.parseFloat(rs.get(4))
                );

                results.add(dData);
            }


        }


    }
}
