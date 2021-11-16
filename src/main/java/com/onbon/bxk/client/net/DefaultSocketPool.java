package com.onbon.bxk.client.net;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.net.Socket;
import java.net.SocketException;
import java.time.Duration;

/**
 * @author huangjie
 * @description
 * @date 2021/11/12
 */
public class DefaultSocketPool implements SocketPool {

    private Integer maxTotal;   //连接池里面的最大连接数量,默认10个
    private Long minEvictableIdleTimeMillis;    //最小空闲等待时间，单位：毫秒，默认30分钟
    private Integer maxIdle;    //最大空闲数量，默认5个
    private Long timeBetweenEvictionRunsMillis; //空闲连接检测的周期（单位毫秒）；如果为负值，表示不运行检测线程；默认30分钟
    private Integer minIdle;    //最小空闲连接数量,默认1个
    private ObjectPool<Socket> objectPool;
    private String host;
    private Integer port;

    public DefaultSocketPool(Integer maxTotal, Long minEvictableIdleTimeMillis, Integer maxIdle, Long timeBetweenEvictionRunsMillis, Integer minIdle,String host,Integer port){
        this.maxTotal=maxTotal;
        this.minEvictableIdleTimeMillis=minEvictableIdleTimeMillis;
        this.maxIdle=maxIdle;
        this.timeBetweenEvictionRunsMillis=timeBetweenEvictionRunsMillis;
        this.minIdle=minIdle;
        this.host = host;
        this.port=port;
    }

    @Override
    public void init() {
        //初始化对象池
        GenericObjectPool genericObjectPool=new GenericObjectPool<Socket>(new SocketFactory(host,port));
        //设置池中最大对象数量
        if(null !=maxTotal){
            genericObjectPool.setMaxTotal(maxTotal);
        }

        //设置最小空闲等待时间(当对象池中空闲对象持续时间达到这个时间时可能会被移除)
        if(null != minEvictableIdleTimeMillis){
            genericObjectPool.setMinEvictableIdleTime(Duration.ofMillis(minEvictableIdleTimeMillis));
        }
        //设置最大空闲数量
        if(null != maxIdle){
           genericObjectPool.setMaxIdle(maxIdle);
        }
        //连接空闲的最小时间，达到此值后空闲链接将会被移除，且保留minIdle个空闲连接数；
        if (null != minEvictableIdleTimeMillis) {
            genericObjectPool.setSoftMinEvictableIdleTime(Duration.ofMillis(minEvictableIdleTimeMillis));
        }

        //空闲连接检测的周期（单位毫秒）；如果为负值，表示不运行检测线程；
        if(null != timeBetweenEvictionRunsMillis){
            genericObjectPool.setTimeBetweenEvictionRuns(Duration.ofMillis(timeBetweenEvictionRunsMillis));
        }

        //设置最小空闲链接数量
        if(null != minIdle){
            genericObjectPool.setMinIdle(minIdle);
        }
        ObjectPool<Socket> socketPooled=(ObjectPool<Socket>)genericObjectPool;
        this.objectPool = socketPooled;
    }

    private void check() throws SocketException {
        if(objectPool == null){
            throw new SocketException("socket pool not init please check client is opened");
        }
    }

    @Override
    public Socket borrowSocket() throws Exception {
        check();
        return objectPool.borrowObject();
    }

    @Override
    public void returnSocket(Socket socket) throws Exception {
        check();
        objectPool.returnObject(socket);
    }

    @Override
    public void clear() throws Exception {
        check();
        objectPool.clear();
    }

    @Override
    public void close() throws SocketException {
        check();
        objectPool.close();
    }
}
