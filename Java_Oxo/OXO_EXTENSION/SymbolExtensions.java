public enum SymbolExtensions {
    XS("X"), OS("O"), BLANK("");

    private String getsymbol;

    SymbolExtensions(String getsymbol) {
        this.getsymbol = getsymbol;
    }

    public String getsymbol() {
        return getsymbol;
    }
}