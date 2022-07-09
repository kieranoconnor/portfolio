# -------------------------------------------------------------------------
'''
    Problem 5: Gradient Descent and Newton method Training of Logistic Regression
    20/100 points
'''

import problem3
import problem4
from problem2 import *
import numpy as np # linear algebra
import pickle

def batch_gradient_descent(X, Y, X_test, Y_test, num_iters = 50, lr = 0.001, log=True):
    '''
    Train Logistic Regression using Gradient Descent
    X: d x m training sample vectors
    Y: 1 x m labels
    X_test: test sample vectors
    Y_test: test labels
    num_iters: number of gradient descent iterations
    lr: learning rate
    log: True if you want to track the training process, by default True
    :return: (theta, training_log)
    training_log: contains training_loss, test_loss, and norm of theta
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    # print(X,Y)
    
    theta = np.random.rand(X.shape[0], 1)
    # print(theta)
    training_loss = []
    test_loss = []
    norm = []
    for iter in range(num_iters):
        grad = 0

        z = problem4.linear(theta,X)
        grad= problem4.dtheta(z, X,Y )
        training_loss.append(problem4.loss(problem4.sigmoid(z),Y))
        norm.append(np.linalg.norm(theta))
        # print(theta.shape)
        theta += lr * grad
        ztest = problem4.linear(theta, X_test)
        test_loss.append(problem4.loss(problem4.sigmoid(ztest),Y_test))


    training_loss,test_loss, norm = np.array(training_loss),np.array(test_loss),np.array(norm)

    # print(training_loss.size, test_loss.size, norm.size)
    training_log = np.stack((training_loss,test_loss, norm), axis = -1)
    # print(theta, training_log)
    return (theta,training_log)




def stochastic_gradient_descent(X, Y, X_test, Y_test, num_iters = 50, lr = 0.001, log=True):
    '''
    Train Logistic Regression using Gradient Descent
    X: d x m training sample vectors
    Y: 1 x m labels
    X_test: test sample vectors
    Y_test: test labels
    num_iters: number of gradient descent iterations
    lr: learning rate
    log: True if you want to track the training process, by default True
    :return: (theta, training_log)
    training_log: contains training_loss, test_loss, and norm of theta
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################'
    # print(len(X),Y)
    
    theta = np.random.rand(X.shape[0])
    training_loss = []
    test_loss = []
    norm = []
    
    for iter in range(num_iters):
        grad = 0
        loss = 0
        for i in range(len(Y)):
            z = theta.dot(X.T[i])
        loss += problem3.loss(problem3.sigmoid(z),Y[i])
        training_loss.append(loss )
        norm.append(np.linalg.norm(theta))

        stoch = np.random.randint(0,len(Y))
        grad= problem3.dtheta(theta.dot(X.T[stoch]), X.T[stoch],Y [stoch] )
        # print(grad)
        
        theta += lr * grad
        loss = 0
        for i in range(len(Y_test)):
            z = theta.dot( X_test.T[i])
        loss += problem3.loss(problem3.sigmoid(z),Y_test[i])
        test_loss.append(loss)
    
    training_loss,test_loss, norm = np.array(training_loss),np.array(test_loss),np.array(norm)

    # print(training_loss.size, test_loss.size, norm.size)

    training_log = np.stack((training_loss,test_loss, norm), axis = -1)
    # print(training_loss.size, test_loss.size, norm.size)

    # print(theta, training_log)
    return(theta,training_log)


def Newton_method(X, Y, X_test, Y_test, num_iters = 50, log=True):
    '''
    Train Logistic Regression using Gradient Descent
    X: d x m training sample vectors
    Y: 1 x m labels
    X_test: test sample vectors
    Y_test: test labels
    num_iters: number of gradient descent iterations
    log: True if you want to track the training process, by default True
    :return: (theta, training_log)
    training_log: contains training_loss, test_loss, and norm of theta
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################


# --------------------------
def train_SGD(**kwargs):
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    # use functions defined in problem3.py to perform stochastic gradient descent

    tr_X = kwargs['Training X']
    tr_y = kwargs['Training y']
    te_X = kwargs['Test X']
    te_y = kwargs['Test y']
    num_iters = kwargs['num_iters']
    lr = kwargs['lr']
    log = kwargs['log']
    return stochastic_gradient_descent(tr_X, tr_y, te_X, te_y, num_iters, lr, log)


# --------------------------
def train_GD(**kwargs):
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    # use functions defined in problem4.py to perform batch gradient descent

    tr_X = kwargs['Training X']
    tr_y = kwargs['Training y']
    te_X = kwargs['Test X']
    te_y = kwargs['Test y']
    num_iters = kwargs['num_iters']
    lr = kwargs['lr']
    log = kwargs['log']
    return batch_gradient_descent(tr_X, tr_y, te_X, te_y, num_iters, lr, log)

# --------------------------
def train_Newton(**kwargs):
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    tr_X = kwargs['Training X']
    tr_y = kwargs['Training y']
    te_X = kwargs['Test X']
    te_y = kwargs['Test y']
    num_iters = kwargs['num_iters']
    log = kwargs['log']
    return Newton_method(tr_X, tr_y, te_X, te_y, num_iters, log)


if __name__ == "__main__":
    '''
    Load and split data, and use the three training methods to train the logistic regression model.
    The training log will be recorded in three files.
    The problem5.py will be graded based on the plots in plot_training_log.ipynb (a jupyter notebook).
    You can plot the logs using the "jupyter notebook plot_training_log.ipynb" on commandline on MacOS/Linux.
    Windows should have similar functionality if you use Anaconda to manage python environments.
    '''
    X, y = loadData()
    X = appendConstant(X)
    (tr_X, tr_y), (te_X, te_y) = splitData(X, y)

    kwargs = {'Training X': tr_X,
              'Training y': tr_y,
              'Test X': te_X,
              'Test y': te_y,
              'num_iters': 1000,
              'lr': 0.01,
              'log': True}

    theta, training_log = train_SGD(**kwargs)
    with open('./data/SGD_outcome.pkl', 'wb') as f:
        pickle.dump((theta, training_log), f)


    theta, training_log = train_GD(**kwargs)
    with open('./data/batch_outcome.pkl', 'wb') as f:
        pickle.dump((theta, training_log), f)
#
#
    # theta, training_log = train_Newton(**kwargs)
    with open('./data/newton_outcome.pkl', 'wb') as f:
        pickle.dump((theta, training_log), f)


