package org.opensmartgridplatform.adapter.ws.infra.jms;

public class EndpointClassAndMethod {
    private final String className;
    private final String methodName;

    public EndpointClassAndMethod(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }
}
