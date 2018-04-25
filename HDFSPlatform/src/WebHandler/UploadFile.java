package WebHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Algorithm.*;

import FSOpt.FileOperation;
import Model.AlgInfo;
import Model.FileInfo;
import Model.OptInfo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.*;
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
            // check session
            if(fileList == null){
                InfoOperation.loadUserFileInfo(count, req.getSession(), new FileOperation());
                fileList = (ArrayList<FileInfo>)req.getSession().getAttribute("fileList");
            }
            if(optList == null){
                optList = new ArrayList<OptInfo>();
            }
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
                    if (!this.checkRepeat(count, realFile.getName())) {
                        req.setAttribute("error", "文件存在");
                        req.getRequestDispatcher("FilePage1.jsp").forward(req,res);
                        return;
                    }

                    if(realFile != null && realFile.getName()!="") {
                        System.out.println(realFile.getString());
                        if (!realFile.isFormField()) {
                            String partTime = "";
                            Date pDate1 = new Date();
                            Long pTime1 = pDate1.getTime();

                            File tmpFile = new File(realFile.getName());
                            String fileName = tmpFile.getName();
                            String filePath = uploadPath + "/" + fileName;
                            if(fileName.contains(".")){
                                fileName = fileName.substring(0, fileName.lastIndexOf("."));
                            }
                            File storeFile = new File(filePath);
                            System.out.println("path? " + filePath);
                            realFile.write(storeFile);

                            Date pDate2 = new Date();
                            Long pTime2 = pDate2.getTime();
                            partTime = Math.abs(pTime2-pTime1)+"ms";
                            System.out.println("PPPPPPPPPPPPPP "+partTime);

                            //Predefination
                            String algorithmName = "";
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
                            OptInfo optInfo = new OptInfo(fileName, count, "", "upload", time, "", partTime, 0);
                            //upload to HDFS
                            Boolean b=false;
                            switch (algorithm) {
                                //replication
                                case 0:
                                    System.out.println("do replication");
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
                                        b=rpl.encoding();

                                        if(!b) {
                                            req.setAttribute("error","上传失败");
                                            req.getRequestDispatcher("FilePage1.jsp").forward(req,res);
                                            return;
                                        }

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
                                    System.out.println("do evenodd");
                                    algorithmName = "EVENODD";
                                    paramK = Integer.parseInt(formItems.get(2).getString());
                                    fileInfo = new FileInfo(filePath, count, time, algInfo);
                                    EVENODD evenodd = new EVENODD(paramK, fileInfo, fop);
                                    if (evenodd.checkParam()) {
                                        algInfo = new AlgInfo(paramK, 0, evenodd.zeroNum, algorithmName);
                                        evenodd.fileInfo.setAlgInfo(algInfo);
                                        b=evenodd.Encoding();

                                        if(!b) {
                                            req.setAttribute("error","上传失败");
                                            req.getRequestDispatcher("FilePage1.jsp").forward(req,res);
                                            return;
                                        }
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
                                    System.out.println("do lrevenodd");
                                    algorithmName = "LREVENODD";
                                    paramK = Integer.parseInt(formItems.get(2).getString());
                                    fileInfo = new FileInfo(filePath, count, time, algInfo);
                                    LREVENODD lrevenodd = new LREVENODD(paramK, fileInfo, fop);
                                    if (lrevenodd.checkParam()) {
                                        algInfo = new AlgInfo(paramK, 0, lrevenodd.zeroNum, algorithmName);
                                        lrevenodd.fileInfo.setAlgInfo(algInfo);
                                        b=lrevenodd.Encoding();

                                        if(!b) {
                                            req.setAttribute("error","上传失败");
                                            req.getRequestDispatcher("FilePage1.jsp").forward(req,res);
                                            return;
                                        }
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
                                // RDP
                                case 3:
                                    System.out.println("do rdp");
                                    algorithmName = "RDP";
                                    paramK = Integer.parseInt(formItems.get(2).getString());
                                    fileInfo = new FileInfo(filePath, count, time, algInfo);
                                    RDP rdp = new RDP(paramK, fileInfo, fop);
                                    if (rdp.checkParam()) {
                                        algInfo = new AlgInfo(paramK, 0, rdp.zeroNum, algorithmName);
                                        rdp.fileInfo.setAlgInfo(algInfo);
                                        b=rdp.Encoding();

                                        if(!b) {
                                            req.setAttribute("error","上传失败");
                                            req.getRequestDispatcher("FilePage1.jsp").forward(req,res);
                                            return;
                                        }
                                        date2 = new Date();
                                        time1 = date1.getTime();
                                        time2 = date2.getTime();
                                        resTime = Math.abs(time2 - time1);
                                        lastTime = resTime + "ms";
                                        optInfo.setOptLastTime(lastTime);

                                        optInfo.setAlgName(algorithmName);
                                        optInfo.setOptUsedBytes(rdp.getOptFileSize());
                                        addFileInfo(fileInfo, paramK, 0, algorithmName, rdp.zeroNum);
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
                                // RDP
                                case 4:
                                    System.out.println("do lrrdp");
                                    algorithmName = "LRRDP";
                                    paramK = Integer.parseInt(formItems.get(2).getString());
                                    fileInfo = new FileInfo(filePath, count, time, algInfo);
                                    LRRDP lrrdp = new LRRDP(paramK, fileInfo, fop);
                                    if (lrrdp.checkParam()) {
                                        algInfo = new AlgInfo(paramK, 0, lrrdp.zeroNum, algorithmName);
                                        lrrdp.fileInfo.setAlgInfo(algInfo);
                                        b=lrrdp.Encoding();

                                        if(!b) {
                                            req.setAttribute("error","上传失败");
                                            req.getRequestDispatcher("FilePage1.jsp").forward(req,res);
                                            return;
                                        }
                                        date2 = new Date();
                                        time1 = date1.getTime();
                                        time2 = date2.getTime();
                                        resTime = Math.abs(time2 - time1);
                                        lastTime = resTime + "ms";
                                        optInfo.setOptLastTime(lastTime);

                                        optInfo.setAlgName(algorithmName);
                                        optInfo.setOptUsedBytes(lrrdp.getOptFileSize());
                                        addFileInfo(fileInfo, paramK, 0, algorithmName, lrrdp.zeroNum);
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
                            File del = new File(filePath);
                            del.delete();
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
            res.sendRedirect("FilePage1.jsp");
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
    private boolean checkRepeat(String count, String fileName) {
        System.out.println(fileName+"qqqq");
        if(fileName.indexOf(".")!=-1)
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        String pathName = "/home/luke/fileInfo.txt";
        File file = new File(pathName);
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream("/home/luke/fileInfo.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(reader);
        String tempLine = "";
        try {
            while ((tempLine = br.readLine()) != null) {
                if (tempLine.split("\\|\\|")[0].equals(count) && tempLine.split("\\|\\|")[1].split("\\|")[4].equals(fileName)) {
                    br.close();
                    reader.close();
                    return false;
                }

            }
            br.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
