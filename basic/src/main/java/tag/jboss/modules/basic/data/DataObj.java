package tag.jboss.modules.basic.data;

public class DataObj {
    private String name;
    private String valueString;
    private int valueInt;
    private boolean valueBoolean;
    private double valueDouble;
    private float valueFloat;
    private long valueLong;
    private short valueShort;

    public Number asIntNumber() {
        return valueInt;
    }
}
