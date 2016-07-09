package com.app.movein.postad;

public class Config {
    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = "http://192.168.1.6:3000/api/v1/sessions/post/create?auth_token=qP-TAofr4xFiG6QEixZw";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    public static final String CONTACT_NUMBER_FETCHING = "http://192.168.1.6:3000/api/v1/sessions/profile/no?auth_token=qP-TAofr4xFiG6QEixZw&email=rgupta993@gmail.com";
}
