package com.haveFood.Application;

import com.haveFood.Application.consumer.ConsumerService;
import com.haveFood.Application.consumer.ServiceRunner;
import com.haveFood.Application.database.LocalDatabase;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.SQLException;
import java.util.UUID;

public class CreateUserService implements ConsumerService<Order> {

    private final LocalDatabase database;

    CreateUserService() throws SQLException {
        this.database = new LocalDatabase("users_database");
        this.database.createdIfNotExist("create table Users (" +
                "uuid varchar(200) primary key," +
                "email varchar(200)");
    }

    public static void main(String[] args) {
        new ServiceRunner(CreateUserService::new).start(1);
    }


    public void parse(ConsumerRecord<String, Message<Order>> record) throws SQLException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking new user");
        System.out.println(record.value());
        var order = record.value().getPayload();
        if (isNewUser(order.getEmail())) {
            insertNewUser(order.getEmail());
        }
    }

    @Override
    public String getTopic() {
        return "RESTAURANT_NEW_ORDER";
    }

    @Override
    public String getConsumerGroup() {
        return CreateUserService.class.getSimpleName();
    }

    private void insertNewUser(String email) throws SQLException {
        var uuid = UUID.randomUUID().toString();
        database.update("insert into Users (uuid, email)" +
                "values (?,?)", uuid, email);
        System.out.println("Usuario" + uuid + " e " + email + " adicionado");
    }

    private boolean isNewUser(String email) throws SQLException {
        var results = database.query("select uuid from Users " +
                "where email = ? limit 1", email);
        return !results.next();
    }

}

