/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmonitoring;

import MysqlBD.MysqlDB;
import Scripts.RunScript;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Ronny Alcívar
 */
public class MainController implements Initializable {

    @FXML
    private Button btActualizar;
    @FXML
    private Button btRAccess;
    @FXML
    private TextArea txtVersion;
    @FXML
    private TextArea txtInterfaces;
    @FXML
    private TableView<Running> tvConfigs;
    @FXML
    private TableColumn<Running, String> tcFecha;
    @FXML
    private TableColumn<Running, String> tcPath;
    @FXML
    private TextArea txtConfig;
    @FXML
    private TextArea txtLogs;
    @FXML
    private TextArea txtRouting;
    @FXML
    private TableView<Device> tvDevices;
    @FXML
    private TableColumn<Device, String> tcHost;
    @FXML
    private TableColumn<Device, String> tcIp;
    @FXML
    private TableView<Cliente> tvCliente;
    @FXML
    private TableColumn<Cliente,String> tcIDC;
    @FXML
    private TableColumn<Cliente,String> tcName;
    
    private ObservableList<Cliente> dataCliente;
    
    private ObservableList<Device> dataRouters;
    
    private ObservableList<Running> dataRunning;

    private final ListChangeListener<Cliente> tableSelectionChangedClient =
            (ListChangeListener.Change<? extends Cliente> change) -> {
                updateBugDetailsClient();
    };
    
    private final ListChangeListener<Device> tableSelectionChangedDevice=
            (ListChangeListener.Change<? extends Device> change) -> {
                updateDetailsDevices();
    };
    
    private final ListChangeListener<Running> tableSelectionChangedRunning=
            (ListChangeListener.Change<? extends Running> change) -> {
                updateDetailsRunning();
    };
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RunScript rs1= new RunScript("perl /home/soporte/scripts_backup/getInfoRouter.pl");
        new Thread(rs1).start();
        
        tcIDC.setCellValueFactory(new PropertyValueFactory("ID"));
        tcName.setCellValueFactory(new PropertyValueFactory("name"));
        tcHost.setCellValueFactory(new PropertyValueFactory("hostname"));
        tcIp.setCellValueFactory(new PropertyValueFactory("ip"));
        tcFecha.setCellValueFactory(new PropertyValueFactory("date"));
        tcPath.setCellValueFactory(new PropertyValueFactory("path"));
        
        dataRouters=null;
        dataRunning=null;
        dataCliente=cargarDatosClientes();
        tvCliente.setItems(dataCliente);

        final ObservableList<Cliente> tableSelection = tvCliente.getSelectionModel().getSelectedItems();
        tableSelection.addListener(tableSelectionChangedClient);
        
        final ObservableList<Device> tableSelectDevice = tvDevices.getSelectionModel().getSelectedItems();
        tableSelectDevice.addListener(tableSelectionChangedDevice);
        
        final ObservableList<Running> tableSelectRunning = tvConfigs.getSelectionModel().getSelectedItems();
        tableSelectRunning.addListener(tableSelectionChangedRunning);
        
        btActualizar.setOnMouseClicked((MouseEvent event) -> {
            RunScript rActualizar= new RunScript("perl /home/soporte/scripts_backup/getInfoRouter.pl");
            new Thread(rActualizar).start();
            txtVersion.clear();
            txtInterfaces.clear();
            txtRouting.clear();
            txtLogs.clear();
            txtConfig.clear();
            tvConfigs.getItems().clear();
        });
        
