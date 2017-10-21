package com.wangsheng.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by wangsheng on 2017/10/20.
 */
public class WordCountApp {

    public static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        LongWritable one = new LongWritable(1);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //接收到的每一行数据
            String line = value.toString();
            //按照指定的分隔符进行拆分
            String[] words = line.split(" ");

            for (String word : words) {
                //通过上下文把map的处理结果输出
                context.write(new Text(word), one);
            }
        }
    }

    public static class MyReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable value : values) {
                sum += value.get();
            }
            //最终统计结果的输出
            context.write(key, new LongWritable(sum));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //创建Configuration
        Configuration configuration = new Configuration();

        //准备清理已存在的输出目录
        Path outputPath = new Path(args[1]);
        FileSystem fileSystem = FileSystem.get(configuration);
        if (fileSystem.exists(outputPath)) {
            fileSystem.delete(outputPath);
        }

        //创建Job
        Job job = Job.getInstance(configuration, "wordcount");

        //设置Job的处理类
        job.setJarByClass(WordCountApp.class);



        //设置作业处理的输入路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));

        //设置map相关参数
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //设置reduce相关参数
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //设置作业输出目录
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //提交作业
        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}



























