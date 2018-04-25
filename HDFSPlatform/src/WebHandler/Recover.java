package WebHandler;

import Algorithm.*;
import FSOpt.FileOperation;
import Model.AlgInfo;
import Model.FileInfo;
import Model.OptInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Recover extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String file = request.getParameter("file");
        String count = (String) request.getSession().getAttribute("count");
        ArrayList<FileInfo> fileList = (ArrayList<FileInfo>) request.getSession().getAttribute("fileList");
        FileInfo fileinfo = null;
        for (int i = 0; i < fileList.size(); i++) {
            System.out.println(fileList.get(i).getFileName());
            if (fileList.get(i).getUserName().equals(count) && fileList.get(i).getFileName().equals(file))
                fileinfo = fileList.get(i);
        }
        String Method = fileinfo.getAlgInfo().getAlgName();
        AlgInfo alginfo = fileinfo.getAlgInfo();
        Date date1 = new Date();
        long time1 = date1.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(date1);
        OptInfo optInfo = new OptInfo(fileinfo.getFileName(), count, "", "reCover", time, "", "0ms", 0);

        Date date2 = null;
        long time2 = 0;
        long resTime = 0;
        String lastTime = null;
        switch (Method) {
            case "replication":
                Replication rpl = new Replication(alginfo.getParamK(), fileinfo, new FileOperation());
                try {
                    Boolean rpl_b=false;
                    rpl_b=rpl.decoding();
                    if (rpl_b) {
                        request.setAttribute("ccheck", "1");
                        date2 = new Date();
                        time2 = date2.getTime();
                        resTime = Math.abs(time2 - time1);
                        lastTime = resTime + "ms";
                        optInfo.setOptLastTime(lastTime);
                        optInfo.setAlgName("replication");
                        optInfo.setOptUsedBytes(rpl.getOptFileSize());
                    } else
                        request.setAttribute("ccheck", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "EVENODD":
                EVENODD evenodd = new EVENODD(fileinfo.getAlgInfo().getParamK(), fileinfo, new FileOperation());
                boolean eve_b = false;
                try {
                    int n[] = evenodd.GetErrorPos();
                    eve_b=evenodd.Decoding(n);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (eve_b) {
                    //request.setAttribute("ccheck", "1");
                    date2 = new Date();
                    time2 = date2.getTime();
                    resTime = Math.abs(time2 - time1);
                    lastTime = resTime + "ms";
                    optInfo.setOptLastTime(lastTime);
                    optInfo.setAlgName("EVENODD");
                    optInfo.setOptUsedBytes(evenodd.getOptFileSize());
                }// else
                   // request.setAttribute("ccheck", null);
                break;
            case "LREVENODD":
                LREVENODD lre = new LREVENODD(fileinfo.getAlgInfo().getParamK(), fileinfo, new FileOperation());
                boolean lre_b = false;
                try {
                    int n[] = lre.GetErrorPos();
                    lre_b=lre.Decoding(n);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (lre_b){
                    //request.setAttribute("ccheck", "1");
                    date2 = new Date();
                    time2=date2.getTime();
                    resTime=Math.abs(time2-time1);
                    lastTime = resTime+"ms";
                    optInfo.setOptLastTime(lastTime);
                    optInfo.setAlgName("LREVENOOD");
                    optInfo.setOptUsedBytes(lre.getOptFileSize());
                }
                //else
                  //  request.setAttribute("ccheck", null);
                break;
            case "LRRDP":
                LRRDP lrrdr = new LRRDP(fileinfo.getAlgInfo().getParamK(), fileinfo, new FileOperation());
                boolean lrrdr_b = false;
                try {
                    int n[] = lrrdr.GetErrorPos();
                    lrrdr_b=lrrdr.Decoding(n);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (lrrdr_b){
                   // request.setAttribute("ccheck", "1");
                    date2 = new Date();
                    time2=date2.getTime();
                    resTime=Math.abs(time2-time1);
                    lastTime = resTime+"ms";
                    optInfo.setOptLastTime(lastTime);
                    optInfo.setAlgName("LRRDR");
                    optInfo.setOptUsedBytes(lrrdr.getOptFileSize());
                }
                /*else
                    request.setAttribute("ccheck", null);*/

                break;
            case "RDP":
                RDP rdp = new RDP(fileinfo.getAlgInfo().getParamK(), fileinfo, new FileOperation());
                boolean rdp_b = false;
                try {
                    int n[] = rdp.GetErrorPos();
                    rdp_b=rdp.Decoding(n);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (rdp_b){
                    //request.setAttribute("ccheck", "1");
                    date2 = new Date();
                    time2=date2.getTime();
                    resTime=Math.abs(time2-time1);
                    lastTime = resTime+"ms";
                    optInfo.setOptLastTime(lastTime);
                    optInfo.setAlgName("RDP");
                    optInfo.setOptUsedBytes(rdp.getOptFileSize());
                }
               /* else
                    request.setAttribute("ccheck", null);*/

                break;
        }
        ArrayList optList= (ArrayList) request.getSession().getAttribute("optList");
        optList.add(optInfo);
        request.getSession().setAttribute("optList",optList);


        request.setAttribute("fileName",file);
        request.setAttribute("count",count);
        response.sendRedirect("showBlock");
    }
}
