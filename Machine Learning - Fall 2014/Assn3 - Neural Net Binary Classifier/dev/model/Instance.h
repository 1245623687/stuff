#ifndef INSTANCE_H
#define INSTANCE_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

class NetTeach;

class Instance
{
	friend NetTeach;

private:
	std::vector<unsigned int> _values;

	unsigned int _class;

	unsigned int _numAttrs;

public:
	Instance();

	Instance(unsigned int numAttrs, std::vector<unsigned int> values, unsigned int classVal);

	Instance(const Instance& i);

	Instance& operator =(const Instance& i);

	friend std::ostream& operator<<(std::ostream& os, const Instance& w);

};
#endif
