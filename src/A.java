
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.nio.channels.CancelledKeyException;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName; 
public class A implements Runnable {
	
	public static String[] ALGORITHMSNAME= {"MD5","SHA-256","SHA-384","SHA-512"};
	public static char[] ABECEDARY= {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','ñ','u','v','w','x','y','z'};
	public static String orignial="";
	public static String msg="";
	public static int numthread,inicio=0,fin, threadactual;
	public static String generar_codigo(String message, String AlgorithmName)
	{
		String encryptedMessage="";
		boolean found = false;
		for(int i=0;i<ALGORITHMSNAME.length && !found;i++)
		{
			if(AlgorithmName.equals(ALGORITHMSNAME[i]))
			{
				found =!found;
				try { 
					MessageDigest md = MessageDigest.getInstance(AlgorithmName); 
					byte[] messageDigest = md.digest(message.getBytes()); 
					BigInteger no = new BigInteger(1, messageDigest); 
					encryptedMessage = no.toString(16); 
					while (encryptedMessage.length() < 32) 
					{ 
						encryptedMessage = '0' + encryptedMessage; 
					} 
					return encryptedMessage; 
				}  
				catch (NoSuchAlgorithmException e) 
				{ 
					throw new RuntimeException(e); 
				} 
			}
		}
		return encryptedMessage;
	}
	
	
	public static void identificar_entrada(String hash, String AlgorithmName)
	{
		int hackeadopai = 0;
		while(hackeadopai==0)
		{
			hackeadopai= generarCadenas(orignial.length(),hash,AlgorithmName);
			inicio=fin;
		}
		
		if(hackeadopai==-1)
		{
			System.out.println("no se pudo pai");
			Thread.currentThread().interrupt();
		}
	}
	
	public static int generarCadenas(int r, String hash,String AlgorithmName)
	{ 
		int output[] = new int[r];	
		output[r-1]=inicio;
		output[0]=inicio-1;
		int termino2=-1;
		while(termino2==-1 && output[r-1]<fin)
		{
			int index = 0;
			boolean termino = true;
			while(index < r && termino)
			{
				
				if(output[index] < ABECEDARY.length-1)
				{
					output[index]++;
					termino=false;
				}
				else
				{
					output[index]=0;
				}
				index++;
			}
			
			if(decrypt(ABECEDARY, r, output, hash, AlgorithmName)){
				termino2=1;
			}
		}
		return termino2;
	}
	private static boolean decrypt(char[] ABECEDARY, int r, int[] output,String hash, String AlgorithmName)
	{
	
		String x="";
		while(r-- > 0)
		{
			char a= ABECEDARY[output[r]];
		//	System.out.println(ABECEDARY[output[r]]+":"+output[r]);
			
			x+=a;	
			
			
		}
//		if(threadactual==1)
	//	System.out.println(x);
		String hashcreado= generar_codigo(x, AlgorithmName);
		if(hash.equals(hashcreado))
		{
			System.out.println("decrtpted");
			Thread.currentThread().interrupt();
			return true;
		}
		
		return false;
	}
	public static void main(String[] args) {
			
		numthread=1;
		String algoritmo="MD5";
		orignial="bbbbbb";
		msg= generar_codigo(orignial, algoritmo);
		ThreadPoolExecutor cpuExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
		CPUMonitor cpuMonitor = new CPUMonitor();
		cpuExecutor.execute(cpuMonitor);
		for (int i = 0; i < numthread; i++) {
			A obj =  new A();
			Thread tobj = new Thread(obj);
			try {tobj.start();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
//		System.out.println("Encrypted: "+ msg);
//		
//		identificar_entrada(0,2,msg,algoritmo);
//		long endTime = System.currentTimeMillis();

	//long duration = (endTime - startTime); 
		//System.out.println(duration+ " ms");
	}


	@Override
	public void run() {
		int len= ABECEDARY.length;
		int interval= len/numthread;
		//System.out.println(interval);
		fin = inicio+ interval;
	
			identificar_entrada(msg,"MD5");
		threadactual++;	
		
		
		
	
	}

}
