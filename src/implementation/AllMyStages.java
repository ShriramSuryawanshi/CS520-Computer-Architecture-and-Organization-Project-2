/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.AllMyLatches.*;
import utilitytypes.EnumOpcode;
import baseclasses.InstructionBase;
import baseclasses.PipelineRegister;
import baseclasses.PipelineStageBase;
import voidtypes.VoidLatch;
import baseclasses.CpuCore;

//shree - Excel libraries
//import java.io.File;
//import java.io.IOException;
//import jxl.write.WritableSheet;
//import jxl.Workbook;
//import jxl.read.biff.BiffException;
//import jxl.write.*;


/**
 * The AllMyStages class merely collects together all of the pipeline stage 
 * classes into one place.  You are free to split them out into top-level
 * classes.
 * 
 * Each inner class here implements the logic for a pipeline stage.
 * 
 * It is recommended that the compute methods be idempotent.  This means
 * that if compute is called multiple times in a clock cycle, it should
 * compute the same output for the same input.
 * 
 * How might we make updating the program counter idempotent?
 * 
 * @author
 */
public class AllMyStages {
    
    //shree - to udate DFG Excel as per clock cycle
    public static int clock = 0;       
   
    /*** Fetch Stage ***/
    static class Fetch extends PipelineStageBase<VoidLatch,FetchToDecode> {
        public Fetch(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }
        
        @Override
        public String getStatus() {
            // Generate a string that helps you debug.
            return null;
        }

        @Override
        public void compute(VoidLatch input, FetchToDecode output) {
            GlobalData globals = (GlobalData)core.getGlobalResources();
            int pc = globals.program_counter;
            
            // Fetch the instruction
            InstructionBase ins = globals.program.getInstructionAt(pc);
            if (ins.isNull()) return;
       

           //shree - Updating DFG sheet
            //clock = globals.ClockValue;
            
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
//                    WritableCell cell;
//                 
//                    Label l = new Label(1, clock+1, ins.toString());
//                    cell = (WritableCell) l;
//                    ws.addCell(cell);
//                    
//                    wbt.write();
//                    wbt.close();
//                    wb.close();
//                                                      
//                    file.delete();
//                    File file1 = new File("DebugSheet_New.xls");
//                    file1.renameTo(file);
//                }
//                catch(IOException | IndexOutOfBoundsException | BiffException | WriteException e) {
//                    System.out.println("Excel Error!" + e);
//                }
//            }
            
            globals.ClockValue++;
            //System.out.println("\n"+globals.ClockValue);
            //System.out.println("\nFetch : "+ins.toString());
                
            
            
            // Do something idempotent to compute the next program counter.
            //shree - increment PC by one
            globals.program_counter++;
            
            // Don't forget branches, which MUST be resolved in the Decode
            // stage.  You will make use of global resources to commmunicate
            // between stages.
            
            // Your code goes here...
            output.setInstruction(ins);
            
        }
        
        @Override
        public boolean stageWaitingOnResource() {
            // Hint:  You will need to implement this for when branches
            // are being resolved.
            return false;
        }
        
        
        /**
         * This function is to advance state to the next clock cycle and
         * can be applied to any data that must be updated but which is
         * not stored in a pipeline register.
         */
        @Override
        public void advanceClock() {
            // Hint:  You will need to implement this help with waiting
            // for branch resolution and updating the program counter.
            // Don't forget to check for stall conditions, such as when
            // nextStageCanAcceptWork() returns false.
        }
    }

    
    
    
    
    /*** Decode Stage ***/
    static class Decode extends PipelineStageBase<FetchToDecode,DecodeToExecute> {
        public Decode(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }
        
        boolean stall = false;    
        
        @Override
        public boolean stageWaitingOnResource() {
            // Hint:  You will need to implement this to deal with 
            // dependencies.
            
            //shree - stall set from here        
            return stall;
        }
        

