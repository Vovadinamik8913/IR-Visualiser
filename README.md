# IR-Visualiser
A client-server project for a better view and navigation between ir and svg

---

## 📌 Table of Contents  
- [📝 Description](#-description)  
- [🛠️ Required Components](#️-required-components)  
- [💻 Local Deployment](#-local-deployment)  
- [🐳 Container Deployment](#-container-deployment)  

---

## 📝 Description  
Provide a clear and concise overview of your project.  
- Purpose  
- Key features  
- Technologies used  

---

## 🛠️ Required Components  
List all dependencies, tools needed to run the project:  

### Local Deployment
- [Java 21 JDK](https://jdk.java.net/21/) - Required for backend execution
- [LLVM opt](https://llvm.org/) - The LLVM optimizer tool (part of LLVM installation)
- [Graphviz](https://graphviz.org/) - Graph visualization software
- [Node.js](https://nodejs.org/) (with npm) - For frontend dependencies and tooling 

### Container Deployment
- [Docker](https://www.docker.com/) (optional, for container deployment)

---

## 💻 Local Deployment  
Step-by-step guide to running the project locally:  

1. **Clone the repository**  
   ```sh
   git clone git@github.com:Vovadinamik8913/IR-Visualiser.git
2. **Set properties for Server in file backend/run.sh**
   ```
   OPT_PATH - path to your opt
   WORK_PATH - folder where tree and files will be saved
   DATASOURCE_URL - path to your db file
3. **Execute backend/run.sh and front/run.sh**
4. **Open in browser localhost:3000 - you are welcome**

## 🐳 Container Deployment  
Step-by-step guide to running the project in container: 
1. **Clone the repository**  
   ```sh
   git clone git@github.com:Vovadinamik8913/IR-Visualiser.git
2. **Set properties for Server in file compose.yml**
   ```
   OPT_PATH - path to your opt
   WORK_PATH - folder where tree and files will be saved
   DATASOURCE_URL - path to your db file
   and change connected volumes
3. **Run docker compose up --build**
4. **Open in browser localhost:3000 - you are welcome**