        btRAccess.setOnMouseClicked((MouseEvent event) -> {
            Runnable ipJOp =new Runnable() {
                @Override
                public void run() {
                    String ip=null;
            try{
                ip=JOptionPane.showInputDialog("Ingrese la ip del host remoto: ");
            }catch(HeadlessException e){
                return;
            }
            if(ip==null)
                return;
            
            if(ip.equalsIgnoreCase(""))
                return;
            
            RunScript rTelnet= new RunScript("bash /home/soporte/scripts_backup/initTelnet.sh "+ip);
            new Thread(rTelnet).start();
                }
            };
            
            new Thread(ipJOp).start();
            
            
        });
        
  
    }    
    
    /*
    Obtiene la una instancia de la clase Cliente que se encuentra seleccionado para mostrar los routers asociados a este.
    */    
    public Cliente getSelectedClient() {
        if (tvCliente != null) {
            List<Cliente> selectedCliente = tvCliente.getSelectionModel().getSelectedItems();
            if (selectedCliente.size() == 1) {
                final Cliente selectedClient = selectedCliente.get(0);
                System.out.println(selectedClient); 
                return selectedClient;
            }
        }
        return null;
    }
    /*
    Obtiene la una instancia de la clase Device que se encuentra seleccionado para mostrar los distintos datos asociados a este router.
    */
    public Device getSelectedDevice() {
        if (tvDevices != null) {
            List<Device> selectedDevice = tvDevices.getSelectionModel().getSelectedItems();
            if (selectedDevice.size() == 1) {
                final Device selectedDev = selectedDevice.get(0);
                System.out.println(selectedDev); 
                return selectedDev;
            }
        }
        return null;
    }
    /*
    Obtiene la una instancia de la clase Running que se encuentra seleccionado para mostrar el contenido del archivo.
    */
    public Running getSelectedRunning() {
        if (tvConfigs != null) {
            List<Running> selectedRunning = tvConfigs.getSelectionModel().getSelectedItems();
            if (selectedRunning.size() == 1) {
                final Running selectedRun = selectedRunning.get(0);
                System.out.println(selectedRun); 
                return selectedRun;
            }
        }
        return null;
    }
    /*
    
    */
    private void updateBugDetailsClient() {
        
        Cliente client= getSelectedClient();
        if(client==null)
            return;
        
        if(dataRouters!=null)
            if(!dataRouters.isEmpty())
                dataRouters.clear();
        txtVersion.clear();
        txtInterfaces.clear();
        txtRouting.clear();
        txtLogs.clear();
        txtConfig.clear();
        tvConfigs.getItems().clear();
        
        dataRouters=cargarDatosRouters(client.getID());
        tvDevices.setItems(dataRouters);
    }
    
    private void updateDetailsDevices(){
        Device device= getSelectedDevice();
        if(device==null)
            return;
        txtVersion.clear();
        txtInterfaces.clear();
        txtRouting.clear();
        txtLogs.clear();
        tvConfigs.getItems().clear();
        txtConfig.clear();
        
        String cod= cargarInfoRouter(device.getIp());
        if(cod==null)
            return;
        dataRunning=cargarDatosRunning(cod);
        tvConfigs.setItems(dataRunning);
    }
    
    private void updateDetailsRunning(){
        Running run= getSelectedRunning();
        if(run==null)
            return;
        Scanner config;
        try {
            
            config = new Scanner(new File(run.getPath())).useDelimiter("\n");
            txtConfig.clear();
            while(config.hasNext()){
                txtConfig.appendText(config.next()+"\n");
            }
            
        } catch (FileNotFoundException ex) {
            System.err.println("No se pudo cargar el archivo.");
        }
        
        
        
        
    }
    
    private ObservableList<Cliente> cargarDatosClientes(){
        try {
            ObservableList<Cliente> list= FXCollections.observableArrayList();
            String sql="SELECT *  FROM clientes ;";
            
            ResultSet rs= MysqlDB.MysqlResult(sql);
            
            while(rs.next()){
                
                Cliente ifpc= new Cliente(rs.getString(1),rs.getString(2));
                list.add(ifpc);
            }
            
            return list;
        } catch (SQLException ex) {
            return null;
        }
    }
    
    private ObservableList<Device> cargarDatosRouters(String codCliente){
       
        try {
            ObservableList<Device> list= FXCollections.observableArrayList();
            String sql="select HOSTNAME,IP from routers where ID_CLIENT="+codCliente+";";
            
            ResultSet rs= MysqlDB.MysqlResult(sql);
            
            while(rs.next()){
                Device dev= new Device(rs.getString(1),rs.getString(2));
                list.add(dev);
            }
            
            return list;
        } catch (SQLException ex) {
            return null;
        }
    }
    
    private ObservableList<Running> cargarDatosRunning(String codDev){
       
        try {
            ObservableList<Running> list= FXCollections.observableArrayList();
            String sql="select fecha,PATH from configs where ID_DEV="+codDev+";";
            
            ResultSet rs= MysqlDB.MysqlResult(sql);
            
            while(rs.next()){
                Running dev= new Running(rs.getString(1),rs.getString(2));
                list.add(dev);
            }
            
            return list;
        } catch (SQLException ex) {
            return null;
        }
    }
    
    private String cargarInfoRouter(String ip){
        String sql="select PATH_VERSION,PATH_INTERFACES,PATH_ROUTING,"
                + "PATH_LOGS,ID from routers where IP='"+ip+"';";
        
        try {
            ResultSet rs= MysqlDB.MysqlResult(sql);
            if(rs==null)
                return null;
            
            Scanner version,interfaces,route,logs;
            while(rs.next()){
                
                version = new Scanner(new File(rs.getString(1))).useDelimiter("\n");
                while(version.hasNext()){
                    txtVersion.appendText(version.next()+"\n");
                }
                interfaces = new Scanner(new File(rs.getString(2))).useDelimiter("\n");
                while(interfaces.hasNext()){
                    txtInterfaces.appendText(interfaces.next()+"\n");
                }
                route = new Scanner(new File(rs.getString(3))).useDelimiter("\n");
                while(route.hasNext()){
                    txtRouting.appendText(route.next()+"\n");
                }
                logs = new Scanner(new File(rs.getString(4))).useDelimiter("\n");
                while(logs.hasNext()){
                txtLogs.appendText(logs.next()+"\n");
                }
                
                return rs.getString(5);
                
            }    
        } catch (FileNotFoundException | SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
