let Http = (superclass) => class extends superclass {
    fetch(method, url) {
        const deferred = Q.defer();
        const req = new XMLHttpRequest();

        req.open(method, url, true);
        req.onreadystatechange = () => {
            if (req.readyState == 4) {
                if (req.status == 200) {
                    try {
                        const retval = JSON.parse(req.responseText);
                        deferred.resolve(retval);
                    } catch (e) {
                        deferred.reject(e.message);
                        this.dispatchCustomError(e.message);
                    }
                } else {
                    deferred.reject(req.responseText);
                    this.dispatchRequestError(req);
                }
            }
        };
        req.send(null);

        return deferred.promise;
    }

    dispatchRequestError(req) {
        document.dispatchEvent(new CustomEvent('error', {
            detail: {
                status: req.status,
                statusText: req.statusText,
                message: req.responseText
            }
        }));
    }

    dispatchCustomError(message) {
        document.dispatchEvent(new CustomEvent('error', {
            detail: {
                message: message
            }
        }));
    }
};