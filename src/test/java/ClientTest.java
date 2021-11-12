import com.onbon.bxk.client.base.BxCmd;
import com.onbon.bxk.client.base.BxCmdTurnOnOff;
import com.onbon.bxk.client.net.BxDataPack;
import com.onbon.bxk.client.net.SocketClient;
import com.onbon.bxk.client.net.SocketClientBuilder;
import org.junit.Test;

/**
 * @author huangjie
 * @description
 * @date 2021/11/12
 */
public class ClientTest {

    private SocketClient client;

    private void init(){
        SocketClient socketClient = new SocketClientBuilder().build("192.168.1.115", 5005);
        this.client = socketClient;
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
    }


}
