package cn.itcast.demo2.myoutputformat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class JobMain extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        //1:获取job对象
        Job job = Job.getInstance(super.getConf(), "myoutputformat_job");

        //2:设置job任务
            //第一步:设置输入类和输入的路径
            job.setInputFormatClass(TextInputFormat.class);
            TextInputFormat.addInputPath(job, new Path("file:///D:\\input\\myoutputformat_input"));

            //第二步:设置Mapper类和数据类型
            job.setMapperClass(MyOutputFormatMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(NullWritable.class);

            //第八步:设置输出类和输出的路径
            job.setOutputFormatClass(MyOutputFormat.class);
            MyOutputFormat.setOutputPath(job, new Path("file:///D:\\out\\myoutputformat_out"));


        //3:等待任务结束
        boolean bl = job.waitForCompletion(true);
        return bl ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        int run = ToolRunner.run(configuration, new JobMain(), args);
        System.exit(run);
    }
}
