package com.haveFood.Application.consumer;

public interface ServiceFactory<T> {
    ConsumerService<T> create() throws Exception;
}
