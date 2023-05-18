package com.capstone.server.detector_dir;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Handler;

public class Detector {
    private ProcessBuilder processBuilder = null;

    public Detector() {
        String path = "src\\main\\java\\com\\capstone\\server\\detector_dir";
        try {
        } catch (Exception e) {
            System.err.println("detector.java: 해당 이미지가 존재하지 않습니다.");
        }
    }

    public String Run() throws IOException, InterruptedException {

        String commands = "cmd /c " +
                "\"cd src/main/java/com/capstone/server/detector_dir/yolov5" +
                "&&python detect.py --weights best.pt --img 320 --conf 0.40 --source images/image.jpg\"";

        //Process process1 = new ProcessBuilder(commands).start();
        Process process1 = Runtime.getRuntime().exec(commands);

        //BufferedReader stdOut = new BufferedReader(new InputStreamReader(process1.getInputStream()));

        //String str1 = null;
        //while((str1 = stdOut.readLine()) != null) {
        //    System.out.println(str1);
        //}

        //Process process2 = new ProcessBuilder("/c", "dir").start();

        BufferedReader bReader = new BufferedReader(new InputStreamReader(process1.getInputStream(), StandardCharsets.UTF_8));

        String result = "";
        StringBuilder sb = new StringBuilder();

        try{
            System.out.println("start");

            String line = "";
            while((line = bReader.readLine()) != null) {
                result = sb.append(result).append(line).toString();
                System.out.println("detect: " + line);
            }

            System.out.println("End");

            return result;
        } catch (Exception e) {
            System.err.println("detector.java: 버퍼에서 읽을 수 없습니다.");

        }



        return null;

    }
}
