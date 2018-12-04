package main.java.Interfaces;

import main.java.Data.ServiceProviderData;
import main.java.Exceptions.negativeValueException;

public interface ServiceProviderInterfaceInterface {
    void setServiceProviderData(ServiceProviderData data) throws negativeValueException;
}
