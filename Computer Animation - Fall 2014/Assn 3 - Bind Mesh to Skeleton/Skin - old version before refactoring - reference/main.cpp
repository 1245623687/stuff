
#include <iostream>
#include <vector>
#include <string>
#include <iterator>
#include <algorithm>
#include <GL/glui.h>
#include "token.h"
#include "BallJoint.h"
#include "SkeletonParser.h"
#include "SkinParser.h"

using namespace std;

SkeletonParser skeletonParser;
SkinParser skinParser;
Model model;

tree<BallJoint>::iterator currJoint;
int currJointIndex = 0;

float xy_aspect;
int   last_x, last_y;
float rotationX = 0.0, rotationY = 0.0;
int   main_window;

static float xRot = 0;
static float yRot = 0;
static float zRot = 0;
static float xTran = 0;
static float yTran = 0;
static float zTran = 0;

void myGlutKeyboard(unsigned char Key, int x, int y);
void transform(float direction, int currentMode, int currentAxis);
void drawAxes(void);
void drawTriangles();
void incrementSelectedJoint();
void decrementSelectedJoint();
void increaseRotate(int _currentAxis);
void decreaseRotate(int _currentAxis);
void updateSkel();

void myGlutKeyboard(unsigned char Key, int x, int y)
{
	enum mode { MODE_ROTATE, MODE_TRANSLATE };
	static int currentMode = MODE_TRANSLATE;

	enum axis { X_AXIS = 1, Y_AXIS = 3, Z_AXIS = 5 };
	static int currentAxis = X_AXIS;

	switch(Key)
	{
	case 27:
	case 'q':
	case 'Q':
		xRot = 0;
		yRot = 0;
		zRot = 0;
		xTran = 0;
		yTran = 0;
		zTran = 0;
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		break;
		//switch to rotate mode
	case 'r':
	case 'R':
		//std::cout << "rotate" << std::endl;
		currentMode = MODE_ROTATE;
		break;
		//switch to translate mode
	case 't':
	case 'T':
		//std::cout << "translate" << std::endl;
		currentMode = MODE_TRANSLATE;
		break;
		//switch to x axis
	case 'x':
	case 'X':
		//std::cout << "x" << std::endl;
		currentAxis = X_AXIS;
		break;
		//switch to y axis
	case 'y':
	case 'Y':
		//std::cout << "y" << std::endl;
		currentAxis = Y_AXIS;
		break;
		//switch to z axis
	case 'z':
	case 'Z':
		//std::cout << "z" << std::endl;
		currentAxis = Z_AXIS;
		break;
		//increase val
	case '+':
		//std::cout << "up" << std::endl;
		transform(1, currentMode, currentAxis);
		break;
		//decrease val
	case '-':
		//std::cout << "down" << std::endl;
		transform(-1, currentMode, currentAxis);
		break;
	case '\b':
		exit(0);
	case '>':
		incrementSelectedJoint();
		break;
	case '<':
		decrementSelectedJoint();
	case '[':
		decreaseRotate(currentAxis);
		break;
	case ']':
		increaseRotate(currentAxis);
		break;
	};

	glutPostRedisplay();
}

void increaseRotate(int _currentAxis){
	std::cout << "inc rotate" << std::endl;
	switch(_currentAxis){
	case 0: //x axis
		currJoint->pose[0] += .2;
		break;
	case 1: //y axis
		currJoint->pose[1] += .2;
		break;
	case 2: //z axis
		currJoint->pose[2] += .2;
		break;
	}
}

void decreaseRotate(int _currentAxis){
	std::cout << "dec rotate" << std::endl;
	switch(_currentAxis){
		case 0: //x axis
			currJoint->pose[0] -= .2;
			break;
		case 1: //y axis
			currJoint->pose[1] -= .2;
			break;
		case 2: //z axis
			currJoint->pose[2] -= .2;
			break;
	}
}

void incrementSelectedJoint(){
	
	if(currJointIndex == skeletonParser.numBallJoints - 1){
		currJointIndex = 0;
	}
	else{
		currJointIndex++;
	}
	
	currJoint = skeletonParser.getJointByIndex(currJointIndex);
	std::cout << "currJoint " << currJointIndex << " is " << currJoint->name << std::endl;
}

