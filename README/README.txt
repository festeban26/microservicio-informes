DEPLOY to Google Cloud Run

1. mvn clean install
2. OPEN Google Cloud SDK Shell
	2.1. cd C:\dev\projects\core-microservices\microservicio-informes
	2.2. Deploy to
		Production:
			gcloud run deploy generacion-informes --source .
		Test:
			gcloud run deploy generacion-informes-prueba --source .
	NOTE: Dont forget to comment #spring.cloud.gcp.credentials.location on application properties when deploying to cloud

GENERAL
gcloud config set project core-340817
gcloud config set run/region us-east1

Run in docker
docker build . -t generacion-informes
docker run -p 8080:8080 -p 8083:8083 generacion-informes


#Reports editing
Reuired Java17. Afeter installing jaspersoftstudio, make sure to:
- Instal a jdk17 ç
-Edit the Jasper Softstudi.ini, the parameter vm to the path to the jdk17, example
-vm
C:\Program Files\Eclipse Adoptium\jdk-17.0.4.101-hotspot\bin

