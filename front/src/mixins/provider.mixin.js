let provider = (superclass) => class extends superclass {
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
};