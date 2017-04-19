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

    requestProvider(key) {
        const event = new CustomEvent('request-provider',
            {
                detail: {key},
                bubble: true,
                cancelable: true
            }
        );

        document.dispatchEvent(event);
        return event.detail.provider;
    }
}

const Injector = new ReactivityInjector();