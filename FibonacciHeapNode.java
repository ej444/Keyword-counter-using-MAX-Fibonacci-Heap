/* Node class */
public class FibonacciHeapNode {
	
	/* Node pointers */
	FibonacciHeapNode parent;
	FibonacciHeapNode left;
	FibonacciHeapNode right;
	FibonacciHeapNode child;
	
	/* Degree: Number of children of a node */
	int degree;
	
	/* True: if a child has been deleted from the node, else False */
	boolean childCut;
	
	/* Reference to the node */
	int frequency;
	
	/* String contained by the node */
	String keyword;
	
	/* Initializing properties using Constructor */
	public FibonacciHeapNode(String keyword, int frequency) {
		
		parent = null;
		left = this;
		right = this;
		child = null;
		degree = 0;
		childCut = false;
		this.frequency = frequency;
		this.keyword = keyword;
		}
	
	/* Function to get keyword associated with the node */
	public String getKeyword() {
		return this.keyword;
		}
	
	/* Function to get the frequency associated with the node */
	public int getFrequency() {
		return this.frequency;
		}
	}
