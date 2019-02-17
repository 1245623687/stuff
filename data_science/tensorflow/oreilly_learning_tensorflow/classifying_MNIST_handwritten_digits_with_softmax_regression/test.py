import tensorflow as tf

from tensorflow.examples.tutorials.mnist import input_data

DATA_DIR = '/tmp/data'
NUM_STEPS = 1000
MINIBATCH_SIZE = 100

# we pretty much cannot do or execute anything without creating a session
sess = tf.Session()

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

input_tensor_before_reduce_mean = tf.nn.softmax_cross_entropy_with_logits(logits=y_pred, labels=y_true)
cross_entropy = tf.reduce_mean(input_tensor_before_reduce_mean)
print(sess.run(cross_entropy))

# gradient descent is all about trying to find a local minima, here, we are trying to find the minimal cross entropy. 
# we pass in a learning rate of 0.5 to the GradiantDescentOptimizer constructor
# we pass in the tensor "cross_entropy" as the loss argument. This makes sense, because is the thing that you always want to minimize right?
# gd_step is An Operation that updates the variables in var_list GraphKeys.TRAINABLE_VARIABLES
gd_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)

# we collect a bool correct_mask if the true value tensor matches the predicated value tensor
correct_mask = tf.equal(tf.argmax(y_pred, 1), tf.argmax(y_true, 1))
print("correct_mask: ")
print(sess.run(correct_mask))
print("accuracy: ")
accuracy = tf.reduce_mean(tf.cast(correct_mask, tf.float32))
print(sess.run(accuracy))

