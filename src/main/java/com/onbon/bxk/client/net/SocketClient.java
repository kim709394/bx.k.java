package com.onbon.bxk.client.net;

import com.onbon.bxk.client.base.BxCmd;

/**
 * @author huangjie
 * @description   tcp专用客户端
 * @date 2021/11/12
 */
public interface SocketClient {


    /**
     * 打开连接
     * */
    void open();


    /**
     * 发送命令
     * */
    void sendCmd(BxCmd cmd) throws Exception;

    /**
     * 关闭
     * */
    void close() throws Exception;

}
