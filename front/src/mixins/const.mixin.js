
// This index is used to select the domains from API_DOMAINS array one by one
// This allows to fetch data from the backend in a round robin manner
let nextApiIndex = 0;

let GlobalConst = (superclass) => class extends mix(superclass).with(provider) {
    constructor () {
        super();
        this.API_DOMAINS = this.requestProvider('api-const');
    }
    get wsURL() {
        return this.API_DOMAINS.then((URLS) => {
            if (nextApiIndex === URLS.length) {
                nextApiIndex = 0;
            }
            return URLS[nextApiIndex++];
        });
    }
};
