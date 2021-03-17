package com.sunshine;

import java.sql.*;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sunshine.model.Reminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteReminderHandler implements RequestHandler<APIGatewayProxyRequestEvent,
        APIGatewayProxyResponseEvent> {

    private static final Logger LOG = LogManager.getLogger(DeleteReminderHandler.class);

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context){

        LOG.info("request received");

        String UserId = request.getPathParameters().get("userId");
        String ReminderId = request.getPathParameters().get("reminderId");

        ObjectMapper objectMapper = new ObjectMapper();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        response.setHeaders(headers);

        try {
            Class.forName("com.mysql.jdbc.Driver");

            LOG.debug(String.format("Connecting to DB on %s", System.getenv("DB_HOST")));

            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s" +
                            "&password=%s",
                    System.getenv("DB_HOST"),
                    System.getenv("DB_NAME"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASSWORD")));

            preparedStatement = connection.prepareStatement("DELETE FROM reminder WHERE id = ?");
            preparedStatement.setString(1, ReminderId);

            preparedStatement.execute();

            response.setStatusCode(204);

            connection.close();


        } catch (ClassNotFoundException exception){
            LOG.error(String.format("Class not found exception: %s",
                   exception.getMessage()),
                    exception);
            response.setStatusCode(500);
        } catch (SQLException exception){
            LOG.error(String.format("SQL Exception: %s", exception.getMessage()), exception);
            response.setStatusCode(501);
        } finally {
            closeConnection();
        }

        return response;

    }

    public void closeConnection(){

        try {
            if (resultSet != null){
                resultSet.close();
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception){
            LOG.error("Unable to close connection to MySQL - {}", exception.getMessage());
        }
    }

}
