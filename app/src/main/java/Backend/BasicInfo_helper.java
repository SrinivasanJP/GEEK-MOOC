package Backend;

import java.util.ArrayList;

public class BasicInfo_helper {
    String name, mobile;
    ArrayList<String> basket = new ArrayList<>();

    public BasicInfo_helper(String name, String mobile, ArrayList<String> basket) {
        this.name = name;
        this.mobile = mobile;
        this.basket = basket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ArrayList<String> getBasket() {
        return basket;
    }

    public void setBasket(ArrayList<String> basket) {
        this.basket = basket;
    }
}
