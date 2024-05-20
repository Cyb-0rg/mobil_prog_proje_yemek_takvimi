import android.os.Parcel;
import android.os.Parcelable;

public class Menu implements Parcelable {
    private String day;
    private String mainDish;
    private String sideDish;
    private String soup;
    private String dessertFruit;
    private String calorieCount;

    // Constructors, getters, and setters

    // Constructor
    public Menu(String day, String mainDish, String sideDish, String soup, String dessertFruit, String calorieCount) {
        this.day = day;
        this.mainDish = mainDish;
        this.sideDish = sideDish;
        this.soup = soup;
        this.dessertFruit = dessertFruit;
        this.calorieCount = calorieCount;
    }

    // Getters and Setters
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMainDish() {
        return mainDish;
    }

    public void setMainDish(String mainDish) {
        this.mainDish = mainDish;
    }

    public String getSideDish() {
        return sideDish;
    }

    public void setSideDish(String sideDish) {
        this.sideDish = sideDish;
    }

    public String getSoup() {
        return soup;
    }

    public void setSoup(String soup) {
        this.soup = soup;
    }

    public String getDessertFruit() {
        return dessertFruit;
    }

    public void setDessertFruit(String dessertFruit) {
        this.dessertFruit = dessertFruit;
    }

    public String getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(String calorieCount) {
        this.calorieCount = calorieCount;
    }

    // Parcelable Implementation
    protected Menu(Parcel in) {
        day = in.readString();
        mainDish = in.readString();
        sideDish = in.readString();
        soup = in.readString();
        dessertFruit = in.readString();
        calorieCount = in.readString();
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeString(mainDish);
        dest.writeString(sideDish);
        dest.writeString(soup);
        dest.writeString(dessertFruit);
        dest.writeString(calorieCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
