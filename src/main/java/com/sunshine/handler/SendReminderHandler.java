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
import com.sunshine.service.ReminderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sunshine.model.Reminder;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;



public class SendReminderHandler implements RequestHandler<APIGatewayProxyRequestEvent,
        APIGatewayProxyResponseEvent> {

    private static final Logger LOG = LogManager.getLogger(SendReminderHandler.class);

    private final MySqlConnect mySqlConnect = new MySqlConnect();

    private final ReminderService reminderService = new ReminderService(mySqlConnect);

    static final String FROM = "ranaqrenawi@gmail.com";

    static final String TO = "engranaahmad@gmail.com";

//    static final String CONFIGSET = "ConfigSet";

    // subject line for email
    static final String SUBJECT = "Remember to go for your walk :-)";

    // HTML body for email
    static final String HTMLBODY = "<h1>Remember your walk</h1> <p>This email was sent to remind " +
            "you to go for your walk";

    // email body for recipients with non-html email clients
    static final String TEXTBODY = "This is a reminder to go for your walk";

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context) {

        LOG.info("request received");

        String UserId = request.getPathParameters().get("userId");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        response.setHeaders(headers);

        if (UserId.length() != 36) {

            response.setStatusCode(400);

        } else {

            ArrayList<Reminder> reminder = reminderService.sendReminder(UserId);

            ObjectMapper objectMapper = new ObjectMapper();

            try {

                String responseBody = objectMapper.writeValueAsString(reminder);
                response.setBody(responseBody);

                AmazonSimpleEmailService client =
                        AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();

                SendEmailRequest emailRequest = new SendEmailRequest()
                        .withDestination(
                                new Destination().withToAddresses(TO))
                        .withMessage(new Message()
                                .withBody(new Body()
                                        .withHtml(new Content()
                                                .withCharset("UTF-8").withData(HTMLBODY))
                                        .withText(new Content()
                                                .withCharset("UTF-8").withData(TEXTBODY)))
                                .withSubject(new Content()
                                        .withCharset("UTF-8").withData(SUBJECT)))
                        .withSource(FROM);
//                         .withConfigurationSetName(CONFIGSET);

                client.sendEmail(emailRequest);
                LOG.info("Email sent :-)");
            } catch (JsonProcessingException exception) {
                LOG.error("unable to marshal tasks array", exception);
                response.setStatusCode(500);
            } catch (Exception exception){
                LOG.error("Email not sent", exception);
                response.setStatusCode(500);
            }
        }

        return response;

    }

}
