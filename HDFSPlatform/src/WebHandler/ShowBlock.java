package WebHandler;

import Algorithm.*;
import FSOpt.FileOperation;
import Model.AlgInfo;
import Model.FileInfo;
import Model.OptInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import java.util.List;

public class ShowBlock extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        String fileName = req.getParameter("fileName");
        String count = req.getParameter("count");
        String recoverFlage = req.getParameter("recoverFlage");
        String de_blockName = req.getParameter("de_blockName");
        ArrayList<FileInfo> fileList = (ArrayList<FileInfo>) req.getSession().getAttribute("fileList");

        if(recoverFlage.equals("recover")){
            recoverBlock(count,fileName,fileList,req);
        }

        FileInfo targetFile = null;
        for (FileInfo item : fileList) {
            System.out.println("itemfile " + item.getFileName() + " itemuser " + item.getUserName() + " fileName " + fileName + " usr " + count);
            if (item.getFileName().equals(fileName) && item.getUserName().equals(count)) {
                System.out.println("bingo!");
                targetFile = item;
                AlgInfo algInfo = item.getAlgInfo();
                System.out.println("realAlginfo " + algInfo.getAlgName() + " " + algInfo.getParamK() + " " + algInfo.getParamN());
                String algName = algInfo.getAlgName();
                System.out.println("realAlgName??? " + algName);
                FileOperation fop = new FileOperation();
                int len = 0;
                String[] blockName;
                int[] check;
                String tempPath = null;
                List<String> list;
                boolean flag = true;
                switch (algName) {
                    case "replication":
                        Replication rpl = new Replication(algInfo.getParamK(), item, fop);
                        len = rpl.getRealColNum();
                        System.out.println("rpl.getRealColNum():"+rpl.getRealColNum()+"_rpl.getRealRowNum():"+rpl.getRealRowNum());
                        blockName = new String[len];
                        check = new int[len];

                        if (!(de_blockName.equals(""))){
                            try {
                                char[] a = de_blockName.toCharArray();
                                int num = (int)a[a.length-1]-48;

                                System.out.println(de_blockName + " ________________ " +num);

                                for(int k=0;k<rpl.getRealRowNum();k++) {
                                    FileOperation.DeleteFile("/testdir/" + count + "_" + fileName + "_" + k + "_" + num + ".txt");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        for (int m = 0; m < len; m++) {
                            blockName[m] = "Disk " + (m+1) + ": " + fileName + "_" + m;
                            System.out.println(blockName[m]);
                        }
                        for (int i = 0; i < rpl.getRealColNum(); i++) {
                            for (int j = 0; j < rpl.getRealRowNum(); j++) {
                                tempPath = "/testdir/" + count + "_" + fileName + "_" + j + "_" + i + ".txt";
                                try {
                                    if (fop.IsExisted(tempPath) == false) {
                                        flag = false;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (flag == true) {
                                check[i] = 1;
                            } else {
                                check[i] = 0;
                                flag = true;
                            }
                        }
                        try {
                            JSONObject RESULT = new JSONObject();
                            JSONArray jsonArray = new JSONArray();
                            JSONObject jsonObject = new JSONObject();
                            for (int cou = 0; cou < blockName.length; cou++) {
                                jsonObject.put("blockname", blockName[cou]);
                                jsonObject.put("check", check[cou]);
                                jsonArray.add(jsonObject);
                            }
                            RESULT.put("blockInfo", jsonArray);

                            PrintWriter out = res.getWriter();
                            out.print(RESULT);
                            out.flush();
                            out.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "EVENODD":
                        EVENODD evenodd = new EVENODD(algInfo.getParamK(), item, fop);
                        len = evenodd.getRealColNum();
                        System.out.println("rpl.getRealColNum():"+evenodd.getRealColNum()+"_rpl.getRealRowNum():"+evenodd.getRealRowNum());
                        blockName = new String[len];
                        check = new int[len];

                        if (!(de_blockName.equals(""))){
                            try {
                                char[] a = de_blockName.toCharArray();
                                int num = (int)a[a.length-1]-48;

                                System.out.println(de_blockName + " ________________ " +num);

                                for(int k=0;k<evenodd.getRealRowNum();k++) {
                                    FileOperation.DeleteFile("/testdir/" + count + "_" + fileName + "_" + k + "_" + num + ".txt");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        for (int m = 0; m < len; m++) {
                            blockName[m] = "Disk " + (m+1) + ": " + fileName + "_" + m;
                            System.out.println(blockName[m]);
                        }
                        for (int i = 0; i < evenodd.getRealColNum(); i++) {
                            for (int j = 0; j < evenodd.getRealRowNum(); j++) {
                                tempPath = "/testdir/" + count + "_" + fileName + "_" + j + "_" + i + ".txt";
                                try {
                                    if (fop.IsExisted(tempPath) == false) {
                                        flag = false;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (flag == true) {
                                check[i] = 1;
                            } else {
                                check[i] = 0;
                                flag = true;
                            }
                        }
                        try {
                            JSONObject RESULT = new JSONObject();
                            JSONArray jsonArray = new JSONArray();
                            JSONObject jsonObject = new JSONObject();
                            for (int cou = 0; cou < blockName.length; cou++) {
                                jsonObject.put("blockname", blockName[cou]);
                                jsonObject.put("check", check[cou]);
                                jsonArray.add(jsonObject);
                            }
                            RESULT.put("blockInfo", jsonArray);

                            PrintWriter out = res.getWriter();
                            out.print(RESULT);
                            out.flush();
                            out.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "LREVENODD":
                        LREVENODD lrevenodd = new LREVENODD(algInfo.getParamK(), item, fop);
                        len = lrevenodd.getRealColNum();
                        System.out.println("rpl.getRealColNum():"+lrevenodd.getRealColNum()+"_rpl.getRealRowNum():"+lrevenodd.getRealRowNum());
                        blockName = new String[len];
                        check = new int[len];

                        if (!(de_blockName.equals(""))){
                            try {
                                char[] a = de_blockName.toCharArray();
                                int num = (int)a[a.length-1]-48;

                                System.out.println(de_blockName + " ________________ " +num);

                                for(int k=0;k<lrevenodd.getRealRowNum();k++) {
                                    FileOperation.DeleteFile("/testdir/" + count + "_" + fileName + "_" + k + "_" + num + ".txt");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        for (int m = 0; m < len; m++) {
                            blockName[m] = "Disk " + (m+1) + ": " + fileName + "_" + m;
                            System.out.println(blockName[m]);
                        }
                        for (int i = 0; i < lrevenodd.getRealColNum(); i++) {
                            for (int j = 0; j < lrevenodd.getRealRowNum(); j++) {
                                tempPath = "/testdir/" + count + "_" + fileName + "_" + j + "_" + i + ".txt";
                                try {
                                    if (fop.IsExisted(tempPath) == false) {
                                        flag = false;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (flag == true) {
                                check[i] = 1;
                            } else {
                                check[i] = 0;
                                flag = true;
                            }
                        }
                        try {
                            JSONObject RESULT = new JSONObject();
                            JSONArray jsonArray = new JSONArray();
                            JSONObject jsonObject = new JSONObject();
                            for (int cou = 0; cou < blockName.length; cou++) {
                                jsonObject.put("blockname", blockName[cou]);
                                jsonObject.put("check", check[cou]);
                                jsonArray.add(jsonObject);
                            }
                            RESULT.put("blockInfo", jsonArray);

                            PrintWriter out = res.getWriter();
                            out.print(RESULT);
                            out.flush();
                            out.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "RDP":
                        RDP rdp = new RDP(algInfo.getParamK(), item, fop);
                        len = rdp.getRealColNum();
                        System.out.println("rpl.getRealColNum():"+rdp.getRealColNum()+"_rpl.getRealRowNum():"+rdp.getRealRowNum());
                        blockName = new String[len];
                        check = new int[len];

                        if (!(de_blockName.equals(""))){
                            try {
                                char[] a = de_blockName.toCharArray();
                                int num = (int)a[a.length-1]-48;

                                System.out.println(de_blockName + " ________________ " +num);

                                for(int k=0;k<rdp.getRealRowNum();k++) {
                                    FileOperation.DeleteFile("/testdir/" + count + "_" + fileName + "_" + k + "_" + num + ".txt");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        for (int m = 0; m < len; m++) {
                            blockName[m] = "Disk " + (m+1) + ": " + fileName + "_" + m;
                            System.out.println(blockName[m]);
                        }
                        for (int i = 0; i < rdp.getRealColNum(); i++) {
                            for (int j = 0; j < rdp.getRealRowNum(); j++) {
                                tempPath = "/testdir/" + count + "_" + fileName + "_" + j + "_" + i + ".txt";
                                try {
                                    if (fop.IsExisted(tempPath) == false) {
                                        flag = false;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (flag == true) {
                                check[i] = 1;
                            } else {
                                check[i] = 0;
                                flag = true;
                            }
                        }
                        try {
                            JSONObject RESULT = new JSONObject();
                            JSONArray jsonArray = new JSONArray();
                            JSONObject jsonObject = new JSONObject();
                            for (int cou = 0; cou < blockName.length; cou++) {
                                jsonObject.put("blockname", blockName[cou]);
                                jsonObject.put("check", check[cou]);
                                jsonArray.add(jsonObject);
                            }
                            RESULT.put("blockInfo", jsonArray);

                            PrintWriter out = res.getWriter();
                            out.print(RESULT);
                            out.flush();
                            out.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "LRRDP":
                        LRRDP lrrdp = new LRRDP(algInfo.getParamK(), item, fop);
                        len = lrrdp.getRealColNum();
                        System.out.println("rpl.getRealColNum():"+lrrdp.getRealColNum()+"_rpl.getRealRowNum():"+lrrdp.getRealRowNum());
                        blockName = new String[len];
                        check = new int[len];

                        if (!(de_blockName.equals(""))){
                            try {
                                char[] a = de_blockName.toCharArray();
                                int num = (int)a[a.length-1]-48;

                                System.out.println(de_blockName + " ________________ " +num);

                                for(int k=0;k<lrrdp.getRealRowNum();k++) {
                                    FileOperation.DeleteFile("/testdir/" + count + "_" + fileName + "_" + k + "_" + num + ".txt");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        for (int m = 0; m < len; m++) {
                            blockName[m] = "Disk " + (m+1) + ": " + fileName + "_" + m;
                            System.out.println(blockName[m]);
                        }
                        for (int i = 0; i < lrrdp.getRealColNum(); i++) {
                            for (int j = 0; j < lrrdp.getRealRowNum(); j++) {
                                tempPath = "/testdir/" + count + "_" + fileName + "_" + j + "_" + i + ".txt";
                                try {
                                    if (fop.IsExisted(tempPath) == false) {
                                        flag = false;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (flag == true) {
                                check[i] = 1;
                            } else {
                                check[i] = 0;
                                flag = true;
                            }
                        }
                        try {
                            JSONObject RESULT = new JSONObject();
                            JSONArray jsonArray = new JSONArray();
                            JSONObject jsonObject = new JSONObject();
                            for (int cou = 0; cou < blockName.length; cou++) {
                                jsonObject.put("blockname", blockName[cou]);
                                jsonObject.put("check", check[cou]);
                                jsonArray.add(jsonObject);
                            }
                            RESULT.put("blockInfo", jsonArray);

                            PrintWriter out = res.getWriter();
                            out.print(RESULT);
                            out.flush();
                            out.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    }

    public void recoverBlock(String count,String file,ArrayList<FileInfo> fileList,HttpServletRequest request){
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
        System.out.println(Method);
        switch (Method) {
            case "replication":
                Replication rpl = new Replication(alginfo.getParamK(), fileinfo, new FileOperation());
                try {
                    Boolean rpl_b=false;
                    rpl_b=rpl.decoding();
                    System.out.println(rpl_b);
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
    }
}
