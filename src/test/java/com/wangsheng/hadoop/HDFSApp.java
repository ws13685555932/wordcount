package com.wangsheng.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.zookeeper.common.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.net.URI;

/**
 * Created by wangsheng on 2017/10/20.
 */
public class HDFSApp {
    public static final String HDFS_PATH = "hdfs://localhost:8020";

    FileSystem fileSystem = null;
    Configuration configuration =null;

    @Test
    public void mkdirs() throws Exception{
        fileSystem.mkdirs(new Path("/hdfsapi/test"));
    }

    @Test
    public void create() throws  Exception{
        FSDataOutputStream output = fileSystem.create(new Path("/hdfsapi/test/a.txt"));
        output.write("hello hadoop".getBytes());
        output.flush();
        output.close();
    }

    @Test
    public void cat() throws Exception{
        FSDataInputStream input = fileSystem.open(new Path("/hdfsapi/test/a.txt"));
        IOUtils.copyBytes(input,System.out,1024);
        input.close();
    }

    @Test
    public void rename() throws Exception{
        Path oldPath = new Path("/hdfsapi/test/a.txt");
        Path newPath = new Path("/hdfsapi/test/b.txt");
        fileSystem.rename(oldPath,newPath);
    }



    @Before
    public void setUp() throws Exception{
        System.out.println("set up");
        configuration = new Configuration();
        fileSystem = FileSystem.get(new URI(HDFS_PATH),configuration);
    }

    @After
    public void tearDown() throws Exception{
        configuration = null;
        fileSystem = null;

        System.out.println();
        System.out.println("tear down");
    }


}
