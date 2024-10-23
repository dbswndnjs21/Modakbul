package com.modakbul.utils;

import java.io.File;

public class TransferPath {
    private static final String WINDOWS_BASE_PATH = "C:/modakbul/";
    private static final String MAC_BASE_PATH = "/Users/kimjiwoong/modakbul/";

    public static String getUploadPath( String directoryName){
        if (directoryName == null || directoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("디렉터리 이름이 비어있을 수 없습니다.");
        }

        String os = System.getProperty("os.name").toLowerCase();
        String uploadPath;

        if(os.contains("win")){
            uploadPath = WINDOWS_BASE_PATH + directoryName;
        } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
            uploadPath = MAC_BASE_PATH + directoryName;
        }else {
            throw new UnsupportedOperationException("운영체제 안맞아요");
        }

        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }
        return  uploadPath;
    }
}
