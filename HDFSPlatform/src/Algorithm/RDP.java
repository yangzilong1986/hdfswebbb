package Algorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import FSOpt.FileOperation;
import Model.FileInfo;

/**
 * Created by Administrator on 4/8 0008.
 */
public class RDP {
    public FileInfo fileInfo;
    public int zeroNum;
    public int logicBlockSize;
    public static int pNum;
    private FileOperation fop;
    private int optFileSize;
    private int realRowNum = 0;
    private int realColNum = 0;

    public RDP(int p, FileInfo fileInfo, FileOperation ufop){
        pNum = p;
        this.realColNum = p+1;
        this.realRowNum = p-1;
        this.fileInfo = fileInfo;
        fop = ufop;

        if(this.fileInfo.getFileSize()%((pNum-1)*(pNum-1))==0){
            zeroNum = 0;
        }
        else{
            zeroNum = (int)(((this.fileInfo.getFileSize()/((pNum-1)*(pNum-1)))+1)*((pNum-1)*(pNum-1))-this.fileInfo.getFileSize());
        }
        logicBlockSize = (int)((this.fileInfo.getFileSize()+zeroNum)/((pNum-1)*(pNum-1)));
        System.out.println("Logicblocksize "+logicBlockSize+" "+zeroNum);

    }

    public int getRealRowNum(){
        return this.realRowNum;
    }

    public int getRealColNum(){
        return this.realColNum;
    }

//    public RDP(int p, String path, FileOperation ufop){
//        pNum = p;
//        filePath = path;
//        File f = new File(path);
//        fileName = f.getName();
//        fileSize = f.length();
//        System.out.println("Real up length "+fileSize);
//        fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
//        fileName = fileName.substring(0, fileName.lastIndexOf("."));
//
//        fop = ufop;
//
//        if(fileSize%((pNum-1)*(pNum-1))==0){
//            zeroNum = 0;
//        }
//        else{
//            zeroNum = (int)(((fileSize/((pNum-1)*(pNum-1)))+1)*((pNum-1)*(pNum-1))-fileSize);
//        }
//        logicBlockSize = (int)((fileSize+zeroNum)/((pNum-1)*(pNum-1)));
//    }

    public Boolean Encoding() throws Exception{
        setOptFileSize(0);
        // horizontal redundant
        for(int i=0; i<(pNum-1); i++){
            byte[] tempData = new byte[logicBlockSize];
            for(int j=0; j<(pNum-1); j++){
                byte[] bs = new byte[logicBlockSize];
                // should fixed
                RandomAccessFile r = new RandomAccessFile(this.fileInfo.getFilePath(), "r");
                r.seek((i*(pNum-1)+j)*logicBlockSize);
                if((i==pNum-2) && (j==pNum-2) && (zeroNum!=0)){
                    byte[] tempbs = new byte[logicBlockSize-zeroNum];
                    r.read(tempbs);
                    for(int l=0; l<logicBlockSize; l++){
                        if(l<(logicBlockSize-zeroNum)){
                            bs[l] = tempbs[l];
                        }
                        else{
                            bs[l] = 0;
                        }
                    }
                }
                else{
                    r.read(bs);
                }
                r.close();
                if(!WriteToFile(i, j, bs))
                    return false;
                for(int k=0; k<logicBlockSize; k++){
                    tempData[k] = (byte)(tempData[k]^bs[k]);
                }
            }
            if(!WriteToFile(i, pNum-1, tempData))
                return false;
        }
        int sum = 0;
        // diagonal redundant
        //byte[] sValue = new byte[logicBlockSize];
//        for(int i=0; i<(pNum-1); i++){
//            byte[] bs = new byte[logicBlockSize];
//            // should fixed
//            RandomAccessFile r = new RandomAccessFile(filePath, "r");
//            r.seek(((i*pNum)+(pNum-i-1))*logicBlockSize);
//            r.read(bs);
//            r.close();
//            for(int k=0; k<logicBlockSize; k++){
//                sValue[k] = (byte)(sValue[k]^bs[k]);
//            }
//        }
        for(int loop=0; loop<(pNum-1); loop++){
            byte[] tempData = new byte[logicBlockSize];
            for(int i=0; i<(pNum-1); i++){
                byte[] bs = new byte[logicBlockSize];
                int j = (loop-i+pNum)%pNum;
                // should fixed
                RandomAccessFile r = new RandomAccessFile(this.fileInfo.getFilePath(),"r");
                r.seek((i*pNum+j)*logicBlockSize);
                if(i==(pNum-2) && j==(pNum-2) && zeroNum!=0){
                    byte[] tempbs = new byte[logicBlockSize-zeroNum];
                    r.read(tempbs);
                    for(int l=0; l<logicBlockSize; l++){
                        if(l<(logicBlockSize-zeroNum)){
                            bs[l] = tempbs[l];
                        }
                        else{
                            bs[l] = 0;
                        }
                    }
                }
                else{
                    r.read(bs);
                }
                for(int k=0; k<logicBlockSize; k++){
                    tempData[k] = (byte)(tempData[k]^bs[k]);
                }
            }
//            for(int k=0; k<logicBlockSize; k++){
//                tempData[k] = (byte)(tempData[k]^sValue[k]);
//            }
            if(!WriteToFile(loop, pNum, tempData))
                return false;
        }
        return true;
    }

