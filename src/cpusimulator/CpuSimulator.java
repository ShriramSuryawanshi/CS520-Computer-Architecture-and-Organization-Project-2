/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpusimulator;

import implementation.MyCpuCore;
import java.io.IOException;
import tools.InstructionSequence;

//shree - Excel libraries
//import java.io.File;
//import java.io.IOException;
//import jxl.write.WritableSheet;
//import jxl.Workbook;
//import jxl.read.biff.BiffException;
//import jxl.write.*;
//import utilitytypes.Operand;

/**
 *
 * @author millerti
 */
public class CpuSimulator {
    
    public static boolean printStagesEveryCycle = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        InstructionSequence seq = new InstructionSequence();
        seq.loadFile("samples/sieve.asm");
        seq.printProgram();
       
            //shree - clearing DFG sheet
//            File file = new File("DebugSheet_Base.xls");
//            
//            if(!file.exists()) 
//                System.out.println("Issue in Excel file");
//            else  {
//                try {
//                    Workbook wb = Workbook.getWorkbook(file);
//                    WritableWorkbook wbt = Workbook.createWorkbook(new File("DebugSheet_New.xls"), wb);
//                    
//                    WritableSheet ws = wbt.getSheet(0);
//                    
//                    for(int i = 1; i<10000; i++) {
//                        WritableCell cell;
//                        
//                        for( int j = 1; j<=5; j++) {
//                 
//                            Label l = new Label(j, i, "");
//                            cell = (WritableCell) l;
//                            ws.addCell(cell);
//                        }
//                    }
//                    
//                    wbt.write();
//                    wbt.close();
//                    wb.close();
//                                                      
//                    file.delete();
//                    File file1 = new File("DebugSheet_New.xls");
//                    file1.renameTo(file);
//                }
//                catch(Exception e) {
//                    System.out.println("Excel Error!" + e);
//                }
//            }
        
        MyCpuCore core = new MyCpuCore();
        core.loadProgram(seq);
        core.runProgram();
    }    
}
