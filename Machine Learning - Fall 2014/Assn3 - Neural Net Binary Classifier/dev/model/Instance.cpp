#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "Instance.h"


Instance::Instance()
{
}

Instance::Instance(unsigned int numAttrs, std::vector<unsigned int> values, unsigned int classVal)
{
	_numAttrs = numAttrs;
	_values = values;
	_class = classVal;
}

Instance::Instance(const Instance& i)
{
	_numAttrs = i._numAttrs;
	_values = i._values;
	_class = i._class;
}

Instance& Instance::operator =(const Instance& i)
{
	_numAttrs = i._numAttrs;
	_values = i._values;
	_class = i._class;
	return *this;
}

std::ostream& operator<<(std::ostream& os, const Instance& w)
{
	return os;
}
