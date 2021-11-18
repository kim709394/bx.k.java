package com.onbon.bxk.client.net;

import com.onbon.bxk.client.base.BxCmd;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author huangjie
 * @description
 * @date 2021/11/12
 */
public class DefaultBxSocketClient implements BxSocketClient {

    private Boolean isOpen;
    private SocketPool socketPool;

    public DefaultBxSocketClient(SocketPool socketPool){
        this.socketPool = socketPool;
    }


    @Override
    public void open() throws SocketException {
        if(isOpen){
            throw new SocketException("bxSocketClient have alread opened");
        }else{
            socketPool.init();
            isOpen=true;
        }

    }

    @Override
    public void sendCmd(BxCmd cmd) throws Exception {
        Socket socket = socketPool.borrowSocket();
        try (OutputStream out=socket.getOutputStream(); InputStream in=socket.getInputStream()){
            // 对命令进行封装
            BxDataPack dataPack = new BxDataPack(cmd);
            // 生成命令序列
            byte[] seq = dataPack.pack();
            out.write(seq);
            out.flush();
            byte[] b=new byte[1024];
            int len=in.read(b);
            BxResp bxResp = BxResp.parse(b, len);
            if(bxResp!= null &&!bxResp.isAck()){
                throw new SocketException(" no socket ack have been returned");
            }
        }  finally {
            socketPool.returnSocket(socket);
        }

    }


    @Override
    public void close() throws Exception {
        socketPool.clear();
        socketPool.close();
    }
}
