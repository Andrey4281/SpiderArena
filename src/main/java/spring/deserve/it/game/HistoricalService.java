package spring.deserve.it.game;

import java.util.List;

public interface HistoricalService {
    void saveHistory(int battleId, HistoricalServiceImpl.Move move);
    HistoricalServiceImpl.SpiderStatistics getSpiderStatistics(int spiderId);
    List<HistoricalServiceImpl.Move> getBattleHistory(int battleId);
    String getBattleHistory();
}
