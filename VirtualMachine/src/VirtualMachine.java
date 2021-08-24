import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JOptionPane;

public class VirtualMachine {
	ArrayList<Integer> dataStack;
	InstructionList instructions;
	public ArrayList<Integer> breakPoints;
	private int printValue;
	
	int programCounter;
	int stackPointer;
	int auxRegister;
	private boolean executing;
	boolean virtualMachineOn;
	
	int count;
	String leitor;
	String jumpPos;
	int stackRecover;
	
	public VirtualMachine(String path) {
		dataStack = new ArrayList<Integer>();
		instructions = new InstructionList(path);
		breakPoints = new ArrayList<Integer>();
		
		for (int i = 0; i < instructions.getList().size(); i++) {
			System.out.println(instructions.getList().get(i).getLabel() + " " + instructions.getList().get(i).getInstructionName() + " " + instructions.getList().get(i).getArgument1String() + " " + instructions.getList().get(i).getArgument2String());	
		}
		
		programCounter = 0;
		stackPointer = 0;
		auxRegister = 0;
		executing = false;
		virtualMachineOn = false;
	}
	public void virtualMachineReset() {
		programCounter = 0;
		stackPointer = -1;
		auxRegister = 0;
		executing = true;
		virtualMachineOn = true;
		dataStack = new ArrayList<Integer>();
	}
	public void addBreakPoint(int breakIndex) {
		breakPoints.add(breakIndex);
	}
	public void clearBreakPoint() {
		breakPoints = new ArrayList<Integer>();
	}
	public int getStackPointer() {
		return stackPointer;
	}
	public int getProgramPointer() {
		return programCounter;
	}
	public int getPrintValue() {
		return this.printValue;
	}
	public boolean getExecuting() {
		return executing;
	}
	public boolean getVirtualMachineOn() {
		return virtualMachineOn;
	}
	public void setExecutingTrue() {
		executing = true;
	}
	public void setExecutingFalse() {
		executing = false;
	}
	public void setVirtualMachineOn() {
		virtualMachineOn = true;
	}
	public void setVirtualMachineOff() {
		virtualMachineOn = false;
	}
	public boolean isBreakLine() {
		return breakPoints.contains(programCounter);
	}
	public InstructionList getInstructionList() {
		return instructions;
	}
	public Instruction getInstruction() {
		return instructions.getInstruction(programCounter);
	}
	public ArrayList<Integer> getDataStack() {
		return dataStack;
	}
	public boolean isPrintInstruction() {
		if (instructions.getInstruction(programCounter).getInstructionName().equals("PRN")) {
			return true;
		} else {
			return false;
		}
	}
	public void programCounterIncrement() {
		programCounter++;
	}
	
	public ArrayList<Integer> getBreakPoints() {
		return breakPoints;
	}

	public void setBreakPoints(ArrayList<Integer> breakPoints) {
		this.breakPoints = breakPoints;
	}

