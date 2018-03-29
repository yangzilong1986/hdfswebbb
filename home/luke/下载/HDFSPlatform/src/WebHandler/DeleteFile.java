package WebHandler;

import Algorithm.EVENODD;
import Algorithm.LREVENODD;
import Algorithm.Replication;
import FSOpt.FileOperation;
import Model.AlgInfo;
import Model.FileInfo;
import Model.OptInfo;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DeleteFile extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse res){
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res){
        //do delete
        String fileName = req.getParameter("fileName");
        String count = req.getParameter("count");
        ArrayList<FileInfo> fileList = (ArrayList<FileInfo>)req.getSession().getAttribute("fileList");
        ArrayList<OptInfo> optList = (ArrayList<OptInfo>)req.getSession().getAttribute("optList");
        System.out.println("before download fileList "+fileList.size());
        int index = 0;
        int pos = 0;
        String resMessage = "";
        boolean flag = false;
        //search file in hdfs
        try{
            for(FileInfo item:fileList){
                System.out.println("itemfile "+item.getFileName()+" itemuser "+item.getUserName()+" fileName "+fileName+" usr "+count);
                if(item.getFileName().equals(fileName) && item.getUserName().equals(count)){
                    System.out.println("bingo!");
                    AlgInfo algInfo = item.getAlgInfo();
                    System.out.println("realAlginfo "+algInfo.getAlgName()+" "+algInfo.getParamK()+" "+algInfo.getParamN());
                    String algName = algInfo.getAlgName();
                    System.out.println("realAlgName??? "+algName);

                    Date date1 = new Date();
                    long time1=date1.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = df.format(date1);
                    OptInfo optInfo = new OptInfo(fileName, count, "", "delete", time, "", 0);
                    FileOperation fop = new FileOperation();

                    //Predefination
                    Date date2 = null;
                    long time2 = 0;
                    long resTime = 0;
                    String lastTime = null;

                    switch(algName){
                        case "replication":
                            Replication rpl = new Replication(algInfo.getParamK(), item, fop);
                            System.out.println("init blockNum "+rpl.blockNum);
                            rpl.DeleteFile();

                            date2 = new Date();
                            time2=date2.getTime();
                            resTime=Math.abs(time2-time1);
                            lastTime = resTime+"ms";
                            optInfo.setOptLastTime(lastTime);
                            optInfo.setAlgName("replication");
                            optInfo.setOptUsedBytes(rpl.getOptFileSize());
                            optInfo.writeOneOptInfo();
                            optList.add(optInfo);
                            break;
                        case "EVENODD":
                            EVENODD evenodd = new EVENODD(algInfo.getParamK(), item, fop);
                            evenodd.DeleteFile();

                            date2 = new Date();
                            time2 = date2.getTime();
                            resTime = Math.abs(time2 - time1);
                            lastTime = resTime+"ms";
                            optInfo.setOptLastTime(lastTime);
                            optInfo.setAlgName("EVENODD");
                            optInfo.setOptUsedBytes(evenodd.getOptFileSize());
                            optInfo.writeOneOptInfo();
                            optList.add(optInfo);
                            break;

                        case "LREVENODD":
                            LREVENODD lrevenodd = new LREVENODD(algInfo.getParamK(), item, fop);
                            lrevenodd.DeleteFile();

                            date2 = new Date();
                            time2 = date2.getTime();
                            resTime = Math.abs(time2 - time1);
                            lastTime = resTime+"ms";
                            optInfo.setOptLastTime(lastTime);
                            optInfo.setAlgName("LREVENODD");
                            optInfo.setOptUsedBytes(lrevenodd.getOptFileSize());
                            optInfo.writeOneOptInfo();
                            optList.add(optInfo);
                            break;
                    }
                    flag = true;
                    resMessage = "delete success";
                    pos = index;
                }
                index++;
            }
            if(flag){
                fileList.remove(pos);
                deleteFileInfo(count, fileName);
            }else {
                resMessage = "delete failed, target file doesn't exist";
            }
            req.getSession().setAttribute("message", resMessage);
            //refresh fileList
            req.getSession().setAttribute("fileList", fileList);
            //refresh optList
            req.getSession().setAttribute("optList", optList);
            res.sendRedirect("FilePage.jsp");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteFileInfo(String count, String fileName){
        try{
            String fileInfoPath = "/home/luke/fileInfo.txt";
            String fileInfo1Path = "/home/luke/fileInfo1.txt";
            File fileInfo = new File(fileInfoPath);
            File fileInfo1 = new File(fileInfo1Path);

            //do clear fileInfo1
            FileWriter fw0 = new FileWriter(fileInfo1);
            PrintWriter pw0 = new PrintWriter(fw0);
            pw0.print("");
            pw0.flush();
            pw0.close();

            InputStreamReader reader = new InputStreamReader(new FileInputStream(fileInfo));
            BufferedReader br = new BufferedReader(reader);
            String tempLine = "";
            tempLine = br.readLine();
            while(tempLine!=null){
                String[] temp = tempLine.split("\\|\\|");
                System.out.println("count "+count+" temp0 "+temp[0]);
                if(temp[0].equals(count)){
                    String[] tempOne = temp[1].split("\\|");
                    if(!tempOne[4].equals(fileName)){
                        FileWriter fw = new FileWriter(fileInfo1, true);
                        PrintWriter pw = new PrintWriter(fw);
                        pw.println(tempLine);
                        pw.flush();
                        pw.close();
                    }
                }
                tempLine = br.readLine();
            }
            //refresh
            InputStreamReader reader1 = new InputStreamReader(new FileInputStream(fileInfo1));
            BufferedReader br1 = new BufferedReader(reader1);
            tempLine = br1.readLine();
            //do clear fileInfo
            FileWriter fw01 = new FileWriter(fileInfo);
            PrintWriter pw01 = new PrintWriter(fw01);
            pw01.print("");
            pw01.flush();
            pw01.close();
            //do copy
            while(tempLine!=null){
                FileWriter fw = new FileWriter(fileInfo, true);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(tempLine);
                pw.flush();
                pw.close();
                tempLine = br.readLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
