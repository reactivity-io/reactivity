const API_DOMAINS = (() => {
    const req = new XMLHttpRequest();
    const deferred = Q.defer()
    req.open('GET', '/domain-api.json', true);
    let retval = null;

    req.onreadystatechange = () => {
        if (req.readyState == 4 && req.status == 200) {
            retval = JSON.parse(req.responseText);
            deferred.resolve(retval);
        }
    };

    try {
        req.send(null);
    } catch (e) {
        deferred.reject(e);
        console.log('%cDomain API is unavailable ! Let\'s start OFFLINE FIRST ?!', "font-size:1.5em;color:#4558c9;", "color:#d61a7f;font-size:1em;")
    }
    return deferred.promise;
})();

// This index is used to select the domains from API_DOMAINS array one by one
// This allows to fetch data from the backend in a round robin manner
let nextApiIndex = 0;

    let GlobalConst = (superclass) => class extends superclass {

    get wsURL() {
        return API_DOMAINS.then((URLS) => {
            if (nextApiIndex === URLS.length) {
                nextApiIndex = 0;
            }
            return URLS[nextApiIndex++];
        });
    }
};
