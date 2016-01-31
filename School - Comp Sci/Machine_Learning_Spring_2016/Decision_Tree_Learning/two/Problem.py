
from __future__ import division
import math
from Training_Data import Training_Data
from Decision_Tree import Decision_Tree

class Problem:

    # all the training examples and functions for accessing them are contained in this object
    training_set = Training_Data()

    # the decision tree and all the functions for manipulating it are contained in this object
    decision_tree = Decision_Tree()

    def __init__(self):
        pass

    def create_decision_tree(self):

        if(self.decision_tree.get_num_nodes() == 0):
            # create the root node
            pass
        # todo: finish this

    # information gain is a metric to measure the amount of information gained if we split the tree at this category
    def calculate_information_gain_for_category(self, category, training_set):

        parent_entropy = self.calculate_entropy_for_training_set(training_set)

    def calculate_entropy_for_training_set(self, training_set):

        sum = 0
        num_training_examples = self.training_set.get_number_training_examples(training_set)
        class_types = self.training_set.get_set_of_unique_class_types(training_set)
        num_class_types = len(class_types)
        for i in range(0, num_class_types):
            num_current_type = self.training_set.get_number_of_training_examples_of_class_type(class_types[i], training_set)
            Pi = float(num_current_type / num_training_examples)
            sum -= float(Pi)*(float(round(math.log(Pi, 2), 3)))
        return round(sum, 3)

    def calculate_entropy_for_category(self, category, training_set):

        sum = 0
        attributes = self.training_set.get_unique_attributes_for_category(category, training_set)
        for attribute in attributes:
            num_current_attribute = self.training_set.get_number_of_training_examples_for_attribute(category, attribute, training_set)
            num_training_examples = self.training_set.get_number_training_examples(training_set)
            Pi = float(num_current_attribute / num_training_examples)
            Hi = self.calculate_entropy_for_attribute(category, attribute, training_set)
            sum += float( Pi * Hi)
        return sum

    def calculate_entropy_for_attribute(self, category, attribute, training_set):
        sum = 0

        # get all the training examples where this attribute is present for the given category
        reduced_training_set = self.training_set.get_training_set_for_single_attribute(category, attribute, training_set)
        class_type_frequency = self.get_class_type_frequency_dictionary(reduced_training_set)
        num_training_examples_for_current_attribute = self.training_set.get_number_training_examples(reduced_training_set)
        for current_type, num_current_type in class_type_frequency.iteritems():

            Pi = float(num_current_type)/float(num_training_examples_for_current_attribute)

            # for the edge case of log(0) (convenient loophole for computing entropy)
            if Pi == 0.0:
                sum = 0.0
            else:
                sum -= float(Pi)*(float(round(math.log(Pi, 2), 3)))
            sum = round(sum, 3)

        return round(sum, 3)

    def get_categories(self):
        return self.training_set.get_categories()

    def get_training_set(self):
        return self.training_set.get_training_set()

    def print_training_set(self, training_set):
        for row in training_set:
            print str(row)

    def get_class_type_frequency_dictionary(self, training_set):
        return self.training_set.get_class_type_frequency_dictionary(training_set)

    def get_class_type_frequency_dictionary_for_category(self, category, training_set):
        return self.training_set.get_class_type_frequency_dictionary_for_category(category, training_set)