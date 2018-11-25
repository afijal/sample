package model;

import error.InvestmentApportionmentSumException;

import java.math.BigDecimal;
import java.util.Map;

public class InvestmentStyle {
    private String name;
    private Map<FundType, BigDecimal> apportionment;

    public InvestmentStyle(String name, Map<FundType, BigDecimal> apportionment) {
        if (apportionment.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add).compareTo(BigDecimal.valueOf(1)) != 0) {
            throw new InvestmentApportionmentSumException("Investment apportionment sum has to equal to 100%");
        }
        this.name = name;
        this.apportionment = apportionment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<FundType, BigDecimal> getApportionment() {
        return apportionment;
    }

    public void setApportionment(Map<FundType, BigDecimal> apportionment) {
        this.apportionment = apportionment;
    }
}
