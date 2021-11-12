package com.onbon.bxk.client.net;

/**
 * @author huangjie
 * @description  tcp链接客户端构建器
 * @date 2021/11/12
 */
public class SocketClientBuilder {



    //采取默认值构建tcp客户端
    public SocketClient build(String host,Integer port){
        SocketPoolBuilder builder=new SocketPoolBuilder();
        SocketPool socketPool = builder.build(host, port);
        return build(socketPool);

    }

    //自由构建tcp连接池来构建tcp客户端
    public SocketClient build(SocketPool socketPool){
        return new DefaultSocketClient(socketPool);
    }

}
