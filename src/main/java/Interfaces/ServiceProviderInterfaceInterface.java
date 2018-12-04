package Interfaces;


import Data.ServiceProviderData;
import Exceptions.negativeValueException;

public interface ServiceProviderInterfaceInterface {
    void setServiceProviderData(ServiceProviderData data) throws negativeValueException;
}
