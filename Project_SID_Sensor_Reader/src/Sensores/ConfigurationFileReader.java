package Sensores;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ConfigurationFileReader {
	
	
private String topic;
private int qos;
private String broker;
private String mySqlUser;
private String mongoDBName;
private String collectionName;
private String SQLDBName;
private String pathName="C:\\Users\\Samuel\\Desktop\\Ficheiro de Configuração Mongo\\Propriedades_Config.txt";
private Long sleepTime;

	public ConfigurationFileReader() {
		
		loadConfiguration();
		
		}
	
	
public void loadConfiguration() {
	
    try {
		Scanner input = new Scanner(new FileReader(pathName));
	    while(input.hasNext()) {
	    	
	    	String line= input.nextLine();
	    	String[] splited= line.split(": ");
	    	String key=splited[0];
	    		if(key.equals("Tópico")) {
	    			this.topic=splited[1];
	    		}
	    		else if(key.equals("QoS")) {
	    			this.qos=Integer.parseInt(splited[1]);
	    		}
	    		else if(key.equals("Broker")) {
	    			this.broker=splited[1];

	   
	    		}
	    		else if(key.equals("mySQL User")) {
	    			this.mySqlUser=splited[1];
	    		}
	    		
	    		else if(key.equals("Nome Base de Dados Mongo")) {
	    			this.mongoDBName=splited[1];
	
	    		}
	    		
	    		else if(key.equals("Nome da Colecção")) {
	    			this.collectionName=splited[1];
	    		}
	    		else if(key.equals("Nome da Base de Dados MySQL")) {
	    			
	    			String[]v= splited[1].split(" ");
	    			this.SQLDBName=v[0];
	    		}
	    		else if( key.equals("Periodicidade de Busca à BD Mongo (em segundos)")) {
	    			String sleep = new String(splited[1].concat("000"));
	    			this.sleepTime=Long.parseLong(sleep);
	    			
	    		}
	    	}
	    input.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}

public String getTopic() {
	
	return topic;
}

public int getQoS() {
	
	return qos;
}

public String getBroker() {
	 return broker;

}

public String getSQLUser() {
	 return mySqlUser;

}

public String getMongoDBName() {
	 return mongoDBName;

}

public String collectionName() {
	 return collectionName;

}

public String getSQLDB() {
	 return SQLDBName;

}

public Long getSleepTime() {
	
	return this.sleepTime;
}



}
