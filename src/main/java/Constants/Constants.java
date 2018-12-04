package Constants;

public class Constants {
    public static class ServiceProviderInterfaceMessages{
        public static final String VERIFY_RESERVATION= "verify_reservation";
        public static final String SEND_SERVICE_DATA="send_service_data";
    }

    public static class ServiceProviderSecretaryMessages{
        public static final String SEND_SERVICE_DATA = "send_service_data";
        public static final String RECEIVE_RESERVATION_REQUEST = "receive_reservation_request";
        public static final String SEND_RESERVATION_RESPONSE = "send_reservation_response";
        public static final String SEND_RESERVATION_TO_PROCESS = "send_reservation_to_process";
        public static final String RECEIVE_RESERVATION_STATUS = "receive_reservation_status";
    }

    public static class ServiceProviderSchedulerMessages{
        public static final String RECEIVE_RESERVATION_TO_PROCESS = "receive_reservation_to_process";
        public static final String RECEIVE_SERVICE_DATA = "receive_service_data";
        public static final String SEND_RESERVATION_STATUS = "send_reservation_status";
        public static final String NOTIFY_CHANGES = "notify_changes";
    }
}
