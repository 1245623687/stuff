import copy
import sys


class ParseTools:
    # this holds the initial training set gathered from file.
    # subsets of this training set will be passed down the decision tree
    # because there is some sense of change of state as we traverse the tree,
    # but this variable will hold the entirety of the training data
    training_set = []

    # this holds the set of all categories and their attributes and the counts for each
    # attribute of each class type for the initial training set gathered from file
    # you can think of this dictionary as the initial snapshot of the categories for the training data
    # as we travel down the tree, categories will be removed from the training set local to a particular node
    # so it is useful to have the initial entire category set stored here
    categories = {}

    def __init__(self):
        pass

    @staticmethod
    def get_training_set(file_path=None, num_examples=None):

        # if filename not specified during call, fetch file name from the command line
        if file_path is None:
            file_path = sys.argv[1]

        return ParseTools.get_tokenized_data(file_path, num_examples)

    @staticmethod
    def get_testing_set(file_path=None, num_examples=None):

        # if filename not specified during call, fetch file name from the command line
        if file_path is None:
            file_path = sys.argv[2]

        return ParseTools.get_tokenized_data(file_path, num_examples)

    @staticmethod
    def get_tokenized_data(file_path, num_examples=None):

        tokenized_data_buffer = []
        example_set_file = open(file_path)

        if num_examples is None:
            for line in example_set_file:

                # skip blank line
                if line == "\n":
                    continue

                tokens = line.split()
                tokenized_data_buffer.append(tokens)
        else:
            for i in range(0, num_examples):


                line = example_set_file.readline()

                # skip blank line
                if line == "\n" or line == "" or line is None:
                    continue

                tokens = line.split()
                tokenized_data_buffer.append(tokens)

        example_set_file.close()

        return tokenized_data_buffer

    @staticmethod
    def get_category(category, training_set):
        categories = ParseTools.get_categories(training_set)
        return categories[category]

    @staticmethod
    def get_categories(training_data):
        return ParseTools.create_categories_from_training_set(training_data)

    @staticmethod
    def create_categories_from_training_set(training_set):

        categories = {}

        # last column before the class column
        last_category_column = ParseTools.get_index_of_class_column(training_set)

        for i in range(1, last_category_column):
            current_category = ParseTools.create_category_tuple_from_column(i, training_set)
            name = current_category[0]
            attribute_dictionary = current_category[1]
            categories[name] = attribute_dictionary

        return categories

    # returns a tuple such that the first item is the category name for the given
    # column in the training set, and the second item is a dictionary of the attributes
    # for that category and the frequency that attribute is classified as each class type
    @staticmethod
    def create_category_tuple_from_column(index, training_set):

        # the name of the category
        category_name = ParseTools.get_category_name_from_column(index, training_set)

        # for every every unique attribute in this column, create a dictionary with a dictionary inside of
        # if containing all the unique class types and their counts set to zero
        # what this data structure is essentially saying is that this category has these attributes, and
        # they have not been classified as any type any number of times
        # so their count is of course initialized to zero
        attribute_dictionary = ParseTools.create_attribute_dictionary_from_column(index, training_set)

        return category_name, attribute_dictionary

    @staticmethod
    def create_attribute_dictionary_from_column(index, training_set):
        attribute_dictionary = {}
        for unique_attribute_name in ParseTools.get_unique_attribute_names_from_column(index, training_set):
            attribute_dictionary[unique_attribute_name] = {'n': 0, 'y': 0}
        return attribute_dictionary

    @staticmethod
    def get_category_name_from_column(index, training_set):
        column = ParseTools.get_column(index, training_set)
        return copy.deepcopy(column[0])

    # this is a classification problem, so this function will mine from the training data set given the set of types
    # any training instance can be classified as
    @staticmethod
    def get_set_of_unique_class_types(training_set):
        last_column_index = ParseTools.get_index_of_class_column(training_set)
        return sorted(ParseTools.get_unique_attribute_names_from_column(last_column_index, training_set))

    @staticmethod
    def get_number_of_training_examples_of_class_type(current_class_type, training_set):
        num_of_specified_type = 0
        class_values = ParseTools.get_class_values_for_training_set(training_set)
        for value in class_values:
            if value == current_class_type:
                num_of_specified_type += 1
        return num_of_specified_type

    # simply returns the count of training examples classified as each class type
    @staticmethod
    def get_class_type_frequency_dictionary(reduced_training_set, training_set=None):

        proportion_to_class_type_for_training_set = {}

        # if we have two args
        if training_set is not None:
            class_types = ParseTools.get_set_of_unique_class_types(training_set)
            for current_class_type in class_types:
                num_of_type = ParseTools.get_number_of_training_examples_of_class_type(current_class_type, reduced_training_set)
                proportion_to_class_type_for_training_set[current_class_type] = num_of_type

        # if we have one arg
        else:
            class_types = ParseTools.get_set_of_unique_class_types(reduced_training_set)
            for current_class_type in class_types:
                num_of_type = ParseTools.get_number_of_training_examples_of_class_type(current_class_type, reduced_training_set)
                proportion_to_class_type_for_training_set[current_class_type] = num_of_type

        return proportion_to_class_type_for_training_set

    @staticmethod
    def get_class_type_frequency_dictionary_for_all_categories(training_set):
        categories = ParseTools.get_category_names(training_set)

        dictionary = {}
        for category in categories:
            dictionary[category] = ParseTools.get_class_type_frequency_dictionary_for_category(category, training_set)

        return dictionary

    # returns a dictionary that contains all the attributes for the given category and
    # the count that each attribute has been classified as each class type
    @staticmethod
    def get_class_type_frequency_dictionary_for_category(category, training_set):

        # if the category doesn't exist in the current training set throw an error
        assert ParseTools.has_category(category,
                                       training_set) == True, 'error: training set does not have category %s' % category

        # get the list of unique attributes for the given category
        attributes = ParseTools.get_unique_attributes_for_category(category, training_set)

        class_type_frequency_dictionary = {}

        for attribute in attributes:
            class_type_frequency_attribute_tuple = ParseTools.get_class_type_frequency_for_attribute_tuple(category,
                                                                                                           attribute,
                                                                                                           training_set)
            attribute_name = class_type_frequency_attribute_tuple[0]
            attribute_frequency_dictionary = class_type_frequency_attribute_tuple[1]
            class_type_frequency_dictionary[attribute_name] = attribute_frequency_dictionary

        return class_type_frequency_dictionary

    # this function is going to return a tuple with the attribute name as the first item and a dictionary
    # containing the count of attributes classified as each class type for the given attribute.
    # this will later be used to compute the conditional entropy of a category
    @staticmethod
    def get_class_type_frequency_for_attribute_tuple(category, attribute, training_set):

        # get all the training examples where the column contains the given attribute
        single_attribute_training_set = ParseTools.get_training_set_for_single_attribute(category, attribute,
                                                                                         training_set)

        # get class type frequency dictionary on the training set we just made that only concerns our given attribute
        class_type_frequency = ParseTools.get_class_type_frequency_dictionary(single_attribute_training_set)

        return attribute, class_type_frequency

    @staticmethod
    def get_number_of_training_examples_for_attribute(category, attribute, training_set):
        return len(ParseTools.get_training_set_for_single_attribute(category, attribute, training_set)) - 1

    @staticmethod
    def get_training_set_for_single_attribute(category, attribute, training_set):
        reduced_training_set = []
        category_column = ParseTools.get_column_for_category(category, training_set)

        reduced_training_set.append(training_set[0])
        i = 0
        for item in category_column:
            if item == attribute:
                reduced_training_set.append(training_set[i])
            i += 1

        return reduced_training_set

    @staticmethod
    def get_unique_attributes_for_category(category, training_set):

        if category is None:
            return []

        index = ParseTools.get_index_of_column_for_category(category, training_set)
        return sorted(ParseTools.get_unique_attribute_names_from_column(index, training_set))

    # note: this is referring to the actual column data, not just the attribute data.
    # the data is still pretty raw when this function is called
    @staticmethod
    def get_unique_attribute_names_from_column(index, training_set):

        column = copy.deepcopy(ParseTools.get_column(index, training_set))

        # delete the category name from this data column
        if len(column) > 0:
            del column[0]

        # add all names not seen before to the list of attr names
        # note: attribute names cannot be the same as a category name
        known_attribute_names = []
        for item in column:
            if item not in known_attribute_names and item not in ParseTools.get_category_names(training_set):
                known_attribute_names.append(item)

        return known_attribute_names

    @staticmethod
    def has_category(category, training_set):
        return category in ParseTools.get_category_names(training_set)

    @staticmethod
    def get_num_categories(training_set):
        return len(ParseTools.get_category_names(training_set))

    @staticmethod
    def get_category_names(training_set):
        temp_row = copy.deepcopy(ParseTools.get_row(0, training_set))
        # del temp_row[0]
        del temp_row[-1]

        return temp_row

    @staticmethod
    def get_class_values_for_training_set(training_set):
        class_column = ParseTools.get_class_column_for_training_set(training_set)
        del class_column[0]
        return class_column

    @staticmethod
    def get_counter_name(training_set):
        temp_row = copy.deepcopy(ParseTools.get_row(0, training_set))
        return temp_row[0]

    # do not confuse class with category.  Class is what we classify a training example as
    # a category is a variable that can have an attribute value.
    # the value of the attributes for the categories of a training example determine what the
    # classification is of the training example
    @staticmethod
    def get_class_name(training_set):
        temp_row = copy.deepcopy(ParseTools.get_row(0, training_set))
        return temp_row[-1]

    @staticmethod
    def get_row(index, training_set):
        return training_set[index]

    @staticmethod
    def get_column(index, training_set):
        temp_column = []
        for row in training_set:
            temp_column.append(row[index])
        return temp_column

    @staticmethod
    def get_column_for_category(category, training_set):
        index = ParseTools.get_index_of_column_for_category(category, training_set)
        return ParseTools.get_column(index, training_set)

    @staticmethod
    def get_index_of_column_for_category(category, training_set):

        # throw exception if category is not in this training set
        assert ParseTools.has_category(category,
                                       training_set) == True, "error: category %s doesn't exist for training set %s" % (category, str(training_set))

        # + 1 is added here because the first column is reserved for training example id, thus
        # incrementing the column of every category
        return ParseTools.get_category_names(training_set).index(category)

    @staticmethod
    def get_class_column_for_training_set(training_set):
        return copy.deepcopy(ParseTools.get_column(ParseTools.get_index_of_class_column(training_set), training_set))

    @staticmethod
    def get_index_of_class_column(training_set):
        if len(training_set) == 0:
            return 0
        else:
            return len(training_set[0]) - 1

    @staticmethod
    def get_number_training_examples(training_set):
        return len(training_set) - 1

    @staticmethod
    def is_leaf(training_set):
        if ParseTools.is_single_class_type(training_set):
            return True
        else:
            if ParseTools.get_num_categories(training_set) == 0 and ParseTools.is_split_equally_between_class_types(
                    training_set):
                return True
            else:

                temp_training = copy.deepcopy(training_set)

                # get rid of the category row
                del temp_training[0]

                # get rid of the class column
                for i in range(0, len(temp_training)):
                    del temp_training[i][-1]

                # if all the rows in temp_training it means that the classes are divided equally among the attributes
                comparison_row = temp_training[0]

                if all(comparison_row == row for row in temp_training):
                    return True
                else:
                    return False

    @staticmethod
    def is_pure(training_set):
        return ParseTools.is_single_class_type(training_set)

    @staticmethod
    def is_single_class_type(training_set):

        class_types = ParseTools.get_set_of_unique_class_types(training_set)
        if len(class_types) > 1:
            return False
        else:
            return True

    @staticmethod
    def get_training_set_with_category_removed(category, training_set):
        index = ParseTools.get_index_of_column_for_category(category, training_set)

        temp_set = copy.deepcopy(training_set)
        for row in temp_set:
            del row[index]
        return copy.deepcopy(temp_set)

    @staticmethod
    def get_lowest_alpha_category(categories):
        categories_in_alphabetical_order = sorted(categories)
        return categories_in_alphabetical_order[0]

    @staticmethod
    def get_most_common_class_type(training_set):

        type_dic = {}
        class_types = ParseTools.get_set_of_unique_class_types(training_set)
        for current_class_type in class_types:
            num_of_type = ParseTools.get_number_of_training_examples_of_class_type(current_class_type, training_set)
            type_dic[current_class_type] = num_of_type

        greatest_num_items = 0

        # determine what the greatest number of items is
        for key, value in type_dic.iteritems():
            if value > greatest_num_items:
                greatest_num_items = value

        # find all keys with that many items
        # in the case of a tie we will take the lowest alpha key
        keys = []
        for key, value in type_dic.iteritems():
            if value == greatest_num_items:
                keys.append(key)

        most_common = sorted(keys)[0]

        return most_common

    @staticmethod
    def get_reduced_training_set(category, attribute, training_set):
        if attribute is None:
            reduced_training_set = training_set
        else:
            reduced_training_set = ParseTools.get_training_set_for_single_attribute(category, attribute, training_set)
        return reduced_training_set

    @staticmethod
    def is_split_equally_between_class_types(training_set):

        if len(training_set) == 0:
            return True

        # If you don't have exotic data in your dicts equality should be transitive,
        # i. e. from
        # a == b and a == c
        # follows
        # b == c
        # so that you don't have to test the latter explicitly.
        # This reduces the number of tests significantly.

        freq_dict = ParseTools.get_class_type_frequency_dictionary(training_set)

        if len(freq_dict) == 0:
            return True

        values = freq_dict.itervalues()  # get all the values

        first = next(values)  # pick an arbitrary value to compare against all the others

        # if this arbitrary value matches all the others...
        if all(first == item for item in values):
            return True
        else:
            return False

    @staticmethod
    def get_training_set_partitions_by_attribute(category, training_set):

        if category is None:
            return

        attributes = ParseTools.get_unique_attributes_for_category(category, training_set)
        partitions = {}

        for attribute in attributes:
            temp_partition = ParseTools.get_training_set_for_single_attribute(category, attribute, training_set)

            partitions[attribute] = ParseTools.get_training_set_with_category_removed(category, temp_partition)

        return partitions
