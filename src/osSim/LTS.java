package osSim;

public class LTS {
    private Memory memory;
    private int memoryPointer;

    public LTS(Memory memory) {
        this.memory = memory;
        this.memoryPointer = 0; // Start of RAM
    }

    public void loadSingleJobFromDisk(PCB pcb) {
        System.out.printf("LTS: Loading Job %d from disk into RAM.%n", pcb.getJobID());

        // Load code into memory
        int codeStartAddress = memoryPointer;
        for (String instruction : pcb.getJob().getCode()) {
            int instructionValue = Integer.parseUnsignedInt(instruction.substring(2), 16);
            memory.writeToRAM(memoryPointer++, instructionValue);
        }
        int codeEndAddress = memoryPointer - 1;

        // Load data into memory
        int dataStartAddress = memoryPointer;
        for (String dataWord : pcb.getJob().getData()) {
            int dataValue = Integer.parseUnsignedInt(dataWord.substring(2), 16);
            memory.writeToRAM(memoryPointer++, dataValue);
        }
        int dataEndAddress = memoryPointer - 1;

        // Update PCB with code and data addresses
        pcb.setCodeStartAddress(codeStartAddress);
        pcb.setCodeEndAddress(codeEndAddress);
        pcb.setDataStartAddress(dataStartAddress);
        pcb.setDataEndAddress(dataEndAddress);

        System.out.printf("LTS: Job %d loaded into RAM addresses 0x%X to 0x%X.%n",
                pcb.getJobID(), codeStartAddress, dataEndAddress);
    }

    public void resetMemoryPointer() {
        memoryPointer = 0;
    }

	public void clearJobQueue() {
		// TODO Auto-generated method stub
		
	}
}
