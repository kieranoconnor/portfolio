'''
    Problem 1: Implement linear and Gaussian kernels and hinge loss
'''

import numpy as np
from sklearn.metrics.pairwise  import euclidean_distances, kernel_metrics
import math

def linear_kernel(X1, X2):
    
    """
    Compute linear kernel between two set of feature vectors.
    The constant 1 is not appended to the x's.

    X1: n x m1 matrix, each of the m1 column is an n-dim feature vector.
    X2: n x m2 matrix, each of the m2 column is an n-dim feature vector.
    
    Note that m1 may not equal m2

    :return: if both m1 and m2 are 1, return linear kernel on the two vectors; else return a m1 x m2 kernel matrix K,
            where K(i,j)=linear kernel evaluated on column i from X1 and column j from X2.
    """
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    # print(X1, X2)
    # if (len(X1[0]) == len(X2[0])) and len(X1[0])==1:
        # print(np.dot(X1.T,X2.T))
        # return np.dot(X1.T,X2)

    # else:
    #     # for i in X1:
    #     print(np.dot(X1.T,X2))
    return np.dot(X1.T,X2)




def Gaussian_kernel(X1, X2, sigma=1):
    """
    Compute Gaussian kernel between two set of feature vectors.
    
    The constant 1 is not appended to the x's.
    
    For your convenience, please use euclidean_distances.

    X1: n x m1 matrix, each of the m1 column is an n-dim feature vector.
    X2: n x m2 matrix, each of the m2 column is an n-dim feature vector.
    sigma: Gaussian variance (called bandwidth)

    Note that m1 may not equal m2

    :return: if both m1 and m2 are 1, return Gaussian kernel on the two vectors; else return a m1 x m2 kernel matrix K,
            where K(i,j)=Gaussian kernel evaluated on column i from X1 and column j from X2

    """
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    count = 0
    if len(X1[0]) == len(X2[0]) and len(X1[0]) == 1:

        result = np.array( math.e ** ((np.linalg.norm(euclidean_distances(X1.T,X2.T) **2)) / (-2 * sigma **2)))
        return np.array([result]).reshape(1,1)

    else:
        row,col = X1.shape[0], X2.shape[1]
        result = np.zeros(shape = (row,col))

        for i in range(row):
            for j in range(col):
                # if count == 0:
                result[i,j] = np.array( math.e ** ((np.linalg.norm(euclidean_distances(X1[:,i].reshape(1,-1),X2[:,j].reshape(1,-1)) **2)) / (-2 * sigma **2)))
                # result = np.stack(result,np.array( math.e ** ((np.linalg.norm(euclidean_distances(i.T,j.T) **2)) /- (2 * sigma **2))), axis=-1)
                # count+=1

    print(result)
    # print(np.array([result]).reshape(1,1))
    return result


def hinge_loss(z, y):
    """
    Compute the hinge loss on a set of training examples
    z: 1 x m vector, each entry is <w, x> + b (may be calculated using a kernel function)
    y: 1 x m label vector. Each entry is -1 or 1
    :return: 1 x m hinge losses over the m examples
    """
    #########################################
    ## INSERT YOUR CODE HERE
    #########################################
    # print(z,y)
    m = y.shape[1]
    result = np.zeros(shape = (1,m))
    for i in range(m):
        if y[:,i] == 1:
            if z[0,i]>1:
                result[0,i] = 0
            else:
                result[0,i] = 1 - z[0,i]
        if y[:,i] == -1:
            if z[0,i]<-1:
                result[0,i] = 0
            else:
                result[0,i] =  1 + z[0,i] 

    return result
            
            




