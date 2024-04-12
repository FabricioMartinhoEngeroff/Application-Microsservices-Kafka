package org.example.com.haveFood.Application.restaurant;


import com.haveFood.Application.CorrelationID;
import com.haveFood.Application.KafkaDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GenerateAllReportsServlet extends HttpServlet {


    private final KafkaDispatcher<String> batchDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        batchDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            batchDispatcher.send("RESTAURANT_SEND_MESSAGE_TO_ALL_USERS", "RESTAURANT_USER_GENERATE_READING_REPORT",
                    new CorrelationID(GenerateAllReportsServlet.class.getSimpleName()),
                    "RESTAURANT_USER_GENERATE_READING_REPORT");


            System.out.println("Sent generate report to all users.");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Reports request generated");

        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }

    }
}