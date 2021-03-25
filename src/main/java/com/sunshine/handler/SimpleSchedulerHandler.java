package com.sunshine.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class SimpleSchedulerHandler implements RequestHandler<Void, Void>{

    private static final Logger LOG = LogManager.getLogger(SimpleSchedulerHandler.class);

    @Override
    public Void handleRequest(Void input, Context context){

        LOG.info("About to check availability of https://ia7thtfozg.execute-api.eu-west-2" +
                ".amazonaws.com/users/{userId}/reminder ");

        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL("https://ia7thtfozg.execute-api" +
                    ".eu-west-2.amazonaws.com/users/5e37e4d7-d053-4936-8827-01500c10a959/reminder").openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(3000); //set timeout to 2 seconds
            con.setReadTimeout(3000);
            int responseCode = con.getResponseCode();
            LOG.info(String.format("Response code: %s", responseCode));

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                LOG.info(response.toString());
            } else {
                LOG.info("GET request not worked");
            }
        } catch (java.net.MalformedURLException exception){
            LOG.debug("MalformedURLException: {}", exception.getMessage());
        } catch (java.net.ProtocolException exception){
            LOG.debug("ProtocolException: {}", exception.getMessage());
        } catch (java.io.IOException exception){
            LOG.debug("IOException: {}", exception.getMessage());
        }
        return null;
    }
}
