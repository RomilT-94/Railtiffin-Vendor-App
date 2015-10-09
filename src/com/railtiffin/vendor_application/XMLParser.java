package com.railtiffin.vendor_application;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Romil
 * 
 */
public class XMLParser {

	// constructor
	private Context context;

	public XMLParser(Context c) {
		context = c;

	}

	/**
	 * Getting XML from URL making HTTP request
	 * 
	 * @param url
	 *            string
	 * */
	public String getXmlFromUrl(String url) {
		String xml = null;

		try {
			// defaultHttpClient

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpget = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpget);
			int response = httpResponse.getStatusLine().getStatusCode();

			System.out.println("Response is: " + response);
			// response = 502;
			System.out.println("Toast Context: " + context);
			if (response == 200) {

				HttpEntity httpEntity = httpResponse.getEntity();
				xml = EntityUtils.toString(httpEntity);
				// xml = URLDecoder.decode(xml1, "utf-8");

			} else {
				xml = "error";

			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			System.out.println("UnsupportedEncodingException");

			showToast(context);

		} catch (ClientProtocolException e) {
			e.printStackTrace();

			System.out.println("ClientProtocolException");

			showToast(context);

		} catch (IOException e) {
			e.printStackTrace();

			System.out.println("IOException");

			showToast(context);

		}
		// return XML
		return xml;
	}

	/**
	 * Getting XML DOM element
	 * 
	 * @param XML
	 *            string
	 * */
	public Document getDomElement(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}

		return doc;
	}

	/**
	 * Getting node value
	 * 
	 * @param elem
	 *            element
	 */
	public final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	/**
	 * Getting node value
	 * 
	 * @param Element
	 *            node
	 * @param key
	 *            string
	 * */
	public String getValue(Element item, String str) {

		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));

	}

	public void showToast(Context context) {

		Toast.makeText(context, "Error! Not Found! Please Reload.",
				Toast.LENGTH_LONG).show();

	}
}
