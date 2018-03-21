/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import baseclasses.InstructionBase;
import utilitytypes.IGlobals;
import tools.InstructionSequence;

/**
 * As a design choice, some data elements that are accessed by multiple
 * pipeline stages are stored in a common object.
 * 
 * TODO:  Add to this any additional global or shared state that is missing.
 * 
 * @author 
 */
public class GlobalData implements IGlobals {
    public InstructionSequence program;
    public int program_counter = 0;
    public int[] register_file = new int[32];
    public boolean[] register_invalid = new boolean[32];
    
   

    @Override
    public void reset() {
        program_counter = 0;
        register_file = new int[32];
    }
    
    
    // Other global and shared variables here....

   //shree - my variables
    public int ClockValue = 0;
    public int[] memory_file = new int[1000];
    public int[] reg_invalid_cnt = new int[32];
    public int branch = 0;
    public InstructionBase[] Forward = new InstructionBase[3];
    public int[] result = new int[3];
    
}
