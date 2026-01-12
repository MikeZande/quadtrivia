## Trivia Webapplicatie
Een trivia webapplicatie met twee API endpoints /questions /checkanswers. Vragen en antwoorden komen van "https://opentdb.com/". Backend gemaakt met Java Spring Boot, en frontend met React.

## Bouwen en Uitvoeren
Vereisten: Node.js, Java. 

### 1. Open terminals <br>
Open 2 terminals zoals bijvoorbeeld command prompt. <br>

### 2. Clone de repo (Terminal 1) <br>
```git clone https://github.com/MikeZande/quadtrivia```

### 3. Frontend (Terminal 1) <br>
Navigeer naar de frontend directory <br>
```cd quadtrivia\frontend``` <br>
Installeer npm packages <br>
```npm install```<br>
Bouw frontend <br>
```npm run build```<br>
Frontend uitvoeren <br>
```npm start```<br>

### 4. Backend (Terminal 2) <br>
Navigeer naar de backend directory<br>
```cd quadtrivia\backend```<br>
Backend uitvoeren <br>
```./mvnw spring-boot:run```<br>

### 5. Uitvoeren
Open http://localhost:3000/ in een browser.

## Author
Mike van der Zande
