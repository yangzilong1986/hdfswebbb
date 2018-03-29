package Algorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import FSOpt.FileOperation;
import Model.FileInfo;

public class Replication {
    public FileInfo fileInfo;
    public int zeroNum;
    public static int logicBlockSize = 1024;
    public int blockNum;
    public int rplNum;
    private FileOperation fop;
    private int optFileSize;

    public Replication(int rpl, FileInfo fileInfo, FileOperation ufop){
        this.rplNum = rpl;
        this.fileInfo = fileInfo;
        this.fop = ufop;

        System.out.println("realFileInfo-size "+fileInfo.getFileSize());
        if(fileInfo.getFileSize()%(logicBlockSize)==0){
            zeroNum = 0;
            blockNum = (int)(fileInfo.getFileSize()/logicBlockSize);
        }
        else{
            zeroNum = (int)(((fileInfo.getFileSize()/logicBlockSize)+1)*(logicBlockSize)-fileInfo.getFileSize());
            blockNum = (int)(fileInfo.getFileSize()/logicBlockSize)+1;
        }
    }

    public void encoding(){
        //reset
        setOptFileSize(0);
        for(int i=0; i<rplNum; i++) {
            for(int j=0; j<blockNum; j++){
//                byte[] bs = new byte[logicBlockSize];
                try{
                    System.out.println("filepath "+fileInfo.getFilePath());
                    RandomAccessFile r = new RandomAccessFile(fileInfo.getFilePath(), "r");
                    byte[] tempFile = null;
                    r.seek(j*logicBlockSize);
                    if(j==blockNum-1 && zeroNum!=0){
                        tempFile = new byte[logicBlockSize-zeroNum];
                    }
                    else {
                        tempFile = new byte[logicBlockSize];
                    }

                    r.read(tempFile);
                    r.close();
                    WriteToFile(i,j,tempFile);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void WriteToFile(int i, int j, byte[] fileContent) throws Exception{
        String targetPath = "/testdir/"+fileInfo.getUserName()+"_"+fileInfo.getFileName()+"_"+i+"_"+j+".txt";
        addOptFileSize(fileContent.length);
        fop.CreateAndWriteToFile(targetPath, fileContent);
    }

    private byte[] ReadFromFile(String tempPath) throws Exception{
        addOptFileSize(logicBlockSize);
        return fop.ReadFromFile(tempPath, logicBlockSize);
    }

    public boolean decoding() throws Exception{
        //reset
        setOptFileSize(0);
        String targetPath = "/home/luke/download/"+this.fileInfo.getUserName()+"_"+this.fileInfo.getFileName()+this.fileInfo.getFileType();
        File resFile = new File(targetPath);
        FileOutputStream out = new FileOutputStream(resFile);
        System.out.println("block num"+blockNum);
        for(int j=0; j<blockNum; j++){
            int num=0;
            String tempPath = "/testdir/"+fileInfo.getUserName()+"_"+fileInfo.getFileName()+"_"+num+"_"+j+".txt";
            boolean tempFlag = false;
            //check replicate file existed
            for(int i=0; i<rplNum; i++){
                if(!fop.IsExisted(tempPath)){
                    tempPath = "/testdir/"+fileInfo.getUserName()+"_"+fileInfo.getFileName()+"_"+i+"_"+j+".txt";
                }
                else {
                    tempFlag = true;
                }
            }
            //all replicate lost
            if(!tempFlag){
                System.out.println("all lost");
                return false;
            }
            else {
                byte[] tempFile = ReadFromFile(tempPath);

                byte[] realTempFile = null;
                if(j==blockNum-1 && zeroNum!=0){
                    realTempFile = new byte[logicBlockSize-zeroNum];
                    System.arraycopy(tempFile, 0, realTempFile, 0, logicBlockSize-zeroNum);
                }
                else {
                    realTempFile = tempFile;
                }
                System.out.println("writting tempFile "+realTempFile);
                out.write(realTempFile);
            }
        }
        out.close();
        return true;
    }

    public void DeleteFile() throws Exception{
        //delete doesn't need file transition
        setOptFileSize(0);
        //do real delete
        for(int i=0; i<rplNum; i++){
            for(int j=0; j<blockNum; j++){
                String tempPath = "/testdir/"+this.fileInfo.getUserName()+"_"+this.fileInfo.getFileName()+"_"+i+"_"+j+".txt";
                fop.DeleteFile(tempPath);
            }
        }
    }

    public void setOptFileSize(int size){
        this.optFileSize = size;
    }

    public int getOptFileSize(){
        return this.optFileSize;
    }

    public void addOptFileSize(int size){
        this.optFileSize += size;
    }

    public boolean checkParam(){
        if((this.rplNum+"").equals("")){
            return false;
        }
        else if(this.rplNum<=0){
            return false;
        }
        else {
            return true;
        }
    }
}
