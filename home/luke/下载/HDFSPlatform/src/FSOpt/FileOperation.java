package FSOpt;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DistributedFileSystem;
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
            hdfs = FileSystem.get(URI.create("hdfs://node0:9000"), conf);
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

    public static void CreateAndWriteToFile(String filePath, byte[] buffer) throws Exception{
        CreateAndWriteToFile(new Path(filePath), buffer);
    }

    public static void CreateAndWriteToFile(Path filePath, byte[] buffer) throws Exception{
        if(hdfs.exists(filePath)){
            System.out.println("File Existed!");
        }
        else{
            FSDataOutputStream ops = hdfs.create(filePath);
            ops.write(buffer, 0, buffer.length);
            ops.close();
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

    public static void DeleteFile(String path) throws Exception{
        DeleteFile(new Path(path));
    }

    public static void DeleteFile(Path path) throws Exception{
        if(!hdfs.exists(path)){
            System.out.println("Target file doesn't existed!");
        }
        else{
            hdfs.delete(path, false);
        }
    }
}
