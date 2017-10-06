/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scripts;
import java.io.IOException;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
/**
 *
 * @author root
 */
public class RunScript implements Runnable{
    String comando;

    public RunScript(String comando) {
        this.comando = comando;
    }

    

    public void RunScriptLinea(String command){
        String sCommandString = command;
        int iExitValue;
        CommandLine oCmdLine = CommandLine.parse(sCommandString);
        DefaultExecutor oDefaultExecutor = new DefaultExecutor();
        oDefaultExecutor.setExitValue(0);
        try {
            iExitValue = oDefaultExecutor.execute(oCmdLine);
        } catch (ExecuteException e) {
            System.err.println("Execution failed.");
        } catch (IOException e) {
            System.err.println("permission denied.");
        }
    }
    
    

    @Override
    public void run() {
        RunScriptLinea(comando);
    }
    
    public static void main(String[] args) {
        RunScript rs1= new RunScript("perl /home/soporte/scripts_backup/getInfoRouter.pl");
        RunScript rs2= new RunScript("bash /home/soporte/scripts_backup/initTelnet.sh 10.10.10.2");
        
        new Thread(rs1).start();
        new Thread(rs2).start();
    }
    
}
