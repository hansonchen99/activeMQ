package com.aiguigu.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce {

    //  linux 上部署的activemq 的 IP 地址 + activemq 的端口号
    public static final String ACTIVE_URL = "tcp://192.168.93.130:61616";
    public static final String QUERE_NAME = "quere01";

    public static void main(String[] args) throws JMSException {
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
        // 5 创建消费的生产者
        MessageProducer messageProducer = session.createProducer(queue);
        // 6 通过messageProducer生产3条消息到消费者对列中
        for (int i = 1; i <= 3; i++) {
            // 7 创建消息
            TextMessage textMessage = session.createTextMessage("msg--" + i);
            // 8 通过messageProduce发布消息
            messageProducer.send(textMessage);
        }
        // 9 关闭资源
        messageProducer.close();
        session.close();
        connection.close();
        System.out.println("消息发送到MQ完成");
    }
}
