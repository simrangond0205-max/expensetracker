package com.SpringBootMVC.ExpensesTracker.DTO;

public class FilterDTO {
    private static final int DEFAULT_MAX_AMOUNT = 999999999;
    private String category = "all";
    private Integer from = 0;
    private Integer to = DEFAULT_MAX_AMOUNT;
    private String month = "all";
    private String year = "all";

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "category='" + category + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
