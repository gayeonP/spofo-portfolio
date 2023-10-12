package spofo.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.service.port.PortfolioRepository;

public class FakePortfolioRepository implements PortfolioRepository {

    private long autoIncrement = 0;
    List<Portfolio> data = new ArrayList<>();

    @Override
    public List<Portfolio> findByMemberId(Long id) {
        return data.stream()
                .filter(item -> item.getMemberId().equals(id))
                .toList();
    }

    @Override
    public Optional<Portfolio> findById(Long id) {
        return data.stream()
                .filter(item -> item.getId().equals(id))
                .findAny();
    }

    @Override
    public Optional<Portfolio> findByIdWithTradeLogs(Long id) {
        return data.stream()
                .filter(item -> item.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Portfolio> findByMemberIdWithTradeLogs(Long id) {
        return data.stream()
                .filter(item -> item.getMemberId().equals(id))
                .toList();
    }

    @Override
    public Portfolio getReferenceById(Long id) {
        return null;
    }

    @Override
    public Portfolio save(Portfolio portfolio) {
        if (portfolio.getId() == null || portfolio.getId() == 0) {
            Portfolio newPortfolio = Portfolio.builder()
                    .id(++autoIncrement)
                    .memberId(portfolio.getMemberId())
                    .name(portfolio.getName())
                    .description(portfolio.getDescription())
                    .currency(portfolio.getCurrency())
                    .includeType(portfolio.getIncludeType())
                    .stockHaves(portfolio.getStockHaves())
                    .type(portfolio.getType())
                    .build();
            data.add(newPortfolio);
            return newPortfolio;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), portfolio.getId()));
            data.add(portfolio);
            return portfolio;
        }
    }

    @Override
    public void delete(Portfolio portfolio) {
        data.removeIf(item -> Objects.equals(item.getId(), portfolio.getId()));
    }
}