    public boolean Decoding(int[] errorPos) throws Exception{
        boolean res = false;
        //reset
        setOptFileSize(0);
        if (errorPos.length>2) {
            System.out.println("Too many errors to recover!");
            return res;
        }
        else if(errorPos.length<=0 || (errorPos.length==2 && errorPos[0]>=errorPos[1])){
            System.out.println("Invalid errorPos");
            return res;
        }
        else{
            // single disk failure
            if(errorPos.length == 1){
                System.out.println("Recover from single disk failure.");
                if(errorPos[0]<0 || errorPos[0]>pNum){
                    System.out.println("Invalid errorPos");
                    return res;
                }
                else if(errorPos[0]<pNum-1){
                    System.out.println("Recover from data disk failure");
                    for(int i=0; i<(pNum-1); i++){
                        byte[] temp = new byte[logicBlockSize];
                        for(int j=0; j<pNum; j++){
                            if(j!=errorPos[0]){
                                byte[] nowBlock = ReadFromFile(i, j);
                                for(int k=0; k<logicBlockSize; k++){
                                    temp[k] = (byte)(temp[k]^nowBlock[k]);
                                }
                            }
                        }
                        WriteToFile(i, errorPos[0], temp);
                    }
                }
                else if(errorPos[0] == pNum-1){
                    System.out.println("Recover from horizontal redundant disk failure");
                    for(int i=0; i<(pNum-1); i++){
                        byte[] tempData = new byte[logicBlockSize];
                        for(int j=0; j<(pNum-1); j++){
                            byte[] nowBlock = ReadFromFile(i, j);
                            for(int k=0; k<logicBlockSize; k++){
                                tempData[k] = (byte)(tempData[k]^nowBlock[k]);
                            }
                        }
                        WriteToFile(i, pNum-1, tempData);
                    }
                }
                else{
                    System.out.println("Recover from diagonal redundant disk failure");
                    // calculate S
//                    byte[] sValue = new byte[logicBlockSize];
//                    for(int i=0; i<(pNum-1); i++){
//                        byte[] bs = ReadFromFile(i, pNum-i-1);
//                        for(int k=0; k<logicBlockSize; k++){
//                            sValue[k] = (byte)(sValue[k]^bs[k]);
//                        }
//                    }
                    // calculate diagonal redundant disk
                    for(int loop=0; loop<(pNum-1); loop++){
                        byte[] tempData = new byte[logicBlockSize];
                        for(int i=0; i<(pNum-1); i++){
                            int j = (loop-i+pNum)%pNum;
                            byte[] bs = ReadFromFile(i, j);
                            for(int k=0; k<logicBlockSize; k++){
                                tempData[k] = (byte)(tempData[k]^bs[k]);
                            }
                        }
//                        for(int k=0; k<logicBlockSize; k++){
//                            tempData[k] = (byte)(tempData[k]^sValue[k]);
//                        }
                        WriteToFile(loop, pNum, tempData);
                    }
                }
            }
            // double disk failures
            else{
                // two data disks failed or one data disk and horizontal redundant disk failed
                if(errorPos[0]<pNum-1 && errorPos[1]<pNum){
                    System.out.println("Recover from two data disks failed.");
                    //byte[] sValue = new byte[logicBlockSize];
                    // calculate S
//                    for(int i=0; i<(pNum-1); i++){
//                        byte[] temp1 = ReadFromFile(i, pNum);
//                        byte[] temp2 = ReadFromFile(i, pNum+1);
//                        for(int k=0; k<logicBlockSize; k++){
//                            temp1[k] = (byte)(temp1[k]^temp2[k]);
//                        }
//                        for(int k=0; k<logicBlockSize; k++){
//                            sValue[k] = (byte)(temp1[k]^sValue[k]);
//                        }
//                    }
                    int iniI = errorPos[1]-errorPos[0]-1;
                    int iniJ = errorPos[0];
                    int modNum = (iniI+iniJ)%(pNum-1);
                    for(int loop=0; loop<(pNum-1); loop++){
                        byte[] diaR = ReadFromFile(modNum, pNum+1);
                        byte[] reBlock_dia = new byte[logicBlockSize];
                        for(int k=0; k<logicBlockSize; k++){
                            reBlock_dia[k] = diaR[k];
                        }
                        if(loop == 0){
                            iniI = (iniI+1)%(pNum-1);
                        }
                        for(int loop2=0; loop2<(pNum-1); loop2++){
                            if((modNum-((iniI+loop2)%(pNum-1))+pNum)%pNum==errorPos[0]){

                            }
                            else{
                                byte[] otherBlock = ReadFromFile((iniI+loop2)%(pNum-1), (modNum-((iniI+loop2)%(pNum-1))+pNum)%pNum);
                                for(int k=0; k<logicBlockSize; k++){
                                    reBlock_dia[k] = (byte)(reBlock_dia[k]^otherBlock[k]);
                                }
                            }
                        }
                        WriteToFile(modNum, errorPos[0], reBlock_dia);
                        byte[] reBlock_hor = new byte[logicBlockSize];
                        for(int j=0; j<pNum; j++){
                            if(j!=errorPos[1]){
                                byte[] otherBlock = ReadFromFile(modNum, j);
                                for(int k=0; k<logicBlockSize; k++){
                                    reBlock_hor[k] = (byte)(reBlock_hor[k]^otherBlock[k]);
                                }
                            }
                        }
                        WriteToFile(modNum, errorPos[1], reBlock_hor);
                        iniI = (iniI+(errorPos[1]-errorPos[0]))%(pNum-1);
                        modNum = (modNum+1)%(pNum-1);
                    }
                }
                // one data disk and diagonal redundant disk failed
                else if(errorPos[0]<pNum-1 && errorPos[1]==pNum){
                    System.out.println("Recover from one data disk and diagonal redundant disk failed.");
                    // calculate data disk
                    for(int i=0; i<(pNum-1); i++){
                        byte[] reBlock_hor = new byte[logicBlockSize];
                        for(int j=0; j<pNum; j++){
                            if(j!=errorPos[0]){
                                byte[] otherBlock = ReadFromFile(i, j);
                                for(int k=0; k<logicBlockSize; k++){
                                    reBlock_hor[k] = (byte)(reBlock_hor[k]^otherBlock[k]);
                                }
                            }
                        }
                        WriteToFile(i, errorPos[0], reBlock_hor);
                    }
//                    // calculate S
//                    byte[] sValue = new byte[logicBlockSize];
//                    for(int i=0; i<(pNum-1); i++){
//                        byte[] bs = ReadFromFile(i, pNum-i-1);
//                        for(int k=0; k<logicBlockSize; k++){
//                            sValue[k] = (byte)(sValue[k]^bs[k]);
//                        }
//                    }
                    // calculate redundant
                    for(int loop=0; loop<(pNum-1); loop++){
                        byte[] tempData = new byte[logicBlockSize];
                        for(int i=0; i<(pNum-1); i++){
                            int j = (loop-i+pNum)%pNum;
                            byte[] bs = ReadFromFile(i, j);
                            for(int k=0; k<logicBlockSize; k++){
                                tempData[k] = (byte)(tempData[k]^bs[k]);
                            }
                        }
//                        for(int k=0; k<logicBlockSize; k++){
//                            tempData[k] = (byte)(tempData[k]^sValue[k]);
//                        }
                        WriteToFile(loop, pNum, tempData);
                    }
                }
                // two redundant disks failed
                else{
                    System.out.println("Recover from two redundant disks failed.");
                    // calculate horizontal redundant disk
                    for(int i=0; i<(pNum-1); i++){
                        byte[] tempData = new byte[logicBlockSize];
                        for(int j=0; j<(pNum-1); j++){
                            byte[] bs = ReadFromFile(i, j);
                            for(int k=0; k<logicBlockSize; k++){
                                tempData[k] = (byte)(tempData[k]^bs[k]);
                            }
                        }
                        WriteToFile(i, pNum-1, tempData);
                    }
                    // calculate S
//                    byte[] sValue = new byte[logicBlockSize];
//                    for(int i=0; i<(pNum-1); i++){
//                        byte[] bs = ReadFromFile(i, pNum-i-1);
//                        for(int k=0; k<logicBlockSize; k++){
//                            sValue[k] = (byte)(sValue[k]^bs[k]);
//                        }
//                    }
                    // calculate diagonal redundant disk
                    for(int loop=0; loop<(pNum-1); loop++){
                        byte[] tempData = new byte[logicBlockSize];
                        for(int i=0; i<(pNum-1); i++){
                            int j = (loop-i+pNum)%pNum;
                            byte[] bs = ReadFromFile(i, j);
                            for(int k=0; k<logicBlockSize; k++){
                                tempData[k] = (byte)(tempData[k]^bs[k]);
                            }
                        }
//                        for(int k=0; k<logicBlockSize; k++){
//                            tempData[k] = (byte)(tempData[k]^sValue[k]);
//                        }
                        WriteToFile(loop, pNum, tempData);
                    }
                }
            }
        }
        res = true;
        return res;
    }

