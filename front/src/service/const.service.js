"use strict";
(function () {
// This index is used to select the domains from API_DOMAINS array one by one
// This allows to fetch data from the backend in a round robin manner
    let nextApiIndex = 0;
    const deferred = Q.defer();

    class GlobalConst {
        constructor() {
            this.API_DOMAINS = Injector.requestProvider('api-domain');
            this.API_CONST = "";
        }

        get wsURL() {
            if (!this.API_CONST) {
                this.API_DOMAINS.then((URLS) => {
                    if (nextApiIndex === URLS.length) {
                        nextApiIndex = 0;
                    }
                    this.API_CONST = URLS[nextApiIndex++];
                    deferred.resolve(this.API_CONST);
                });
            }
            return deferred.promise;
        }
    }
    Injector.provide('GLOBAL_CONST', () => new GlobalConst());
})();