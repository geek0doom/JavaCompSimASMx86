package osSim;

public class PCB {
    public static final String NEW = "new";
    public static final String READY = "ready";
    public static final String RUNNING = "running";
    public static final String WAITING = "waiting";
    public static final String TERMINATED = "terminated";

    private int jobID;
    private int codeStartAddress;
    private int codeEndAddress;
    private int dataStartAddress;
    private int dataEndAddress;
    private int programCounter;
    private int[] registers;
    private String state;
    private Job job; // Reference to the Job object

    // **New Fields for Disk Addresses**
    private int diskStartAddress;
    private int diskEndAddress;

    public PCB(Job job) {
        this.job = job;
        this.jobID = job.getJobID();
        this.programCounter = 0;
        this.registers = new int[16];
        this.state = NEW;
    }

    // Getters and setters for code/data addresses
    public int getJobID() {
        return jobID;
    }

    public int getCodeStartAddress() {
        return codeStartAddress;
    }

    public void setCodeStartAddress(int codeStartAddress) {
        this.codeStartAddress = codeStartAddress;
    }

    public int getCodeEndAddress() {
        return codeEndAddress;
    }

    public void setCodeEndAddress(int codeEndAddress) {
        this.codeEndAddress = codeEndAddress;
    }

    public int getDataStartAddress() {
        return dataStartAddress;
    }

    public void setDataStartAddress(int dataStartAddress) {
        this.dataStartAddress = dataStartAddress;
    }

    public int getDataEndAddress() {
        return dataEndAddress;
    }

    public void setDataEndAddress(int dataEndAddress) {
        this.dataEndAddress = dataEndAddress;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public int[] getRegisters() {
        return registers;
    }

    public void setRegisters(int[] registers) {
        this.registers = registers;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        System.out.printf("PCB: Job ID %d changing state from %s to %s%n", jobID, this.state, state);
        this.state = state;
    }

    public Job getJob() {
        return job;
    }

    // **New Getters and Setters for Disk Addresses**

    public int getDiskStartAddress() {
        return diskStartAddress;
    }

    public void setDiskStartAddress(int diskStartAddress) {
        this.diskStartAddress = diskStartAddress;
    }

    public int getDiskEndAddress() {
        return diskEndAddress;
    }

    public void setDiskEndAddress(int diskEndAddress) {
        this.diskEndAddress = diskEndAddress;
    }
}
