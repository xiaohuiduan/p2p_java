import common.Const;
import common.MsgPacket;
import org.tio.server.ServerChannelContext;
import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;
import server.P2PServerAioHandler;
import server.ServerListener;

import java.io.IOException;
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
 * @data: 2020/2/12 下午6:19
 * @description: 服务端主类
 */
public class ServerMain {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("输入ip：");
        String ip = "127.0.0.1";
        System.out.println("输入端口：");
        int port = in.nextInt();

        // 处理消息
        ServerAioHandler handler = new P2PServerAioHandler();
        // 监听
        ServerAioListener listener = new ServerListener();
        // 配置
        ServerTioConfig config = new ServerTioConfig("服务端", handler, listener);
        TioServer tioServer = new TioServer(config);
        // 设置timeout
        config.setHeartbeatTimeout(Const.TIMEOUT);
        try {
            // 启动
            tioServer.start(ip, port);
        } catch (IOException e) {
            System.out.println("启动错误：" + e.getMessage());
        }

    }
}
