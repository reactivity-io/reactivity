class ShellOrganisation extends mix(Polymer.Element).with(GlobalConst, Http) {
    static get is() {
        return 's-organisation';
    }

    constructor(props) {
        super(props);
        this.views = [];
        this.artifacts = [];
    }

    connectedCallback() {
        super.connectedCallback();

        if (this.id) {
            this.fetch('GET', `${this.wsURL}/subscribe/${this.id}`).then((data) => {
                const groups = _.groupBy(data, 'event');
                this.setProperties({views: groups['READ_VIEW'], artifacts: groups['READ_ARTIFACT']});
            });
        }
    }
}
// Register custom element definition using standard platform API
customElements.define(ShellOrganisation.is, ShellOrganisation);
