# Используем официальный образ PostgreSQL
FROM postgres:12

# Устанавливаем переменные окружения
ENV POSTGRES_DB=wallet_v9dv
ENV POSTGRES_USER=wallet_v9dv_user
ENV POSTGRES_PASSWORD=GAqNaY7ew86J0SOs8JdaUVtVWUv6hyoL

# Если нужно инициализировать БД при запуске, можно добавить скрипты сюда:
# COPY ./init-db.sh /docker-entrypoint-initdb.d/

# Открываем порт 5432
EXPOSE 5432
