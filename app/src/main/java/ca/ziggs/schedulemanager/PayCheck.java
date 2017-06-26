package ca.ziggs.schedulemanager;

/**
 * Created by Robby Sharma on 6/26/2017.
 */

public class PayCheck {
    int id;
    String paycheckPeriod;
    String name;
    String salary;
    String salaryPayDate;
    String salaryType;

    public PayCheck(){

    }

    public PayCheck(int id,String paycheckPeriod,String name,String salaryPayDate, String salary,String salaryType){
        this.id = id;
        this.paycheckPeriod = paycheckPeriod;
        this.name = name;
        this.salaryPayDate = salaryPayDate;
        this.salary = salary;
        this.salaryType = salaryType;
    }

    public String getSalaryPayDate() {
        return salaryPayDate;
    }

    public void setSalaryPayDate(String salaryPayDate) {
        this.salaryPayDate = salaryPayDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaycheckPeriod() {
        return paycheckPeriod;
    }

    public void setPaycheckPeriod(String paycheckPeriod) {
        this.paycheckPeriod = paycheckPeriod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }
}
