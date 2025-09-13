package hk.hkprog.elf;

import hk.hkprog.codegen.AssemblyProgram;
import hk.hkprog.codegen.AssemblySection;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

/**
 * Writes ELF (Executable and Linkable Format) files
 */
public class ELFWriter {
    
    // ELF constants
    private static final byte[] ELF_MAGIC = {0x7f, 'E', 'L', 'F'};
    private static final byte ELF_CLASS_64 = 2;
    private static final byte ELF_DATA_LSB = 1;
    private static final byte ELF_VERSION_CURRENT = 1;
    private static final byte ELF_OSABI_SYSV = 0;
    
    private static final short ET_EXEC = 2; // Executable file
    private static final short EM_X86_64 = 62; // x86-64 architecture
    
    private static final int PT_LOAD = 1; // Loadable segment
    private static final int PF_X = 1; // Execute
    private static final int PF_W = 2; // Write
    private static final int PF_R = 4; // Read
    
    private static final long BASE_ADDRESS = 0x400000L;
    
    public void writeELF(AssemblyProgram assembly, String outputFile) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        
        // Convert assembly to machine code
        MachineCodeGenerator codeGen = new MachineCodeGenerator();
        byte[] textData = codeGen.generateMachineCode(assembly.getTextSection());
        byte[] dataData = assembly.getDataSection() != null ? 
            assembly.getDataSection().getDataAsBytes() : new byte[0];
        
        // Calculate offsets and addresses
        int ehSize = 64; // ELF header size
        int phSize = 56; // Program header size
        int numProgHeaders = 2; // TEXT and DATA segments
        
        long textOffset = ehSize + (phSize * numProgHeaders);
        long dataOffset = textOffset + textData.length;
        
        // Align to page boundary
        textOffset = alignToPage(textOffset);
        dataOffset = alignToPage(dataOffset);
        
        long textVAddr = BASE_ADDRESS + textOffset;
        long dataVAddr = BASE_ADDRESS + dataOffset;
        
        // Write ELF header
        writeELFHeader(buffer, ehSize, phSize, numProgHeaders, textVAddr);
        
        // Write program headers
        writeTextProgramHeader(buffer, textOffset, textData.length, textVAddr);
        writeDataProgramHeader(buffer, dataOffset, dataData.length, dataVAddr);
        
        // Pad to text section offset
        padToOffset(buffer, (int)textOffset);
        
        // Write text section
        buffer.write(textData);
        
        // Pad to data section offset
        padToOffset(buffer, (int)dataOffset);
        
        // Write data section
        buffer.write(dataData);
        
        // Write to file
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(buffer.toByteArray());
        }
        
        // Make executable
        new File(outputFile).setExecutable(true);
        
        System.out.println("ELF file written: " + outputFile);
    }
    
    private void writeELFHeader(ByteArrayOutputStream buffer, int ehSize, int phSize, int numProgHeaders, long entryPoint) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(64);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        
        // ELF identification
        bb.put(ELF_MAGIC);
        bb.put(ELF_CLASS_64);
        bb.put(ELF_DATA_LSB);
        bb.put(ELF_VERSION_CURRENT);
        bb.put(ELF_OSABI_SYSV);
        bb.put((byte)0); // ABI version
        bb.put(new byte[7]); // Padding
        
        // ELF header fields
        bb.putShort(ET_EXEC); // e_type
        bb.putShort(EM_X86_64); // e_machine
        bb.putInt(1); // e_version
        bb.putLong(entryPoint); // e_entry
        bb.putLong(ehSize); // e_phoff (program header offset)
        bb.putLong(0); // e_shoff (section header offset)
        bb.putInt(0); // e_flags
        bb.putShort((short)ehSize); // e_ehsize
        bb.putShort((short)phSize); // e_phentsize
        bb.putShort((short)numProgHeaders); // e_phnum
        bb.putShort((short)0); // e_shentsize
        bb.putShort((short)0); // e_shnum
        bb.putShort((short)0); // e_shstrndx
        
        buffer.write(bb.array());
    }
    
    private void writeTextProgramHeader(ByteArrayOutputStream buffer, long offset, int size, long vaddr) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(56);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        
        bb.putInt(PT_LOAD); // p_type
        bb.putInt(PF_R | PF_X); // p_flags
        bb.putLong(offset); // p_offset
        bb.putLong(vaddr); // p_vaddr
        bb.putLong(vaddr); // p_paddr
        bb.putLong(size); // p_filesz
        bb.putLong(size); // p_memsz
        bb.putLong(0x1000); // p_align (page size)
        
        buffer.write(bb.array());
    }
    
    private void writeDataProgramHeader(ByteArrayOutputStream buffer, long offset, int size, long vaddr) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(56);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        
        bb.putInt(PT_LOAD); // p_type
        bb.putInt(PF_R | PF_W); // p_flags
        bb.putLong(offset); // p_offset
        bb.putLong(vaddr); // p_vaddr
        bb.putLong(vaddr); // p_paddr
        bb.putLong(size); // p_filesz
        bb.putLong(size); // p_memsz
        bb.putLong(0x1000); // p_align (page size)
        
        buffer.write(bb.array());
    }
    
    private long alignToPage(long offset) {
        long pageSize = 0x1000;
        return ((offset + pageSize - 1) / pageSize) * pageSize;
    }
    
    private void padToOffset(ByteArrayOutputStream buffer, int targetOffset) {
        int currentSize = buffer.size();
        if (targetOffset > currentSize) {
            int padding = targetOffset - currentSize;
            buffer.write(new byte[padding], 0, padding);
        }
    }
}