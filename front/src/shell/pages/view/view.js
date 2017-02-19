class ShellView extends mix(Polymer.Element).with(GlobalConst, Http) {
    static get is() {
        return 's-view';
    }

    constructor(props) {
        super(props);
        this.artifactLimit = 50;
        //Limit in pixel before fetching others data
        this.threshold = 1500;
        this.artifacts = [];
        //load on init
        this.isLoading = true;
        this.maxage = -1;
    }

    connectedCallback() {
        super.connectedCallback();
        if (this.id) {
            this.$.content.addEventListener('scroll', this._handleScroll.bind(this));
            this.fetchNextData(this.artifactLimit, -1);
        }
    }

    _handleScroll() {
        if (this.$.content.scrollTop >= (this.$.content.scrollHeight - this.$.content.offsetHeight) - this.threshold) {
            if (!this.isLoading && this.maxage) {
                this.setProperties({isLoading: true});
                this.fetchNextData(this.artifactLimit, this.maxage);
            }
        }
    }

    fetchNextData(limit, maxage) {
        this.fetch('GET', `${this.wsURL}/load/artifacts/${this.id}/limit/${limit}/maxage/${maxage}`)
            .then((data) => {
                if (data.length) {
                    this.maxage = data[data.length - 1].updated - 1;
                    this.setProperties({artifacts: this.artifacts.concat(data), isLoading: false});
                } else if (data.length == 0) {
                    this.maxage = 0;
                    this.setProperties({isLoading: false});
                }
            });
    }
}
// Register custom element definition using standard platform API
customElements.define(ShellView.is, ShellView);
