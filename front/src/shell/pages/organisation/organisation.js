class ShellOrganisation extends GlobalConst(Polymer.Element) {
    static get is() {
        return 's-organisation';
    }
    constructor (props) {
        super(props);
        this.views = [];
        this.artifacts = [];
    }
    connectedCallback() {
        super.connectedCallback();

        if(this.id) {
            var req = new XMLHttpRequest();
            req.open('GET', `${this.wsURL}/subscribe/${this.id}`, true);
            req.onreadystatechange = () => {
                if (req.readyState == 4) {
                    if (req.status == 200) {
                        this.data = JSON.parse(req.responseText);
                        var groups = _.groupBy(this.data, "event");
                        this.setProperties({views : groups['READ_VIEW'], artifacts: groups['READ_ARTIFACT']});

                    } else {
                        alert("Erreur pendant le chargement de la page.\n");
                    }
                }
            };
            req.send(null);
        }
    }
}
// Register custom element definition using standard platform API
customElements.define(ShellOrganisation.is, ShellOrganisation);