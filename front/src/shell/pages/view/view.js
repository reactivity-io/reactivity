class ShellView extends GlobalConst(Polymer.Element) {
    static get is() {
        return 's-view';
    }
    constructor (props) {
        super(props);
        this.limit = 50;
        this.artifacts = [];
        //load on init
        this.isLoading = true;
        this.maxage = -1;
    }
    connectedCallback() {
        super.connectedCallback();

        //throttle : function that only invokes func at most once per every wait milliseconds (350)
        this._scroll= _.throttle(this.scroll.bind(this), 350);
        this.$.content.addEventListener('scroll', this._scroll);

        if(this.id) {
            this.fetch(this.limit, -1, (data) => {
                this.setProperties({artifacts: data});
            })
        }
    }
    scroll(e) {
        if(this.$.content.scrollTop >= (this.$.content.scrollHeight - this.$.content.offsetHeight) - 150) {
            if (!this.isLoading && this.maxage ) {
                this.fetch(100, this.maxage, (data) => {
                    this.setProperties({artifacts: this.artifacts.concat(data)});
                })
            }
        }
    }
    fetch(limit, maxage, callback) {
        var data;
        var req = new XMLHttpRequest();
        this.isLoading = true;
        req.open('GET', `${this.wsURL}/load/artifacts/${this.id}/limit/${this.limit}/maxage/${maxage}`, true);
        req.onreadystatechange = () => {
            if (req.readyState == 4) {
                if (req.status == 200) {
                    data = JSON.parse(req.responseText);
                    // -1 to be sure to not get the last item infinitely
                    if (data.length) {
                        this.maxage = data[data.length - 1].updated - 1;
                        callback(data);
                    }
                } else {
                    alert("Erreur pendant le chargement de la page.\n");
                }
                this.isLoading = false;
            }
        };
        req.send(null);
    }
}
// Register custom element definition using standard platform API
customElements.define(ShellView.is, ShellView);