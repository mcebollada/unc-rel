/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.unc.app;

import app.IGenerador;
import app.LoginAbs;
import app.comp.Arbol;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import mvc.persistencia.bd.Conexion;
import mvc.persistencia.bd.Diccionario;
import mvc.persistencia.bd.Tabla;
import mvc.v.ColumnaBoton;
import mvc.v.FBoton;
import mvc.v.FBoton1P;
import mvc.v.FLabel;
import mvc.v.FPanel;
import mvc.v.FRotulo;
import mvc.v.FTexto1Captcha;
import mvc.v.FilaBoton;
import mvc.v.IView;
import mvc.v.Recurso;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.Button;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.Grid;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.Label;
import nextapp.echo.app.Window;
import nextapp.echo.app.list.DefaultListModel;
import nextapp.echo.extras.app.TabPane;

/**
 *
 * @author mc
 */
public class RelacionesApp extends ApplicationInstance 
        implements PropertyChangeListener, IGenerador{

    private static final long serialVersionUID = 0L;
//    private static final Color COLOR_I = new Color(0x7f9fbf);
//    private static final Font BIG_FONT = new Font(Font.COURIER_NEW, Font.BOLD, new Extent(30));
    private static final Font SBIG_FONT = new Font(Font.TIMES_NEW_ROMAN, Font.BOLD, new Extent(50));

    private static final String LOCAL_DEFECTO = "es";

    private static final ClassLoader clKernel = Thread.currentThread().getContextClassLoader();
    private static ClassLoader clAplica = Thread.currentThread().getContextClassLoader();
    private static Conexion conexion;
    private static String rutaSesion;
    private static Conexion conexdicc;

    public static ClassLoader getClKernel() {
        return clKernel;
    }

    public static ClassLoader getClAplica() {
        return clAplica;
    }

    public static void setClAplica(ClassLoader classloader) {
        clAplica = classloader;
    }

//    /**
//     * @return the conexion
//     */
//    public static Conexion getConexion() {
//        return conexion;
//    }

    /**
     * @return the conexdix
     */
    public static Conexion getConexdicc() {
        return conexdicc;
    }

    /**
     * @param aRutaSesion the rutaSesion to set
     */
    public static void setRutaSesion(String aRutaSesion) {
        rutaSesion = aRutaSesion;
    }

    /**
     * @return the rutaSesion
     */
    public static String getRutaSesion() {
        return rutaSesion;
    }

    /**
     * @return the LOCAL_DEFECTO
     */
    public static String getLOCAL_DEFECTO() {
        return LOCAL_DEFECTO;
    }

    /**
     * @param aConexion the conexion to set
     */
    public static void setConexion(Conexion aConexion) {
        conexion = aConexion;
    }

    public static void setConexdicc(Conexion aConexion) {
        conexion = aConexion;
    }

    private final Map<String, String> mapa;
    private IView panel;

    public final static String[] ids={"id"};
    private Tabla t;
    private int ancho;
    private IView fcblogon;
    private IView ftpass;
    private IView ftpassr;
    private IView ftcaptcha;
    private IView panel1;
    private Timer time;
    private TimerTask ttask;
    private IView recibo;
    private Conexion con;
    private String nombre;
    private String dbDri;
    private String dbPas;
    private String dbUse;
    private String dbPre;
    private String dbRut;
    private String nombr;
    private String jndi;
    
    public RelacionesApp( Map<String, String> map, String jndi) {
        mapa=map;
        this.jndi=jndi;
        createWidget();
//        this.addPropertyChangeListener(this);
        
    }
    
    @Override
    public Window init() {
        
        Window window = new Window();
        ContentPane contentPane = new ContentPane();
        window.setContent(contentPane);

        contentPane.add((Component) panel1);
//        System.out.println(window.getTitle());
        return window;
    }
    
    private Grid creoCabecera() {
        ancho = 1050;
        Grid cabe = new Grid(4);
        cabe.setColumnWidth(0, new Extent(240, Extent.PX));//im.getWidth());
        cabe.setColumnWidth(1, new Extent(50, Extent.PX));
        cabe.setColumnWidth(2, new Extent(ancho - 500, Extent.PX));
        cabe.setColumnWidth(3, new Extent(210, Extent.PX));
        segTitulo0(cabe, 0);
        segTitulo1(cabe, 1);
        segTitulo2(cabe, 2);
        segTitulo3(cabe, 3);
        return cabe;
    }

    private void segTitulo1(Grid cabe, Integer i) {
        Button bot = (Button) FBoton.creo(Recurso.get("email_go"), this, "envioEmailEmpresa");
        cabe.add((Component) bot, i);
    }

    private void segTitulo0(Grid cabe, Integer i) {
        ImageReference im = Recurso.getListaLogos();
        cabe.setHeight(im.getHeight());
        Button bot = (Button) FBoton.creo(im, this, "veoPaginaEmpresa");
        cabe.add((Component) bot, i);
    }

    private void segTitulo2(Grid cabe, Integer i) {
        IView usu = FRotulo.creo("Habilitado por:");
        Grid col = new Grid();
        col.setOrientation(Grid.ORIENTATION_VERTICAL);
        Label emp =new Label(getNombre());
        emp.setTextAlignment(Alignment.ALIGN_CENTER);
        emp.setTextPosition(Alignment.ALIGN_TOP);
        emp.setFont(SBIG_FONT);
        col.setRowHeight(0, new Extent(75, Extent.PERCENT));
        col.setRowHeight(1, new Extent(25, Extent.PERCENT));
        col.add(emp, 0);
        col.add((Component)usu, 1);
        cabe.add((Component) col, i);
    }

    private void segTitulo3(Grid cabe, Integer i) {
        String st = "SELECT lengua FROM lang order by defecto desc";
        Tabla tt = null;
        Grid lengua = new Grid(3);
        try {
            tt = Tabla.cargaTabla(st, getConexion());
            lengua = new Grid(3 + tt.getRowCount());
            lengua.add(doLabel("Estoy en:", Font.TIMES, Font.PLAIN, 8), 0);
            String localPais = ApplicationInstance.getActive().getLocale().getCountry();
            lengua.add(new Label(Recurso.get(localPais.toLowerCase())), 1);
            lengua.add(doLabel("  Lengua:", Font.TIMES, Font.PLAIN, 8), 2);
            for (int i1 = 0; i1 < tt.getRowCount(); i1++) {
                lengua.add((Component) FBoton1P.creo("", Recurso.get(tt.getCuerpoRotulo(i1, 0).toString()),
                        this, "ejecutoIdioma", tt.getCuerpoRotulo(i1, 0).toString()), i1 + 3);
            }
        } catch (Exception ex) {
        }

        Grid col = new Grid(3);
        col.setOrientation(Grid.ORIENTATION_VERTICAL);
        col.setRowHeight(0, new Extent(50, Extent.PERCENT));
        col.setRowHeight(1, new Extent(20, Extent.PERCENT));
        col.setRowHeight(2, new Extent(30, Extent.PERCENT));
        String sttt = "Singular es marca registrada y el framework singular propiedad intelectual de Marcelo Cebollada";

        Label rr = new Label(sttt);
        rr.setLineWrap(true);
        rr.setTextAlignment(Alignment.ALIGN_CENTER);
        rr.setTextPosition(Alignment.ALIGN_BOTTOM);
        rr.setFont(new Font(Font.TIMES_NEW_ROMAN, Font.BOLD, new Extent(8)));
        //        col.add(new Label(Recurso.get("singular")), 0);
        Button bot = (Button) FBoton.creo(Recurso.get("singular"), this, "veoPagina");
        bot.setToolTipText(sttt);
        col.add(bot, 0);
//        col.add(new Label(""), 1);
        col.add(lengua, 1);
        bot = (Button) FBoton.creo(Recurso.get("email_go"), this, "envioEmail");
        bot.setToolTipText("Envio e-mail a Singular");
        col.add(bot, 2);
        cabe.add((Component) col, i);
    }

    private Label doLabel(String doc, Font.Typeface tipo, Integer estilo, Integer alto) {
        Label la = new Label(doc);
        la.setFont(new Font(tipo, estilo, new Extent(alto)));
        la.setLineWrap(false);
        la.setFormatWhitespace(true);
        return la;
    }

    private void createWidget() {
        panel1=FPanel.creo("");
        panel1.getCmp().add(creoCabecera(),-1);
        ((Component)((FPanel)panel1).getMensaje()).setVisible(false);
        panel1.add(FLabel.creo("nnn", ""));
        IView fb = FilaBoton.creo("fb");
        fb.add(recibo=FBoton.creo("Recibo Comunicacion", this, "comunicacion"));
        panel1.add(fb);
        recibo.setColor(Color.GREEN);
        ((Component)((FPanel)panel1).getMensaje()).setVisible(false);
        panel1.add(FLabel.creo("nnn", ""));
        
        panel=FPanel.creo("");
        panel.setNombre("comunicacion");
        panel1.add(panel);
    }
    public void comunicacion(){ //createWidget1() {
//        String uri="http://localhost:7001/unc-sga/comunicacion?id="+id;
//        this.enqueueCommand(
//                new BrowserOpenWindowCommand(uri, "Comunicacion", new Extent(1050), new Extent(800),
//                        BrowserOpenWindowCommand.FLAG_RESIZABLE+BrowserOpenWindowCommand.FLAG_REPLACE));        
//        recibo.getCmp().setVisible(false);
//        System.out.println(">>>>> comunicacion()");
        String sql ="";
        try {
        sql = "select pre.id_estado, pr.id_persona_rol, p.*, tr.*, uo.*, pre.digesto_acceso " +
            "from sga_persona_rol_estado pre " +
            "	join sga_persona_rol pr on pre.id_persona_rol=pr.id_persona_rol and now() between pr.desde and pr.hasta " +
            "    join sga_unidad_organizacional uo on pr.id_unidad_organizacional=uo.id_unidad_organizacional " +
            "    join sga_persona p on pr.id_persona=p.id_persona " +
            "    join sga_tipo_rol tr on pr.id_tipo_rol= tr.id_tipo_rol " +
            "where exists(select pre1.id_persona_rol from sga_persona_rol_estado pre1 where pre1.marca between pre1.marca and addtime(pre1.marca, '24:1:0.0') " +
            "	and pre1.digesto_acceso= '"+ mapa.get("id") + "' " +
            "    and pre1.id_persona_rol=pre.id_persona_rol)" +
            "order by pre.marca desc";
//        System.out.println(">>>>> "+sql);
            System.out.println(">>>> "+getConexion().toString());
            t = Tabla.cargaTabla(sql, getConexion());
            System.out.println(">>>> "+t);
            t.getCuerpoRotulo("digesto_acceso", 0).equals(t.getCuerpoRotulo("digesto_acceso", 0));
            veoProc();
        } catch (Exception ex) {
            System.out.println(sql+"\n"+ex);
            errorProc();
        }
    }
    
    private void errorProc(){
        try{
            FLabel fl = FLabel.creo(panel, "rotulo", Font.TIMES_NEW_ROMAN, 
                    "Acceso no valido", Font.BOLD, 36, 900);
            FLabel.creo(panel, "rotulo1", Font.TIMES_NEW_ROMAN, 
                    "", Font.BOLD, 36, 900);
            fl.setAlignment(Alignment.ALIGN_CENTER);
            IView fb = FilaBoton.creo("fb1");
            panel.add(fb);
            FLabel.creo(fb, "l1", Font.TIMES_NEW_ROMAN, 
                    "Acceso puede ser invalido por: ", Font.BOLD, 16, 900);
            IView cb = ColumnaBoton.creo("cb");
            fb.add(cb);
            FLabel.creo(cb, "l1", Font.TIMES_NEW_ROMAN, 
                    "      no provee un id valido.", Font.PLAIN, 16, 900);

            FLabel.creo(cb, "l1", Font.TIMES_NEW_ROMAN, 
                    "      el id ya esta vencido.", Font.PLAIN, 16, 900);

            FLabel.creo(cb, "l1", Font.TIMES_NEW_ROMAN, 
                    "      el id ya ha sido utilizado.", Font.PLAIN, 16, 900);

    //        Logger.getLogger(RelacionesApp.class.getName()).log(Level.SEVERE, null, ex);
            terminoVer(5);
        }catch(Exception ex1){
            Logger.getLogger(RelacionesApp.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    private void veoProc() {
        String deno = t.getCuerpoRotulo("tratamiento", 0)+". "
            +t.getCuerpoRotulo("nombre", 0)+" "
            +t.getCuerpoRotulo("apellido", 0);
        FLabel fl = FLabel.creo(panel, "rotulo", Font.TIMES_NEW_ROMAN, 
                "Forma de Aceptacion/Rechazo de responsabilidad de rol", Font.BOLD, 36, 900);
        fl.setAlignment(Alignment.ALIGN_CENTER);
        FLabel.creo(panel, "rotulo1", Font.TIMES_NEW_ROMAN, 
                "", Font.BOLD, 36, 900);
        fl= FLabel.creo(panel, "l1", Font.TIMES_NEW_ROMAN, 
                deno, Font.BOLD, 20, 900);
        fl.setHeight(new Extent(32));
        String texto="Ud. debe tomar la decision de tomar la responsabilidad que se le"
                + " asigna a traves de la designacion en el rol que se especifica"
                + " abajo. La aceptacion, lo acredita para ejecutar los procesos"
                + " disponibles aplicables al nivel de la Unidad Organizacional"
                + " que se le detalla y las dependientes de la misma. Para mayor"
                + " informacion comunicarse con el equipo de entrenamiento y consulta";
        fl=FLabel.creo(panel, "l2", Font.TIMES_NEW_ROMAN, texto, Font.PLAIN, 18, 950);
        fl.setHeight(new Extent(70));
        ((Label)fl.getCmp()).setLineWrap(true);
        texto="Lugar a desempeñar la tarea: "+t.getCuerpoRotulo("id_unidad_organizacional", 0)
                + " - Descripcion: "+t.getCuerpoRotulo("desc_unidad_organizacional", 0);
        fl=FLabel.creo(panel, "l3", Font.TIMES_NEW_ROMAN, texto, Font.BOLD, 18, 950);
        fl.setHeight(new Extent(30));
        texto="Responsabilidad: "+t.getCuerpoRotulo("id_tipo_rol", 0)
                + " - Descripcion: "+t.getCuerpoRotulo("desc_tipo_rol", 0);
        fl=FLabel.creo(panel, "l4", Font.TIMES_NEW_ROMAN, texto, Font.BOLD, 18, 950);
        fl.setHeight(new Extent(30));
        
        texto="Se requiere que Ud elija una opcion 'Acepto' o 'Rechazo' el rol en que se le ha designado.";
        fl=FLabel.creo(panel, "l5", Font.TIMES_NEW_ROMAN, texto, Font.BOLD, 20, 950);
        fl.setHeight(new Extent(30));
        fl.setAlignment(Alignment.ALIGN_CENTER);
        
        DefaultListModel dlm = new DefaultListModel();
        
        String st = t.getCuerpoRotulo("nombre", 0).toString().toLowerCase().substring(0, 1)
                +t.getCuerpoRotulo("apellido", 0).toString().toLowerCase();
        String stdoc=t.getCuerpoRotulo("doc", 0).toString().toLowerCase();
        String stemail=t.getCuerpoRotulo("email", 0).toString().toLowerCase();
        String st1 =t.getCuerpoRotulo("id_unidad_organizacional", 0).toString().toLowerCase();
        String st2 =t.getCuerpoRotulo("id_tipo_rol", 0).toString().toLowerCase();
        dlm.add(st);
        dlm.add(st+st1);
        dlm.add(st+st2);
        dlm.add(stdoc);
        dlm.add(stdoc+st1);
        dlm.add(stdoc+st2);
        dlm.add(stemail);
        
//        FilaBoton fb = (FilaBoton) FilaBoton.creo("fb");
//        fcblogon = FComboBox.creo("Nombre de Usuario");
//        fcblogon.setMultiple(dlm);
//        fb.add(fcblogon);
//        ftpass = mvc.v.FTextoP.creo("Clave Usuario","clave por defecto\n'unc-sga'");
//        fb.add(ftpass);
//        ftpassr = mvc.v.FTextoP.creo("Repita Clave","Por favor \nrepita la clave");
//        fb.add(ftpassr);
//        ftpass.set("unc-sga");
//        ftpassr.set("unc-sga");
//        ftcaptcha = mvc.v.FTexto1Captcha.creo("texto");
//        fb.add(ftcaptcha);
//        
//        panel.add(fb);

        FilaBoton fb = (FilaBoton) FilaBoton.creo("fbb");
//        fb.add(FRotulo.creo(""));
        fb.add(FBoton.creo("Acepto", "imageni/accept.png", this, "acepto"));
//        panel.add(fb);
//        fb = (FilaBoton) FilaBoton.creo("fb1");
//        fb.add(FRotulo.creo( ""));
        fb.add(FBoton.creo("Rechazo", "imageni/cancel.png", this, "rechazo"));
        panel.add(fb);
    }
    
    public void acepto(){
        Boolean cond = true;//ftpass.get().equals(ftpassr.get()) & ((FTexto1Captcha)ftcaptcha).esvalido();
        ((Component)((FPanel)panel1).getMensaje()).setVisible(true);
        try {
            Object invoke = getClass().getMethod("acepto"+cond.toString(), (Class [])null)
                    .invoke(this, (Object[])null);
//            this.dispose();
        } catch (Exception ex) {
            Logger.getLogger(RelacionesApp.class.getName()).log(Level.SEVERE, "acepto()", ex);
        }
    }
    public void aceptotrue(){
        try {
            String qwr0 = "update sga_persona_rol set user= null" //+fcblogon.get()
                    +", pass=sha1('unc_sga"//+ftpass.get()
                    +"'), pass_fecha=now(), pass_cambio=0 "
                    +"where id_persona_rol="+t.getCuerpoRotulo("id_persona_rol", 0).toString();
            
            String sel="select * from v_estados_siguiente where id_estado_ant='"
                    +t.getCuerpoRotulo("id_estado", 0).toString()
                    +"' and id_estado_act like '%acept%'";
            Tabla tsel=Tabla.cargaTabla(sel, getConexion());
            
            String qwr1 = "insert sga_persona_rol_estado values('"
                    +t.getCuerpoRotulo("id_persona_rol", 0).toString()
                    +"', now(), '"+tsel.getCuerpoRotulo("id_estado_act", 0).toString()
                    +"','',null)";
            Boolean b=tsel.ejecutaUpdate(qwr0)&&tsel.ejecutaUpdate(qwr1);
//            System.out.println((b=tsel.ejecutaUpdate(qwr0))+" >>>>>"+qwr0);
//            System.out.println((b=b&&tsel.ejecutaUpdate(qwr1))+" >>>>>"+qwr1);
            ((FPanel)panel1).getMensaje().set(b?"Actualizado con exito. Cierre la pestaña del navegador"
                    :"Fallo la actualizacion. Cierre la pestaña del navegador");
//            this.dispose();
            terminoVer(0);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "", ex);
            ((FPanel)panel1).getMensaje().set("Error en la actualizacion, "+ex+". Cierre la pestaña del navegador");
        }
    }
    public void aceptofalse(){
        ((FTexto1Captcha)ftcaptcha).regenera();
        ftpass.set("");
        ftpassr.set("");
        ((FPanel)panel1).getMensaje().set("Hay inconsistencias entre las claves o el texto verificador");
    }
   
    
    public void rechazo(){
        ((Component)((FPanel)panel1).getMensaje()).setVisible(true);
        String sel="select * from v_estados_siguiente where id_estado_ant='"
                +t.getCuerpoRotulo("id_estado", 0).toString()
                +"' and id_estado_act like '%rechaz%'";
        String qwr1 ="";
        try {
            Tabla tsel=Tabla.cargaTabla(sel, getConexion());
            qwr1 = "insert sga_persona_rol_estado values("
                    +t.getCuerpoRotulo("id_persona_rol", 0).toString()
                    +", now(), '"+tsel.getCuerpoRotulo("id_estado_act", 0).toString()
                    +"','',null)";
            System.out.println(getClass().getName()+" rechazo() "+qwr1);
            tsel.ejecutaUpdate(qwr1);
            
            ((FPanel)panel1).getMensaje().set("Ud ha rechazado el rol y el proceso ha terminado. Cierre la pestaña del navegador");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, qwr1, ex);
        }
        terminoVer(0);
//        this.dispose();
    }
    
    private Conexion getConexion(){
        try{
            con.equals(con);
            return con;
        }catch(NullPointerException npe){ 
            Logger.getLogger(RelacionesApp.class.getName()).log(Level.INFO, "ini conexion servlet RelacionesApp");
            try {
                con=Conexion.creo(getDbDriver(),getDbruta(), getDbUser(), getDbPass());
                Logger.getLogger(RelacionesApp.class.getName()).log(Level.INFO, "Conexion BD servlet RelacionesApp con driver");
                return con;
            } catch (Exception ex) {
                Logger.getLogger(RelacionesApp.class.getName()).log(Level.INFO, "fallo conexion BD servlet RelacionesApp");
                try{
                    con=Conexion.creo(getJndi());
                    Logger.getLogger(RelacionesApp.class.getName()).log(Level.INFO, "Conexion BD servlet RelacionesApp con JNDI");
                    return con;
                } catch (Exception ex1) {
                    Logger.getLogger(RelacionesApp.class.getName()).log(Level.INFO, "fallo conexion JNDI servlet RelacionesApp: "+getJndi());
                }
            }
        }
        
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
//    System.out.println(System.currentTimeMillis()+" >>>"+mapa);
        try{
            panel.equals(panel);
           ((Column) panel1.getCmp()).remove((Component)panel);
//    System.out.println("try >>>"+panel1.toString());
        }catch(Exception ex){
    System.out.println("catch >>>"+panel1.toString());
        }finally{
//    System.out.println("finally >>>"+panel1.toString());
            panel=FPanel.creo("");
            panel1.add(panel);
        }
    }
    
    
    private String id;

    public static final String PROP_ID = "id";

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(String id) {
        String oldId = this.id;
        this.id = id;
        propertyChangeSupport.firePropertyChange(PROP_ID, oldId, id);
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private void terminoVer(Integer segundo) {
        ((Component)panel).setVisible(false);
        
//        time=new Timer();
//        ttask= new TimerTask() {
//            @Override
//            public void run() {
//                panel.getCmp().setVisible(false);
//                panel1.remove(panel);
//                panel=FPanel.creo("");
//                panel.setNombre("comunicacion");
//                panel1.add(panel);        
////                recibo.getCmp().setVisible(true);
//                panel.refresco();
//            }
//        };
////        int segundo=5;
//        time.schedule(ttask, segundo*1000);
////        try {
////            int segundo=5;
////            Thread.sleep(segundo*1000);
////            panel1.remove(panel);
////            panel=FPanel.creo("");
////            panel.setNombre("comunicacion");
////            panel1.add(panel);
////
////        } catch (InterruptedException ex) {
////            Logger.getLogger(RelacionesApp.class.getName()).log(Level.SEVERE, null, ex);
////        }
    }
    /**
     * @return the nombre
     */

    public String getNombre() {
        return nombre;
    }

    public String getClaseAcercaDe() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getDbDriver() {
        return dbDri;
    }

    public String getDbPass() {
        return dbPas;
    }

    public String getDbUser() {
        return dbUse;
    }

    public String getDbpre() {
        return dbPre;
    }

    @Override
    public String getDbruta() {
        return dbRut;
    }

    @Override
    public String getEmail() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Label getEmpresa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getJndi() {
        return jndi;
    }

    @Override
    public String getRotuloAcercaDe() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTipEmpresa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IView getUsuario() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setClaseAcercaDe(String claseAcercaDe, String rot) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setClaseAcercaDe(String claseAcercaDe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDbDriver(String dbDriver) {
        dbDri=dbDriver;
    }

    @Override
    public void setDbPass(String dbPass) {
        dbPas=dbPass;
    }

    @Override
    public void setDbUser(String dbUser) {
        dbUse=dbUser;
    }

    @Override
    public void setDbpre(String st) {
        dbPre=st;
    }

    @Override
    public void setDbruta(String dbruta) {
        dbRut=dbruta;
    }

    @Override
    public void setEmpresa(Label empresa) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTipEmpresa(String tipempresa) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setEmail(String email) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCommand(String command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setJndi(String jndi) {
        this.jndi=jndi;
    }

//    public void setLogin(Login login) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public void setNombre(String nombr) {
        nombre=nombr;
    }

    @Override
    public void setRotuloAcercaDe(String rotuloAcercaDe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUsuario(IView usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Component createPTab(boolean closeEnabled, FPanel fp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTree(Arbol la) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TabPane getTpane() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Diccionario getLocalDiccionario() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLocalDiccionario(Diccionario localDiccionario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HttpServletRequest getServletRqt() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setServletRqt(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Conexion getConex() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Conexion getConexDicc() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLogin(LoginAbs login) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getClaseAcercaDeN1() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRotuloAcercaDeN1() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setClaseAcercaDeN1(String claseAcercaDe, String rot) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setClaseAcercaDeN1(String claseAcercaDe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRotuloAcercaDeN1(String rotuloAcercaDe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


   
}
