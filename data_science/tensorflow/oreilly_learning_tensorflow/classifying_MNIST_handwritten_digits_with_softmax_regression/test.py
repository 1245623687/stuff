import tensorflow as tf

from tensorflow.examples.tutorials.mnist import input_data

DATA_DIR = '/tmp/data'
NUM_STEPS = 1000
MINIBATCH_SIZE = 100

# First we copy the dataset onto our local harddrive into /tmp/data
# one_hot refers to if the data will use one_hot encoding. In this case, we will.
# Eg. In MNIST dataset, we use digits 0-9. We can encode as integer, ie. 0, 1, 2, etc. 
# or as one hot, 000000000, 010000000, 001000000
# The index of the thing we want is the 'hot' one.
# This is pretty much the case in classification scenaries where the probability of the thing being one class is 100%, and the probability of it 
# being any other class is 0%. (eg. If I am 100% sure this is a panda, there is a 0% chance that it is a duck, or a lion)
data = input_data.read_data_sets(DATA_DIR, one_hot=True)

# Inserts a placeholder for a tensor that will be always fed.
# Important: This tensor will produce an error if evaluated.
# 

# Passing None to a shape argument of a tf.placeholder tells it simply that that dimension is unspecified, and to infer that dimension
# from the tensor you are feeding it during run-time (when you run a session). Only some arguments (generally the batch_size argument)
# can be set to None since Tensorflow needs to be able to construct a working graph before run time. This is useful for when you don't
# want to specify a batch_size before run time.
x = tf.placeholder(tf.float32, [None, 784])

# A variable maintains state in the graph across calls to run(). You add a variable to the graph by constructing an instance of the class Variable.
# The Variable() constructor requires an initial value for the variable, which can be a Tensor of any type and shape. The initial value defines the 
# type and shape of the variable. After construction, the type and shape of the variable are fixed.
W = tf.Variable(tf.zeros([784, 10]))

y_true = tf.placeholder(tf.float32, [None, 10]) # the ground truth probability distrubution
y_pred = tf.matmul(x, W) # the predicted probability distribution

# Before getting to cross entropy, let's do a little refresher on entropy, a measurement of how certain an event is. It describes how unpredictable that
# probability distribution is. entropy = 1, truly random, entropy = 0, data is truly uniform and so we can always predicts its value.
#
# so now, cross entropy, is just the average message length, if there are 8 options, we can make a bit string to rep each of these states with 3 bits, 
# because 2^3 == 8
#
# for cross entropy, we do sum of pilog(qi), instead of the normal entropy equation, sumo f pilog(pi). Here, qi, is the predicted probability, and pi
# is the actual probability. 

# cross entropy, also known as log loss, measures the performance of a classification model, whose output is a probability between 0 and 1.
# log loss penalizes both types of errors, but especially those that are both confident and wrong. It is a common loss function for training classifiers.
#
# when the predicted distribution is identical to the actual distribution, the cross-entropy is simply equal to the entropy. When they are not the same, the cross entropy will be greater than the entropy by some number of bits. This extra amount that the cross entropy is greater than the entropy is called
# the relative entropy, aka, the KL Divergence.
#
# Code
#
#def CrossEntropy(yHat, y):
#    if y == 1:
#      return -log(yHat)
#    else:
#      return -log(1 - yHat)
#
# In information theory, the cross entropy between two probability distributions p and q over the same
# underlying set of events measures the average number of bits needed to identify an event drawn from the set, if a coding scheme is used that
# is optimized for an "artificial" probability distribution q, rather than the "true" distribution p. 

# Here is the function signature for tf.reduce_mean
# tf.math.reduce_mean(
#    input_tensor,
#    axis=None,
#    keepdims=None,
#    name=None,
#    reduction_indices=None,
#    keep_dims=None
#)

# For me axis translates to "where do you add the additional numbers to increase the dimension." We don't care about that here, you can see that we
# are only passing in once tensor, and no other arguments.
input_tensor_before_reduce_mean = tf.nn.softmax_cross_entropy_with_logits(logits=y_pred, labels=y_true)
print "input tensor before reduce mean: "
print input_tensor_before_reduce_mean

cross_entropy = tf.reduce_mean(input_tensor_before_reduce_mean)
print "cross entropy after reduce mean: "
print cross_entropy

