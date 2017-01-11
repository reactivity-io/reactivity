suite('<s-reactivity>', function () {
    test('SHOULD BE TRUUUUe', function () {
        assert.isNotNull({toto: 'toto'});
    });
    test('have navbar', function () {
        assert.isNotNull(document.querySelector('s-reactivity').shadowRoot.querySelector('s-navbar'));
    });
});