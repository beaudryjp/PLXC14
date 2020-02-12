import java.util.*;

class SymbolTable {
    private static ArrayList<HashMap<String,String>> table;
    private static int blockIndex;

    static {
        table = new ArrayList<HashMap<String,String>>();
        table.add(new HashMap<String,String>());
        blockIndex = 0;
    }
	
	public static void put(String variable, String value) {
        table.get(blockIndex).put(variable, value);
	}
	
	public static String get(String variable) {
		int k = 0;
        while((blockIndex >= k) && !table.get(blockIndex - k).containsKey(variable)){
            k++;
        }
        return table.get(blockIndex - k).get(variable);
	}

	public static void startBlock(){
        blockIndex++;
        table.add(blockIndex, new HashMap<String,String>());    
    }

	public static void endBlock(){
        table.remove(blockIndex);
        blockIndex--; 
    }

	public static boolean findInCurrentBlock(String variable){
        if(table.get(blockIndex).containsKey(variable))
            return true;
		return false;
    }

	public static int inWhichBlock(String variable){
        int k = 0;
        int tmp = blockIndex;
        while((blockIndex > k) && !table.get(blockIndex - k).containsKey(variable + "_" + tmp)){
            k++;
            tmp--;
        } 
        if(blockIndex == k) return 0;
        return tmp;
    }

	public static boolean find(String variable){
        int k = 0;
        while((blockIndex >= k) && !table.get(blockIndex - k).containsKey(variable)){
            k++;
        }

        if(blockIndex < k){
            return false;
        } else {
            return true;
        }
    }

    public static int getIndex(){
        return blockIndex;
    }

    public static String varBlock(String variable){
        return variable + "_" + blockIndex;
    }

	public static void showTable() {
		for(int k = 0; k < table.size(); k++){
			System.out.println("Block index: " + k + "\tVariables: " + table.get(k).toString());
		}
	}
}