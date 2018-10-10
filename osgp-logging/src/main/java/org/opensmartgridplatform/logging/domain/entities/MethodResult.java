package org.opensmartgridplatform.logging.domain.entities;

public class MethodResult {
    private final String applicationName;
    private final String className;
    private final String methodName;
    private final String responseResult;
    private final int responseDataSize;

    public MethodResult(final String applicationName, final String className, final String methodName,
            final String responseResult,
            int responseDataSize) {
        this.applicationName = applicationName;
        this.className = className;
        this.methodName = methodName;
        this.responseResult = responseResult;
        this.responseDataSize = responseDataSize;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getResponseResult() {
        return responseResult;
    }

    public int getResponseDataSize() {
        return responseDataSize;
    }
}
