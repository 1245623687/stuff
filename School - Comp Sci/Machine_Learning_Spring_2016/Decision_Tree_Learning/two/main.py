
__author__ = 'ashley tharp'

import copy
from Problem import Problem

def main():

    problem = Problem()

    run_tests(problem)

def run_tests(problem):
    run_training_data_tests(problem)
    run_problem_tests(problem)
    run_curiosity_tests()

def run_problem_tests(problem):
    pass

def run_training_data_tests(problem):
    test_get_tokenized_data(problem)
    test_get_row(problem)
    test_get_column(problem)
    test_get_category_names(problem)
    test_get_unique_attribute_names_from_column(problem)
    test_get_index_of_last_data_column(problem)
    test_get_set_of_unique_class_types(problem)

def run_curiosity_tests():
    run_test_copy_triple_nested_dictionaries()


# TRAINING DATA TESTS
def test_get_tokenized_data(problem):
    tokenized_data = problem.get_tokenized_data()
    assert len(tokenized_data[0]) == 6, "improper row length"
    assert len(tokenized_data) == 15, "improper column length"

def test_get_row(problem):
    test_row = ['Day', 'Outlook', 'Temp', 'Humidity', 'Wind', 'PlayTennis']
    assert problem.training_data.get_row(0) == test_row, 'get_row is broken'

def test_get_column(problem):
    test_column_0 = ['Day', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14']
    assert problem.training_data.get_column(0) == test_column_0, 'get_column is broken on column 0'

    test_column_1 = ['Outlook', 's', 's', 'o', 'r', 'r', 'r', 'o', 's', 's', 'r', 's', 'o', 'o', 'r']
    assert problem.training_data.get_column(1) == test_column_1, 'get_column is broken on column 1'

    test_column_2 = ['Temp', 'h', 'h', 'h', 'm', 'c', 'c', 'c', 'm', 'c', 'm', 'm', 'm', 'h', 'm']
    assert problem.training_data.get_column(2) == test_column_2, 'get_column is broken on column 2'

    test_column_3 = ['Humidity', 'h', 'h', 'h', 'h', 'n', 'n', 'n', 'h', 'n', 'n', 'n', 'h', 'n', 'h']
    assert problem.training_data.get_column(3) == test_column_3, 'get_column is broken on column 3'

    test_column_4 = ['Wind', 'w', 's', 'w', 'w', 'w', 's', 's', 'w', 'w', 'w', 's', 's', 'w', 's']
    assert problem.training_data.get_column(4) == test_column_4, 'get_column is broken on column 4'

def test_get_category_names(problem):
    test_names = ['Outlook', 'Temp', 'Humidity', 'Wind']
    assert problem.training_data.get_category_names() == test_names, 'get_category_names_is_broken'

def test_get_unique_attribute_names_from_column(problem):
    test_unique_names_for_outlook = ['s', 'o', 'r']
    test_unique_names_for_temp = ['h', 'm', 'c']
    test_unique_names_for_humidity = ['h', 'n']
    test_unique_names_for_wind = ['w', 's']

    output_for_column_1 = problem.training_data.get_unique_attribute_names_from_column(1)
    output_for_column_2 = problem.training_data.get_unique_attribute_names_from_column(2)
    output_for_column_3 = problem.training_data.get_unique_attribute_names_from_column(3)
    output_for_column_4 = problem.training_data.get_unique_attribute_names_from_column(4)

    assert output_for_column_1 == test_unique_names_for_outlook, 'get_unique attrbute names broke for outlook'
    assert output_for_column_2 == test_unique_names_for_temp, 'get_unique attrbute names broke for temp'
    assert output_for_column_3 == test_unique_names_for_humidity, 'get_unique attrbute names broke for humidity'
    assert output_for_column_4 == test_unique_names_for_wind, 'get_unique attrbute names broke for wind'

def test_get_index_of_last_data_column(problem):
    assert problem.training_data.get_index_of_last_data_column() == 5, 'get_index_last_data_column() is broken'

def test_get_set_of_unique_class_types(problem):

    # what the set should be
    test_set_of_unique_class_types = ['n', 'y']

    # what the function actually returned
    actual_set_of_unique_class_types = problem.training_data.get_set_of_unique_class_types()

    # these sets should be the same and if they are not we have a problem
    assert test_set_of_unique_class_types == actual_set_of_unique_class_types, 'get_set_of_unique_training_types() is broken'

# CURIOSITY TESTS
def run_test_copy_triple_nested_dictionaries():

    test_attributes_outlook = {'s' : { 'n' : 3, 'y' : 4}, 'o' : {'n' : 0, 'y':  2}, 'r' : {'n' : 2, 'y' : 3}}
    test_attributes_temp = {'h' : { 'n' : 2, 'y' : 2}, 'm' : {'n' : 2, 'y':  4}, 'c' : {'n' : 1, 'y' : 3}}
    test_attributes_humidity = {'h' : { 'n' : 4, 'y' : 3}, 'n' : {'n' : 1, 'y':  6}}
    test_attributes_wind = {'w' : { 'n' : 2, 'y' : 6}, 's' : {'n' : 3, 'y':  3}}

    test_categories = {'Outlook' : test_attributes_outlook, 'Temp' : test_attributes_temp, 'Humidity' : test_attributes_humidity, 'Wind' : test_attributes_wind}

    #so, now we are going to do a deep copy, and then alter the second one.  If they are not equal, then we have correctly done a deep copy and passed the test
    copy_test_categories = copy.deepcopy(test_categories)

    # renames the key "Outlook" to be "new_key"
    copy_test_categories['new_key'] =copy_test_categories.pop('Outlook')

    # these two separete dictionaries should not be the same, if they are it means they share the same memory space and we have a problem because we have not truly made a deep copy
    assert copy_test_categories != test_categories, 'there was a problem making a deep copy of the trip nested dictionary'

if __name__ == '__main__':
    main()