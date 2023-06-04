package com.capstone.server.read_excel;

import com.capstone.server.controller.ServerController;
import com.capstone.server.data.entity.Roadway;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class UploadLineData {

    private ServerController serverController;
    private ExcelReadOption ro;

    @Autowired
    public UploadLineData() {
        ro = new ExcelReadOption();
        try {
            ro.setFilePath("C:\\Users\\sa\\Desktop\\server\\server\\src\\main\\java\\source\\Seoul_Line.xlsx");
            //ro.setFilePath("C:/Users/USER/Desktop/SungSengmo/Capstone_Project/Plan/Software/k.xlsx");
            ro.setOutputColumns("A", "B", "C");
            ro.setStartRow(2);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void Upload(ServerController serverController) {


        List<Map<String, String>> result = ExcelRead.read(ro);

        for(Map<String, String> map : result) {
            Long id = (long)Float.parseFloat(map.get("A"));
            String name = map.get("B");
            Integer length = (int)Float.parseFloat(map.get("C"));
            Boolean existence = false;
            Integer score = 0;
            Integer updateNum = 0;

            Roadway newRoadway = new Roadway(
                id, name, length, existence, score, updateNum
            );


            serverController.uploadRoadwayData(newRoadway);
        }

        System.out.println("Upload complete..!");
    }
}
