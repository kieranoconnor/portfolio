# -------------------------------------------------------------------------
'''
    Problem 3: SMO training algorithm

'''
from problem1 import *
from problem2 import *

import numpy as np

import copy

class SVMModel():
    """
    The class containing information about the SVM model, including parameters, data, and hyperparameters.

    DONT CHANGE THIS DEFINITION!
    """
    def __init__(self, train_X, train_y, C, kernel_function, sigma=1):
        """
            train_X: n x m training feature matrix. n: number of features; m: number training examples.
            train_y: 1 x m labels (-1 or 1) of training data.
            C: a positive scalar
            kernel_function: a kernel function implemented in problem1 (Python treats functions as objects).
            sigma: need to be provided when Gaussian kernel is used.
        """
        # data
        self.train_X = train_X
        self.train_y = train_y
        self.n, self.m = train_X.shape

        # hyper-parameters
        self.C = C
        self.kernel_func = kernel_function
        self.sigma = sigma

        # parameters
        self.alpha = np.zeros((1, self.m))
        self.b = 0

def train(model, max_iters = 10, record_every = 1, max_passes = 1, tol=1e-6):
    """
    SMO training of SVM
    model: an SVMModel
    max_iters: how many iterations of optimization
    record_every: record intermediate dual and primal objective values and models every record_every iterations
    max_passes: each iteration can have maximally max_passes without change any alpha, used in the SMO alpha selection.
    tol: numerical tolerance (exact equality of two floating numbers may be impossible).
    :return: 4 lists (of iteration numbers, dual objectives, primal objectives, and models)
    Hint: refer to subsection 3.5 "SMO" in notes.
    """
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################


    for t in range (max_iters):
        num_passes = 0
        while num_passes<max_passes:
            num_changes = 0
            for i in range (model.m):
            
                WTXi = 0
                for j in range(model.m):
                    if model.sigma == None:
                        WTXi += model.alpha[0,j] * model.train_y[0,j] *  Gaussian_kernel(model.train_X[:,[i]],model.train_X[:,[j]],model.sigma)
                    else:
                        WTXi += model.alpha[0,j]  * model.train_y[0,j] * linear_kernel(model.train_X[:,i].T,model.train_X[:,j].T)
                    # z[0,i] = WTXi + b
                    print(WTXi * model.train_y[0,i])

                KKT_valid = False
                if (model.alpha[0,i] == 0)and WTXi+model.b<=1:
                    KKT_valid = True
                elif (model.C >model.alpha[i] and model.alpha[i] > 0 and WTXi+model.b == model.train_y[:,i]):
                    KKT_valid = True
                elif (model.alpha[i] == model.C and WTXi+model.b == model.train_y[:,i]):
                    KKT_valid = True
                    if not KKT_valid:
                        j = i+1
                        alpha_old = model.alpha[i]
                        alpha_new = model.alpha[j]
                    # rand samp
                    # if a <tol:
                        # i+=1
                    # New value of ai given aj
                    # two new values to actual dual vairables
                    # update bias
                    # num_changes+=1
            # L = max{0, −ρ} = max{0, −(αold1 − αold2)}.
            # H = min{C, ρ}
            # if num_changes == 0:
                # num_passes+=1
            # else:
                # num_passes = 0
        
        # if t%record_every == 0:
            # record in function
            # call()
    # returns x


                



def predict(model, test_X):
    """
    Predict the labels of test_X
    model: an SVMModel
    test_X: n x m matrix, test feature vectors
    :return: 1 x m matrix, predicted labels
    """
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    n, m = test_X.shape
    print(m)
    useSig = None
    if model.kernel_func == linear_kernel:
        useSig = None
    else:
        useSig = model.sigma
    result = np.zeros(shape = (1,m))
    result = decision_function(model.alpha, model.train_y,model.train_X, model.b,model.kernel_func, useSig, test_X)
    result2 = np.zeros(shape = (1,m))
    for i in range(m):
        if result[0,i]>0:
            result2[0,i] = 1
        else:
            result2[0,i] = -1

    return result2
