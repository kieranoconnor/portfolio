# -------------------------------------------------------------------------
'''
    Problem 2: Compute the objective function and decision function of dual SVM.

'''
from problem1 import *

import numpy as np

# -------------------------------------------------------------------------
def dual_objective_function(alpha, train_y, train_X, kernel_function, sigma):
    """
    Compute the dual objective function value.

    alpha: 1 x m learned Lagrangian multipliers (the dual variables).
    train_y: 1 x m labels (-1 or 1) of training data.
    train_X: n x m training feature matrix. n: number of features; m: number training examples.
    kernel_function: a kernel function implemented in problem1 (Python treats functions as objects).
    sigma: need to be provided when Gaussian kernel is used.
    :return: a scalar representing the dual objective function value at alpha
    Hint: refer to subsection 3.2 "Dual problem for SVM" in notes, specifically, Eqn. 55.
          You can try to call kernel_function.__name__ to figure out which kernel are used.
    """
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    m = len(alpha[0])
    print(m)
    result = 0
    result2 = 0
    alpha_sum = np.sum(alpha)
    for i in range(m):
        for j in range(m):
            if sigma != None:
                result += alpha[:,i] * alpha [:,j] * train_y[:,i] * train_y[:,j] * Gaussian_kernel(train_X[:,[i]],train_X[:,[j]],sigma)
            else:
                result += alpha[:,i] * alpha [:,j] * train_y[:,i] * train_y[:,j] * linear_kernel(train_X[:,i].T,train_X[:,j].T)
            print(result)
    result2  = alpha_sum - .5 * result
    print(result2)
    return result2
    








# -------------------------------------------------------------------------
def primal_objective_function(alpha, train_y, train_X, b, C, kernel_function, sigma):
    """
    Compute the primal objective function value.
    When with linear kernel:
        The primal parameter w is recovered from the dual variable alpha.
    When with Gaussian kernel:
        Can't recover the primal parameter and kernel trick needs to be used to compute the primal objective function.

    alpha: 1 x m learned Lagrangian multipliers (the dual variables).
    train_y: 1 x m labels (-1 or 1) of training data.
    train_X: n x m training feature matrix.
    b: bias term
    C: regularization parameter of soft-SVM
    kernel_function: a kernel function implemented in problem1 (Python treats functions as objects).
    sigma: need to be provided when Gaussian kernel is used.

    :return: a scalar representing the primal objective function value at alpha
    Hint: refer to subsection 3.4 "Non-separabale problems" in notes, specifically, Eqn. 62-64.
    """
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    m = len(alpha[0])
    WTXi = 0
    Wnorm_sq =0
    z = np.zeros(train_y.shape)
    for i in range(m):
            for j in range(m):
                if sigma != None:
                    Wnorm_sq += alpha[0,i] * alpha [0,j] * train_y[0,i] * train_y[0,j] * Gaussian_kernel(train_X[:,[i]],train_X[:,[j]],sigma)
                else:
                    Wnorm_sq += alpha[0,i] * alpha [0,j] * train_y[0,i] * train_y[0,j] * linear_kernel(train_X[:,i].T,train_X[:,j].T)
    print(Wnorm_sq)
    Wnorm_sq = Wnorm_sq/2
    for i in range(m):
        WTXi = 0
        for j in range(m):
            if sigma != None:
                WTXi += alpha[0,j] * train_y[0,j] *  Gaussian_kernel(train_X[:,[i]],train_X[:,[j]],sigma)
            else:
                WTXi += alpha[0,j]  * train_y[0,j] * linear_kernel(train_X[:,i].T,train_X[:,j].T)
        z[0,i] = WTXi + b
    
    eps = np.sum(hinge_loss(z,train_y))
    result = Wnorm_sq + C * eps
    print (result)
    return result
#||w||^2 s second half of eq 61, 1/2 on


def decision_function(alpha, train_y, train_X, b, kernel_function, sigma, test_X):
    """
    Compute the linear function <w, x> + b on examples in test_X, using the current SVM.

    alpha: 1 x m learned Lagrangian multipliers (the dual variables).
    train_y: 1 x m labels (-1 or 1) of training data.
    train_X: n x m training feature matrix.
    test_X: n x m2 test feature matrix.
    b: scalar, the bias term in SVM <w, x> + b.
    kernel_function: a kernel function implemented in problem1 (Python treats functions as objects).
    sigma: need to be provided when Gaussian kernel is used.

    :return: 1 x m2 vector <w, x> + b
    """
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    result = np.zeros(train_y.shape)   
    m = len(alpha[0])
    for i in range(m):
        hval = 0
        for j in range(m):
            if sigma != None:
                hval += alpha[0,j] * train_y[0,j] *  Gaussian_kernel(test_X[:,[i]],train_X[:,[j]],sigma) 
            else:
                hval += alpha[0,j]  * train_y[0,j] * linear_kernel(test_X[:,i].T,train_X[:,j].T) 
        result[0,i] = hval
        print(result)
    return result + b
