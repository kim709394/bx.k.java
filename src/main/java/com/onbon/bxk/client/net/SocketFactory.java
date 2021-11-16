package com.onbon.bxk.client.net;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.net.Socket;

/**
 * @author huangjie
 * @description socket创建工厂类
 * @date 2021/11/12
 */
public class SocketFactory extends BasePooledObjectFactory<Socket> {


    private String host;
    private Integer port;


    public SocketFactory(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Socket create() throws Exception {
        return new Socket(host,port);
    }

    @Override
    public PooledObject<Socket> wrap(Socket socket) {
        return new DefaultPooledObject<Socket>(socket);
    }

    @Override
    public void passivateObject(PooledObject<Socket> p) throws Exception {

    }

    @Override
    public void destroyObject(PooledObject<Socket> p) throws Exception {
        System.out.println("关闭");
        Socket socket = p.getObject();
        if(socket != null && !socket.isClosed()){
            socket.close();
        }
    }
}