	public void execInstruction() throws Exception {
		Instruction instruction = instructions.getInstruction(programCounter);
		switch (instruction.getInstructionName()) {
		case "LDC":
			if(dataStack.size() <= stackPointer + 1) {
				dataStack.add(0);
			}
			stackPointer++;
			dataStack.set(stackPointer, instruction.getArgument1Int());
			break;
			
		case "LDV":
			if(dataStack.size() <= stackPointer + 1) {
				dataStack.add(0);
			}
			stackPointer++;
			dataStack.set(stackPointer, dataStack.get(instruction.getArgument1Int()));			
			break;
			
		case "ADD":
			auxRegister = dataStack.get(stackPointer - 1) + dataStack.get(stackPointer);
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
			
		case "SUB":
			auxRegister = dataStack.get(stackPointer - 1) - dataStack.get(stackPointer);
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
			
		case "MULT":
			auxRegister = dataStack.get(stackPointer - 1) * dataStack.get(stackPointer);
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
			
		case "DIVI":
			auxRegister = dataStack.get(stackPointer - 1) / dataStack.get(stackPointer);
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
			
		case "INV":
			auxRegister = dataStack.get(stackPointer);
			auxRegister = -1 * auxRegister;
			dataStack.set(stackPointer, auxRegister);
			break;
			
		case "AND":
			if (dataStack.get(stackPointer - 1) == 1 && dataStack.get(stackPointer) == 1) {
				auxRegister = 1;
			}else {
				auxRegister = 0;
			}
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
			
		case "OR":
			if (dataStack.get(stackPointer - 1) == 1 || dataStack.get(stackPointer) == 1) {
				auxRegister = 1;
			}else {
				auxRegister = 0;
			}
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
			
		case "NEG":
			auxRegister = dataStack.get(stackPointer);
			auxRegister = 1 - auxRegister;
			dataStack.set(stackPointer, auxRegister);
			break;
		
		case "CME":
			if (dataStack.get(stackPointer - 1) < dataStack.get(stackPointer)) {
				auxRegister = 1;
			}else {
				auxRegister = 0;
			}
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
			
		case "CMA":
			if (dataStack.get(stackPointer - 1) > dataStack.get(stackPointer)) {
				auxRegister = 1;
			}else {
				auxRegister = 0;
			}
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
			
		case "CEQ":
			if (dataStack.get(stackPointer - 1) == dataStack.get(stackPointer)) {
				auxRegister = 1;
			}else {
				auxRegister = 0;
			}
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
			
		case "CDIF":
			if (dataStack.get(stackPointer - 1) != dataStack.get(stackPointer)) {
				auxRegister = 1;
			}else {
				auxRegister = 0;
			}
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
			
		case "CMEQ":
			if (dataStack.get(stackPointer - 1) <= dataStack.get(stackPointer)) {
				auxRegister = 1;
			}else {
				auxRegister = 0;
			}
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
		
		case "CMAQ":
			if (dataStack.get(stackPointer - 1) >= dataStack.get(stackPointer)) {
				auxRegister = 1;
			}else {
				auxRegister = 0;
			}
			dataStack.set(stackPointer - 1, auxRegister);
			stackPointer--;
			break;
			
		case "START":
			stackPointer = -1;
			break;
			
		case "HLT":
			setExecutingFalse();
			setVirtualMachineOff();
			JOptionPane.showMessageDialog(null, "Halt machine!");
			break;
			
		case "STR":
			while (dataStack.size() <= instruction.getArgument1Int() + 1) {
				dataStack.add(0);
			}
			auxRegister = dataStack.get(stackPointer);
			dataStack.set(instruction.getArgument1Int(), auxRegister);
			stackPointer--;
			break;
			
		case "JMP":
			jumpPos = instruction.getArgument1String();
			for (count = 0; count < instructions.getList().size(); count++) {
				String auxLabel = instructions.getInstruction(count).getLabel();
				if (auxLabel != null) {
					if (auxLabel.equals(jumpPos)) {
						programCounter = count;
						break;
					}	
				}
			}
			break;
			
		case "JMPF":
			if (dataStack.get(stackPointer) == 0) {
				jumpPos = instruction.getArgument1String();
				for (count = 0; count < instructions.getList().size(); count++) {
					String auxLabel = instructions.getInstruction(count).getLabel();
					if (auxLabel != null) {
						if (auxLabel.equals(jumpPos)) {
							programCounter = count;
							break;
						}	
					}
				}
			}
			stackPointer--;
			break;
			
		case "NULL":
			break;
			
		case "RD":
			if(dataStack.size() <= stackPointer + 1) {
				dataStack.add(0);
			}
			stackPointer++;
			dataStack.set(stackPointer, Integer.parseInt(JOptionPane.showInputDialog(null, "Digite um valor ")));
			break;
			
		case "PRN":
			printValue = dataStack.get(stackPointer);
			stackPointer--;
			break;
			
		case "ALLOC":
			for (int k = 0; k < (instruction.getArgument2Int()); k++) {
				if(dataStack.size() < stackPointer + instruction.getArgument2Int() + 1) {
					while (dataStack.size() < stackPointer + instruction.getArgument2Int() + 1) {
						dataStack.add(0);
					}
				}
				stackPointer++;
				dataStack.set(stackPointer, dataStack.get(instruction.getArgument1Int() + k));
			}
			break;
			
		case "DALLOC":
			for (int k = instruction.getArgument2Int() - 1; k >= 0; k--) {
				dataStack.set(instruction.getArgument1Int() + k, dataStack.get(stackPointer));
				stackPointer--;
			}
			break;
			
		case "CALL":
			if(dataStack.size() <= stackPointer + 1) {
				dataStack.add(0);
			}
			stackPointer++;
			dataStack.set(stackPointer, programCounter + 1);
			jumpPos = instruction.getArgument1String();
			for (count = 0; count < instructions.getList().size(); count++) {
				String auxLabel = instructions.getInstruction(count).getLabel();
				if (auxLabel != null) {
					if (auxLabel.equals(jumpPos)) {
						programCounter = count;
						break;
					}	
				}
			}
			break;
			
		case "RETURN":
			programCounter = dataStack.get(stackPointer) - 1;
			stackPointer--;
			break;
	
		default:
			JOptionPane.showMessageDialog(null, "Invalid Instruction at " + programCounter);
			break;
		}
	}
}
