import tensorflow as tf
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'


def print_hi(name):
    print(f'Hi, {name}')


hello = tf.constant('Hello, Tensorflow!')
print(hello.numpy())
