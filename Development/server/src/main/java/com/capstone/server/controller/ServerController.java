package com.capstone.server.controller;

import com.capstone.server.data.dto.RouteDto;
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

    private final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);
    private final RoadwayService roadwayService;

    @Autowired
    public ServerController(RoadwayService roadwayService) {

        this.roadwayService = roadwayService;

        System.out.print("Hi! ServerController is created...\n" + "need uploading? (y/n): ");
        String isRequireUploading = new Scanner(System.in).next();

        if(isRequireUploading.equals("y")) {
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

    @GetMapping("/test")
    public String test() {
        String msg = roadwayService.test();

        return msg;
    }

    @GetMapping("checkscore")
    public float getScore(@RequestBody RouteDto routeDto) {
        float sum = 0;
        for(String roadAddress: routeDto.getRoadAddressList()) {
            float score = roadwayService.checkScore(roadAddress);
            sum += score;
        }

        return sum/(routeDto.getRoadAddressList().size());
    }

    @PostMapping("setscore")
    public float setScore(@RequestBody String roadAddress, @RequestBody float score) {
        return roadwayService.setScore(roadAddress, score);
    }

    @GetMapping("detect")
    public String detect( @RequestBody String encodedBitmap ) {
        System.out.println(encodedBitmap);
        byte[] imageData = javax.xml.bind.DatatypeConverter.parseBase64Binary(encodedBitmap);
        String imageUrl = "../detector_dir/images/image.jpg";

        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(imageData);
            BufferedImage img = ImageIO.read(bi);
            bi.close();

            File outputFile = new File(imageUrl);
            ImageIO.write(img, "jpg", outputFile);

            //Detector detector = new Detector();
            //String detectResult = detector.Run();

            return "hello";
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "It's empty";
    }


    // To upload excel data
    @PostMapping("/")
    public void uploadRoadwayData(Roadway roadway) {
        roadwayService.saveRoadway(roadway);
    }
}
