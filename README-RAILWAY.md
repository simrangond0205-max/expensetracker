# Railway deployment

This project is a Spring Boot app that can be deployed to Railway using Docker.

## Build locally

./mvnw clean package

## Railway setup

1. Push this project to GitHub.
2. In Railway, click New Project > Deploy from GitHub repo.
3. Select this repository.
4. Add these environment variables:
   - PORT=8080
   - DB_URL=jdbc:mysql://<host>:3306/<database>
   - DB_USERNAME=<db-user>
   - DB_PASSWORD=<db-password>
   - GOOGLE_CLIENT_ID=<your-google-client-id>
   - GOOGLE_CLIENT_SECRET=<your-google-client-secret>
5. Make sure your Google OAuth redirect URI matches the Railway URL, for example:
   - https://<your-railway-domain>/login/oauth2/code/google

## Notes

The app uses the environment variables above with fallback defaults for local development.
