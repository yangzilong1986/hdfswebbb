package WebHandler;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.zookeeper.Op;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Init extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse res){
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res){
        String count = req.getParameter("count");
        int algorithm = Integer.parseInt(req.getParameter("algorithm"));
        int paramK = Integer.parseInt(req.getParameter("paramK"));
        int paramN = Integer.parseInt(req.getParameter("paramN"));
        // load file info which belongs to current user

    }
}
