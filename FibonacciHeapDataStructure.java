import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/* FibonacciHeap class */
public class FibonacciHeapDataStructure {

	/* maximumNode points to the maximum element root in the root level circular DLL */
	FibonacciHeapNode maximumNode;
	
	/* Total number of nodes in the FibonacciHeap */
	int totalNodes;
	
	/* The HashMap stores keyword-frequency pairs */
	HashMap<String, FibonacciHeapNode> hmap = new HashMap<String,FibonacciHeapNode>();

	
	/* This function inserts the keyword associated with the node and its frequency in the FibonacciHeap */
	public void insertKeyword(String keyword, int frequency) {

		/* If the new keyword being checked is in the HashMap */
		if(hmap.get(keyword) != null) {
			
			/* Increment the frequency associated with that keyword */
			increaseKeyValue(hmap.get(keyword), frequency);
			
		}
		
		/* If the new keyword being checked is not in the HashMap */
		else {
			
			/* Create a new node and associate that keyword with that */
			FibonacciHeapNode node = new FibonacciHeapNode(keyword, frequency);
			
			/* The node to be inserted is not the first node i.e. the maximum node exists */
			if(maximumNode != null) {
				
				/* Attaching the new node in the root level circular DLL */
				node.left = maximumNode;
				node.right = maximumNode.right;
				maximumNode.right = node;
				node.right.left = node;

				/* Updating the maximumNode */
				if(maximumNode.frequency < node.frequency) {
					maximumNode = node;
				}
				
			}
			
			/* The node to be inserted is the first node i.e. maximumNode does not exist. Make the new node as the maximumNode */
			else {
				maximumNode = node;
			}

			/* Incrementing the total number of nodes and add the reference of this node along with the keyword to the HashMap*/
			++totalNodes;
			hmap.put(keyword, node);
			
		}	
	}


	/* This function increments the frequency associated with a keyword/node */
	public void increaseKeyValue(FibonacciHeapNode node, int frequency) {
		
		/* Updating the frequency */
		node.frequency = node.frequency + frequency;
		
		/* Checking if max heap property exists (the parent's frequency has to be greater than the frequency of it's children) */
		FibonacciHeapNode parentNode = node.parent;
		
		/* If child's frequency is greater than the parent's frequency, then detach the child. As a result, cascading cut takes place */
		if((parentNode != null) && (node.frequency > parentNode.frequency)) {
			
			detachNode(node, parentNode);
			triggerCascadeCut(parentNode);
			
		}

		/* Updating the maximumNode */
		if(node.frequency > maximumNode.frequency) {
			maximumNode = node;
		}	
		
	}


	/* This function detaches the childNode from the parentNode and places the childNode in the root level circular DLL */
	public void detachNode(FibonacciHeapNode childNode, FibonacciHeapNode parentNode) {
		
		/* Updating the child level circular DLL */
		childNode.left.right = childNode.right;
		childNode.right.left = childNode.left;

		/* Decrementing the degree of the parentNode by 1 */
		--parentNode.degree;

		/* If the parentNode was pointing to the childNode, update the pointer to another child */
		if(parentNode.child == childNode) {
			parentNode.child = childNode.right;
		}

		/* If after deletion of childNode, no other child exists, update the child pointer of the parentNode  */
		if(parentNode.degree == 0) {
			parentNode.child = null;
		}

		/* Attaching the childNode to the root level circular DLL and updating the pointers */
		childNode.left = maximumNode;
		childNode.right = maximumNode.right;
		maximumNode.right = childNode;
		childNode.right.left = childNode;
		childNode.parent = null;
		childNode.childCut = false;

	}


	/* This function recursively updates the childCut values of the parent node from which a child has been detached */
	public void triggerCascadeCut(FibonacciHeapNode node) {
			
		FibonacciHeapNode parentNode = node.parent;
		
		/* If parent does not exist, do nothing and return */
		if(parentNode == null) {
			return;
		}
		
		/* If parent exists, update the childCut value */
		else {
			
			/* If the value is false, change it to true */
			if(node.childCut == false) {
				node.childCut = true;
			}
			
			/* If the value is already true, we have to perform the cut function */
			else {
				detachNode(node, parentNode);
				triggerCascadeCut(parentNode);
				
			}
		}
	}


