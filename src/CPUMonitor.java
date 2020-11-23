import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class CPUMonitor implements Runnable {
   private File file;
   private FileWriter fw;
   private int contu=0;
   public CPUMonitor () {
       String ruta = "./MD5usocpu1.txt";
       file = new File(ruta);
       try {
           if (!file.exists()) {
               file.createNewFile();
           }
           fw = new FileWriter(file);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   public synchronized void escribirMensaje(String pCadena) {

       try {
           FileWriter fw = new FileWriter(file,true);
           fw.write(contu+","+pCadena + "\n");
           contu++;
           fw.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   public void run() {
       Runnable CPUmeter = new Runnable() {
           public void run() {
               try {
                  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                  LocalDateTime now = LocalDateTime.now();
                   double cpuLoad = getSystemCpuLoad();
                   escribirMensaje(String.valueOf(cpuLoad));
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       };
       ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
       executor.scheduleAtFixedRate(CPUmeter, 0, 1, TimeUnit.SECONDS);
   }
   public double getSystemCpuLoad() throws Exception {
       MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
       ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
       AttributeList list = mbs.getAttributes(name, new String[]{ "SystemCpuLoad" });
       if (list.isEmpty()) return Double.NaN;
       Attribute att = (Attribute)list.get(0);
       Double value = (Double)att.getValue();
       if (value == -1.0) return Double.NaN;
       return ((int)(value * 1000) / 10.0);
   }
}