        @Override
        public void compute(FetchToDecode input, DecodeToExecute output) {
            InstructionBase ins = input.getInstruction();
            
            GlobalData globals = (GlobalData)core.getGlobalResources();
            int[] regfile = globals.register_file;
            
            
            if(globals.branch == 1) {
                EnumOpcode op = EnumOpcode.NULL;
                ins.setOpcode(op);
                globals.branch = 0;
            }
                
            // You're going to want to do something like this:
            
            // VVVVV LOOK AT THIS VVVVV
            ins = ins.duplicate();
            if (ins.isNull()) return;
         
            // ^^^^^ LOOK AT THIS ^^^^^
           
            // The above will allow you to do things like look up register values for operands in the instruction and set them but avoid altering the input latch if you're in a stall condition.
            // The point is that every time you enter this method, you want the instruction and other contents of the input latch to be in their original state, unaffected by whatever you did 
            // in this method when there was a stall condition.
            // By cloning the instruction, you can alter it however you want, and if this stage is stalled, the duplicate gets thrown away without affecting the original.  This helps with idempotency.
            
            // These null instruction checks are mostly just to speed up the simulation.  The Void types were created so that null checks can be almost completely avoided.           
            
            
          //System.out.println("Decode : " + ins.toString());
            
            //shree - updating DFG sheet
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
//                    WritableCell cell;
//                 
//                    Label l = new Label(2, clock+1, ins.toString());
//                    cell = (WritableCell) l;
//                    ws.addCell(cell);
//                    
//                    wbt.write();
//                    wbt.close();
//                    wb.close();
//                                                      
//                    file.delete();
//                    File file1 = new File("DebugSheet_New.xls");
//                    file1.renameTo(file);
//                }
//                catch(IOException | IndexOutOfBoundsException | BiffException | WriteException e) {
//                    System.out.println("Excel Error!" + e);
//                }
//            }
            
            // Do what the decode stage does:
            // - Look up source operands
            // - Decode instruction
            // - Resolve branches 
            
            //shree - checking for stall condition
            if(ins.getOper0().isRegister()) {
                if(globals.register_invalid[ins.getOper0().getRegisterNumber()]) 
                    stall = true;                   
                else
                    stall = false;
            }
   
            if(!stall && ins.getSrc1().isRegister()) {
                if(globals.register_invalid[ins.getSrc1().getRegisterNumber()]) 
                    stall = true;
                else
                    stall = false;
            }
            
            if(!stall && ins.getSrc2().isRegister()) {
                if(globals.register_invalid[ins.getSrc2().getRegisterNumber()]) 
                    stall = true;
                else
                    stall = false;
            }
       
                
                                  
            //shree - decide SRC1, SRC2 and DEST from INS; get SRC1 and SRC2 value from REG, set DEST to REG, set stall condition on all DEST, SRC1, SRC2
            if(!stall) {
                    
                    EnumOpcode op;
                                                
                    switch(ins.getOpcode().toString()) {

                        case "MOVC":
                            ins.getOper0().setValue(ins.getOper0().getRegisterNumber());
                            globals.register_invalid[ins.getOper0().getRegisterNumber()] = true;

                            if(ins.getSrc1().isRegister())
                                ins.getSrc1().setValue(regfile[ins.getSrc1().getRegisterNumber()]);
                           else 
                               ins.getSrc1().setValue(ins.getSrc1().getValue());

                            ins.getSrc2().setValue(0);

                            break;


                        case "ADD":
                        case "CMP":
                        case "LOAD":
                            ins.getOper0().setValue(ins.getOper0().getRegisterNumber());
                            globals.register_invalid[ins.getOper0().getRegisterNumber()] = true;

                            if(ins.getSrc1().isRegister()) 
                                ins.getSrc1().setValue(regfile[ins.getSrc1().getRegisterNumber()]);                       
                            else 
                               ins.getSrc1().setValue(ins.getSrc1().getValue());

                            if(ins.getSrc2().isRegister())
                                ins.getSrc2().setValue(regfile[ins.getSrc2().getRegisterNumber()]);                                
                            else 
                               ins.getSrc2().setValue(ins.getSrc2().getValue());

                            break;                   

                            
                        case "STORE":
                            ins.getOper0().setValue(regfile[ins.getOper0().getRegisterNumber()]);       //shree - assigning the register value (only for store case)
                            globals.register_invalid[ins.getOper0().getRegisterNumber()] = true;

                            if(ins.getSrc1().isRegister()) 
                                ins.getSrc1().setValue(regfile[ins.getSrc1().getRegisterNumber()]);                                                            
                            else 
                               ins.getSrc1().setValue(ins.getSrc1().getValue());

                            if(ins.getSrc2().isRegister()) 
                                ins.getSrc2().setValue(regfile[ins.getSrc2().getRegisterNumber()]);
                            else 
                               ins.getSrc2().setValue(ins.getSrc2().getValue());

                            break;                   
                            
                            
                        case "OUT":
                           if(ins.getOper0().isRegister()) {
                                ins.getOper0().setValue(regfile[ins.getOper0().getRegisterNumber()]);
                                globals.register_invalid[ins.getOper0().getRegisterNumber()] = true;
                            }
                            else 
                               ins.getSrc1().setValue(ins.getOper0().getValue());

                            break;


                        case "BRA":
                            int temp;
                            
                            if(ins.getOper0().isRegister())
                                temp = regfile[ins.getOper0().getRegisterNumber()];
                            else
                                temp = ins.getOper0().getValue();
                                                                                  
                            switch(ins.getComparison().toString()){
                            
                                case "LT":
                                    if (temp < 0) {                       
                                        globals.program_counter = ins.getLabelTarget().getAddress();
                                        globals.branch = 1;
                                    }
                                    
                                    op = EnumOpcode.NULL;
                                    ins.setOpcode(op);                                    
                                    break;
                                    
                                case "EQ":
                                    if (temp == 0) {
                                        globals.program_counter = ins.getLabelTarget().getAddress();
                                        globals.branch = 1;
                                    }         
                                    
                                    op = EnumOpcode.NULL;
                                    ins.setOpcode(op);                                 
                                    break;
                                    
                                case "GE":
                                    if (temp > 0) {
                                        globals.program_counter = ins.getLabelTarget().getAddress();
                                        globals.branch = 1;
                                    }           

                                    op = EnumOpcode.NULL;
                                    ins.setOpcode(op);
                                    break;
                            }

                            break;


                        case "JMP":                           
                            globals.program_counter = ins.getLabelTarget().getAddress();
                            
                             op = EnumOpcode.NULL;
                             ins.setOpcode(op);
                             globals.branch = 1;
                            
                            break;


                        case "HALT":
                            break;

                    }        

                    output.setInstruction(ins);
            }
            else
                globals.program_counter--;

            // Set other data that's passed to the next stage.
        }
    }
    
    
    

