package Service.Impl;

import Service.InvestmentService;
import error.FundsOfRequiredTypeMissingException;
import javafx.util.Pair;
import model.Fund;
import model.FundType;
import model.InvestmentStyle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

public class InvestmentServiceImpl implements InvestmentService {

    @Override
    public Pair<BigDecimal, Map<Fund, BigDecimal>> distributeInvestments(InvestmentStyle investmentStyle,
                                                                         List<Fund> funds, BigDecimal bankroll) {
        Map<Fund, BigDecimal> result = new HashMap<>();
        Map<FundType, List<Fund>> fundsByType = funds.stream().collect(Collectors.groupingBy(Fund::getType));

        for (Map.Entry<FundType, BigDecimal> portion : investmentStyle.getApportionment().entrySet()) {
            List<Fund> fundList = fundsByType.get(portion.getKey());
            if (fundList == null) {
                throw new FundsOfRequiredTypeMissingException("Please select at least 1 fund of type: " + portion.getKey().name());
            }
            BigDecimal bankrollPartPerType = bankroll.multiply(portion.getValue()).setScale(0, RoundingMode.DOWN);
            BigDecimal bankrollPartPerFund = bankrollPartPerType.divide(
                    BigDecimal.valueOf(fundList.size()), 0, RoundingMode.DOWN);
            ListIterator<Fund> iterator =  fundList.listIterator(fundList.size());
            while (iterator.hasPrevious()) {
                Fund fund = iterator.previous();
                if (iterator.hasPrevious()) {
                    bankrollPartPerType = bankrollPartPerType.subtract(bankrollPartPerFund);
                    result.put(fund, bankrollPartPerFund);
                } else {
                    result.put(fund, bankrollPartPerType);
                }
            }
        }

        return new Pair(bankroll.subtract(result.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add)), result);
    }
}
