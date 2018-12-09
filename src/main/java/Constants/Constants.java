package Constants;

public class Constants {
    public static class ServiceProviderInterfaceMessages {
        public static final String VERIFY_RESERVATION = "verify_reservation";
        public static final String SEND_SERVICE_DATA = "send_service_data_interface";
        public static final String SET_SERVICE_DATA = "set_service_data";
    }

    public static class ServiceProviderSecretaryMessages {
        public static final String SEND_SERVICE_DATA = "send_service_data_secretary";
        public static final String RECEIVE_RESERVATION_REQUEST = "receive_reservation_request";
        public static final String SEND_RESERVATION_RESPONSE = "send_reservation_response";
        public static final String SEND_RESERVATION_TO_PROCESS = "send_reservation_to_process";
        public static final String RECEIVE_RESERVATION_STATUS = "receive_reservation_status";
        public static final String RECEIVE_SERVICE_DATA = "receive_service_data_secretary";
        public static final String CANCEL_RESERVATION = "cancel_reservation";
    }

    public static class ServiceProviderSchedulerMessages {
        public static final String RECEIVE_RESERVATION_TO_PROCESS = "receive_reservation_to_process";
        public static final String RECEIVE_SERVICE_DATA = "receive_service_data_scheduler";
        public static final String SEND_RESERVATION_STATUS = "send_reservation_status";
        public static final String NOTIFY_CHANGES = "notify_changes";
    }

    public static class ServiceAgentGuiMessages {
        public static final int SERVICE_PROVIDER_DATA = 0;
        public static final int RESERVATION_DATA = 1;
    }

    //-------------------------------------------------------------------------------------------------------------
    public static class CustomerInterfaceMessages {
        public static final String SEND_TASK_DATA = "send_task_data";
    }

    public static class CustomerSecretaryMessages {
        public static final String SEND_TASK_DATA = "send_task_data";
        public static final String RECEIVE_TASK = "receive_task";
        public static final String SEND_RESERVATION_REQUEST = "send_reservation_request";
        public static final String RECEIVE_RESERVATION_RESPONSE = "receive_reservation_response";
        public static final String SEND_RESERVATION_STATUS = "send_reservation_status";
        public static final String RECEIVE_SERVICE_DATA = "receive_service_data";
    }

    public static class CustomerSchedulerMessages {
        public static final String NOTIFY_CHANGES = "notify_changes";
        public static final String RECIVE_TASK_DATA = "receive_task_data";
        public static final String SEND_TASK = "send_task";
        public static final String RECEIVE_RESERVATION_STATUS = "receive_reservation_status";
    }

}
