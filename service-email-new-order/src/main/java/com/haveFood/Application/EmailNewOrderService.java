package com.haveFood.Application;

import com.haveFood.Application.consumer.ConsumerService;
import com.haveFood.Application.consumer.ServiceRunner;
import com.haveFood.Application.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

public class EmailNewOrderService implements ConsumerService<Order> {

    public static void main(String[] args) {
        new ServiceRunner(EmailNewOrderService::new).start(1);
    }


    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, prepering email");
        var message = record.value();
        System.out.println(record.value());

        var order = record.value().getPayload();
        var emailCode = "Thank you for your order! We are processing your order!";
        var id = message.getId().continueWith(EmailNewOrderService.class.getSimpleName());
        emailDispatcher.send("RESTAURANT_SEND_EMAIL", order.getEmail(),
                id, emailCode);
    }

    @Override
    public String getTopic() {
        return "RESTAURANT_SEND_EMAIL";
    }

    @Override
    public String getConsumerGroup() {
        return EmailNewOrderService.class.getSimpleName();
    }


private boolean isFraud(Order order) {
    return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;

}

}
