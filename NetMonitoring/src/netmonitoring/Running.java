/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmonitoring;

/**
 * Contiene la fecha y ruta en donde se guarda el archivo con los running-config de los dispositivos.
 * @author Ronny Alcívar
 */
public class Running {
    private String date,path;

    public Running(String date, String path) {
        this.date = date;
        this.path = path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Running{" + "date=" + date + ", path=" + path + '}';
    }
    
    
    
}
