package WebHandler;

import FSOpt.FileOperation;
import Model.AlgInfo;
import Model.FileInfo;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;

public class InfoOperation {

    public static void loadUserFileInfo(String count, HttpSession session, FileOperation fsOpt){
        ArrayList<FileInfo> fileList = new ArrayList<FileInfo>();
        try{
            String pathName = "/home/luke/fileInfo.txt";
            File fileName = new File(pathName);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName));
            BufferedReader br = new BufferedReader(reader);
            String tempLine = "";
            tempLine = br.readLine();
            while(tempLine!=null){
                String[] temp = tempLine.split("\\|\\|");
                System.out.println("count "+count+" temp0 "+temp[0]);
                if(temp[0].equals(count)){
                    String[] tempOne = temp[1].split("\\|");
                    AlgInfo tempAlg = new AlgInfo(Integer.parseInt(tempOne[0]), Integer.parseInt(tempOne[1]), Integer.parseInt(tempOne[3]), tempOne[2]);
                    FileInfo tempFile = new FileInfo(tempOne[4], Long.parseLong(tempOne[5]), tempOne[6], temp[0], tempOne[7], tempAlg);
                    fileList.add(tempFile);
                }
                tempLine = br.readLine();
            }
            session.setAttribute("fileList", fileList);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
