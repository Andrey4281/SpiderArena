package spring.deserve.starter.spider;

public interface Spider {
    RPSEnum fight(Spider opponent, int battleId);  // Метод для боя (камень, ножницы, бумага)

    boolean isAlive();  // Проверка, жив ли паук

    int getLives();  // Получение оставшихся жизней

    void loseLife();  // Уменьшение количества жизней на 1

    void setLives(int lives);  // Установка количества жизней

    void setOwner(String owner);
    String getOwner();
}

