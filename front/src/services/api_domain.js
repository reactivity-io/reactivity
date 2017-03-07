const deferred = Q.defer();
let retval = null;
const API_DOMAINS = () => {
    const req = new XMLHttpRequest();
    req.open('GET', '/domain-api.json', true);

    req.onreadystatechange = () => {
        if (req.readyState == 4 && req.status == 200) {
            retval = JSON.parse(req.responseText);
            deferred.resolve(retval);
        }
    };

    try {
        if (!retval) {
            req.send(null);
        }
    } catch (e) {
        deferred.reject(e);
        console.log('%cDomain API is unavailable ! Let\'s start OFFLINE FIRST ?!', "font-size:1.5em;color:#4558c9;", "color:#d61a7f;font-size:1em;")
    }
    return deferred.promise;
};

Injector.provide('api-const', () => API_DOMAINS());
