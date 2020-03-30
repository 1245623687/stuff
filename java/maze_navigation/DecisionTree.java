
public class DecisionTree {

	private Labrinth labrinth;

	private DecisionTreeNode root;

	public DecisionTree(DecisionTreeNode _root, Labrinth _labrinth) {
		labrinth = _labrinth;
		root = _root;

	}

	public boolean makeNextDecision(DecisionTreeNode lastDecision) {

		System.out.println("makeNextDecision(): " + lastDecision.coordinate().toString());

		labrinth.print();

		boolean atGoal = false;
		int choiceNumber = 0;
		int numberOfChoices = 4;
		while(atGoal == false && choiceNumber < numberOfChoices) {
			int thisDecisionChoice = lastDecision.nextChoice();
			DecisionTreeNode currentDecision = new DecisionTreeNode(labrinth.nextChoiceAsCoordinate(thisDecisionChoice, lastDecision.coordinate()));

			System.out.println("lastDecision: ");
			lastDecision.coordinate().print(); 

			if(labrinth.isValid(thisDecisionChoice, lastDecision.coordinate())) {	
				System.out.println("was valid: " + thisDecisionChoice);
				// record this decision choice
				lastDecision.setChild(currentDecision);
				currentDecision.setParent(lastDecision);
				if(labrinth.canEndPath(currentDecision.coordinate())) {
					return true;
				} else {
					atGoal = makeNextDecision(currentDecision); // reduce problem
					if(atGoal == false) { // backtrack has occurred
						//un - record this decision choice
						lastDecision.setChild(null);
					}
				}
			}
			choiceNumber += 1;
		}
		return atGoal;
	}

	public DecisionTreeNode root() {
		return root;
	}

	public static void main(String[] args) {

		// create decision tree

		Labrinth labrinth = new Labrinth();
		DecisionTree decisionTree = new DecisionTree(new DecisionTreeNode(labrinth.start()), labrinth);

		decisionTree.makeNextDecision(decisionTree.root());

	}

}