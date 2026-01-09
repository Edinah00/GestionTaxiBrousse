#!/bin/bash
set -e

# ============================
# CONFIGURATION
# ============================
APP_NAME=taxi-brousse
TOMCAT_HOME=/home/edinah/Documents/logiciel/tomcat
WAR_FILE=target/${APP_NAME}.war

# ============================
# BUILD MAVEN
# ============================
echo "📦 Build du projet..."
mvn clean package

# ============================
# ARRET TOMCAT
# ============================
echo "🛑 Arrêt de Tomcat..."
$TOMCAT_HOME/bin/shutdown.sh || true
sleep 3

# ============================
# NETTOYAGE ANCIEN DEPLOIEMENT
# ============================
echo "🧹 Nettoyage ancien déploiement..."
rm -rf $TOMCAT_HOME/webapps/${APP_NAME}
rm -f  $TOMCAT_HOME/webapps/${APP_NAME}.war

# ============================
# DEPLOIEMENT
# ============================
echo "🚀 Déploiement du WAR..."
cp $WAR_FILE $TOMCAT_HOME/webapps/

# ============================
# DEMARRAGE TOMCAT
# ============================
echo "▶️ Démarrage de Tomcat..."
$TOMCAT_HOME/bin/startup.sh

echo "✅ Déploiement terminé avec succès"
