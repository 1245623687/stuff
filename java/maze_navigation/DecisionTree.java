import java.util.Scanner;


public class DecisionTree {

	private DecisionTreeNode root;

	private Scanner scan;

	public DecisionTree(DecisionTreeNode _root) {
		root = _root;
		scan = new Scanner(System.in);
	}

	public DecisionTree insertNode(DecisionTreeNode _node) {
		// System.out.println("insertNode: " + _node.toString());
		DecisionTreeNode currentNode = root;
		boolean isNull = currentNode.child() == null;

		int num = 0;
		while(!isNull) {
			isNull = currentNode.child() == null;
			//System.out.println("isNull: " + isNull);
			currentNode = currentNode.child();
			num++;
			if(num == 15) {
				break;
			}
		}
		if(currentNode != null) {
			currentNode.setChild(_node);	
		}

		return this;

	}

	public DecisionTree removeLeaf() {
		//System.out.println("removeLeaf()");

		DecisionTreeNode currentNode = root;
		boolean isNull = currentNode.child() == null;

		int num = 0;
		while(!isNull) {
			isNull = currentNode.child() == null;
			//System.out.println("isNull: " + isNull);
			currentNode = currentNode.child();
			num++;
			if(num == 15) {
				break;
			}
		}
		if(currentNode != null) {
			currentNode.setChild(null);	
		}
		return this;
	}

	public DecisionTreeNode getLeaf() {
		System.out.println("getLeaf()");

		DecisionTreeNode currentNode = root;
		System.out.println("root: " + root.coordinate().toString());
		boolean isNull = currentNode.child() == null;

		int num = 0;
		while(true) {
			if(currentNode.child() == null) {
				System.out.println("returning: " + currentNode.coordinate().toString());
				return currentNode;
			} else {
				currentNode = currentNode.child();	
			}
			
			num++;
			if(num == 15) {
				break;
			}
		}
		
		return currentNode;
	}

	public DecisionTreeNode root() {
		return root;
	}

	public void print() {
		System.out.println("tree");
		
		DecisionTreeNode currentNode = root;
		System.out.println("root: " + currentNode.toString());
		boolean isNull = currentNode.child() == null;

		if(!isNull) {
			currentNode = currentNode.child();
		}
		

		int num = 0;
		while(!isNull) {
			isNull = currentNode.child() == null;
		// System.out.println("isNull: " + isNull);
			System.out.println("\t" + currentNode.toString());
			currentNode = currentNode.child();
			num++;
			if(num == 15) {
				break;
			}
		}
	}
}
