package Service;

import javafx.util
        .Pair;
import model.Fund;
import model.InvestmentStyle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface InvestmentService {

    Pair<BigDecimal, Map<Fund, BigDecimal>> distributeInvestments(InvestmentStyle investmentStyle,
                                                               List<Fund> funds, BigDecimal bankroll);
}
