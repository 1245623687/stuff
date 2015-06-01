#ifndef HIDDEN_NEURON_H
#define HIDDEN_NEURON_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

class Net;
class NetTeach;

class HiddenNeuron
{

	friend Net;
	friend NetTeach;

private:

	std::string _name;

	unsigned int _numIncomingWeights;

	std::vector<unsigned int> _incomingWeights;

	unsigned int _numOutgoingWeights;

	//the indices of the weights pointed to by this neuron
	std::vector<unsigned int> _outgoingWeights;

	float _output;


	float _delta;

	float _sumOfIncomingtWeights;


public:
	HiddenNeuron();

	HiddenNeuron(std::string name, unsigned int numIncomingWeights, unsigned int numOutgoingWeights);

	HiddenNeuron(const HiddenNeuron& hn);

	HiddenNeuron& operator =(const HiddenNeuron& hn);

	friend std::ostream& operator<<(std::ostream& os, const HiddenNeuron& hn);

};
#endif
