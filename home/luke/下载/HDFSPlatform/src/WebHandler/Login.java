package WebHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.*;
import java.util.*;

import FSOpt.FileOperation;
import Model.FileInfo;
import Model.AlgInfo;
import Model.OptInfo;

public class Login extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse res){
        doPost(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res){
        String count = req.getParameter("count");
        String key = req.getParameter("key");
        boolean flag = verificateUser(count, key);
        if(flag){
            HttpSession session = req.getSession();
            session.setAttribute("count", count);
            //init
            FileOperation fsOpt = new FileOperation();
            // load file info which belongs to current user
            loadUserFileInfo(count, session, fsOpt);

            ArrayList<OptInfo> optList = new ArrayList<OptInfo>();
            session.setAttribute("optList", optList);
            try{
                res.sendRedirect("FilePage.jsp");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private boolean verificateUser(String count, String key){
        boolean flag = false;
        try{
            String pathName = "/home/luke/usrInfo.txt";
            File fileName = new File(pathName);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName));
            BufferedReader br = new BufferedReader(reader);
            String tempLine = "";
            tempLine = br.readLine();
            while(tempLine!=null){
                String[] temp = tempLine.split("\\|\\|");
                if(temp[0].equals(count)&&temp[1].equals(key)){
                    flag = true;
                    break;
                }
                tempLine = br.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return flag;
    }

    private void loadUserFileInfo(String count, HttpSession session, FileOperation fsOpt){
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
