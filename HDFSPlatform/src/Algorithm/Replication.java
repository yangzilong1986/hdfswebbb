package Algorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import FSOpt.FileOperation;
import Model.FileInfo;
//import org.apache.hadoop.fs.shell.CopyCommands;

public class Replication {
    public FileInfo fileInfo;
    public int zeroNum;
    public static int logicBlockSize = 1024;
    public int blockNum;
    public int rplNum;
    private FileOperation fop;
    private int optFileSize;
    private int realColNum = 0;
    private int realRowNum = 0;

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
        this.realColNum = rplNum;
        this.realRowNum = blockNum;
    }

    public int getRealRowNum(){
        return this.realRowNum;
    }

    public int getRealColNum(){
        return this.realColNum;
    }

    public Boolean encoding(){
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
                   if( !WriteToFile(i,j,tempFile))
                       return false;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private Boolean WriteToFile(int i, int j, byte[] fileContent) throws Exception{
        String targetPath = "/testdir/"+fileInfo.getUserName()+"_"+fileInfo.getFileName()+"_"+j+"_"+i+".txt";
        addOptFileSize(fileContent.length);
       return fop.CreateAndWriteToFile(targetPath, fileContent);
    }

    private byte[] ReadFromFile(int i, int j) throws Exception{
        String targetPath = "/testdir/"+this.fileInfo.getUserName()+"_"+this.fileInfo.getFileName()+"_"+j+"_"+i+".txt";
        return ReadFromFile(targetPath);
    }

    private byte[] ReadFromFile(String tempPath) throws Exception{
        addOptFileSize(logicBlockSize);
        return fop.ReadFromFile(tempPath, logicBlockSize);
    }

    public boolean RebuildFile() throws Exception{
        //reset
        setOptFileSize(0);
        String targetPath = "/home/luke/download/"+this.fileInfo.getUserName()+"_"+this.fileInfo.getFileName()+this.fileInfo.getFileType();
        File resFile = new File(targetPath);
        FileOutputStream out = new FileOutputStream(resFile);
        System.out.println("block num"+blockNum);
        for(int j=0; j<blockNum; j++){
            int num=0;
            String tempPath = "/testdir/"+fileInfo.getUserName()+"_"+fileInfo.getFileName()+"_"+j+"_"+num+".txt";
            boolean tempFlag = false;
            //check replicate file existed
            for(int i=0; i<rplNum; i++){
                if(!fop.IsExisted(tempPath)){
                    tempPath = "/testdir/"+fileInfo.getUserName()+"_"+fileInfo.getFileName()+"_"+j+"_"+i+".txt";
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

    public boolean decoding() throws Exception{
        boolean res = false;
        int[] errPos = GetErrorPos();
        if(errPos.length<=0 || errPos.length>rplNum){
            System.out.println("Invalid errPos");
            return res;
        }
        else{
            // find the good replication
            int target = 0;
            for(int i=0; i<rplNum; i++){
                int j=0;
                for(; j<errPos.length; j++){
                    if(errPos[j]==i)
                        break;
                }
                if(j==errPos.length){
                    target = i;
                    break;
                }
            }
            System.out.println("??????? "+errPos[0]+" "+target);
            //reset
            setOptFileSize(0);
            // restore
            for(int i=0; i<errPos.length; i++){
                for(int j=0; j<rplNum; j++){
                    byte[] tempFile = ReadFromFile(target, j);
                    WriteToFile(errPos[i], j, tempFile);
                }
            }
            res = true;
            return res;
        }
    }

    public Boolean DeleteFile() throws Exception{
        //delete doesn't need file transition
        setOptFileSize(0);
        Boolean flag=true;
        //do real delete
        for(int i=0; i<rplNum; i++){
            for(int j=0; j<blockNum; j++){
                String tempPath = "/testdir/"+this.fileInfo.getUserName()+"_"+this.fileInfo.getFileName()+"_"+j+"_"+i+".txt";
                if(!fop.DeleteFile(tempPath))
                    flag=false;
            }
        }
        return  flag;
    }

    public int[] GetErrorPos() throws Exception{
        int[] errPos = new int[0];
        for(int i=0; i<rplNum; i++){
            boolean colFlag = true;
            for(int j=0; j<blockNum; j++){
                String tempPath = "/testdir/"+this.fileInfo.getUserName()+"_"+this.fileInfo.getFileName()+"_"+j+"_"+i+".txt";
                boolean flag = fop.IsExisted(tempPath);
                // once one block file lost, the entire column doesn't need check but set to be lost
                if(!flag && colFlag){
                    colFlag = false;
                    int[] tempErrPos = new int[errPos.length+1];
                    System.arraycopy(errPos, 0, tempErrPos, 0, errPos.length);
                    tempErrPos[tempErrPos.length-1]=i;
                    errPos = tempErrPos;
                }
            }
        }
        return errPos;
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
