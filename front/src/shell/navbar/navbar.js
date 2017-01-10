class ShellNavbar extends Polymer.Element {
    static get is() {
        return 's-navbar';
    }

    handleClick(e) {
        e.preventDefault();
        document.dispatchEvent(new CustomEvent('nav', {detail: {path: e.currentTarget.pathname}}));
    }
    handlePrev(e) {
        e.preventDefault();
        window.history.back();
    }
}

// Register custom element definition using standard platform API
customElements.define(ShellNavbar.is, ShellNavbar);