    public Boolean WriteToFile(int i, int j, byte[] fileContent) throws Exception{
        String targetPath = "/testdir/"+this.fileInfo.getUserName()+"_"+this.fileInfo.getFileName()+"_"+i+"_"+j+".txt";
        addOptFileSize(fileContent.length);
        return  fop.CreateAndWriteToFile(targetPath, fileContent);
    }

    public byte[] ReadFromFile(int i, int j) throws Exception{
        String targetPath = "/testdir/"+this.fileInfo.getUserName()+"_"+this.fileInfo.getFileName()+"_"+i+"_"+j+".txt";
        addOptFileSize(logicBlockSize);
        return fop.ReadFromFile(targetPath, logicBlockSize);
    }

    // rebuild source file and download to target path with file name
    public void RebuildFile(String targetPath, String fileName) throws Exception{
        //reset
        setOptFileSize(0);
        System.out.println("Rebuild begin!");
        int[] errPos = GetErrorPos();
        // do decode first if some disks failed
        if(errPos.length > 0) {
            Decoding(errPos);
        }
        else{
            // compose real file path
            targetPath = targetPath+"/"+fileName+"."+this.fileInfo.getFileType();
            File resFile = new File(targetPath);
            FileOutputStream out = new FileOutputStream(resFile);
            int sumSize = 0;
            for( int i=0; i<(pNum-1); i++){
                for(int j=0; j<(pNum-1); j++){
                    byte[] tempFile = ReadFromFile(i, j);
                    byte[] realTempFile = null;
                    if(i==pNum-2 && j==pNum-2 && zeroNum != 0){
                        realTempFile = new byte[logicBlockSize - zeroNum];
                        System.arraycopy(tempFile, 0, realTempFile, 0, logicBlockSize - zeroNum -1);
                    } else{
                        realTempFile = tempFile;
                    }
                    System.out.println("One get " + i + " " + j + " " + realTempFile.length);
                    sumSize = sumSize + realTempFile.length;
                    out.write(realTempFile);
                }
            }
            System.out.println("Real recv length " + sumSize);
            out.close();
        }
    }

