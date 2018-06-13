public enum Symbol {
    XS('X'), OS('O'), BLANK('.'), FINISH('D');

    private char getsymbol;

    Symbol(char getsymbol) {
        this.getsymbol = getsymbol;
    }

    public char getsymbol() {
        return getsymbol;
    }
}