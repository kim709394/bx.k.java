package com.onbon.bxk.client.net;

import java.net.Socket;
import java.net.SocketException;

/**
 * @author huangjie
 * @description
 * @date 2021/11/12
 */
public interface SocketPool {



    /**
     * 初始化
     * */
    void init();

    /**
     * 从连接池里面拿一个连接
     * */
    Socket borrowSocket() throws Exception;

    /**
     * 将连接还回给连接池
     * */
    void returnSocket(Socket socket) throws Exception;


    /**
     * 清除连接池
     * */
    void clear() throws Exception;

    /**
     * 关闭连接池
     * */
    void close() throws SocketException;



}
