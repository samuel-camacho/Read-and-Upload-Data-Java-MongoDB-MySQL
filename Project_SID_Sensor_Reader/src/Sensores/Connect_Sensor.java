package Sensores;



import java.util.LinkedList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Connect_Sensor implements MqttCallback {

	private String clientId = MqttClient.generateClientId();
	//private String broker= "wss://iot.eclipse.org:443/ws";
	private  MemoryPersistence persistence = new MemoryPersistence();
	private MqttClient client;
	private String currentMessage="";
	private LinkedList<MqttMessage> measures= new LinkedList<MqttMessage>();
	private ConfigurationFileReader build_file;
	//private String topic[]= {"/sid_lab_2019", "/sid_lab_2019_2"};
	private String topic= "/sid_lab_2019";
	
	
	
	public Connect_Sensor(ConfigurationFileReader build_file) {
		
		this.build_file= build_file;
		connect();
		
	}

	
	public void connect(){
		
        try {
        	this.client = new MqttClient(build_file.getBroker(), clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            

            connOpts.setCleanSession(true);
            connOpts.setConnectionTimeout(30);
            connOpts.setAutomaticReconnect(true);
            client.connect(connOpts);
            client.subscribe(topic);
            System.out.println("Connected");
            client.setCallback(this);

		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	@Override
	public void connectionLost(Throwable arg0) {}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {}
	
	
@Override
public void messageArrived(String topic, MqttMessage arg1) throws Exception {
		
synchronized(this) {
	arg1.setQos(build_file.getQoS());
	byte[] bytes = arg1.getPayload();
	currentMessage = new String(bytes, "UTF-8");
	System.out.println(currentMessage);
		
	if(!currentMessage.contains("so dead...")) {
		measures.add(arg1);
		notifyAll();
		}
	}
}
		
public synchronized MqttMessage sendMeasure() {

MqttMessage msg = null;
try {
	while (measures.size() < 1) {
		System.out.println("À espera de Medições do Sensor");
		wait();
			}
		msg = measures.poll();
		System.out.println("Medição Recebida e Retirada da Fila de Espera");
		} catch (InterruptedException e) {
			e.printStackTrace();
	}
return msg;
	}	
}
