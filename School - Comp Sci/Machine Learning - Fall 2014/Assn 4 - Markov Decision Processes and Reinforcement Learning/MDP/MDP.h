#ifndef M_D_P_H
#define M_D_P_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "ActionVariable.h"
#include "State.h"

class MDP_Parser;

class MDP
{
private:	

	friend MDP_Parser;

	std::vector<State> states;

	float gamma;

	std::vector<unsigned int> policy;

	std::vector<unsigned int> optimalPolicy;


public:

	void init();

	float reward(State& state);

	State transition(State& state, ActionVariable& actionVar);

	float utility(State& state);

	ActionVariable* determineOptimalPolicy(unsigned int numIter);

	void makePolicy();

	void updateUtilities();

	ActionVariable Bellman(State& state);

	void printPolicy();

	friend void main(int argc, char* argv[]);

};
#endif