    /*** Execute Stage ***/
    static class Execute extends PipelineStageBase<DecodeToExecute,ExecuteToMemory> {
        public Execute(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }
        
        @Override
        public void compute(DecodeToExecute input, ExecuteToMemory output) {
            InstructionBase ins = input.getInstruction();        
            
            if (ins.isNull()) return;
            //System.out.println("Execute : " + ins.toString());
            
            GlobalData globals = (GlobalData)core.getGlobalResources();
            
             //shree - Updating DFG sheet
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
//                    WritableCell cell;
//                 
//                    Label l = new Label(3, clock+1, ins.toString());
//                    cell = (WritableCell) l;
//                    ws.addCell(cell);
//                    
//                    wbt.write();
//                    wbt.close();
//                    wb.close();
//                                                      
//                    file.delete();
//                    File file1 = new File("DebugSheet_New.xls");
//                    file1.renameTo(file);
//                }
//                catch(IOException | IndexOutOfBoundsException | BiffException | WriteException e) {
//                    System.out.println("Excel Error!" + e);
//                }
//            }
            //shree - DFG sheet updated
                  
            int source1 = ins.getSrc1().getValue();
            int source2 = ins.getSrc2().getValue();
            int oper0 =   ins.getOper0().getValue();
            int result = 0;
                      
            if(!ins.getOpcode().toString().equalsIgnoreCase("HALT"))                        //shree - skip ALU if HALT
                result = MyALU.execute(ins.getOpcode(), source1, source2, oper0);
            
            if(ins.getOpcode().toString().equalsIgnoreCase("OUT")){                         //shree - OUT set to NULL
                EnumOpcode op = EnumOpcode.NULL;
                ins.setOpcode(op);
                
                globals.register_invalid[ins.getOper0().getRegisterNumber()] = false;
            }
                
                                          
            // Fill output with what passes to Memory stage...
            //shree - updating result
            output.result = result;
            
            output.setInstruction(ins);
            // Set other data that's passed to the next stage.
        }
    }
    

    
    
    /*** Memory Stage ***/
    static class Memory extends PipelineStageBase<ExecuteToMemory,MemoryToWriteback> {
        public Memory(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }

