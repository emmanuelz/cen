package org.cen.services;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttInputService {
	private MqttAsyncClient client;
	private IMqttActionListener connectCallback;
	private MqttCallback clientCallback;

	public MqttInputService(String uri, final String topic, final InputParser parser) {
		super();
		clientCallback = new MqttCallback() {
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				byte[] data = message.getPayload();
				parser.parse(data);
				System.out.println(new String(data));
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
			}

			@Override
			public void connectionLost(Throwable e) {
				System.out.println(e);
			}
		};
		connectCallback = new IMqttActionListener() {
			@Override
			public void onSuccess(IMqttToken token) {
				try {
					client.subscribe(topic, 0);
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(IMqttToken token, Throwable e) {
			}
		};
		try {
			String id = MqttAsyncClient.generateClientId();
			client = new MqttAsyncClient(uri, id);
			client.setCallback(clientCallback);
			client.connect(null, connectCallback);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
