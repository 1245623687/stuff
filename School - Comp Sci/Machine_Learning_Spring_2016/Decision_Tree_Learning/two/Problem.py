
from __future__ import division

import copy
from decimal import *
import math
from Training_Data import Training_Data
from Decision_Tree import Decision_Tree
from Node import Node

class Problem:

    # all the training examples and functions for accessing them are contained in this object
    training_set = Training_Data()

    # the decision tree and all the functions for manipulating it are contained in this object
    decision_tree = Decision_Tree()

    def __init__(self):
        pass

    def create_decision_tree(self, training_set):

        assert len(training_set) != 0, 'error: empty training set'

        if self.training_set.is_homogeneous(training_set):
            return Node()

        category = self.get_best_category_for_root(training_set)
        new_node = Node(category, None, training_set)

        if self.decision_tree.has_root() == False:
            self.decision_tree.set_root(new_node)

        partitions = self.get_training_set_partitions_by_category(category, training_set)
        attributes = self.training_set.get_unique_attributes_for_category(category, training_set)
        for attribute in attributes:
            self.create_decision_tree(partitions[attribute])

        self.decision_tree.print_me()
        return copy.deepcopy(self.decision_tree)

    def get_training_set_partitions_by_category(self, category, training_set):

        attributes = self.training_set.get_unique_attributes_for_category(category, training_set)
        partitions = {}

        for example in training_set:
            for attribute in attributes:
                partitions[attribute] = self.training_set.get_training_set_for_single_attribute(category, attribute, training_set)
        print str(partitions)
        return partitions

    # possibly useless
    def get_training_set_partitions_by_class_type(self, training_set):
        #partition the training examples by class type
        training_set_partitions = {}
        current_training_subset = []
        child_nodes = []
        for type in self.training_set.get_set_of_unique_class_types():
            for example in training_set:
                if example[-1] == type:
                    current_training_subset.append(example)
            training_set_partitions[type] = current_training_subset
        return training_set_partitions

    #calculate the information gain for all the categories and store which one
    #has the highest information gain
    def get_best_category_for_root(self, training_set):
        current_best_information_gain = 0
        current_best_category  = None
        categories = self.training_set.get_category_names(training_set)
        for category in categories:
            current_information_gain = self.calculate_information_gain_for_category_for_undefined_root(category, self.get_training_set())
            if current_information_gain > current_best_information_gain:
                current_best_information_gain = current_information_gain
                current_best_category = category
        return current_best_category

    def get_best_category_for_node(self, parent_entropy, training_set):
        current_best_information_gain = 0
        current_best_category  = None
        categories = self.training_set.get_category_names(training_set)
        for category in categories:
            current_information_gain = self.calculate_information_gain_for_category(category, parent_entropy, training_set)
            if current_information_gain > current_best_information_gain:
                current_best_information_gain = current_information_gain
                current_best_category = category
        return current_best_category

    # information gain is a metric to measure the amount of information gained if we split the tree at this category
    def calculate_information_gain_for_category(self, category, parent_entropy, training_set):
        training_set_for_category_entropy = self.calculate_entropy_for_category(category, training_set)
        information_gain = self.prec(parent_entropy) - self.prec(training_set_for_category_entropy)
        return self.prec(information_gain)

    # information gain is a metric to measure the amount of information gained if we split the tree at this category
    def calculate_information_gain_for_category_for_undefined_root(self, category, training_set):
        training_set_entropy = self.calculate_entropy_for_training_set(training_set)
        information_gain = self.calculate_information_gain_for_category(category, training_set_entropy, training_set)
        return self.prec(information_gain)

    def calculate_entropy_for_training_set(self, training_set):

        sum = 0
        num_training_examples = self.training_set.get_number_training_examples(training_set)
        class_types = self.training_set.get_set_of_unique_class_types(training_set)
        num_class_types = len(class_types)
        for i in range(0, num_class_types):
            num_current_type = self.training_set.get_number_of_training_examples_of_class_type(class_types[i], training_set)
            Pi = self.prec(num_current_type / num_training_examples)
            sum -= self.prec(Pi)*(self.prec(math.log(Pi, 2)))
        return self.prec(sum, 3)

    def calculate_entropy_for_category(self, category, training_set):

        sum = 0
        attributes = self.training_set.get_unique_attributes_for_category(category, training_set)
        for attribute in attributes:
            num_current_attribute = self.training_set.get_number_of_training_examples_for_attribute(category, attribute, training_set)
            num_training_examples = self.training_set.get_number_training_examples(training_set)
            Pi = self.prec(num_current_attribute / num_training_examples)
            Hi = self.calculate_entropy_for_attribute(category, attribute, training_set)
            sum += self.prec( Pi * Hi)
        return sum

    def calculate_entropy_for_attribute(self, category, attribute, training_set):
        sum = 0

        # get all the training examples where this attribute is present for the given category
        reduced_training_set = self.training_set.get_training_set_for_single_attribute(category, attribute, training_set)
        class_type_frequency = self.get_class_type_frequency_dictionary(reduced_training_set)
        num_training_examples_for_current_attribute = self.training_set.get_number_training_examples(reduced_training_set)
        for current_type, num_current_type in class_type_frequency.iteritems():

            Pi = self.prec(num_current_type)/self.prec(num_training_examples_for_current_attribute)

            # for the edge case of log(0) (convenient loophole for computing entropy)
            if Pi == 0.0:
                sum = 0.0
            else:
                sum -= self.prec(Pi) * self.prec(math.log(Pi, 2))
            sum = self.prec(sum, 3)

        return self.prec(sum, 3)

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

    def prec(self, number, precision = 3):
        return round(Decimal(number), precision)