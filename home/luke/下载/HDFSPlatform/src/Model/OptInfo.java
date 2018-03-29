package Model;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class OptInfo {
    private String fileName;
    private String userName;
    private String algName;
    private String optType;
    private String optTime;
    private String optLastTime;
    private int optUsedBytes;

    public OptInfo(String fileName, String userName, String algName, String optType, String optTime, String optLastTime, int optUsedBytes){
        this.fileName = fileName;
        this.userName = userName;
        this.algName = algName;
        this.optType = optType;
        this.optTime = optTime;
        this.optLastTime = optLastTime;
        this.optUsedBytes = optUsedBytes;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setAlgName(String algName){
        this.algName = algName;
    }

    public void setOptType(String optType){
        this.optType = optType;
    }

    public void setOptTime(String optTime){
        this.optTime = optTime;
    }

    public void setOptLastTime(String optLastTime){
        this.optLastTime = optLastTime;
    }

    public void setOptUsedBytes(int optUsedBytes){
        this.optUsedBytes = optUsedBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUserName(){
        return userName;
    }

    public String getAlgName(){
        return algName;
    }

    public String getOptType(){
        return optType;
    }

    public String getOptTime() {
        return optTime;
    }

    public String getOptLastTime() {
        return optLastTime;
    }

    public int getOptUsedBytes(){
        return optUsedBytes;
    }

    public void writeOneOptInfo() throws Exception{
        //write to file
        File f = new File("/home/luke/optInfo.txt");
        FileWriter fw = new FileWriter(f, true);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(userName+"||"+fileName+"|"+algName+"|"+optType+"|"+optTime+"|"+optLastTime+"|"+optUsedBytes);
        pw.flush();
        pw.close();
    }
}
