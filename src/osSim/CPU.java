package osSim;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CPU {
    public static Memory memory;
    public static PCB currentPCB;

    public static int[] registers = new int[16]; // 16 registers (R0 to R15)
    public static int programCounter = 0;
    public int[] instructionMemory;

    public static int currentInstruction;
    public static int opcode;
    public static int reg1;
    public static int reg2;
    public static int reg3;
    public static int address;
    public static int immediate;
    public static int instrType;
    public static boolean running = true;

    public static PrintWriter outputWriter;

    public static Map<Integer, String> opcodeMap = new HashMap<>();

    static {
        opcodeMap.put(0x0, "RD");
        opcodeMap.put(0x1, "WR");
        opcodeMap.put(0x2, "ST");
        opcodeMap.put(0x3, "LW");
        opcodeMap.put(0x4, "MOV");
        opcodeMap.put(0x5, "ADD");
        opcodeMap.put(0x6, "SUB");
        opcodeMap.put(0x7, "MUL");
        opcodeMap.put(0x8, "DIV");
        opcodeMap.put(0x9, "AND");
        opcodeMap.put(0xA, "OR");
        opcodeMap.put(0xB, "MOVI");
        opcodeMap.put(0xC, "ADDI");
        opcodeMap.put(0xD, "MULI");
        opcodeMap.put(0xE, "DIVI");
        opcodeMap.put(0xF, "LDI");
        opcodeMap.put(0x10, "SLT");
        opcodeMap.put(0x11, "SLTI");
        opcodeMap.put(0x12, "HLT");
        opcodeMap.put(0x13, "NOP");
        opcodeMap.put(0x14, "JMP");
        opcodeMap.put(0x15, "BEQ");
        opcodeMap.put(0x16, "BNE");
        opcodeMap.put(0x17, "BEZ");
        opcodeMap.put(0x18, "BNZ");
        opcodeMap.put(0x19, "BGZ");
        opcodeMap.put(0x1A, "BLZ");
    }
    
    public CPU() {
        CPU.registers = new int[16];
        CPU.programCounter = 0;
        CPU.running = true;
    }

    public static void fetch() {
        int physicalPC = CPU.programCounter + CPU.currentPCB.getCodeStartAddress();

        if (physicalPC <= CPU.currentPCB.getCodeEndAddress()) {
            currentInstruction = memory.readFromRAM(physicalPC);
            outputWriter.printf("Fetching instruction at PC=0x%04X: 0x%08X%n", physicalPC * 4, currentInstruction);
            programCounter++;
        } else {
            running = false;
        }
    }



    public static void decode() {
        instrType = (currentInstruction >>> 30) & 0b11; // Bits 31-30
        opcode = (currentInstruction >>> 24) & 0x3F;    // Bits 29-24 (6 bits)

        switch (instrType) {
            case 0b00: // Arithmetic Instruction Format
                reg1 = (currentInstruction >>> 20) & 0xF; // Bits 23-20
                reg2 = (currentInstruction >>> 16) & 0xF; // Bits 19-16
                reg3 = (currentInstruction >>> 12) & 0xF; // Bits 15-12
                outputWriter.printf("Decoded Arithmetic Instruction: Opcode=%s (0x%X), Reg1=R%d, Reg2=R%d, Reg3=R%d%n",
                        opcodeMap.get(opcode), opcode, reg1, reg2, reg3);
                break;
            case 0b01: // Conditional Branch and Immediate Format
                reg1 = (currentInstruction >>> 20) & 0xF; // B-reg or Reg1
                reg2 = (currentInstruction >>> 16) & 0xF; // D-reg or Reg2
                address = currentInstruction & 0xFFFF;    // Bits 15-0
                outputWriter.printf("Decoded Conditional Branch/Immediate Instruction: Opcode=%s (0x%X), Reg1=R%d, Reg2=R%d, Address/Data=0x%X%n",
                        opcodeMap.get(opcode), opcode, reg1, reg2, address);
                break;
            case 0b10: // Unconditional Jump Format
                address = currentInstruction & 0xFFFFFF; // Bits 23-0
                outputWriter.printf("Decoded Unconditional Jump Instruction: Opcode=%s (0x%X), Address=0x%X%n",
                        opcodeMap.get(opcode), opcode, address);
                break;
            case 0b11: // Input and Output Instruction Format
                reg1 = (currentInstruction >>> 20) & 0xF; // Reg1
                reg2 = (currentInstruction >>> 16) & 0xF; // Reg2
                address = currentInstruction & 0xFFFF;    // Bits 15-0
                outputWriter.printf("Decoded I/O Instruction: Opcode=%s (0x%X), Reg1=R%d, Reg2=R%d, Address=0x%X%n",
                        opcodeMap.get(opcode), opcode, reg1, reg2, address);
                break;
            default:
                outputWriter.println("Unknown instruction type");
                break;
        }
    }

    public void execute() {
    	// Debug Statement before execution
        System.out.printf("CPU: Executing instruction 0x%08X at PC=0x%04X%n", currentInstruction, programCounter * 4);
        
        switch (opcode) {
            case 0x0: // RD
                executeRD();
                break;
            case 0x1: // WR
                executeWR();
                break;
            case 0x2: // ST
                executeST();
                break;
            case 0x3: // LW
                executeLW();
                break;
            case 0x4: // MOV
                executeMOV();
                break;
            case 0x5: // ADD
                executeADD();
                break;
            case 0x6: // SUB
                executeSUB();
                break;
            case 0x7: // MUL
                executeMUL();
                break;
            case 0x8: // DIV
                executeDIV();
                break;
            case 0x9: // AND
                executeAND();
                break;
            case 0xA: // OR
                executeOR();
                break;
            case 0xB: // MOVI
                executeMOVI();
                break;
            case 0xC: // ADDI
                executeADDI();
                break;
            case 0xD: // MULI
                executeMULI();
                break;
            case 0xE: // DIVI
                executeDIVI();
                break;
            case 0xF: // LDI
                executeLDI();
                break;
            case 0x10: // SLT
                executeSLT();
                break;
            case 0x11: // SLTI
                executeSLTI();
                break;
            case 0x12: // HLT
                executeHLT();
                break;
            case 0x13: // NOP
                executeNOP();
                break;
            case 0x14: // JMP
                executeJMP();
                break;
            case 0x15: // BEQ
                executeBEQ();
                break;
            case 0x16: // BNE
                executeBNE();
                break;
            case 0x17: // BEZ
                executeBEZ();
                break;
            case 0x18: // BNZ
                executeBNZ();
                break;
            case 0x19: // BGZ
                executeBGZ();
                break;
            case 0x1A: // BLZ
                executeBLZ();
                break;
            default:
                outputWriter.printf("Unknown opcode: 0x%X%n", opcode);
                break;
        }
        
        // Debug Statement after execution
        System.out.printf("CPU: Completed execution of instruction 0x%08X%n", currentInstruction);
        printState();
    }

    // Implementations for each instruction

 // RD: Reads data at address or data pointed to by Reg2 into Reg1
    public static void executeRD() {
        if (instrType == 0b11) { // Input and Output Instruction Format
            int data;
            int effectiveAddress;
            if (reg2 == 0) {
                // Direct addressing
                effectiveAddress = address + CPU.currentPCB.getDataStartAddress();
                data = memory.readFromRAM(effectiveAddress);
                outputWriter.printf("Executing RD (Direct): R%d = MEM[0x%X]%n", reg1, effectiveAddress * 4);
            } else {
                // Indirect addressing
                effectiveAddress = registers[reg2] + address + CPU.currentPCB.getDataStartAddress();
                data = memory.readFromRAM(effectiveAddress);
                outputWriter.printf("Executing RD (Indirect): R%d = MEM[R%d + 0x%X + DataStart(0x%X)] = MEM[0x%X]%n",
                    reg1, reg2, address, CPU.currentPCB.getDataStartAddress() * 4, effectiveAddress * 4);
            }
            registers[reg1] = data;
        } else {
            outputWriter.println("Invalid instruction format for RD");
        }
    }

 // WR: Write data in Reg1 to address or location pointed to by Reg2
    public static void executeWR() {
        if (instrType == 0b11) { // Input and Output Instruction Format
            int effectiveAddress;
            if (reg2 == 0) {
                // Direct addressing
                effectiveAddress = address + CPU.currentPCB.getDataStartAddress();
                memory.writeToRAM(effectiveAddress, registers[reg1]);
                outputWriter.printf("Executing WR (Direct): MEM[0x%X] = R%d%n", effectiveAddress * 4, reg1);
            } else {
                // Indirect addressing
                effectiveAddress = registers[reg2] + address + CPU.currentPCB.getDataStartAddress();
                memory.writeToRAM(effectiveAddress, registers[reg1]);
                outputWriter.printf("Executing WR (Indirect): MEM[R%d + 0x%X + DataStart(0x%X)] = MEM[0x%X] = R%d%n",
                    reg2, address, CPU.currentPCB.getDataStartAddress() * 4, effectiveAddress * 4, reg1);
            }
        } else {
            outputWriter.println("Invalid instruction format for WR");
        }
    }

 // ST: Store contents of Reg1 into address pointed to by Reg2 or specified address
    public static void executeST() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            int data = registers[reg1];
            int effectiveAddress;
            if (reg2 == 0) {
                // Direct addressing
                effectiveAddress = address + CPU.currentPCB.getDataStartAddress();
                outputWriter.printf("Executing ST (Direct): MEM[0x%X] = R%d%n", effectiveAddress * 4, reg1);
            } else {
                // Indirect addressing
                effectiveAddress = registers[reg2] + address + CPU.currentPCB.getDataStartAddress();
                outputWriter.printf("Executing ST (Indirect): MEM[R%d + 0x%X + DataStart(0x%X)] = MEM[0x%X] = R%d%n",
                    reg2, address, CPU.currentPCB.getDataStartAddress() * 4, effectiveAddress * 4, reg1);
            }
            memory.writeToRAM(effectiveAddress, data);
        } else {
            outputWriter.println("Invalid instruction format for ST");
        }
    }

 // LW: Load data into Reg1 from address or location pointed to by Reg2
    public static void executeLW() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            int data;
            int effectiveAddress;
            if (reg2 == 0) {
                // Direct addressing
                effectiveAddress = address + CPU.currentPCB.getDataStartAddress();
            } else {
                // Indirect addressing
                effectiveAddress = registers[reg2] + address + CPU.currentPCB.getDataStartAddress();
            }
            data = memory.readFromRAM(effectiveAddress);
            outputWriter.printf("Executing LW: R%d = MEM[0x%X]%n", reg1, effectiveAddress);
            registers[reg1] = data;
        } else {
            outputWriter.println("Invalid instruction format for LW");
        }
    }


    // MOV: Transfers contents of Reg2 into Reg1
    public static void executeMOV() {
        if (instrType == 0b00) { // Arithmetic Instruction Format
            registers[reg3] = registers[reg2]; // Note: D-reg is reg3
            System.out.printf("Executing MOV: R%d = R%d%n", reg3, reg2);
        } else {
            System.out.println("Invalid instruction format for MOV");
        }
    }

    // ADD: Add Reg2 and Reg3, store in Reg1
    public static void executeADD() {
        if (instrType == 0b00) { // Arithmetic Instruction Format
            registers[reg3] = registers[reg1] + registers[reg2]; // D-reg is reg3
            System.out.printf("Executing ADD: R%d = R%d + R%d%n", reg3, reg1, reg2);
        } else {
            System.out.println("Invalid instruction format for ADD");
        }
    }

    // SUB: Subtract Reg3 from Reg2, store in Reg1
    public static void executeSUB() {
        if (instrType == 0b00) { // Arithmetic Instruction Format
            registers[reg3] = registers[reg2] - registers[reg1]; // D-reg is reg3
            System.out.printf("Executing SUB: R%d = R%d - R%d%n", reg3, reg2, reg1);
        } else {
            System.out.println("Invalid instruction format for SUB");
        }
    }

    // MUL: Multiply Reg2 and Reg3, store in Reg1
    public static void executeMUL() {
        if (instrType == 0b00) { // Arithmetic Instruction Format
            registers[reg3] = registers[reg1] * registers[reg2]; // D-reg is reg3
            System.out.printf("Executing MUL: R%d = R%d * R%d%n", reg3, reg1, reg2);
        } else {
            System.out.println("Invalid instruction format for MUL");
        }
    }

    // DIV: Divide Reg2 by Reg3, store in Reg1
    public static void executeDIV() {
        if (instrType == 0b00) { // Arithmetic Instruction Format
            if (registers[reg2] != 0) {
                registers[reg3] = registers[reg1] / registers[reg2]; // D-reg is reg3
                System.out.printf("Executing DIV: R%d = R%d / R%d%n", reg3, reg1, reg2);
            } else {
                System.out.println("Error: Division by zero");
            }
        } else {
            System.out.println("Invalid instruction format for DIV");
        }
    }

    // AND: Logical AND of Registers 1 and 2, store in Register 3
    public static void executeAND() {
        if (instrType == 0b00) { // Arithmetic Instruction Format
            registers[reg3] = registers[reg1] & registers[reg2];
            System.out.printf("Executing AND: R%d = R%d & R%d%n", reg3, reg1, reg2);
        } else {
            System.out.println("Invalid instruction format for AND");
        }
    }

    // OR: Logical OR of Registers 1 and 2, store in Register 3
    public static void executeOR() {
        if (instrType == 0b00) { // Arithmetic Instruction Format
            registers[reg3] = registers[reg1] | registers[reg2];
            System.out.printf("Executing OR: R%d = R%d | R%d%n", reg3, reg1, reg2);
        } else {
            System.out.println("Invalid instruction format for OR");
        }
    }

    // MOVI: Copy immediate data into Reg1
    public static void executeMOVI() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            registers[reg2] = address; // D-reg is reg2
            System.out.printf("Executing MOVI: R%d = %d%n", reg2, address);
        } else {
            System.out.println("Invalid instruction format for MOVI");
        }
    }

    // LDI: Same as MOVI
    public static void executeLDI() {
        executeMOVI();
    }

    // ADDI: Add immediate value to Reg1, store in Reg1
    public static void executeADDI() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            registers[reg1] += address; // B-reg is reg1
            System.out.printf("Executing ADDI: R%d += %d%n", reg1, address);
        } else {
            System.out.println("Invalid instruction format for ADDI");
        }
    }

    // MULI: Multiply immediate value with Reg1, store in Reg1
    public static void executeMULI() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            registers[reg1] *= address; // B-reg is reg1
            System.out.printf("Executing MULI: R%d *= %d%n", reg1, address);
        } else {
            System.out.println("Invalid instruction format for MULI");
        }
    }

    // DIVI: Divide Reg1 by immediate value, store in Reg1
    public static void executeDIVI() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            if (address != 0) {
                registers[reg1] /= address; // B-reg is reg1
                System.out.printf("Executing DIVI: R%d /= %d%n", reg1, address);
            } else {
                System.out.println("Error: Division by zero");
            }
        } else {
            System.out.println("Invalid instruction format for DIVI");
        }
    }

    // SLT: Set Reg3 to 1 if Reg1 < Reg2; else 0
    public static void executeSLT() {
        if (instrType == 0b00) { // Arithmetic Instruction Format
            registers[reg3] = (registers[reg1] < registers[reg2]) ? 1 : 0;
            System.out.printf("Executing SLT: R%d = (R%d < R%d) ? 1 : 0%n", reg3, reg1, reg2);
        } else {
            System.out.println("Invalid instruction format for SLT");
        }
    }

    // SLTI: Set Reg2 to 1 if Reg1 < immediate value; else 0
    public static void executeSLTI() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            registers[reg2] = (registers[reg1] < address) ? 1 : 0;
            System.out.printf("Executing SLTI: R%d = (R%d < %d) ? 1 : 0%n", reg2, reg1, address);
        } else {
            System.out.println("Invalid instruction format for SLTI");
        }
    }

    // HLT: Logical end of program
    public static void executeHLT() {
        System.out.println("Executing HLT: Halting program execution");
        running = false;
    }

    // NOP: Do nothing and move to next instruction
    public static void executeNOP() {
        System.out.println("Executing NOP: No operation");
    }

    // JMP: Jump program counter to specified address
    public static void executeJMP() {
        if (instrType == 0b10) { // Unconditional Jump Format
            programCounter = address / 4;
            System.out.printf("Executing JMP: Jumping to address 0x%X%n", address);
        } else {
            System.out.println("Invalid instruction format for JMP");
        }
    }


    // BEQ: Branch when Reg1 equals Reg2
    public static void executeBEQ() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            if (registers[reg1] == registers[reg2]) {
                programCounter = address / 4;
                System.out.printf("Executing BEQ: R%d == R%d, branching to address 0x%X%n", reg1, reg2, address);
            } else {
                System.out.printf("Executing BEQ: R%d != R%d, not branching%n", reg1, reg2);
            }
        } else {
            System.out.println("Invalid instruction format for BEQ");
        }
    }


    // BNE: Branch when Reg1 does not equal Reg2
    public static void executeBNE() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            if (registers[reg1] != registers[reg2]) {
                programCounter = address / 4;
                System.out.printf("Executing BNE: R%d != R%d, branching to address 0x%X%n", reg1, reg2, address);
            } else {
                System.out.printf("Executing BNE: R%d == R%d, not branching%n", reg1, reg2);
            }
        } else {
            System.out.println("Invalid instruction format for BNE");
        }
    }

    // BEZ: Branch when Reg1 equals zero
    public static void executeBEZ() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            if (registers[reg1] == 0) {
                programCounter = address / 4;
                System.out.printf("Executing BEZ: R%d == 0, branching to address 0x%X%n", reg1, address);
            } else {
                System.out.printf("Executing BEZ: R%d != 0, not branching%n", reg1);
            }
        } else {
            System.out.println("Invalid instruction format for BEZ");
        }
    }

    // BNZ: Branch when Reg1 does not equal zero
    public static void executeBNZ() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            if (registers[reg1] != 0) {
                programCounter = address / 4;
                System.out.printf("Executing BNZ: R%d != 0, branching to address 0x%X%n", reg1, address);
            } else {
                System.out.printf("Executing BNZ: R%d == 0, not branching%n", reg1);
            }
        } else {
            System.out.println("Invalid instruction format for BNZ");
        }
    }

    // BGZ: Branch when Reg1 is greater than zero
    public static void executeBGZ() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            if (registers[reg1] > 0) {
                programCounter = address / 4;
                System.out.printf("Executing BGZ: R%d > 0, branching to address 0x%X%n", reg1, address);
            } else {
                System.out.printf("Executing BGZ: R%d <= 0, not branching%n", reg1);
            }
        } else {
            System.out.println("Invalid instruction format for BGZ");
        }
    }

    // BLZ: Branch when Reg1 is less than zero
    public static void executeBLZ() {
        if (instrType == 0b01) { // Conditional Branch and Immediate Format
            if (registers[reg1] < 0) {
                programCounter = address / 4;
                System.out.printf("Executing BLZ: R%d < 0, branching to address 0x%X%n", reg1, address);
            } else {
                System.out.printf("Executing BLZ: R%d >= 0, not branching%n", reg1);
            }
        } else {
            System.out.println("Invalid instruction format for BLZ");
        }
    }

    // Method to print the state of registers and relevant memory
    public void printState() {
        outputWriter.println("Registers:");
        for (int i = 0; i < registers.length; i++) {
            outputWriter.printf("R%d: %d\t", i, registers[i]);
            if ((i + 1) % 8 == 0) outputWriter.println();
        }

        // Only print the Output Buffer
        outputWriter.println("\nData Memory (Relevant Addresses):");
        
        int ouptAddress = 0x00AC;
        outputWriter.printf("Output Buffer (Address 0x%X):%n", ouptAddress);
        outputWriter.printf("MEM[0x%X]: %d%n", ouptAddress, memory.readFromRAM(ouptAddress));
        outputWriter.println("----------------------------------------------------");
    }




    public static void resetCPU() {
        registers = new int[16];
        programCounter = 0;
        running = true;
        currentInstruction = 0;
        opcode = 0;
        reg1 = 0;
        reg2 = 0;
        reg3 = 0;
        address = 0;
        immediate = 0;
        instrType = 0;
    }
}
