package com.railtiffin.vendor_application;

/**
 * @author Romil
 * 
 */
public interface ApplicationConstants {

	// PHP Application URL to store Pushy/GCM Reg ID created on Server
	static final String APP_SERVER_URL = "http://test.traveltreats.in/oc_railtiffin/"
			+ "index.php?route=test/isUser/addDevice&vendor_id=";

	// PHP Application URL to store Reg ID created
	static final String SERVER_REGISTRATION_URL = "http://test.traveltreats.in/"
			+ "oc_railtiffin/index.php?route=test/isUser&user=";

	// PHP Application URL to fetch New Order Details
	static final String NEW_ORDER_SERVER_URL = "http://test.traveltreats.in/oc_railtiffin/"
			+ "index.php?route=test/orderinfo&suborder_id=";

	// PHP Application URL to fetch Vendor Details
	static final String STATUS_ORDERS_SERVER_URL = "http://test.traveltreats.in/"
			+ "oc_railtiffin/index.php?route=test/status/updateStatus&to=";

	// PHP Application URL to fetch Vendor Details
	static final String ORDERS_PROCESS_SERVER_URL = "http://test.traveltreats.in/"
			+ "oc_railtiffin/index.php?route=test/status/AcceptedMsg&vid=";

	// PHP Application URL to fetch Vendor Details
	static final String PENDING_ORDERS_SERVER_URL = "http://test.traveltreats.in/oc_railtiffin"
			+ "/index.php?route=test/App_Status/pending&vid=";

	// PHP Application URL to fetch Vendor Details
	static final String ACCEPTED_ORDERS_SERVER_URL = "http://test.traveltreats.in/oc_railtiffin"
			+ "/index.php?route=test/App_Status/accepted&vid=";

	// PHP Application URL to fetch Vendor Details
	static final String DECLINED_ORDERS_SERVER_URL = "http://test.traveltreats.in/"
			+ "oc_railtiffin/index.php?route=test/status/declined";

	// PHP Application URL to fetch Vendor Details
	static final String VENDOR_SERVER_URL = "http://test.traveltreats.in/"
			+ "oc_railtiffin/index.php?route=test/vend&vid=";

	static final String VENDOR_CONTACT_SERVER_URL = "http://test.traveltreats.in/"
			+ "oc_railtiffin/index.php?route=test/vend/addContact&vid=";

	// PHP Application URL to fetch Delivery Guys Details
	static final String DELIVERY_GUYS_SERVER_URL = "http://test.traveltreats.in/"
			+ "oc_railtiffin/delivery_list.php";

	// PHP Application URL to fetch Delivery Guys Details
	static final String ORDER_STATUS_SERVER_URL = "http://test.traveltreats.in/"
			+ "oc_railtiffin/index.php?route=test/status/getStatus&sid=";

	// PHP Application URL to upload images
	static final String IMAGE_UPLOAD_SERVER_URL = "http://test.traveltreats.in/"
			+ "oc_railtiffin/index.php?route=test/imageupload";

	// PHP Application URL to upload images
	static final String CONTACT_UPLOAD_SERVER_URL = "http://test.traveltreats.in/"
			+ "oc_railtiffin/index.php?route=test/vend/contactLink&sid=";

	// Push Received TimeStamp
	static final String PUSH_RECEIVED_CALLBACK = "http://test.traveltreats.in/"
			+ "oc_railtiffin/index.php?route=test/status/notificationReceived&did=";

	static final String ETA_URL = "http://test.traveltreats.in/railtiffin_prod/"
			+ "index.php?route=orderprocess/trainEta/&suborder_id=";

	// Google Project Number. Was being used for GCM.
	static final String GOOGLE_PROJ_ID = "918620057994";

	static final String MSG_KEY = "m";

}
