package osSim;

import java.io.PrintWriter;
import java.util.Arrays;
// memory for our simulator

public class Memory {
    private int[] RAM;
    private int[] disk;
    private int diskPointer = 0; // Points to the next free disk location

    public Memory() {
        RAM = new int[1024];
        disk = new int[2048];
        Arrays.fill(RAM, 00000000);
        Arrays.fill(disk, 00000000);
    }

    public int readFromRAM(int address) {
        if (address < 0 || address >= RAM.length) {
            throw new IllegalArgumentException("Invalid address");
        }
        return RAM[address];
    }

    public void writeToRAM(int address, int data) {
        if (address < 0 || address >= RAM.length) {
            throw new IllegalArgumentException("Invalid address");
        }
        RAM[address] = data;
    }

    public int readFromDisk(int address) {
        if (address < 0 || address >= disk.length) {
            throw new IllegalArgumentException("Invalid address");
        }
        return disk[address];
    }
    
    public int getRAMSize() {
        return RAM.length;
    }

    public void writeToDisk(int address, int data) {
        if (address < 0 || address >= disk.length) {
            throw new IllegalArgumentException("Invalid address");
        }
        disk[address] = data;
    }
    
    public int getDiskSize() {
        return disk.length;
    }
    
    public int effectiveAddress(int base, int displacement, boolean indirect, int indexRegister) {
        if (indirect) {
            return RAM[base] + RAM[indexRegister] + displacement;
        } else {
            return base + displacement;
        }
    }

    public void resetMemory() {
        Arrays.fill(RAM, 0);
    }
    
    public void resetDiskPointer() {
        diskPointer = 0;
    }
    
    public int getDiskPointer() {
        return diskPointer;
    }

    public void incrementDiskPointer(int value) {
        diskPointer += value;
    }
    
    public void printMemoryContents(PrintWriter outputWriter) {
        outputWriter.println("Memory Contents:");
        for (int address = 0; address < RAM.length; address++) {
            int data = RAM[address];
            if (data != 0) { // Optionally, only print non-zero addresses
                outputWriter.printf("Address 0x%04X: 0x%08X%n", address, data);
            }
        }
        outputWriter.println("End of Memory Contents");
    }


    public void dumpEntireMemoryContents(PrintWriter outputWriter) {
        outputWriter.println("----------------------------------------------------");
        outputWriter.println("Complete Memory Dump");

        // Dump RAM Contents
        outputWriter.println("\nRAM Contents:");
        for (int address = 0; address < RAM.length; address++) {
            int data = RAM[address];
            outputWriter.printf("RAM[0x%04X]: 0x%08X%n", address, data);
        }

        // Dump Disk Contents
        outputWriter.println("\nDisk Contents:");
        for (int address = 0; address < disk.length; address++) {
            int data = disk[address];
            outputWriter.printf("Disk[0x%04X]: 0x%08X%n", address, data);
        }

        outputWriter.println("End of Memory Dump");
        outputWriter.println("----------------------------------------------------");
    }
}
