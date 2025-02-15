package osSim;


public class Dispatcher {
    public void loadProcessToCPU(PCB pcb) {
        CPU.registers = pcb.getRegisters().clone();
        CPU.programCounter = pcb.getProgramCounter();
        CPU.currentPCB = pcb; // Set the current PCB in the CPU
        pcb.setState(PCB.RUNNING);
        System.out.printf("Dispatcher: Loaded Job ID %d into CPU. State changed to %s.%n", pcb.getJobID(), pcb.getState());
    }

    public void saveProcessFromCPU(CPU cpu, PCB pcb) {
        pcb.setRegisters(CPU.registers.clone());
        pcb.setProgramCounter(CPU.programCounter);
        pcb.setState(PCB.TERMINATED);
        System.out.printf("Dispatcher: Saved Job ID %d from CPU. State changed to %s.%n", pcb.getJobID(), pcb.getState());
    }
}

