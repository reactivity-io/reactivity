const API_DOMAINS = (() => {
    const req = new XMLHttpRequest();

    req.open('GET', '/domain-api.json', false);
    let retval = null;

    req.onreadystatechange = () => {
        if (req.readyState == 4 && req.status == 200) {
            retval = JSON.parse(req.responseText);
        }

        return retval;
    };

    req.send(null);
    return retval;
})();

// This index is used to select the domains from API_DOMAINS array one by one
// This allows to fetch data from the backend in a round robin manner
let nextApiIndex = 0;

let GlobalConst = (superclass) => class extends superclass {
    get wsURL() {
        if (nextApiIndex === API_DOMAINS.length) {
            nextApiIndex = 0;
        }

        return API_DOMAINS[nextApiIndex++];
    }
};
