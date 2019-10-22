public class Card {
    private String suite;
    private int value;



    Card(String theSuite, int theValue) {
        suite = theSuite;
        value = theValue;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
