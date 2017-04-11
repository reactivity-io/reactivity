by.addLocator('shadow',
    function(selector, opt_parentElement) {

        /*
         * This function extracts from the given element tree the nodes matching the given selector.
         * A selector can be a tag name, an ID (prefixed by a sharp) or a class name (prefixed by a dot).
         * A tag name can be concatenated either with an ID (separated by a sharp) or with a class name (separated by a dot).
         * Examples: "div", "div#foo", "div.bar", "#foo", ".bar"
         * If the target element must have a particular ancestor in the DOM tree, they can be specified and separated with a space.
         * Example: body div span
         * The function will collect a span with a parent in a DOM tree which is a div having a body as parent
         * The method walk through the shadow DOM of the given element and to the regular childNodes if no shadow DOM is set
         *
         * @param selector the selector
         * @param element the DOM element to walk through
         */
        function shadow(selector, element) {
            var elements = selector;

            // The first time the method is called, we convert the selector into an array
            if (typeof selector === 'string') {
                elements = selector.split(" ");
            }

            // No more element to search
            if (elements.length === 0) {
                return [];
            }

            var elementDescriptor = elements[0];
            var tagName = elementDescriptor;
            var className = null;
            var id = null;
            var splitClass = elementDescriptor.split(".");

            // Extract tag name, ID and class name
            if (splitClass.length > 1) {
                tagName = splitClass[0];
                className = splitClass[1];
            } else {
                var splitId = elementDescriptor.split("#");

                if (splitId.length > 1) {
                    tagName = splitId[0];
                    id = splitId[1];
                }
            }

            var match = (tagName || id || className)
                && (!tagName || (element.tagName && (tagName.toUpperCase() === element.tagName.toUpperCase())))
                && (!id || (element.id && (id.toUpperCase() === element.id.toUpperCase())))
                && (!className || (typeof element.className === 'string' && (className.toUpperCase().split(" ").indexOf(element.className.toUpperCase())) !== -1));

            var hasOneElement = elements.length === 1;

            // No more element to match
            if (hasOneElement && match) {
                return [element];
            }

            var childNodes = (element.shadowRoot ? element.shadowRoot : element).childNodes;
            var retval = [];
            var slice = match ? elements.slice(1, elements.length) : elements;

            childNodes.forEach(function (child) {
                retval = retval.concat(shadow(slice, child));
            });

            return retval;
        }

        return shadow(selector, opt_parentElement || document);
    }
);

// spec.js
describe('reactivity app', function() {
    it('should have a title', function () {
        browser.ignoreSynchronization = true;
        browser.get('http://localhost:3000/');
        expect(browser.getTitle()).toEqual('Reactivity');
    });

    it('should have an organization', function () {
        browser.ignoreSynchronization = true;
        browser.get('http://localhost:3000/');
        browser.sleep(1000);

        expect(element.all(by.shadow("s-reactivity s-organisations #content app-orga")).count()).toEqual(1);
    });
});