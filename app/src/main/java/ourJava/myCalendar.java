package ourJava;

/**
 * Created by taber on 11/9/14.
 */
class myCalendar {
    private long cal_id; //unique id of calendar
    private String cal_name; //display name of calendar
    private String cal_description; //brief description of calendar's purpose
    /* calType :int
     * calType is an integer that represents what genre/purpose this calendar fits in
     * Options:
     * * 0: personal calendar
     * * 1: educational calendar
     * * 2: work calendar
     * * 3: other calendar
     * * Default is 0 (personal calendar)
    */
    private int calType;
    private String cal_owner;

    //Create default and overrides of constructors
    public myCalendar() {
        cal_id = 0;
        cal_name = null;
        cal_description = null;
        calType = 0;
    }

    public myCalendar(long id) {
        cal_id = id;
        cal_name = null;
        cal_description = null;
        calType = 0;
    }

    public myCalendar(long id, String name) {
        cal_id = id;
        cal_name = name;
        cal_description = null;
        calType = 0;
    }

    public myCalendar(long id, String name, String descr) {
        cal_id = id;
        cal_name = name;
        cal_description = descr;
        calType = 0;
    }

    public myCalendar(long id, String name, String descr, int type) {
        cal_id = id;
        cal_name = name;
        cal_description = descr;
        calType = type;
    }

    public myCalendar(long id, String name, String descr, int type, String owner) {
        cal_id = id;
        cal_name = name;
        cal_description = descr;
        calType = type;
        cal_owner = owner;
    }

    //Retriever methods
    public long getID() {
        return cal_id;
    }

    public String getIDString() {
        return Long.toString(cal_id);
    }

    public String getName() {
        return cal_name;
    }

    public String getDescription() {
        return cal_description;
    }

    public int getType() {
        return calType;
    }

    public String getOwner() {
        return cal_owner;
    }

    //Modifier methods
    public void setID(long id) {
        cal_id = id;
    }

    public void setName(String name) {
        cal_name = name;
    }

    public void setDescription(String descr) {
        cal_description = descr;
    }

    public void setType(int type) {
        calType = type;
    }

    public void setOwner(String owner) {
        cal_owner = owner;
    }
}