void decrementSelectedJoint(){

	if(currJointIndex == 0){
		currJointIndex = skeletonParser.numBallJoints - 1;
	}
	else{
		currJointIndex--;
	}
	
	currJoint = skeletonParser.getJointByIndex(currJointIndex);
	std::cout << "currJoint " << currJointIndex << " is " << currJoint->name << std::endl;
}

void transform(float direction, int currentMode, int currentAxis){
	enum mode { MODE_ROTATE, MODE_TRANSLATE };
	enum axis { X_AXIS = 1, Y_AXIS = 3, Z_AXIS = 5 };

	int state = currentMode + currentAxis;
	//std::cout << "state " << state << std::endl;

	direction *= (0.25);

	switch(state){
		//x axis + rotate mode
	case 1:
		//std::cout << "rotate x " << direction << std::endl;
		xRot += 5*direction;
		break;
		//x axis + translate mode
	case 2:
		//std::cout << "translate x " << direction << std::endl;
		xTran += direction;
		break;
		//y axis + rotate mode
	case 3:
		//std::cout << "rotate y " << direction << std::endl;
		yRot += 5*direction;
		break;
		//y axis + translate mode
	case 4:
		//std::cout << "translate y " << direction << std::endl;
		yTran += direction;
		break;
		//z axis + rotate
	case 5:
		//std::cout << "rotate z " << direction << std::endl;
		zRot += 5*direction;
		break;
		//z axis + translate
	case 6:
		//std::cout << "translate z " << direction << std::endl;
		zTran += direction;
		break;
	default:
		std::cout << "invalid state" << std::endl;
	}
}

/**************************************** myGlutReshape() *************/

void myGlutReshape(int x, int y)
{
	int tx, ty, tw, th;
	GLUI_Master.get_viewport_area(&tx, &ty, &tw, &th);
	glViewport(tx, ty, tw, th);

	xy_aspect = (float)tw / (float)th;

	glutPostRedisplay();
}

void drawCube(tree<BallJoint>::pre_order_iterator& _it){
	float xboxmin = _it->boxmin[0];
	float yboxmin = _it->boxmin[1];
	float zboxmin = _it->boxmin[2];
	float xboxmax = _it->boxmax[0];
	float yboxmax = _it->boxmax[1];
	float zboxmax = _it->boxmax[2];

	glBegin(GL_LINES);

	glVertex3f(xboxmin, yboxmin, zboxmin);
	glVertex3f(xboxmax, yboxmin, zboxmin);
	
	glVertex3f(xboxmin, yboxmin, zboxmin);
	glVertex3f(xboxmin, yboxmax, zboxmin);

	glVertex3f(xboxmin, yboxmax, zboxmin);
	glVertex3f(xboxmax, yboxmax, zboxmin);

	glVertex3f(xboxmax, yboxmin, zboxmin);
	glVertex3f(xboxmax, yboxmax, zboxmin);
	/////////////////////////////////////////////////////////////
	
	glVertex3f(xboxmin, yboxmin, zboxmin);
	glVertex3f(xboxmin, yboxmin, zboxmax);
	
	glVertex3f(xboxmax, yboxmax, zboxmin);
	glVertex3f(xboxmax, yboxmax, zboxmax);

	glVertex3f(xboxmin, yboxmax, zboxmin);
	glVertex3f(xboxmin, yboxmax, zboxmax);

	glVertex3f(xboxmax, yboxmin, zboxmin);
	glVertex3f(xboxmax, yboxmin, zboxmax);
	/////////////////////////////////////////////////////////////

	glVertex3f(xboxmax, yboxmin, zboxmax);
	glVertex3f(xboxmax, yboxmax, zboxmax);

	glVertex3f(xboxmin, yboxmin, zboxmax);
	glVertex3f(xboxmin, yboxmax, zboxmax);

	glVertex3f(xboxmin, yboxmin, zboxmax);
	glVertex3f(xboxmax, yboxmin, zboxmax);

	glVertex3f(xboxmin, yboxmax, zboxmax);
	glVertex3f(xboxmax, yboxmax, zboxmax);

	glEnd();
}

/***************************************** myGlutDisplay() *****************/

