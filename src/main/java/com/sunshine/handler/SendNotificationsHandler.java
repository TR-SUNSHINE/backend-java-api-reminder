package com.sunshine.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunshine.database.MySqlConnect;
import com.sunshine.model.Notification;
import com.sunshine.service.ReminderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sunshine.model.Reminder;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;



public class SendNotificationsHandler implements RequestHandler<APIGatewayProxyRequestEvent,
        APIGatewayProxyResponseEvent> {

    private static final Logger LOG = LogManager.getLogger(SendNotificationsHandler.class);

    private final MySqlConnect mySqlConnect = new MySqlConnect();

    private final ReminderService reminderService = new ReminderService(mySqlConnect);

    private final String FROM = "ranaqrenawi@gmail.com";

//    static final String CONFIGSET = "ConfigSet";


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context) {

        LOG.info("request received");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        response.setHeaders(headers);

            ArrayList<Notification> notifications = reminderService.sendNotifications();

            ObjectMapper objectMapper = new ObjectMapper();

            try {

                String responseBody = objectMapper.writeValueAsString(notifications);
                response.setBody(responseBody);
            } catch (JsonProcessingException exception) {
                LOG.error("unable to marshal tasks array", exception);
                response.setStatusCode(500);
            }

            try {
                for (int i = 0; i < notifications.size(); i++) {

                    // subject line for email
                    String subject = String.format("Remember to go for your walk, %s",
                            notifications.get(i).getUserName());

                    // HTML body for email
                    String htmlbody = String.format("<h1>Hi %s, remember your walk at %s:00</h1> " +
                            "<p>This " +
                            "email " +
                            "was sent " +
                            "to " +
                            "remind" +
                            " " +
                            "you to go for your walk", notifications.get(i).getUserName(),
                            notifications.get(i).getReminderTime().getHour() );

                    // email body for recipients with non-html email clients
                    String textbody = String.format("Hi %s, This is a reminder to go for your " +
                            "walk at %s:00", notifications.get(i).getUserName(),
                            notifications.get(i).getReminderTime().getHour());

                    LOG.debug("notification email: {}", notifications.get(i).getEmail());


                AmazonSimpleEmailService client =
                        AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();

                SendEmailRequest emailRequest = new SendEmailRequest()
                        .withDestination(
                                new Destination().withToAddresses(notifications.get(i).getEmail()))
                        .withMessage(new Message()
                                .withBody(new Body()
                                        .withHtml(new Content()
                                                .withCharset("UTF-8").withData(htmlbody))
                                        .withText(new Content()
                                                .withCharset("UTF-8").withData(textbody)))
                                .withSubject(new Content()
                                        .withCharset("UTF-8").withData(subject)))
                        .withSource(FROM);
//                         .withConfigurationSetName(CONFIGSET);

                client.sendEmail(emailRequest);
                LOG.info("Email sent :-)");
            }
            } catch (Exception exception){
                    LOG.error("Email not sent", exception);
                   response.setStatusCode(200);
          }

        return response;

    }

}
