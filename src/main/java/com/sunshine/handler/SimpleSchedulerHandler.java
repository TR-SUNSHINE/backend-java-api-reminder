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
                ".amazonaws.com/notifications");

        try {
            LOG.info("about to execute setFollowRedirects");
            HttpURLConnection.setFollowRedirects(false);
            LOG.info("about to open Connection");
            HttpURLConnection con = (HttpURLConnection) new URL("https://ia7thtfozg.execute-api" +
                    ".eu-west-2.amazonaws.com/notifications").openConnection();
            LOG.info("about to setRequestMethod");
            con.setRequestMethod("GET");
            LOG.info("about to setConnectTimeout");
            con.setConnectTimeout(3000); //set timeout to 3 seconds
            LOG.info("about to setReadTimeout");
            con.setReadTimeout(10000);
            LOG.info("about to getResponseCode");
            int responseCode = con.getResponseCode();
            LOG.info(String.format("Response code: %s", responseCode));

            LOG.info("about to go to if clause");
            if (responseCode == HttpURLConnection.HTTP_OK) {
                LOG.info("about to BufferedReader");
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                LOG.info("about to String inline");
                String inputLine;
                LOG.info("about to buffer response");
                StringBuffer response = new StringBuffer();

                LOG.info("about to do while loop");
                while ((inputLine = in.readLine()) != null) {
                    LOG.debug("about append to inputLine");
                    response.append(inputLine);
                }
                LOG.info("about append to in.close");
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
        LOG.debug("about to return null");
        return null;
    }
}
