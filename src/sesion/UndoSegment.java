package sesion;

import java.util.ArrayList;
import java.util.List;

import bridge.transferable.interfaces.CommandInterface;


//implemented with circular vector
public class UndoSegment {
	private final static int segmentLength=20;
	private int cursorIndex;
	private ArrayList<CommandInterface> commands;
	
	public UndoSegment(){
		commands=new ArrayList<CommandInterface>();
		cursorIndex=-1;
	}
	
	public void addCommand(CommandInterface command){
		cursorIndex++;
		commands.add(cursorIndex,command);
		
		for (int i=cursorIndex+1;i<commands.size();i++)
			commands.remove(i);
		
		
		//when the size of the commands excedes allocated memory , the first 7/10  the of the segment is discarded
		//and the index is possitioned acoordingly , so half of the most fresh information is saved
		if (commands.size()==segmentLength) {
			List<CommandInterface> discardList =new ArrayList<CommandInterface>();
			for (int i=0;i<commands.size()/7;i++){
				discardList.add(commands.get(i));
			}
			commands.removeAll(discardList);
			cursorIndex=commands.size()-1;
		}
	}
	
	
	public void undo(){
		if (cursorIndex>-1){
			commands.get(cursorIndex).undo();
			cursorIndex--;
		}
	}
	
	public void undoAndRemove(){
		if (cursorIndex>-1){
			commands.get(cursorIndex).undo();
			commands.remove(commands.get(cursorIndex));
			cursorIndex--;
		}
	}
	
	public void redo(){
		if (cursorIndex<commands.size()-1){
			cursorIndex++;
			commands.get(cursorIndex).execute();
		}
	}

	@Override
	public String toString() {
		return "UndoSegment [cursorIndex=" + cursorIndex + ", commands="
				+ commands + "]";
	}
	
	public int getCursorIndex(){
		return cursorIndex;
	}
	
	public int getSegmentLength(){
		return commands.size();
	}
	
	public boolean canUndo(){
		return cursorIndex>-1;
	}
	
	public boolean canRedo(){
		return cursorIndex<commands.size()-1;
	}
}
