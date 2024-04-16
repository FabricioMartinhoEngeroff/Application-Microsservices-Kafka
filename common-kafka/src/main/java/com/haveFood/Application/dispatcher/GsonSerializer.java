package com.haveFood.Application.dispatcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haveFood.Application.Message;
import com.haveFood.Application.MessageAdapter;
import org.apache.kafka.common.serialization.Serializer;

public class GsonSerializer<T> implements Serializer<T> {

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter()).create();

    @Override
    public byte[] serialize(String s, T object) {
        return gson.toJson(object).getBytes();
    }
}