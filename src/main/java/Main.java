import client.P2PClientLinstener;
import client.P2pClientAioHandler;
import common.Const;
import common.MsgPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientTioConfig;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;
import server.P2PServerAioHandler;
import server.ServerListener;

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
 * @data: 2020/2/12 下午8:38
 * @description: p2p
 */
public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {


        Scanner in = new Scanner(System.in);
        String ip = "127.0.0.1";
        // 服务端开始
        System.out.println("输入端口：");
        int port = in.nextInt();
        // 处理消息handler
        ServerAioHandler handler = new P2PServerAioHandler();
        // 监听
        ServerAioListener listener = new ServerListener();
        // 配置
        ServerTioConfig config = new ServerTioConfig("服务端", handler, listener);
        // 设置timeout
        config.setHeartbeatTimeout(Const.TIMEOUT *2);
        TioServer tioServer = new TioServer(config);
        try {
            // 启动
            tioServer.start(ip, port);
        } catch (IOException e) {
            System.out.println("启动错误：" + e.getMessage());
        }

        // client开始
        ClientChannelContext[] contexts = new ClientChannelContext[3];
        for (int i = 0; i < 3; i++) {
            // client的handler
            ClientAioHandler clientAioHandler = new P2pClientAioHandler();
            // client 的配置
            ClientTioConfig clientTioConfig = new ClientTioConfig(clientAioHandler, new P2PClientLinstener(),new ReconnConf(Const.TIMEOUT));
            clientTioConfig.setHeartbeatTimeout(Const.TIMEOUT);
            ClientChannelContext context;
            try {
                TioClient client = new TioClient(clientTioConfig);
                System.out.println("输入端口：");
                int serverPort = in.nextInt();
                context = client.connect(new Node(ip, serverPort), Const.TIMEOUT);

                contexts[i] = context;
            } catch (Exception e) {
                System.out.println("客户端启动错误：" + e.getMessage());
            }
        }

        while (true) {
            System.out.println("请输入发送的服务端的index");
            int index = in.nextInt();
            System.out.println("请输入发送的内容");
            String body = in.next();
            try {
                MsgPacket msgPacket = new MsgPacket();
                msgPacket.setBody("测试数据".getBytes(MsgPacket.CHARSET));
                Tio.send(contexts[index], msgPacket);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
