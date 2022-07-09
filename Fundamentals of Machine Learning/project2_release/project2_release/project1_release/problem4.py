# -------------------------------------------------------------------------
'''
    Problem 4: compute sigmoid(Z), the loss function, and the gradient.
    This is the vectorized version that handle multiple training examples X.

    20/100 points
'''

import numpy as np
from numpy.core.fromnumeric import shape # linear algebra
from scipy.sparse import diags
from scipy.sparse import csr_matrix

def linear(theta, X):
    '''
    theta: (n+1) x 1 column vector of model parameters
    x: (n+1) x m matrix of m training examples, each with (n+1) features.
    :return: inner product between theta and x
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################0
    # print(theta,X)
    # print(theta.size,X.size)
    # for j in X:
        # print(j.size)
        # out.append( np.dot(theta,j))
    return  np.inner(theta.T,X.T)


def sigmoid(Z):
    '''
    Z: 1 x m vector. <theta, X>
    :return: A = sigmoid(Z)
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    # sigs = []

    sigs = (1/(1 + np.e ** -Z))

    # print(sig
    return np.array(sigs)

def loss(A, Y):
    '''
    A: 1 x m, sigmoid output on m training examples
    Y: 1 x m, labels of the m training examples

    You must use the sigmoid function you defined in *this* file.

    :return: mean negative log-likelihood loss on m training examples.
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################


    return -((Y)* np.log(A) + (1-Y)*np.log(1-A)).mean()
        

def dZ(Z, Y):
    '''
    Z: 1 x m vector. <theta, X>
    Y: 1 x m, label of X

    You must use the sigmoid function you defined in *this* file.

    :return: 1 x m, the gradient of the negative log-likelihood loss on all samples wrt z.
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    # dz = []
    # for z in Z:
    #     for y in Y:
    
    # dz.append  -(y-sigmoid(z))
    return -(Y-sigmoid(Z))

def dtheta(Z, X, Y):
    '''
    Z: 1 x m vector. <theta, X>
    X: (n+1) x m, m example feature vectors.
    Y: 1 x m, label of X
    :return: d x 1, the gradient of the negative log-likelihood loss on all samples wrt w.
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    # print ((X.T * (Y-sigmoid(Z)).T).mean())
    
    # print(np.array([np.mean(X * dZ(Z,Y),axis = 1)]).T.shape)
    return np.array([np.mean(X * dZ(Z,Y),axis = 1)]).T



def Hessian(Z, X):
    '''
    Compute the Hessian matrix on m training examples.
    Z: 1 x m vector. <theta, X>
    X: (n+1) x m, m example feature vectors.
    :return: the Hessian matrix of the negative log-likelihood loss wrt theta
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    sig = sigmoid(Z)*(1-sigmoid(Z))
    # print(sig.tolist()[0])
    # print (diags(sig.tolist()[0], 0).toarray())
    # return - X * diags(sig.tolist()[0], 0).toarray() * X
    return 2 * X * (sigmoid(Z)*(1-sigmoid(Z)))* X.T

