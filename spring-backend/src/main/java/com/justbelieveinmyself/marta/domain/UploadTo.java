package com.justbelieveinmyself.marta.domain;

public enum UploadTo {
    PRODUCTS("products"), AVATARS("avatars");
    private final String path;
    UploadTo(String path){
        this.path = path;
    }
    public String getPath(){
        return this.path;
    }
}
