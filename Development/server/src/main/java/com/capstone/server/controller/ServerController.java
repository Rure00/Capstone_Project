package com.capstone.server.controller;

import com.capstone.server.data.dto.DetectResult;
import com.capstone.server.data.dto.RouteDto;
import com.capstone.server.data.dto.selfMessage;
import com.capstone.server.data.entity.Roadway;
import com.capstone.server.detector_dir.Detector;
import com.capstone.server.read_excel.UploadLineData;
import com.capstone.server.service.RoadwayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

@RestController
@RequestMapping("/roadway")
public class ServerController {

    private final Boolean isNeedUploading = false;

    private final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);
    private final RoadwayService roadwayService;
    private final String TAG = "ServerController's TAG: ";

    @Autowired
    public ServerController(RoadwayService roadwayService) {

        this.roadwayService = roadwayService;

        System.out.print("Hi! ServerController is created...\n" + "need uploading? (y/n): ");

        if(isNeedUploading) {
            System.out.print("Uploading...");
            UploadLineData uploadLineData = new UploadLineData();
            uploadLineData.Upload(this);
        }
    }

    /*
    필요한 함수
     1) 앱에서 직접 이용할 기능
        - RouteData 받아 점수 매겨 Int형으로 반환
        - 도로 점수를 받아 현 DB에 저장되어 있는 값과 비교 후 저장
     2) 서버에서만 이용할 함수
        - 만약 받은 점수와 DB의 점수와 매우 다를 경우 이를 신고하는 기능
        - 사진도 따로 저장하면 좋을거 같긴 하지만...
     */
    /*
        Step0) 이용할 툴들 먼저 선언하기. ex) Lombok, QueryDSL, JPA_Auditing?
        Step1) 자료형 클래스 정의하고 가져온 뒤 DTO 생성
        Step2) Controller 에서 앱이 이용할 기능 메소드 interface 생성
        Step3) DB에서 필요한 data들 생각하여 repository & dao 작성하기
        Step4) Controller 함수 내부 로직 작성
     */

    @GetMapping("test")
    public selfMessage test() {
        String s = roadwayService.test();
        String t = "test";
        selfMessage msg = new selfMessage(t, s);
        System.out.println(TAG + "test() is called");
        return msg;
    }

    @GetMapping("checkscore")
    public float getScore(@RequestBody RouteDto routeDto) {
        System.out.println(TAG+"getScore() is called");
        float sum = 0;
        for(String roadAddress: routeDto.getRoadAddressList()) {
            float score = roadwayService.checkScore(roadAddress);
            sum += score;
        }

        return sum/(routeDto.getRoadAddressList().size());
    }

    @PostMapping("setscore")
    public float setScore(@RequestBody String roadAddress, @RequestBody float score) {
        System.out.println(TAG+"setScore() is called");

        return roadwayService.setScore(roadAddress, score);
    }

    @GetMapping("detect")
    public DetectResult detect(@RequestParam String encodedBitmap) {
        System.out.println(TAG+"detect() is called");

        byte[] imageData = javax.xml.bind.DatatypeConverter.parseBase64Binary(encodedBitmap);
        String imageUrl = "C:\\Users\\sa\\Desktop\\server\\server\\src\\main\\java\\com\\capstone\\server\\detector_dir\\yolov5\\images\\image.jpg";

        DetectResult detectResult = new DetectResult();

        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(imageData);
            BufferedImage img = ImageIO.read(bi);
            bi.close();

            File outputFile = new File(imageUrl);
            ImageIO.write(img, "jpg", outputFile);

            Detector detector = new Detector();
            String dL = detector.Run();
            detectResult.setResult(dL);

            return detectResult;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    // To upload excel data
    @PostMapping("/")
    public void uploadRoadwayData(Roadway roadway) {
        roadwayService.saveRoadway(roadway);
    }
}
