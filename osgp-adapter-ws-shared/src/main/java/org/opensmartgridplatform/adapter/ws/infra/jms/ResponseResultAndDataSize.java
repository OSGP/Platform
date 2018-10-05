package org.opensmartgridplatform.adapter.ws.infra.jms;

public class ResponseResultAndDataSize {
    private final String responseResult;
    private final int responseDataSize;

    public ResponseResultAndDataSize(String responseResult, int responseDataSize) {
        this.responseResult = responseResult;
        this.responseDataSize = responseDataSize;
    }

    public String getResult() {
        return responseResult;
    }

    public int getDataSize() {
        return responseDataSize;
    }
}
