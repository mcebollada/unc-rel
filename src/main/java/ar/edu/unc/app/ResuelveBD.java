/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.unc.app;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mc
 */
public class ResuelveBD {

    private static String rfs(String ruta) {
        String[] rs1 = ruta.split("//");
        String rs = rs1[rs1.length-1].split(":")[0].toLowerCase();
        String[] rss = rs.split("\\.");
        return rss.length>4?rss[0]:rs;
        
    }
    private String[] bdset;

    public static String[] getResuelveBD(String ruta){
        try {
            Logger.getLogger(ResuelveBD.class.getName()).log(Level.INFO, ruta+"-->"+ rfs(ruta));
            return rbd.get(rfs(ruta));
        }catch(NullPointerException npe){
            rbd= new HashMap<>();
            String[] valor={"us_hys_dev","eiKiun8u","jdbc:mariadb://databases-mariadb-4.psi.unc.edu.ar/hys_dev","org.mariadb.jdbc.Driver"};
            rbd.put("hys-dev", valor);
            String[] valor1={"us_hys_demo","Roagtish","jdbc:mariadb://databases-mariadb-dev.psi.unc.edu.ar/hys_demo","org.mariadb.jdbc.Driver"};
            rbd.put("hys-demo", valor1);
            String[] valor2={"us_hys","LelEvfess","jdbc:mariadb://databases-mariadb-5.psi.unc.edu.ar/hys","org.mariadb.jdbc.Driver"};
            rbd.put("hys", valor2);
            String[] valor3={"DocUser","docuser","jdbc:mysql://127.0.0.1/unc","com.mysql.jdbc.Driver"};
//            String[] valor3={"DocUser","docuser","jdbc:mysql://192.168.1.37/unc","com.mysql.jdbc.Driver"};
            rbd.put("localhost", valor3);
            return rbd.get(rfs(ruta));
        }
    }
    
    private static Map<String, String[]> rbd= null; //new HashMap<>();
    
    
    
}