    // rebuild source file and download to target path
    public boolean RebuildFile(String targetPath) throws Exception{
        //reset
        setOptFileSize(0);
        System.out.println("Rebuild begin!");
        int[] errPos = GetErrorPos();
        // do decode first if some disks failed
        if(errPos.length > 0){
            //Decoding(errPos);
            return false;
        }
        else {
            // compose real file path
            targetPath = targetPath+"/"+this.fileInfo.getUserName()+"_"+this.fileInfo.getFileName()+this.fileInfo.getFileType();
            File resFile = new File(targetPath);
            FileOutputStream out = new FileOutputStream(resFile);
            int sumSize = 0;
            for (int i = 0; i < (pNum - 1); i++) {
                for (int j = 0; j < (pNum-1); j++) {
                    byte[] tempFile = ReadFromFile(i, j);
                    byte[] realTempFile = null;
                    if (i == pNum - 2 && j == pNum - 2 && zeroNum != 0) {
                        System.out.println("real in here???");
                        realTempFile = new byte[logicBlockSize - zeroNum];
                        System.arraycopy(tempFile, 0, realTempFile, 0, logicBlockSize - zeroNum);
                    } else {
                        realTempFile = tempFile;
                    }
                    System.out.println("One get " + i + " " + j + " " + realTempFile.length);
                    sumSize = sumSize + realTempFile.length;
                    out.write(realTempFile);
                }
            }
            System.out.println("Real recv length " + sumSize);
            out.close();
            return true;
        }
    }

