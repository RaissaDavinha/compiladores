
public class Instruction {
	private String label;
	private String instructionName;
	private String argument1;
	private String argument2;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setInstructionName(String instructionName) {
		this.instructionName = instructionName;
	}
	public String getInstructionName() {
		return instructionName;
	}
	
	public int getArgument1Int() {
		return Integer.parseInt(argument1);
	}
	public String getArgument1String() {
		return argument1;
	}
	
	public void setArgument1Int(int argument1) {
		this.argument1 = Integer.toString(argument1);
	}
	public void setArgument1String(String argument1) {
		this.argument1 = argument1;
	}
	
	public int getArgument2Int() {
		return Integer.parseInt(argument2);
	}
	public String getArgument2String() {
		return argument2;
	}
	
	public void setArgument2Int(int argument2) {
		this.argument2 = Integer.toString(argument2);
	}
	public void setArgument2String(String argument2) {
		this.argument2 = argument2;
	}
}