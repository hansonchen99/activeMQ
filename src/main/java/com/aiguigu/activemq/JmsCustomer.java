package com.aiguigu.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsCustomer {

    //  linux 上部署的activemq 的 IP 地址 + activemq 的端口号
    public static final String ACTIVE_URL = "tcp://192.168.93.130:61616";
    public static final String QUERE_NAME = "quere01";

    public static void main(String[] args) throws JMSException, IOException {
        // 创建连接工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVE_URL);
        // 2 通过连接工厂连接 connection 和启动
        Connection connection = activeMQConnectionFactory.createConnection();
        //  启动
        connection.start();
        // 3 创建会话 session
        // 两个参数 第一个事务 第二个签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4 创建目的地（两中：队列/主题）这里是队列
        Queue queue = session.createQueue(QUERE_NAME);
        // 5 创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);
        /*while(true){
            TextMessage message = (TextMessage)messageConsumer.receive();
            if(null != message){
                System.out.println("****消费者的消息：" + message.getText());
            }else{
                break;
            }
        }
        messageConsumer.close();
        session.close();
        connection.close();*/
        messageConsumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if(null != message && message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        System.out.println("消费者的消息：" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.in.read();
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
