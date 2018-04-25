package FSOpt;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.zookeeper.Op;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class FileOperation {
    private static Configuration conf;
    private static FileSystem hdfs;

    public FileOperation(){
        try{
            conf = new Configuration();
            conf.set("fs.hdfs.impl",DistributedFileSystem.class.getName());
            hdfs = FileSystem.get(URI.create("hdfs://master:9000"), conf);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean IsExisted(String path) throws Exception{
        return IsExisted(new Path(path));
    }

    public boolean IsExisted(Path path) throws Exception{
        return hdfs.exists(path);
    }

    public static Boolean CreateAndWriteToFile(String filePath, byte[] buffer) throws Exception{
        return  CreateAndWriteToFile(new Path(filePath), buffer);
    }

    public static Boolean CreateAndWriteToFile(Path filePath, byte[] buffer) throws Exception{
        if(hdfs.exists(filePath)){
            System.out.println("File Existed!");
            return  false;
        }
        else{
            FSDataOutputStream ops = hdfs.create(filePath);
            ops.write(buffer, 0, buffer.length);
            ops.close();
            return true;
        }
    }

    public static byte[] ReadFromFile(String filePath, int length) throws Exception{
        return ReadFromFile(new Path(filePath), length);
    }

    public static byte[] ReadFromFile(Path filePath, int length) throws Exception{
        InputStream in = hdfs.open(filePath);
        byte[] resContent = new byte[length];
        in.read(resContent);
        return resContent;
    }

    public static Boolean DeleteFile(String path) throws Exception{
        return DeleteFile(new Path(path));
    }

    public static Boolean DeleteFile(Path path) throws Exception{
        if(!hdfs.exists(path)){
            System.out.println("Do delete failed! Target file "+path+" doesn't existed!");
            return false;
        }
        else{
            return  hdfs.delete(path, false);
        }
    }

    public static String[][] getSlaveInfo() throws Exception {

        DistributedFileSystem xxx = (DistributedFileSystem)hdfs;

        DatanodeInfo[] dataNodeStats = xxx.getDataNodeStats();

        String[][] a = new String[dataNodeStats.length][3];

        for(int i=0;i<dataNodeStats.length;i++) {

            a[i][0] = dataNodeStats[i].getHostName();
            a[i][1] = dataNodeStats[i].getIpAddr();
            String b = String.valueOf(dataNodeStats[i].getNonDfsUsed()*100/dataNodeStats[i].getCapacity());
            char[] c = b.toCharArray();
            a[i][2] = String.valueOf(c[0]) + String.valueOf(c[1]);
        }
        return a;
    }
}
