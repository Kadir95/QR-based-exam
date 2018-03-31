/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author mzp7
 */


public class FileOps {
    public static File filechooser(String title, String desc, String[] types){
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(desc, types)
            );
        return fc.showOpenDialog(stage);
    }
}