        @Override
        public void compute(ExecuteToMemory input, MemoryToWriteback output) {
            InstructionBase ins = input.getInstruction();
            if (ins.isNull()) return;
            
            GlobalData globals = (GlobalData)core.getGlobalResources();
            

            //shree - Updating DFG sheet
//            File file = new File("DebugSheet_Base.xls");
            
//            if(!file.exists()) 
//                System.out.println("Issue in Excel file");
//            else  {
//                try {
//                    Workbook wb = Workbook.getWorkbook(file);
//                    WritableWorkbook wbt = Workbook.createWorkbook(new File("DebugSheet_New.xls"), wb);
//                    
//                    WritableSheet ws = wbt.getSheet(0);
//                    
//                    WritableCell cell;
//                 
//                    Label l = new Label(4, clock+1, input.getInstruction().toString());
//                    cell = (WritableCell) l;
//                    ws.addCell(cell);
//                    
//                    wbt.write();
//                    wbt.close();
//                    wb.close();
//                                                      
//                    file.delete();
//                    File file1 = new File("DebugSheet_New.xls");
//                    file1.renameTo(file);
//                }
//                catch(IOException | IndexOutOfBoundsException | BiffException | WriteException e) {
//                    System.out.println("Excel Error!" + e);
//                }
//            }
            
            
            // Access memory...
            //shree - executing LOAD and STORE
            
            output.result = input.result;
            
            switch(ins.getOpcode().toString()) {
                
                case "LOAD":
                    output.result = globals.memory_file[input.result];                                                 
                    break;
                    
                    
                case "STORE":
                    globals.memory_file[input.result] = ins.getOper0().getValue();              //shree - using reg value stored in Oper0 (STORE special case)
                    EnumOpcode op1 = EnumOpcode.NULL;
                    ins.setOpcode(op1);
                    
                    // System.out.print(" - Stored " + globals.register_file[ins.getOper0().getValue()] + " in memory R" +input.result + "\n");
                    
                    //shree - update invalid flags
                    globals.register_invalid[ins.getOper0().getRegisterNumber()] = false;           
                    break;
                    
            }
 
            output.setInstruction(ins);
            // Set other data that's passed to the next stage.
        }
    }
    
    
    

    /*** Write back Stage ***/
    static class Writeback extends PipelineStageBase<MemoryToWriteback,VoidLatch> {
        public Writeback(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }

        @Override
        public void compute(MemoryToWriteback input, VoidLatch output) {
            InstructionBase ins = input.getInstruction();
            if (ins.isNull()) return;
            
             GlobalData globals = (GlobalData)core.getGlobalResources();
            
              if (input.getInstruction().getOpcode() == EnumOpcode.HALT) {
                // Stop the simulation
                System.out.println("Halting...Total Clocks : " + globals.ClockValue);
                System.exit(0);
              }
            
           
                        
            //System.out.println("\nWriteBack : " + input.getInstruction().toString());

           //shree - Updating DFG sheet
//           File file = new File("DebugSheet_Base.xls");
            
//            if(!file.exists()) 
//                System.out.println("Issue in Excel file");
//            else  {
//                try {
//                    Workbook wb = Workbook.getWorkbook(file);
//                    WritableWorkbook wbt = Workbook.createWorkbook(new File("DebugSheet_New.xls"), wb);
//                    
//                    WritableSheet ws = wbt.getSheet(0);
//                    
//                    WritableCell cell;
//                 
//                    Label l = new Label(5, clock+1, ins.toString());
//                    cell = (WritableCell) l;
//                    ws.addCell(cell);
//                    
//                    wbt.write();
//                    wbt.close();
//                    wb.close();
//                                                      
//                    file.delete();
//                    File file1 = new File("DebugSheet_New.xls");
//                    file1.renameTo(file);
//                }
//                catch(IOException | IndexOutOfBoundsException | BiffException | WriteException e) {
//                    System.out.println("Excel Error!" + e);
//                }
//            }
                       
            
            // Write back result to register file
            globals.register_file[ins.getOper0().getValue()] = input.result;
            
            //shree - update invalid flags
            globals.register_invalid[ins.getOper0().getRegisterNumber()] = false;

            
            //System.out.print(" - Stored " + input.result + " in R" + ins.getOper0().getRegisterNumber() + "\n");         
          
            }
        }
    }

