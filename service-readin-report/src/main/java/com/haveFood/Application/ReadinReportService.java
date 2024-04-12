package com.haveFood.Application;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ReadinReportService {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var readinReportService = new ReadinReportService();
        try (var service = new KafkaService<>(ReadinReportService.class.getSimpleName(),
                "RESTAURANT_USER_GENERATE_READING_REPORT",
                readinReportService::parse,
                new HashMap<>())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String,Message<User>> record) throws IOException {
        System.out.println("------------------------------------------");
        System.out.println("Processing report for" + record.value());

        var message = record.value();
        var user = message.getPayload();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + user.getUuid());

        System.out.println("File created: " + target.getAbsolutePath());

    }
}
