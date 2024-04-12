package com.haveFood.Application;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderDispatcher = new KafkaDispatcher<Order>(GenerateAllReportsServlet.class.getSimpleName())) {
            try (var emailDispatcher = new KafkaDispatcher<String>(GenerateAllReportsServlet.class.getSimpleName())) {
                for (var i = 0; i < 10; i++) {

                    var orderId = UUID.randomUUID().toString();
                    var amount = new BigDecimal(Math.random() * 5000 + 1);
                    var email = Math.random() + "@gmail.com";

                    var order = new Order(orderId, amount, email);
                    orderDispatcher.send("RESTAURANT_NEW_ORDER", email, order);

                    var emailCode = "Thank you for your order! We are processing your order!";
                    emailDispatcher.send("RESTAURANT_SEND_EMAIL", email, emailCode);
                }
            }
        }
    }

}
