def classifier_predict(classifier, content):
    return classifier.predict(content, k=-1, threshold=0.1)
