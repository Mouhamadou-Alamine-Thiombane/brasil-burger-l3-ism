# ============================
# BUILD STAGE
# ============================
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /app

# Copier le fichier csproj et restaurer les dépendances
COPY *.csproj ./
RUN dotnet restore

# Copier tout le projet
COPY . ./

# Publier l'application
RUN dotnet publish -c Release -o /out

# ============================
# RUNTIME STAGE
# ============================
FROM mcr.microsoft.com/dotnet/aspnet:8.0
WORKDIR /app

# Render utilise la variable d'environnement PORT
ENV ASPNETCORE_URLS=http://+:$PORT

# Copier les fichiers publiés
COPY --from=build /out .

# Lancer l'application
ENTRYPOINT ["dotnet", "BrasilBurger.dll"]
