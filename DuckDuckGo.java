import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.ArrayList;

/* KeywordCounter Class */
public class DuckDuckGo {

	public static void main(String[] args) throws Exception
	{
		
		/* Creating an object of class fibonacciHeap */
		FibonacciHeapDataStructure fh = new FibonacciHeapDataStructure();
		
		/* Initializing */
		String[] arr = null;
		int keyword = 0;
		int k = 0;
		String input = "";
		String frequency = "";

		/* For reading an input file */
		FileReader inputFile = new FileReader(args[0]);
		BufferedReader bufferedReader = new BufferedReader(inputFile);

		/* For writing into an output file */
		File outputFile = new File("output_file.txt");
		FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");

		/* Parsing each line until STOP */
		for(;;){
			input = bufferedReader.readLine();
			if(input.equalsIgnoreCase("stop")){
				break;
			}
			else if(input.contains("$")){
				arr = input.split(" ");
				frequency = arr[0].substring(1);
				keyword = Integer.parseInt(arr[1]);
				fh.insertKeyword(frequency, keyword);
			}
			else {
				k = Integer.parseInt(input.trim());
				//System.out.println(k);
				
				List<FibonacciHeapNode> list = new ArrayList<>();
				for(int i=1; i<=k; i++) { 	
					FibonacciHeapNode node = fh.removeMaxNode();
					list.add(node);
					//System.out.println(node.getKeyword());
					outputStreamWriter.append(node.getKeyword());
					if(i == k) {
						continue;
					}
					outputStreamWriter.append(",");
				}
				for(FibonacciHeapNode n : list) { 	
					fh.insertKeyword(n.keyword, n.frequency);
				}
				
				outputStreamWriter.append("\n");
			}
		}
		
		outputStreamWriter.close();
		fileOutputStream.close();
		bufferedReader.close();
		inputFile.close();
			
	}
}