package Model;

import java.io.File;

public class FileInfo {
    private String fileName;
    private String filePath;
    private long fileSize;
    private String fileType;
    private String userName;
    private String uploadTime;
    private AlgInfo algInfo;

    public FileInfo(String filePath, String userName, String uploadTime, AlgInfo algInfo){
        this.filePath = filePath;
        File f = new File(filePath);
        fileName = f.getName();
        fileSize = f.length();
        if(fileName.contains(".")){
            fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        } else {
          fileType = "";
        }


        this.userName = userName;
        this.uploadTime = uploadTime;
        this.algInfo = algInfo;
    }

    public FileInfo(String fileName, long fileSize, String fileType, String userName, String uploadTime, AlgInfo algInfo){
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.userName = userName;
        this.uploadTime = uploadTime;
        this.algInfo = algInfo;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize(){
        return fileSize;
    }

    public String getFileType(){
        return fileType;
    }

    public String getUserName() {
        return userName;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public AlgInfo getAlgInfo() {
        return algInfo;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) { this.filePath = filePath; }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setFileType(String fileType){
        this.fileType = fileType;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setUploadTime(String uploadTime){
        this.uploadTime = uploadTime;
    }

    public void setAlgInfo(AlgInfo algInfo) {
        this.algInfo = algInfo;
    }
}
