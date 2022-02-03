package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    //파일 업로드 메소드
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{

        UUID uuid = UUID.randomUUID();                  // UUID => 서로다른 개체들을 구별하기 위해 이름을 부여할 때 사용 (파일명으로)
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;     //UUID로 받은 값 + 원래 파일의 이름의 확장자를 조합하여 저장될 파일 이름을 만든다.
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;        //저장될 위치 + 파일 이름
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);     //파일 출력스트림을 만든다. (바이트 단위)

        fos.write(fileData);        //파일 데이터를 출력스트림에 입력
        fos.close();
        return savedFileName;       //업로드한 파일의 이름을 반환
    }

    //파일 삭제 메소드
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);       //파일에 저장된 경로를 이용하여 파일 객체를 생성

        if (deleteFile.exists()){           //해당 파일이 존재하면 파일을 삭제한다.
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else{
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
