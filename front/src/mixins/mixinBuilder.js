/**
 * Allow to applied a mixin (abstract subclass)
 * to a different Superclass to create a related Family of modified classes.
 * CF: http://justinfagnani.com/2015/12/21/real-mixins-with-javascript-classes/
 * @param {class} superclass
 * @return {Function} MixinBuilder
 */
let mix = (superclass) => new MixinBuilder(superclass);

class MixinBuilder {
    constructor(superclass) {
        this.superclass = superclass;
    }

    with(...mixins) {
        return mixins.reduce((c, mixin) => mixin(c), this.superclass);
    }
}
