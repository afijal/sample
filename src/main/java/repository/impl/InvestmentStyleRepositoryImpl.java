package repository.impl;

import model.InvestmentStyle;
import repository.InvestmentStyleRepository;

import java.util.List;
import java.util.Optional;

/* Just a simple mockup repo */
public class InvestmentStyleRepositoryImpl implements InvestmentStyleRepository {

    private List<InvestmentStyle> dataBase;

    public InvestmentStyleRepositoryImpl(List<InvestmentStyle> dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public void saveInvestmentStyle(InvestmentStyle investmentStyle) {
        dataBase.add(investmentStyle);
    }

    @Override
    public Optional<InvestmentStyle> getInvestmentStyleByName(String name) {
        return dataBase.stream().filter(obj -> obj.getName().equals(name)).findFirst();
    }
}
