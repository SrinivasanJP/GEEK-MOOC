package Backend;

import java.util.ArrayList;

public class BasicInfo_helper {
    String name, mobile,email,dob,country,language,educationalBackground, ratings,website, git, others,about,gender,upi;
    ArrayList<String> basket = new ArrayList<>();
    public BasicInfo_helper(){}

    public BasicInfo_helper(String name, String mobile, String email, String dob, String country, String language, String educationalBackground, String ratings, String website, String git, String others, String about, String gender, String upi, ArrayList<String> basket) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.dob = dob;
        this.country = country;
        this.language = language;
        this.educationalBackground = educationalBackground;
        this.ratings = ratings;
        this.website = website;
        this.git = git;
        this.others = others;
        this.about = about;
        this.gender = gender;
        this.upi = upi;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEducationalBackground() {
        return educationalBackground;
    }

    public void setEducationalBackground(String educationalBackground) {
        this.educationalBackground = educationalBackground;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGit() {
        return git;
    }

    public void setGit(String git) {
        this.git = git;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }
}
