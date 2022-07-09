# -------------------------------------------------------------------------
'''
    Problem 2: reading data set from a file, and then split them into training, validation and test sets.

    The functions for handling data

    20/100 points
'''

import numpy as np # for linear algebra

def loadData():
    '''
        Read all labeled examples from the text files.
        Note that the data/X.txt has a row for a feature vector for intelligibility.

        n: number of features
        m: number of examples.

        :return: X: numpy.ndarray. Shape = [n, m]
                y: numpy.ndarray. Shape = [m, ]
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    X = np.transpose(np.loadtxt('data/X.txt'))
    # print(X)
    y = np.transpose(np.loadtxt('data/y.txt'))
    # print(y)
    
    return X,y

def appendConstant(X):
    '''
    Appending constant "1" to the beginning of each training feature vector.
    X: numpy.ndarray. Shape = [n, m]
    :return: return the training samples with the appended 1. Shape = [n+1, m]
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    n,m = X.shape
    return np.vstack((np.ones((1,m)),X))


def splitData(X, y, train_ratio = 0.8):
    '''
	X: numpy.ndarray. Shape = [n+1, m]
	y: numpy.ndarray. Shape = [m, ]
    split_ratio: the ratio of examples go into the Training, Validation, and Test sets.
    Split the whole dataset into Training, Validation, and Test sets.
    :return: return (training_X, training_y), (test_X, test_y).
            training_X is a (n+1, m_tr) matrix with m_tr training examples;
            training_y is a (m_tr, ) column vector;
            test_X is a (n+1, m_test) matrix with m_test test examples;
            training_y is a (m_test, ) column vector.
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################

    n,m = X.shape
    m_tr = int(m * train_ratio)
    m_test = m - m_tr

    training_X,test_X  = np.hsplit(X,[m_tr])
    training_y,test_y = np.split(y,[m_tr])
    # = np.hsplit(x,)
    return (training_X,training_y),(test_X,test_y)
    
