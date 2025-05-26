# IR-Visualiser
A client-server project for a better view and navigation between ir and svg

---

## 📌 Table of Contents  
- [📝 Description](#-description)  
- [🛠️ Required Components](#️-required-components)  
- [💻 Local Deployment](#-local-deployment)  
- [🐳 Container Deployment](#-container-deployment)  

---

📝 Project Description

LLVM IR Analyzer & Optimizer

Our project is a web-based tool designed for viewing, analyzing, and optimizing LLVM Intermediate Representation (IR) with an intuitive graphical interface.

✨ Key Features

✅ Cross-Navigation – Easily jump between functions, basic blocks, and instructions.

✅ Project Management – Save and load IR projects for later analysis.

✅ Optimization Pipeline – Generate optimized IR from an input module with customizable passes.

✅ Advanced Analyses – Interactive visualization for:

    Dominator Trees (DomTree)

    Loop Structures

    MemorySSA

    Scalar Evolution (SCEV)

🛠️ Tech Stack

    Backend: Spring (Java)

    Frontend: Modern web framework (React)

🎯 Use Cases

    Compiler developers – Debug and optimize IR during pass development.

    Students & Researchers – Learn LLVM IR semantics visually.

    Performance Engineers – Analyze and tune code at the IR level.  

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
2. **Set properties for Server in file backend/.env**
   ```
   OPT_PATH - path to your opt
   WORK_PATH - folder where tree and files will be saved
3. **Execute backend/run.sh and front/run.sh**
4. **Open in browser localhost:3000 - you are welcome**

## 🐳 Container Deployment  
Step-by-step guide to running the project in container: 
1. **Clone the repository**  
   ```sh
   git clone git@github.com:Vovadinamik8913/IR-Visualiser.git
2. **Run docker compose up --build**
3. **Open in browser localhost:3000 - you are welcome**
