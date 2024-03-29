package eu.russiantrade.api.comtrade;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class APICall {
    private static final String URL_BASE = "https://comtradeapi.un.org/data/v1/get";
    private static final String URL_REQUEST = "http://comtrade.un.org/api/get?";

    ComtradeAPIParameters comtradeAPIParameters;
    private StringBuilder requestUrl;

    public APICall(ComtradeAPIParameters comtradeAPIParametersRequest) {
        this.comtradeAPIParameters = comtradeAPIParametersRequest;
    }

    public String call() {
        System.out.println(buildRequestUrl());
        HttpResponse<JsonNode> httpResponse = Unirest.get(buildRequestUrl())
                .header("Ocp-Apim-Subscription-Key", "0ad25fcd98ee4b6e9190d08bab604279")
                .asJson();
        try {
            return httpResponse.getBody().toString();
        }catch (NullPointerException e) {
            return null;
        }
    }

    public static String call(String request) {
        System.out.println(URL_REQUEST + request);
        HttpResponse<JsonNode> httpResponse = Unirest.get(URL_REQUEST + request).asJson();
        return httpResponse.getBody().toString();
    }

    /**
     * Build URL for API
     * <p>
     * Last updated: 2023/09
     *  API: https://comtradeapi.un.org/data/v1/get/{typeCode}/{freqCode}/{clCode}
     *  [?reporterCode]
     *  [&period]
     *  [&partnerCode]
     *  [&partner2Code]
     *  [&cmdCode]
     *  [&flowCode]
     *  [&customsCode]
     *  [&motCode]
     *  [&aggregateBy]
     *  [&breakdownMode]
     *  [&includeDesc]
     */

    private String buildRequestUrl() {
        requestUrl = new StringBuilder();

        // first part of url
        requestUrl.append(URL_BASE);

        comtradeAPIParameters.streamDataFormat().forEach(param -> {
            if(param != null) {
                requestUrl.append("/").append(param);
            }
        });

        // start char for search parameters
        requestUrl.append("?");

        comtradeAPIParameters.streamSearchParameters().forEach(param -> {
            if(param != null) {
                requestUrl.append(param).append("&");
            }
        });
        // Remove last &
        requestUrl.replace(requestUrl.length()-1, requestUrl.length(), ""); //remove last &

        return requestUrl.toString();
    }
}
