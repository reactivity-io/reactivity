class ShellOrganisations extends mix(Polymer.Element).with(GlobalConst, Http) {
    static get is() {
        return 's-organisations';
    }

    constructor() {
        super();
        this.organisations = [];
    }

    connectedCallback() {
        super.connectedCallback();

        this.fetchReactivity('GET', `/load/organizations`).then((data) => {
            this.setProperties({organisations: data});
        });
    }
}
// Register custom element definition using standard platform API
customElements.define(ShellOrganisations.is, ShellOrganisations);
