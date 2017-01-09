let GlobalConst = (superclass) => class extends superclass {
    get wsURL(){
        return "http://localhost:3000/api";
    };
};