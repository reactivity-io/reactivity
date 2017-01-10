class ShellView extends mix(Polymer.Element).with(GlobalConst, Http) {
    static get is() {
        return 's-view';
    }

    constructor(props) {
        super(props);
        this.limit = 50;
        this.artifacts = [];
        //load on init
        this.isLoading = true;
        this.maxage = -1;
    }

    connectedCallback() {
        super.connectedCallback();

        //throttle:function that only invokes func at most once every (350) milli sec
        this._scrollListener = _.throttle(this._handleScroll.bind(this), 350);
        this.$.content.addEventListener('scroll', this._scrollListener);

        if (this.id) {
            this.fetchNextData(this.limit, -1);
        }
    }

    _handleScroll() {
        if (this.$.content.scrollTop >= (this.$.content.scrollHeight - this.$.content.offsetHeight) - 150) {
            if (!this.isLoading && this.maxage) {
                this.setProperties({isLoading: true});
                this.fetchNextData(100, this.maxage);
            }
        }
    }

    fetchNextData(limit, maxage) {
        this.fetch('GET', `${this.wsURL}/load/artifacts/${this.id}/limit/${this.limit}/maxage/${maxage}`)
            .then((data) => {
                if (data.length) {
                    this.maxage = data[data.length - 1].updated - 1;
                    this.setProperties({artifacts: this.artifacts.concat(data), isLoading: false});
                }
            });
    }
}
// Register custom element definition using standard platform API
customElements.define(ShellView.is, ShellView);
