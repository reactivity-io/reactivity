class ReactivityInjector {
    constructor() {
        this._providers = new Map();

        document.addEventListener('request-provider', (e) => {
            if (this._providers.has(e.detail.key)) {
                e.detail.provider = this._providers.get(e.detail.key)();
                e.stopPropagation();
            }
        });
    }

    provide(key, factory) {
        this._providers.set(key, factory);
    }
}

const Injector = new ReactivityInjector();