/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import utilitytypes.EnumOpcode;

/**
 * The code that implements the ALU has been separates out into a static
 * method in its own class.  However, this is just a design choice, and you
 * are not required to do this.
 * 
 * @author 
 */
public class MyALU {
    static int execute(EnumOpcode opcode, int input1, int input2, int oper0) {
        int result = 0;
        
        // Implement code here that performs appropriate computations for any instruction that requires an ALU operation.  See EnumOpcode.
        
        //shree - performing ALU operations
        
//        System.out.println("ALU Operation : OPCODE - " + opcode.toString() + ", SRC1 - " + input1 + ", SRC2 -" + input2 + ", DEST : " + oper0);
        
        switch(opcode.toString()) {
            
            case "MOVC":
                result = input1;
                break;
    
            case "ADD":
            case "LOAD":
            case "STORE":
                result = input1 + input2;
                break;
                
            case "CMP":
                result = input1 - input2;
                break;
                
            case "OUT":
                System.out.println("@@output: " + oper0);
                break;
                 
        }
        
        return result;
    }    
}
