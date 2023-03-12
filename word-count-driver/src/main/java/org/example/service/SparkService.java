package org.example.service;


import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class SparkService {
    public SparkSession getSparkSession() {
        SparkConf conf = getSparkConfig();
        return SparkSession.builder()
                .config(conf)
                .getOrCreate();
    }

    private SparkConf getSparkConfig() {
        String hostIp;
        try {
            hostIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        SparkConf conf = new SparkConf();
        conf.setAppName("count.words.in.file")
                .setMaster("k8s://https://kubernetes.default.svc:443")
                .setJars(new String[]{"word-count-spark-job.jar"})
//                .setMaster("local")
//                .setJars(new String[]{"/Users/idafna/dev/projects/tutorials/spring-boot-spark-on-minikube/word-count-spark-job/target/word-count-spark-job-1.0-SNAPSHOT.jar"})
                .set("spark.driver.host", hostIp)
                .set("spark.driver.port", "8090")
                .set("spark.kubernetes.namespace", "default")
                .set("spark.kubernetes.container.image", "spark:3.3.2h.1")
                .set("spark.kubernetes.authenticate.executor.serviceAccountName", "spark")
                .set("spark.executor.memory", "1g")
                .set("spark.executor.instances", "2");

        return conf;
    }
}
