/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.unc.app;
import app.GeneradorListener;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequestEvent;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionBindingEvent;
import nextapp.echo.app.ApplicationInstance;

@WebListener
/**
 *
 * @author mc
 */
public class WebListenerGenerador extends GeneradorListener{

    private static final long serialVersionUID = 0L;

    private static WebListenerGenerador insWebListenerGenerador;

    private Map<String, ApplicationInstance> mapaApp= new HashMap<>();
    
    public static WebListenerGenerador get(){
        return insWebListenerGenerador;
    }

    public WebListenerGenerador() {
        super();
        insWebListenerGenerador=this;
    }
 
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {      
//            super.contextInitialized(sce);
//         
//        sce.getServletContext().setSessionTimeout(45); // session timeout in minutes
//    }
    
//    @Override
//    public void attributeReplaced(HttpSessionBindingEvent sre) {
//        super.attributeReplaced(sre); //To change body of generated methods, choose Tools | Templates.
//        
//        System.out.println(">>>>>>>>>>>>>>>>>>>>"+sre.getName()
//        +"\n"+sre.getValue()
////        +"\n"+sre.getServletRequest().getRemoteAddr()
//        );
//    }
//    
////    @Override
////    public void requestInitialized(ServletRequestEvent sre) {
////        super.requestInitialized(sre);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>"+sre.getName()
//        +"\n"+sre.getValue()
////        +"\n"+sre.getServletRequest().getRemoteAddr()
//        );
////        ((RotuloImpresionServlet)sre.getServletContext().
//        try {
//            Object obj = mapaApp.get(sre.getSession().getServletContext().getContextPath());
//            obj.equals(obj);
//            try{
//                obj.getClass().getMethod("refresh", (Class[])null).invoke(obj, (Object[])null);
//            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException 
//                    | SecurityException | InvocationTargetException ex) {
//                Logger.getLogger(WebListenerGenerador.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } catch (NullPointerException npe){}    
//    }
//    
    public void registro(String nombre, ApplicationInstance app){
        mapaApp.put(nombre, app);
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        super.requestInitialized(sre); //To change body of generated methods, choose Tools | Templates.
//        System.out.println(">>>> "+sre.getServletContext().getContextPath());
//        String s="";
        try{
        for(String s:sre.getServletRequest().getParameterValues("op"))
                 System.out.println(">>>> "+sre.getServletContext().getServletContextName()+"   op >"+s);
        }catch(Exception ex){}
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent srae) {
        super.attributeAdded(srae); //To change body of generated methods, choose Tools | Templates.
        System.out.println("********hbe>>> "+srae.getName().split(":")[0]
                +" "+srae.getSession().getId()+" "+srae.getSession().getCreationTime());
        for(String s:srae.getSession().getValueNames())
                 System.out.println("   >"+s+" "+srae.getSession().getAttribute(s));
        
    }

//    @Override
//    public void attributeAdded(ServletRequestAttributeEvent srae) {
//        super.attributeAdded(srae); //To change body of generated methods, choose Tools | Templates.
//        System.out.println("srae>>> "+srae.getName()+" "+srae.getValue()
//                +" "+srae.getServletContext().getContextPath()
//                +"\nmapa >> "+mapaApp
//        );
//    }

//    @Override
//    public void attributeReplaced(ServletRequestAttributeEvent srae) {
//        super.attributeReplaced(srae); //To change body of generated methods, choose Tools | Templates.
//        System.out.println("ar******srae>>> "+srae.getName()+" "+srae.getValue()
////                +" "+srae.getServletContext().getContextPath()
////                +"\nmapa >> "+mapaApp
//        );
//    }
//    
    
 
    
    
}
