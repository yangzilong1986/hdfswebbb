package WebHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.*;

public class Register extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse res){
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res){
        String count = req.getParameter("count");
        String key1 = req.getParameter("key1");
        String key2 = req.getParameter("key2");
        try{
            if(key1.equals(key2)){
                register(count, key1);
                res.sendRedirect("LoginPage.jsp");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void register(String count, String key){
        try{
            File f = new File("/home/luke/usrInfo.txt");
            FileWriter fw = new FileWriter(f, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(count+"||"+key);
            pw.flush();
            pw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
