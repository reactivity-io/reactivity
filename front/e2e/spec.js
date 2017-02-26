// spec.js
describe('reactivity app', function() {
    it('should have a title', function() {
        browser.ignoreSynchronization = true;
        browser.get('http://localhost:3000/');

        expect(browser.getTitle()).toEqual('Reactivity');
    });
});
