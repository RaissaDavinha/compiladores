import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class InstructionList {
	private ArrayList<Instruction> list;
	private Instruction temporaryInstruction;
	private int haveLabel;
	private int i;
	
	public ArrayList<Instruction> getList() {
		return list;
	}
	public void setList(Instruction lista) {
		this.list.add(lista);
	}
	public Instruction getTemporaryInstruction() {
		return temporaryInstruction;
	}
	public Instruction getInstruction(int index) {
		return list.get(index);
	}
	public void setTemporaryInstruction(Instruction temporaryInstruction) {
		this.temporaryInstruction = temporaryInstruction;
	}
	public InstructionList(String path) {
		haveLabel = 0;
		list = new ArrayList<Instruction>();
		try {
			this.readFile(path);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Error opening file:" + e.getMessage());
		}
	}
	public boolean isInstruction(String field) {
		switch (field) {
			case "LDC":
			case "LDV":
			case "ADD":
			case "SUB":
			case "MULT":
			case "DIVI":
			case "INV":
			case "AND":
			case "OR":
			case "NEG":
			case "CME":
			case "CMA":
			case "CEQ":
			case "CDIF":
			case "CMEQ":
			case "CMAQ":
			case "START":
			case "HLT":
			case "STR":
			case "JMP":
			case "JMPF":
			case "NULL":
			case "RD":
			case "PRN":
			case "ALLOC":
			case "DALLOC":
			case "CALL":
			case "RETURN":
			case "RETURNF":
				return true;
			default:
				return false;
			}
	}
	public void readFile(String fileName) throws IOException {
		
		FileReader fileReader = new FileReader(fileName);
		BufferedReader reader = new BufferedReader(fileReader);
		String readContent = reader.readLine();
		String fileReturn = "";
		
		while (readContent != null) {
			fileReturn = fileReturn + readContent;
			fileReturn = fileReturn + "\n";
			readContent = reader.readLine();
		}
		
		fileReader.close();

		String separatedInstructions[] = fileReturn.split("\n");
		for (i = 0; i < separatedInstructions.length; i++) {
			if (i > 91) {
				i++;
				i--;
			}
			temporaryInstruction = new Instruction();
			
			String instructionNameArgument[] = separatedInstructions[i].split(" ");
			
			if (isInstruction(instructionNameArgument[0])) {
				haveLabel = 0;
				temporaryInstruction.setInstructionName(instructionNameArgument[0 + haveLabel]);
			} else {
				temporaryInstruction.setLabel(instructionNameArgument[0]);
				haveLabel = 1;
				temporaryInstruction.setInstructionName(instructionNameArgument[0 + haveLabel]);
			}
			
			
			if (instructionNameArgument.length > 1 + haveLabel) {
				temporaryInstruction.setArgument1String(instructionNameArgument[1 + haveLabel]);
			
				if (instructionNameArgument.length > 2 + haveLabel) {
					temporaryInstruction.setArgument2String(instructionNameArgument[2 + haveLabel]);
				} else {
					temporaryInstruction.setArgument2String(null);
				}
			}else {
				temporaryInstruction.setArgument1String(null);
				temporaryInstruction.setArgument2String(null);
			}
			
			this.setList(temporaryInstruction);
		}
	}
	
}
