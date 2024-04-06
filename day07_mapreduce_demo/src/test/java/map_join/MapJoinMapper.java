package map_join;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;

public class MapJoinMapper  extends Mapper<LongWritable,Text,Text,Text> {
    HashMap<String,String> b_tab = new HashMap<String, String>();
    String line = null;
    /*
    map端的初始化方法当中获取我们的缓存文件，一次性加载到map当中来
     */
    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        //这种方式获取所有的缓存文件
        URI[] cacheFiles = context.getCacheFiles();

        //FileSystem fileSystem = FileSystem.get(cacheFiles[0], context.getConfiguration());
        FileSystem fileSystem1 = FileSystem.get(cacheFiles[0], context.getConfiguration());

        //FSDataInputStream open = fileSystem.open(new Path(cacheFiles[0]));
        FSDataInputStream open1 = fileSystem1.open(new Path(cacheFiles[0]));

        //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(open));
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(open1));


        while((line = bufferedReader1.readLine()) != null){
            String[] split = line.split(",");
            b_tab.put(split[0], line);
        }
        //while ((line = bufferedReader.readLine())!=null){
        //    String[] split = line.split(",");
        //    b_tab.put(split[0],split[1]+"\t"+split[2]+"\t"+split[3]);
        //}
        fileSystem1.close();

        IOUtils.closeStream(bufferedReader1);
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //这里读的是这个map task所负责的那一个切片数据（在hdfs上）
        String[] fields = value.toString().split(",");

        String pdId = fields[2];

        //获取map当中的商品详细信息
        String productInfo = b_tab.get(pdId);
        context.write(new Text(pdId), new Text(productInfo+"\t"+value));
    }
}