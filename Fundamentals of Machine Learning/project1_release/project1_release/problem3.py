# -------------------------------------------------------------------------
'''
    Problem 3: compute sigmoid(<theta, x>), the loss function, and the gradient.
    This is the single training example version.

    20/100 points
'''

import numpy as np # linear algebra

def linear(theta, x):
    '''
    theta: (n+1) x 1 column vector of model parameters
    x: (n+1) x 1 column vector of an example features. Must be a sparse csc_matrix
    :return: inner product between theta and x
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    return np.dot(theta,x)



def sigmoid(z):
    '''
    z: scalar. <theta, x>
    :return: sigmoid(z)
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    denom = 1 + (np.e ** -z)
    # print(denom)
    if denom == 0:
        return .00001
    return 1/denom
    

def loss(a, y):
    '''
    a: 1 x 1, sigmoid of an example x
    y: {0,1}, the label of the corresponding example x
    :return: negative log-likelihood loss on (x, y).
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    # print(a)
    return  -((y)* np.log(a) + (1-y)*np.log(1-a))


def dz(z, y):
    '''
    z: scalar. <theta, x>
    y: {0,1}, label of x
    :return: the gradient of the negative log-likelihood loss on (x, y) wrt z.
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    return  -(y-sigmoid(z))
    print(z,y)
    print(type(z),type(y))


def dtheta(z, x, y):
    '''
    z: scalar. <theta, x>
    x: (n+1) x 1 vector, an example feature vector
    y: {0,1}, label of x
    :return: the gradient of the negative log-likelihood loss on (x, y) wrt theta.
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    return -( y - sigmoid(z) ) *x

def Hessian(z, x):
    '''
    Compute the Hessian matrix on a single training example.
    z: scalar. <theta, x>
    x: (n+1) x 1 vector, an example feature vector
    :return: the Hessian matrix of the negative log-likelihood loss wrt theta
    '''
    #########################################
    ## INSERT YOUR CODE HERE
    ######################################### 
    sigma = np.zeros((x.size,x.size))
    # print(sigma)
    # for i in range(x.size):
    #     sigma[i+1][i+1] = 
    print(sigma)
    print( x * (sigmoid(z)*(1-sigmoid(z)))* x.T)
    return x * (sigmoid(z)*(1-sigmoid(z)))* x.T