	/* Removes and returns the maximum node */
	public FibonacciHeapNode removeMaxNode() {
	
		FibonacciHeapNode max = maximumNode;
		
		/* If maximumNode exists */
		if(max != null) {
			
			
			/* Removing the node from the HashMap */
			Iterator<HashMap.Entry<String,FibonacciHeapNode>> itr = hmap.entrySet().iterator();
			while(itr.hasNext()) {
				HashMap.Entry<String,FibonacciHeapNode> hmapEntry = itr.next(); 
				if(hmapEntry.getValue() == maximumNode)
				{
					hmap.remove(hmapEntry.getKey());
					break;
				}
			} 

			/* Storing the total children of a node */
			int nChildren = max.degree;
			
			/* Detaching all the children from the maximumNode, and attaching them to root level circular DLL one by one */
			FibonacciHeapNode currNode = max.child;
			FibonacciHeapNode nextNode;
			for(int i=1; i<=nChildren; i++) {
				
				nextNode = currNode.right;

				/* Breaking the childNode from the child level circular DLL */
				currNode.left.right = currNode.right;
				currNode.right.left = currNode.left;

				/* Adding the detached childNode to root level circular DLL */
				currNode.left = maximumNode;
				currNode.right = maximumNode.right;
				maximumNode.right = currNode;
				currNode.right.left = currNode; 

				/* Updating the parent pointer of the childNode to null and updating the currNode ptr to next node */
				currNode.parent = null;
				currNode = nextNode;
				
			}

			/* Removing maximumNode from its neighbors */
			max.left.right = max.right;
			max.right.left = max.left;


			/* If initially no child was present, set maximumNode to null */
			if(max == max.right) {
				maximumNode = null;
			}
			
			/* If initially child was present, set maximumNode to the right node, and do pairwise merging */
			else {
				
				maximumNode = max.right;
				treePairWiseMerging();
				
			}
			
			/* Decrementing total number of nodes by 1 */
			--totalNodes;
		}
		
		/* Returning the reference to the removedNode */
		return max;
		
	}


	/* This function makes the one node child of the other node and updates all the pointers */
	public void makeChild(FibonacciHeapNode childNode, FibonacciHeapNode parentNode) {

		/* Updating the degree and the childCut value of the parent node as a new child is getting attached */
		parentNode.degree++;
		childNode.childCut = false;

		/* Removing the child node from the root level circular DLL */
		childNode.left.right = childNode.right;
		childNode.right.left = childNode.left;

		/* Updating the parent pointer of the childNode */
		childNode.parent = parentNode;

		/* If other siblings of the childNode exists */
		if(parentNode.child != null) {
			
			/* Adding the childNode to the sibling level DLL and updating the pointers  */
			childNode.left = parentNode.child; 
			childNode.right = parentNode.child.right; 
			parentNode.child.right = childNode; 
			childNode.right.left = childNode;
			
		}
		
		/* If no siblings exist */
		else {
			
			/* Updating the child pointer of the parentNode */
			parentNode.child = childNode;
			childNode.right = childNode;
			childNode.left = childNode;
		}
	}

	
	/* This function performs a pairwise merge */
	public void treePairWiseMerging() {

		/* Creating a optimal size table to store degrees values */
		int size = ((int) Math.floor(Math.log(totalNodes)*(1.0/Math.log((1.0+Math.sqrt(5.0))/2.0))))+1;
		List<FibonacciHeapNode> table = new ArrayList<>(size);

		/* Initializing all the degree values to null */
		int i = 1;
		while(i <= size) { 	
			++i;
			table.add(null);
		}

		/* Counting the number of root nodes */
		int nRootNode = 0;
		FibonacciHeapNode temp = maximumNode;
		if(temp != null) {
			++nRootNode;
			temp = temp.right;                     
			while(temp != maximumNode) {
				++nRootNode;
				temp = temp.right;
			}
		}
		
		
		/* Iterating each root node one by one and doing pairwise merge */
		for(int j=1; j<=nRootNode; j++) {
			
			int deg = temp.degree; 
			FibonacciHeapNode nxt = temp.right;

			/* Keep checking until no two same degree tree exist in the table */
			while(true) {
				
				/* Check if another same degree tree exists at the root */
				FibonacciHeapNode node2 = table.get(deg);
				
				/* If no other same degree tree exists, break the loop */
				if(node2 == null) {
					break;
				}

				/* If other same degree tree exists, make the tree with smaller frequency child of the tree with higher frequency */
				if(temp.frequency < node2.frequency) {
					FibonacciHeapNode t = node2;
					node2 = temp;
					temp = t;
				}
				makeChild(node2, temp);
				table.set(deg, null);
				++deg;
				
			}
			
			/* Updating the degree of the newly formed tree in the table */
			table.set(deg, temp);
			temp = nxt;
		}

		/* Updating the maximumNode */
		maximumNode = null;
		int k = 0;
		while(k < size) {
			
			/* Iterating over all remaining trees in the root level DLL after pairwise merge */
			FibonacciHeapNode curr = table.get(k);
			++k;
			
			/* If no tree of the degree k exists, continue */
			if(curr == null) {
				continue;
			}
			
			/* If no maximumNode exists, make the current node as maximumNode */
			if(maximumNode == null)								
			{
				maximumNode = curr;
			} 
			
			/* If maximumNode exists */
			else 
			{
				curr.left.right = curr.right;					
				curr.right.left = curr.left;

				curr.left = maximumNode;								
				curr.right = maximumNode.right;
				maximumNode.right = curr;
				curr.right.left = curr;

				if(maximumNode.frequency < curr.frequency)									
				{
					maximumNode = curr;
				}
			}
		}
	}
	
}