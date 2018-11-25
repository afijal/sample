package repository;

import model.InvestmentStyle;

import java.util.Optional;

public interface InvestmentStyleRepository {

    void saveInvestmentStyle(InvestmentStyle investmentStyle);

    Optional<InvestmentStyle> getInvestmentStyleByName(String name);
}
