import Service.Impl.InvestmentServiceImpl;
import Service.InvestmentService;
import error.FundsOfRequiredTypeMissingException;
import error.InvestmentApportionmentSumException;
import javafx.util.Pair;
import model.Fund;
import model.FundType;
import model.InvestmentStyle;
import org.junit.Before;
import org.junit.Test;
import repository.InvestmentStyleRepository;
import repository.impl.InvestmentStyleRepositoryImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;

public class InvestmentServiceTest {

    private InvestmentService investmentService;

    private InvestmentStyleRepository investmentStyleRepository;

    @Before
    public void setUp() {
        investmentService = new InvestmentServiceImpl();
        investmentStyleRepository = new InvestmentStyleRepositoryImpl(new ArrayList<>());
        initInvestmentStyles();
    }

    private void initInvestmentStyles() {
        investmentStyleRepository.saveInvestmentStyle(new InvestmentStyle("Safe",
                Map.of(FundType.POLISH, BigDecimal.valueOf(0.2),
                        FundType.FOREIGN, BigDecimal.valueOf(0.75),
                        FundType.MONETARY, BigDecimal.valueOf(0.05))));
        investmentStyleRepository.saveInvestmentStyle(new InvestmentStyle("Balanced",
                Map.of(FundType.POLISH, BigDecimal.valueOf(0.3),
                        FundType.FOREIGN, BigDecimal.valueOf(0.6),
                        FundType.MONETARY, BigDecimal.valueOf(0.1))));
        investmentStyleRepository.saveInvestmentStyle(new InvestmentStyle("Aggressive",
                Map.of(FundType.POLISH, BigDecimal.valueOf(0.4),
                        FundType.FOREIGN, BigDecimal.valueOf(0.2),
                        FundType.MONETARY, BigDecimal.valueOf(0.4))));
    }

    @Test(expected = InvestmentApportionmentSumException.class)
    public void shouldThrowInvestmentApportionmentSumExceptionWhenSumIsWrong() {
        investmentStyleRepository.saveInvestmentStyle(new InvestmentStyle("Safe",
                Map.of(FundType.POLISH, BigDecimal.valueOf(0.4),
                        FundType.FOREIGN, BigDecimal.valueOf(0.9),
                        FundType.MONETARY, BigDecimal.valueOf(0.4))));
    }

    @Test(expected = FundsOfRequiredTypeMissingException.class)
    public void shouldThrowFundsOfRequiredTypeMissingExceptionWhenTypeIsMissing() {
        Fund fp1 = new Fund(1, "Fundusz Polski 1", FundType.POLISH);
        Fund fp2 = new Fund(2, "Fundusz Polski 2", FundType.POLISH);
        Fund ff1 = new Fund(3, "Fundusz Zagraniczny 1", FundType.FOREIGN);
        Fund ff2 = new Fund(4, "Fundusz Zagraniczny 2", FundType.FOREIGN);
        Fund ff3 = new Fund(5, "Fundusz Zagraniczny 3", FundType.FOREIGN);
        List<Fund> funds = Arrays.asList(fp1, fp2, ff1, ff2, ff3);


        Pair<BigDecimal, Map<Fund, BigDecimal>> result = investmentService.distributeInvestments(
                investmentStyleRepository.getInvestmentStyleByName("Safe").get(), funds, BigDecimal.valueOf(10000));
    }

    @Test
    public void shouldDistributeInvestmentsCorrectlyWithoutRest() {
        Fund fp1 = new Fund(1, "Fundusz Polski 1", FundType.POLISH);
        Fund fp2 = new Fund(2, "Fundusz Polski 2", FundType.POLISH);
        Fund ff1 = new Fund(3, "Fundusz Zagraniczny 1", FundType.FOREIGN);
        Fund ff2 = new Fund(4, "Fundusz Zagraniczny 2", FundType.FOREIGN);
        Fund ff3 = new Fund(5, "Fundusz Zagraniczny 3", FundType.FOREIGN);
        Fund fm1 = new Fund(6, "Fundusz Pieniężny 1", FundType.MONETARY);
        List<Fund> funds = Arrays.asList(fp1, fp2, ff1, ff2, ff3, fm1);


        Pair<BigDecimal, Map<Fund, BigDecimal>> result = investmentService.distributeInvestments(
                investmentStyleRepository.getInvestmentStyleByName("Safe").get(), funds, BigDecimal.valueOf(10000));

        assertThat(BigDecimal.valueOf(1000.0), Matchers.comparesEqualTo(result.getValue().get(fp1)));
        assertThat(BigDecimal.valueOf(1000.0), Matchers.comparesEqualTo(result.getValue().get(fp2)));
        assertThat(BigDecimal.valueOf(2500.0), Matchers.comparesEqualTo(result.getValue().get(ff1)));
        assertThat(BigDecimal.valueOf(2500.0), Matchers.comparesEqualTo(result.getValue().get(ff2)));
        assertThat(BigDecimal.valueOf(2500.0), Matchers.comparesEqualTo(result.getValue().get(ff3)));
        assertThat(BigDecimal.valueOf(500.0), Matchers.comparesEqualTo(result.getValue().get(fm1)));
        assertThat(BigDecimal.valueOf(0), Matchers.comparesEqualTo(result.getKey()));
    }

    @Test
    public void shouldDistributeInvestmentsCorrectlyWithRest() {
        Fund fp1 = new Fund(1, "Fundusz Polski 1", FundType.POLISH);
        Fund fp2 = new Fund(2, "Fundusz Polski 2", FundType.POLISH);
        Fund fp3 = new Fund(3, "Fundusz Polski 3", FundType.POLISH);
        Fund ff2 = new Fund(4, "Fundusz Zagraniczny 1", FundType.FOREIGN);
        Fund ff3 = new Fund(5, "Fundusz Zagraniczny 2", FundType.FOREIGN);
        Fund fm1 = new Fund(6, "Fundusz Pieniężny 1", FundType.MONETARY);
        List<Fund> funds = Arrays.asList(fp1, fp2, fp3, ff2, ff3, fm1);


        Pair<BigDecimal, Map<Fund, BigDecimal>> result = investmentService.distributeInvestments(
                investmentStyleRepository.getInvestmentStyleByName("Safe").get(), funds, BigDecimal.valueOf(10001));

        assertThat(BigDecimal.valueOf(668), Matchers.comparesEqualTo(result.getValue().get(fp1)));
        assertThat(BigDecimal.valueOf(666), Matchers.comparesEqualTo(result.getValue().get(fp2)));
        assertThat(BigDecimal.valueOf(666), Matchers.comparesEqualTo(result.getValue().get(fp3)));
        assertThat(BigDecimal.valueOf(3750), Matchers.comparesEqualTo(result.getValue().get(ff2)));
        assertThat(BigDecimal.valueOf(3750), Matchers.comparesEqualTo(result.getValue().get(ff3)));
        assertThat(BigDecimal.valueOf(500), Matchers.comparesEqualTo(result.getValue().get(fm1)));
        assertThat(BigDecimal.valueOf(1.0), Matchers.comparesEqualTo(result.getKey()));
    }
}
