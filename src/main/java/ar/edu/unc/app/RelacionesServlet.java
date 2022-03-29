/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.unc.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mvc.v.Recurso;
import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.update.ServerUpdateManager;
import nextapp.echo.webcontainer.WebContainerServlet;


@WebServlet(
        name = "unc-sga Servlet",
        description = "este servlet arranca la app",
        urlPatterns = {"/","/app"},
        loadOnStartup = 1,
        asyncSupported = true
)

/**
 *
 * @author mc
 */
public class RelacionesServlet  extends WebContainerServlet {

    private static final long serialVersionUID = 0L;

//    private RelacionesApp ra;
    private ServerUpdateManager upMaSr;
    public static RelacionesApp gen;
    public static RelacionesApp getGen(){
        return gen;
    }
    private static String Ruta_Servicio; //=null;
    
    public static String getRuta_Servicio(){return Ruta_Servicio;}
    
    @Override
    public ApplicationInstance newApplicationInstance() {
        RelacionesApp.setClAplica(Thread.currentThread().getContextClassLoader());
        Recurso.set("gen80", "imagen/generador80.png");
        Recurso.set("registro", "imagen/meregistro.png");
        Recurso.setListaLogos("imagen/logounc171x85-1.png");
        Recurso.setListaLogos("imagen/logounc171x85-2.png");
        Recurso.setListaLogos("imagen/logounc171x85-3.png");
        Recurso.setListaLogos("imagen/logounc171x85-4.png");
        gen = new RelacionesApp(map, "jdbc/unc_SySO");
//        gen.setJndi("jdbc/unc_SySO");
        gen.setId(map.get("id"));
        
//        String[] bdset= ResuelveBD.getResuelveBD(Ruta_Servicio);
//        gen.setDbPass(bdset [1]);
//        gen.setDbUser(bdset [0]);
//        gen.setDbruta(bdset [2]);//pruebas en maquina local
//        gen.setDbDriver(bdset [3]);
        
        gen.setNombre("UNC HyS - Relaciones");
        
        
//        Recurso.setLista("imagen/imagen/"+"unc-700x357.jpg");
        return gen;
    }
    private Map<String, String> map=new HashMap<>();

    @Override
    public void destroy() {
        super.destroy(); 
        gen=null;
    }

    @Override
    protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.process(request, response); //To change body of generated methods, choose Tools | Templates.
        HttpSession session=request.getSession();
        session.setMaxInactiveInterval(300);
        try{
            Ruta_Servicio.equals(Ruta_Servicio);
        }catch(Exception EX){
            StringBuffer st = request.getRequestURL();
            Ruta_Servicio=st.substring(0,st.length()-12);
            System.out.println("request "+Ruta_Servicio);
        } 
        String text=request.getParameter("id");
        if (text!=null) map.put("id", text);
//        System.out.println(this.toString()+"=="+map);
    }
    
}
