package com.onbon.bxk.client.net;

/**
 * @author huangjie
 * @description
 * @date 2021/11/12
 */
public class SocketPoolBuilder {

    private Integer maxTotal;   //连接池里面的最大连接数量,默认10个
    private Long minEvictableIdleTimeMillis;    //最小空闲等待时间，单位：毫秒，默认30分钟
    private Integer maxIdle;    //最大空闲数量，默认5个
    private Long timeBetweenEvictionRunsMillis; //空闲连接检测的周期（单位毫秒）；如果为负值，表示不运行检测线程；默认30分钟
    private Integer minIdle;    //最小空闲连接数量,默认1个

    public SocketPoolBuilder(Integer maxTotal, Long minEvictableIdleTimeMillis, Integer maxIdle, Long timeBetweenEvictionRunsMillis, Integer minIdle){
        this.maxTotal=maxTotal;
        this.minEvictableIdleTimeMillis=minEvictableIdleTimeMillis;
        this.maxIdle=maxIdle;
        this.timeBetweenEvictionRunsMillis=timeBetweenEvictionRunsMillis;
        this.minIdle=minIdle;
    }

    public SocketPoolBuilder(){
        this.maxTotal = 10;
        this.minEvictableIdleTimeMillis = 1000L*60*30;
        this.maxIdle = 5;
        this.timeBetweenEvictionRunsMillis = 1000L*60*30;
        this.minIdle = 1;
    }

    public SocketPool build(String host,Integer port){


        return new DefaultSocketPool(maxTotal,minEvictableIdleTimeMillis,maxIdle,timeBetweenEvictionRunsMillis,minIdle,host,port);
    }


}