void myGlutDisplay()
{
	glClearColor(.9f, .9f, .9f, 1.0f);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glEnable(GL_LIGHTING);

	tree<BallJoint>::pre_order_iterator it;
	
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	//GLfloat aspect = (GLfloat)800 / (GLfloat)600;
	//glOrtho(-2.0*aspect, 2.0*aspect, -2.0, 2.0, -10.0, 10.0);

	gluPerspective(90, 800 / 600, 0, 100);

	gluLookAt(0.0f, 0.0f, 2,     //The camera is 2 'units' behind the center of the scene.
		0.0f, 0.0f, 0.0f,     //the center of the scene is at the origin.  This prolly is easiest way to do things...
		0.0f, 1.0f, 0.0f);    // specifies that Y is the UP vector, and GL figures everything else out. 

	//rotations from keyboard
	glRotatef(xRot, 1.0f, 0.0f, 0.0f);
	glRotatef(yRot, 0.0f, 1.0f, 0.0f);
	glRotatef(zRot, 0.0f, 0.0f, 1.0f);

	//translation from keyboard
	glTranslatef(xTran, yTran, zTran);

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	updateSkel();

	it = skeletonParser.tr.begin();
	while(it != skeletonParser.tr.end()){
		glPushMatrix();
		glLoadIdentity();
		//skeletonParser.calculateJoint(it);
		glLoadMatrixf(it->world.get());

		drawCube(it);
		//glutWireCube(1);
		//std::cout << "got " << it->getName() << " matrix is " << std::endl;
		//cout << it->world << std::endl;
		glPopMatrix();
		it++;
	}

	glLoadIdentity();
	model.drawTriangles();
	drawAxes();
	
	//glMatrixMode(GL_PROJECTION);
	//glPushMatrix();
	

	glutSwapBuffers();
}

void drawAxes(void)
{
	glBegin(GL_LINES);
	//glVerstex3f(x,y,z)
	glColor3f(0, 0, 1);
	glVertex3f(0.0, 0.0, 0.0);
	glVertex3f(0.0, 0.0, 10.0);

	glVertex3f(0.0, 0.0, 0.0);
	glVertex3f(0.01, 0.01, 10.0);
	glColor3f(0, 1, 0);
	glVertex3f(0.0, 0.0, 0.0);
	glVertex3f(0.0, 10.0, 0.0);
	glColor3f(1, 0, 0);
	glVertex3f(0.0, 0.0, 0.0);
	glVertex3f(10.0, 0.0, 0.0);

	glEnd();
}

void updateSkel(){
	//calculate all the joint values
	tree<BallJoint>::pre_order_iterator it;
	it = skeletonParser.tr.begin();
	int i = 0;
	do{
		if(it->index == -1){ it->index = i; }
		//std::cout << it->name << " has index " << i << std::endl;
		std::cout << it->pose[0] << std::endl;
		std::cout << it->pose[1] << std::endl;
		std::cout << it->pose[2] << std::endl;

		skeletonParser.calculateJoint(it);
		it++; i++;
	} while(it != skeletonParser.tr.end());
	skeletonParser.calculateJoint(it);
}

/**************************************** main() ********************/

int main(int argc, char* argv[])
{
	//**************************************************************
	//Build the joint tree
	auto skelTokenizer = Tokenizer("test.skel");
	std::vector<std::string> tokenList;
	std::string token;
	while(skelTokenizer.GetToken(token)){
		//std::cout << token << std::endl;
		tokenList.push_back(token);
		token.clear();
	}
	skelTokenizer.Close();
	skeletonParser = SkeletonParser(tokenList);
	tree<BallJoint> tr = skeletonParser.getTree();

	updateSkel();

	//build the skin model
	//std::cout << "build skin model" << std::endl;
	//tokenList.clear();
	//token.clear();
	//auto skinTokenizer = Tokenizer("wasp.skin");
	//while(skinTokenizer.GetToken(token)){
		//std::cout << token << std::endl;
	//	tokenList.push_back(token);
	//	token.clear();
	//}
	//skinTokenizer.Close();
	//skinParser = SkinParser(tokenList, tr, skeletonParser.numBallJoints);
	//model = skinParser.getModel();
	

	//**************************************************************

	/****************************************/
	/*   Initialize GLUT and create window  */
	/****************************************/

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowPosition(50, 50);
	glutInitWindowSize(800, 600);

	main_window = glutCreateWindow("Assn 1");
	glutDisplayFunc(myGlutDisplay);
	GLUI_Master.set_glutReshapeFunc(myGlutReshape);
	GLUI_Master.set_glutKeyboardFunc(myGlutKeyboard);

	glutMainLoop();

	return EXIT_SUCCESS;
}

