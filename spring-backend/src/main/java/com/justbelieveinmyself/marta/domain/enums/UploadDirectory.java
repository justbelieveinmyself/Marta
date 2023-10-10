package com.justbelieveinmyself.marta.domain.enums;

public enum UploadDirectory {
    PRODUCTS("products"), AVATARS("avatars");
    private final String path;
    UploadDirectory(String path){
        this.path = path;
    }
    public String getPath(){
        return this.path;
    }
}
