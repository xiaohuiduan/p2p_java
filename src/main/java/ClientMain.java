import client.P2pClientAioHandler;
import common.Const;
import common.MsgPacket;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientTioConfig;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.Node;
import org.tio.core.Tio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * //                            _ooOoo_
 * //                           o8888888o
 * //                           88" . "88
 * //                           (| -_- |)
 * //                            O\ = /O
 * //                        ____/`---'\____
 * //                      .   ' \\| |// `.
 * //                       / \\||| : |||// \
 * //                     / _||||| -:- |||||- \
 * //                       | | \\\ - /// | |
 * //                     | \_| ''\---/'' | |
 * //                      \ .-\__ `-` ___/-. /
 * //                   ___`. .' /--.--\ `. . __
 * //                ."" '< `.___\_<|>_/___.' >'"".
 * //               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //                 \ \ `-. \_ __\ /__ _/ .-` / /
 * //         ======`-.____`-.___\_____/___.-`____.-'======
 * //                            `=---='
 * //
 * //         .............................................
 * //                  佛祖镇楼           BUG辟易
 *
 * @author: xiaohuiduan
 * @data: 2020/2/12 下午6:21
 * @description: 客户端主类
 */
public class ClientMain {
    public static void main(String[] args) {
        ClientAioHandler handler = new P2pClientAioHandler();

        ClientTioConfig config = new ClientTioConfig(handler, null);
        ArrayList<ClientChannelContext> contexts = new ArrayList<ClientChannelContext>();
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            ClientChannelContext context;
            try {
                TioClient client = new TioClient(config);
                String ip = "127.0.0.1";
                System.out.println("输入端口：");
                int port = in.nextInt();
                context = client.connect(new Node(ip, port), Const.TIMEOUT);
                System.out.println("客户端：" + context);
                contexts.add(context);
            } catch (Exception e) {
                System.out.println("错误是：" + e.getMessage());
            }
        }

        MsgPacket msgPacket = new MsgPacket();
        while (true) {
            System.out.println("请输入发送的服务端");
            int index = in.nextInt();
            System.out.println("请输入内容");
            String body = in.next();
            try {
                msgPacket.setBody(body.getBytes(MsgPacket.CHARSET));
                Tio.send(contexts.get(index), msgPacket);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }
}