    public int[] GetErrorPos() throws Exception{
        int[] errPos = new int[0];
        for(int j=0; j<pNum+1; j++){
            boolean colFlag = true;
            for(int i=0; i<pNum-1; i++){
                String path = "/testdir/"+this.fileInfo.getUserName()+"_"+this.fileInfo.getFileName()+"_"+i+"_"+j+".txt";
                boolean flag = fop.IsExisted(path);
                if (!flag && colFlag){
                    colFlag = false;
                    int[] tempErrPos = new int[errPos.length+1];
                    System.arraycopy(errPos, 0, tempErrPos, 0, errPos.length);
                    tempErrPos[tempErrPos.length-1] = j;
                    errPos = tempErrPos;
                }
            }
        }
        return errPos;
    }

    public Boolean DeleteFile() throws Exception{
        //
        Boolean flag=true;
        setOptFileSize(0);
        for(int i=0; i<(pNum-1); i++){
            for(int j=0; j<(pNum+1); j++){
                String targetPath = "/testdir/"+this.fileInfo.getUserName()+"_"+this.fileInfo.getFileName()+"_"+i+"_"+j+".txt";
                if(!fop.DeleteFile(targetPath))
                    flag=false;
            }
        }
        return flag ;
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
        if((pNum+"").equals("")){
            return false;
        }
        else if(pNum<=0){
            return false;
        }
        else if(!isPrime(pNum)){
            return false;
        }
        else {
            return true;
        }
    }

    private static boolean isPrime(int p){
        boolean flag = true;
        for(int i=2;i<=p/2;i++){
            if(p%i==0){
                flag = false;
                break;
            }
        }
        return flag;
    }
}
