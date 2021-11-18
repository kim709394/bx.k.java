import com.onbon.bxk.client.base.*;
import com.onbon.bxk.client.net.BxDataPack;
import com.onbon.bxk.client.net.BxResp;
import com.onbon.bxk.client.net.BxSocketClient;
import com.onbon.bxk.client.net.BxSocketClientBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @description
 * @date 2021/11/12
 */
public class ClientTest {

    private BxSocketClient client;

    private void init(){
        BxSocketClient bxSocketClient = new BxSocketClientBuilder().build("192.168.1.115", 5005);
        this.client = bxSocketClient;
    }

    /**
     * 开机
     * */
    @Test
    public void trunOn() throws Exception {
        init();
        client.open();
        // 开机
        BxCmd cmdTurnOn = new BxCmdTurnOnOff(true);
        client.sendCmd(cmdTurnOn);
        client.close();
    }

    /**
     * 关机
     * */
    @Test
    public void trunOff() throws Exception {
        init();
        client.open();
        BxCmd cmdTurnOn = new BxCmdTurnOnOff(false);
        client.sendCmd(cmdTurnOn);
        client.close();
    }

    /**
     * 清除屏幕,暂不支持
     * */
    @Test
    public void clearScreen() throws Exception {
        init();
        client.open();
        client.sendCmd(new BxCmdClearScreen());
        client.close();

    }

    /**
     * 发送动态区
     * */
    @Test
    public void sendDynamicArea() throws Exception{
        init();
        client.open();
        //String content = " 今天是\\FE000\\DY 年\\DL 月\\DD 日\\n\\C3\\DH 时\\DM 分\\DS 秒";
        String content = "淮南万泰电子股份有限公司";
        client.sendCmd(getDynamicCmd(content));
        client.close();
    }

    //获取动态区命令对象
    private BxCmd getDynamicCmd(String content) throws UnsupportedEncodingException {
        byte id=1;      //区域唯一标识
        short x=0;      //横向偏移多少个像素单位
        short y=32;      //纵向偏移多少个像素单位
        short w=96;     //宽度是多少个像素单位，led屏幕上面每一行的小灯个数
        short h=32;     //高度是多少个像素单位
        //动态区要显示的内容
        //String content = "王国培，AI算法大师，朱集东矿，三班倒，工号:12306，淮南万泰电子股份有限公司。";
        //String content = "酷酷酷酷酷酷酷酷酷，哦哦哦哦哦哦，啦啦啦啦啦啦啦，哈哈哈哈哈，怕怕怕怕怕怕怕";
        List<BxArea> areas=new ArrayList<>();
        //创建一个动态区
        BxAreaDynamic dynamicArea=new BxAreaDynamic(id,x,y,w,h,content.getBytes("gb2312"));

        //关闭语音
        dynamicArea.setSoundMode((byte)0x00);
        //设置显示特技为向左移动
        dynamicArea.setDispMode((byte)0x03);
        //设置停留时间为10*0.5秒
        dynamicArea.setHoldTime((byte)10);
        //设置运行状态为自动循环显示
        dynamicArea.setRunMode((byte)0x00);
        //设置超时时间为10秒
        dynamicArea.setTimeout((byte)0x10);
        //设置运行速度为2个单位
        dynamicArea.setSpeed((byte)0x02);
        //设置多行显示
        dynamicArea.setSingleLine((byte)0x02);
        //设置换行方式为自动换行
        dynamicArea.setAutoNewLine((byte)0x02);
        //设置行间距为2个单位
        dynamicArea.setLineSpace((byte)0x02);
        //设置字体对齐方式
        dynamicArea.setAlignment((byte)0x00);
        areas.add(dynamicArea);
        BxCmdSendDynamicArea cmd = new BxCmdSendDynamicArea(areas);
        cmd.setDelAreaIds(new byte[]{1});
        return cmd;
    }

    /**
     * 不用连接池进行动态区接口的调用
     * */
    @Test
    public void dynamicAreaNonPool() throws Exception{

        Socket socket=new Socket("192.168.1.115",5005);
        try (OutputStream out=socket.getOutputStream(); InputStream in = socket.getInputStream();){

            String content = "人名：王国培U，标识卡号:1001,职位：普通矿工";
            // 对命令进行封装
            BxDataPack dataPack = new BxDataPack(getDynamicCmd(content));
            // 生成命令序列
            byte[] seq = dataPack.pack();
            out.write(seq);
            out.flush();
            byte[] b=new byte[1024];

            int len=in.read(b);
            System.out.println("读取的流字节数量"+len);
            BxResp bxResp = BxResp.parse(b, len);
            if(bxResp!= null &&!bxResp.isAck()){
                throw new SocketException(" no socket ack have been returned");
            }
        }finally {

        }


    }

    /**
     * 校时
     * */
    @Test
    public void adjustTime() throws Exception {
        init();
        client.open();
        client.sendCmd(new BxCmdSystemClockCorrect(new Date()));
        client.close();
    }
}
