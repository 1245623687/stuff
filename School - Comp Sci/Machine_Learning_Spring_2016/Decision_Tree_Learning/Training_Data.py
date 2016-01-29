
from Category import Category
from Training_Example import Training_Example
from Attribute import Attribute
import copy

class Training_Data:

    # the raw lines of data from the file
    data = []

    training_examples = []

    categories = []

    class_category = Category

    def __init__(self, file_data):

        #copy the file into a buffer
        for line in file_data:
            self.data.append(copy.deepcopy(line))

        # create the category object
        self.categories = self.get_categories_from_data()

        # remove the column for class from categories
        self.class_category = self.categories[-1]
        del self.categories[-1]

        #create the training examples from the file data
        self.create_training_examples()

        self.get_entropy_for_categories()

    def create_training_examples(self):
        for row in copy.deepcopy(self.data):
            row_tokens = row.split()
            example_number = row_tokens[0]
            del row_tokens[0]
            class_value = row_tokens[-1]
            del row_tokens[-1]
            attribute_value_vector = row_tokens
            self.training_examples.append(Training_Example(example_number, attribute_value_vector, class_value))
        del self.training_examples[0]

    def print_training_examples(self):
        for example in self.training_examples:
            example.print_me()

    def get_categories_from_data(self):
        categories = []
        category_names = self.get_category_names()
        category_index = 1

        while category_index < len(category_names):
            name = category_names[category_index]

            attrs = self.get_unique_attributes_for_category_number(category_index)

            new_category = Category(name, attrs)
            categories.append(new_category)
            category_index += 1
        return categories

    def get_category_names(self):
        return self.data[0].split()

    def get_column_number_for_category(self, category):
        column_number = 0
        for cat in self.categories:
            if cat.name == category:
                return column_number
            column_number += 1

    def get_column_number_for_class_value(self, value):
        column_number = 0
        for class_value in self.class_category.attributes:
            if class_value.name == value:
                return column_number
            column_number += 1

    #def get_entropy_for_category(self, category):
    #    category_number = self.get_column_number_for_category(category)
    #    return self.get_entropy_for_category_number(category_number)

    def get_entropy_for_categories(self):
        print "enter get entropy func"
        for category in self.categories:
            for example in self.training_examples:
                #get the index for the category
                attribute_value = self.get_attribute_for_category(category, example)
                print "attr value was: " + str(attribute_value)

                attribute_value_index = self.get_column_number_for_category_attribute(category, attribute_value)

                print "example class value: " + str(example.class_value)

                #increment the proper class for that category's attribute
                class_value_index = self.get_column_number_for_class_value(example.class_value)

                print "attr val ind: " + str(attribute_value_index)
                print "class val ind: " + str(class_value_index)
                print "class count vector: " + str(category.attributes[attribute_value_index].class_count_vector)
                category.attributes[attribute_value_index].class_count_vector[class_value_index] += 1

        #testing
        for cat in self.categories:
            cat.print_me()


    def get_attribute_for_category(self, category, training_example):
        print "enter get attribute for category func"
        print "\tcategaory was: " + category.name
        category_index = self.get_column_number_for_category(category.name)
        print "\tcat index was: " + str(category_index)
        return training_example.attrbute_values_vector[category_index]

    #    column = self.get_columns()[category_index]
    #    print "len(col): " + str(len(column))
    #    class_column = self.get_columns()[-1]

    #    row_index = 0
    #    class_category_index = 0
    #    attr_index = 0

    #    while row_index < len(column):
    #        while attr_index < len(self.categories[category_index].attributes):
    #            while class_category_index < len(self.class_category.attributes):
    #                if class_column[row_index] == class_column[class_category_index]:
    #                    self.categories[attr_index].class_count_vector[class_category_index] += 1
    #                class_category_index += 1
    #                print 'cci: ' + class_category_index
    #            attr_index += 1
    #        row_index += 1

    #    self.print_categories()

    def get_column_number_for_category_attribute(self, category, attribute):
        attr_index = 0
        for attr in category.attributes:
            if attribute == attr.name:
                return attr_index
            attr_index += 1

    def get_unique_attributes_for_category_number(self, index):
        known_attrs = []
        known_attr_names = []

        column_tokens = self.get_column_from_data_without_category_name(index)
        del column_tokens[0]

        class_column_index = len(self.get_line_tokens(self.data[0])) - 1
        class_column_tokens = self.get_column_from_data_without_category_name(class_column_index)
        known_classes = []
        for class_token in class_column_tokens:
            if class_token not in known_classes:
                known_classes.append(class_token)
        num_classes = len(known_classes)
        print "num classes in unique was: " + str(num_classes)

        for token in column_tokens:
            if token not in known_attr_names:
                known_attr_names.append(token)
                known_attrs.append(Attribute(token, num_classes))

        for attr in known_attrs:
            attr.print_me()

        return known_attrs

    def get_columns(self):
        columns = []
        column_index = 1

        while column_index <= len(self.categories) + 1:
            columns.append(self.get_column_from_data_without_category_name(column_index))
            column_index += 1
        return columns

    def get_number_of_training_examples(self):
        num_lines = 0
        for line in self.data:
            num_lines += 1
        return num_lines

    def get_line_tokens(self, line):
        return line.split()

    def get_column_from_data(self, column_number):
        temp_column = []
        for row in copy.deepcopy(self.data):
            row_tokens = row.split()
            temp_column.append(row_tokens[column_number])
        return temp_column

    def get_column_from_data_without_category_name(self, column_number):
        temp_column = self.get_column_from_data(column_number)
        del temp_column[0]
        return temp_column

    def print_categories(self):
        for category in self.categories:
            category.print_me()
