class ShellSettings extends Polymer.Element {
    static get is() {
        return 's-settings';
    }

    changeColor(e) {
        document.dispatchEvent(new CustomEvent('changeColor', {
            detail: {
                primary: e.currentTarget.getAttribute('primary'),
                secondary: e.currentTarget.getAttribute('secondary')
            }
        }));
    }
}
customElements.define(ShellSettings.is, ShellSettings);
