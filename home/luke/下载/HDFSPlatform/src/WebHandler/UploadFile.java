package WebHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Algorithm.Replication;
import Algorithm.EVENODD;
import Algorithm.LREVENODD;

import FSOpt.FileOperation;
import Model.AlgInfo;
import Model.FileInfo;
import Model.OptInfo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadFile extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{

//        String count = req.getParameter("count");
//        System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSS "+req.getParameter("count"));
//        int algorithm = Integer.parseInt(req.getParameter("algorithm"));
//        int paramK = Integer.parseInt(req.getParameter("paramK"));
//        int paramN = Integer.parseInt(req.getParameter("paramN"));
//        ArrayList<FileInfo> fileList = (ArrayList<FileInfo>)req.getSession().getAttribute("fileList");



        //get file
        if(!ServletFileUpload.isMultipartContent(req)){
            PrintWriter writer = res.getWriter();
            writer.println("Error");
            writer.flush();
            return;
        }

        //upload to master first
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024*1024*3);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(1024*1024*40);
        upload.setSizeMax(1024*1024*50);
        upload.setHeaderEncoding("UTF-8");

        try{
            List<FileItem> formItems = upload.parseRequest(req);

            String count = formItems.get(0).getString();
            System.out.println("formItem: "+formItems.get(1).getString()+" "+formItems.get(2).getString()+" "+formItems.get(3).getString());
            int algorithm = Integer.parseInt(formItems.get(1).getString());
            int paramK,paramN ;
            ArrayList<FileInfo> fileList = (ArrayList<FileInfo>)req.getSession().getAttribute("fileList");
            ArrayList<OptInfo> optList = (ArrayList<OptInfo>)req.getSession().getAttribute("optList");
            String resMessage = "";


            String uploadPath = "/home/luke/upload";
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            System.out.println("ready to upload");

            if(formItems != null && formItems.size() > 0){
                if(formItems.size()>5){
                    resMessage = "upload failed, too many files";
                }
                else {
                    FileItem realFile = formItems.get(4);
                    realFile.getFieldName();
                    if(realFile != null && realFile.getName()!="") {
                        System.out.println(realFile.getString());
                        if (!realFile.isFormField()) {
                            File tmpFile = new File(realFile.getName());
                            String fileName = tmpFile.getName();
                            String filePath = uploadPath + "/" + fileName;
                            File storeFile = new File(filePath);
                            System.out.println("path? " + filePath);
                            realFile.write(storeFile);
                            String algorithmName = "";

                            //Predefination
                            FileOperation fop = new FileOperation();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date1 = new Date();
                            String time = df.format(date1);
                            FileInfo fileInfo = null;
                            AlgInfo algInfo = null;
                            Date date2 = null;
                            long time1 = 0;
                            long time2 = 0;
                            long resTime = 0;
                            String lastTime = null;

                            //construct operation info
                            OptInfo optInfo = new OptInfo(fileName, count, "", "upload", time, "", 0);
                            //upload to HDFS
                            switch (algorithm) {
                                //replication
                                case 0:
                                    algorithmName = "replication";
                                    paramK = Integer.parseInt(formItems.get(2).getString());
                                    fileInfo = new FileInfo(filePath, count, time, algInfo);
                                    System.out.println("fileinfo? " + fileInfo);
                                    Replication rpl = new Replication(paramK, fileInfo, fop);
                                    if (rpl.checkParam()) {
                                        System.out.println("BBBBBBBBBBBBBBBlock num " + rpl.blockNum);
                                        algInfo = new AlgInfo(paramK, 0, rpl.zeroNum, algorithmName);
                                        rpl.fileInfo.setAlgInfo(algInfo);
                                        System.out.println("fileinfo?? " + rpl.fileInfo);
                                        rpl.encoding();

                                        date2 = new Date();
                                        time1 = date1.getTime();
                                        time2 = date2.getTime();
                                        resTime = Math.abs(time2 - time1);
                                        lastTime = resTime + "ms";
                                        optInfo.setOptLastTime(lastTime);

                                        algorithmName = "replication";
                                        optInfo.setAlgName(algorithmName);
                                        optInfo.setOptUsedBytes(rpl.getOptFileSize());
                                        addFileInfo(rpl.fileInfo, paramK, 0, algorithmName, rpl.zeroNum);
                                        //add one file info
                                        fileList.add(rpl.fileInfo);
                                        //add one operation info
                                        optInfo.writeOneOptInfo();
                                        optList.add(optInfo);
                                        System.out.println("after upload " + fileList.size());
                                        System.out.println("realAlginfo after download" + rpl.fileInfo.getAlgInfo().getAlgName() + " " + rpl.fileInfo.getAlgInfo().getParamK() + " " + rpl.fileInfo.getAlgInfo().getParamN());
                                        resMessage = "upload success";
                                    } else {
                                        resMessage = "upload failed, please check parameter";
                                    }
                                    break;
                                //EVENODD
                                case 1:
                                    algorithmName = "EVENODD";
                                    paramK = Integer.parseInt(formItems.get(2).getString());
                                    fileInfo = new FileInfo(filePath, count, time, algInfo);
                                    EVENODD evenodd = new EVENODD(paramK, fileInfo, fop);
                                    if (evenodd.checkParam()) {
                                        algInfo = new AlgInfo(paramK, 0, evenodd.zeroNum, algorithmName);
                                        evenodd.fileInfo.setAlgInfo(algInfo);
                                        evenodd.Encoding();

                                        date2 = new Date();
                                        time1 = date1.getTime();
                                        time2 = date2.getTime();
                                        resTime = Math.abs(time2 - time1);
                                        lastTime = resTime + "ms";
                                        optInfo.setOptLastTime(lastTime);

                                        optInfo.setAlgName(algorithmName);
                                        optInfo.setOptUsedBytes(evenodd.getOptFileSize());
                                        addFileInfo(fileInfo, paramK, 0, algorithmName, evenodd.zeroNum);
                                        //add one file info
                                        fileList.add(fileInfo);
                                        //add one operation info
                                        optInfo.writeOneOptInfo();
                                        optList.add(optInfo);
                                        resMessage = "upload success";
                                    } else {
                                        resMessage = "upload failed, please check parameter";
                                    }
                                    break;
                                //LREVENODD
                                case 2:
                                    algorithmName = "LREVENODD";
                                    paramK = Integer.parseInt(formItems.get(2).getString());
                                    fileInfo = new FileInfo(filePath, count, time, algInfo);
                                    LREVENODD lrevenodd = new LREVENODD(paramK, fileInfo, fop);
                                    if (lrevenodd.checkParam()) {
                                        algInfo = new AlgInfo(paramK, 0, lrevenodd.zeroNum, algorithmName);
                                        lrevenodd.fileInfo.setAlgInfo(algInfo);
                                        lrevenodd.Encoding();

                                        date2 = new Date();
                                        time1 = date1.getTime();
                                        time2 = date2.getTime();
                                        resTime = Math.abs(time2 - time1);
                                        lastTime = resTime + "ms";
                                        optInfo.setOptLastTime(lastTime);

                                        optInfo.setAlgName(algorithmName);
                                        optInfo.setOptUsedBytes(lrevenodd.getOptFileSize());
                                        addFileInfo(fileInfo, paramK, 0, algorithmName, lrevenodd.zeroNum);
                                        //add one file info
                                        fileList.add(fileInfo);
                                        //add one operation info
                                        optInfo.writeOneOptInfo();
                                        optList.add(optInfo);
                                        resMessage = "upload success";
                                    } else {
                                        resMessage = "upload failed, please check parameter";
                                    }
                                    break;
                            }
                        }
                    }
                    else {
                        resMessage = "upload failed, invalid file item";
                    }
                }
            }
            else {
                resMessage = "upload failed, invalid file item";
            }
            req.getSession().setAttribute("message", resMessage);
            //refresh fileList
            req.getSession().setAttribute("fileList", fileList);
            //refresh optList
            req.getSession().setAttribute("optList", optList);
            res.sendRedirect("FilePage.jsp");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addFileInfo(FileInfo fileInfo, int paramK, int paramN, String algorithmName, int zeroNum){
        try{
            //write to file
            File f = new File("/home/luke/fileInfo.txt");
            FileWriter fw = new FileWriter(f, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(fileInfo.getUserName()+"||"+paramK+"|"+paramN+"|"+algorithmName+"|"+zeroNum+"|"+fileInfo.getFileName()+"|"+fileInfo.getFileSize()+"|"+fileInfo.getFileType()+"|"+fileInfo.getUploadTime());
            pw.flush();
            pw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
