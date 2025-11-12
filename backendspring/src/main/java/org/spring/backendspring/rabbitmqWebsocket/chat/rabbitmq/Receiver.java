package org.spring.backendspring.rabbitmqWebsocket.chat.rabbitmq;

import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageDto;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Receiver {

    private SimpMessagingTemplate messagingTemplate;
    private final ConnectionFactory connectionFactory;

    public void receiveMessage(Long crewId) throws Exception {

        String crewIdString = crewId.toString();
        String queueName = "chat.queue.crew." + crewIdString;

        Queue queue = new Queue(queueName, true);

        MessageListenerAdapter adapter = new MessageListenerAdapter(new Object() {
            public void handleMessage(ChatMessageDto message) {
                String destination = "/topic/crew/" + message.getCrewId();
                messagingTemplate.convertAndSend(destination, message);
            }
        }, "handleMessage");

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(queue);
        container.setMessageListener(adapter);
        container.start();
    }
}
