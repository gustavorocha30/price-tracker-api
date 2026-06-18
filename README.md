# 🎯 Price Tracker API - Motor de Web Scraping Automático

Este é um projeto Full Stack desenvolvido para rastrear automaticamente os preços de produtos em e-commerces (como o Mercado Livre) e notificar o usuário por e-mail quando o valor atingir um "Preço Alvo" estabelecido.

> 🔗 **Demonstração:** [Assista ao vídeo do projeto funcionando no LinkedIn](https://www.linkedin.com/posts/grocha-dev_java-springboot-webscraping-ugcPost-7472645766935904256-lGuq/)

## 🧠 Como Funciona

1. **Cadastro:** O usuário acessa a Dashboard (Frontend) e insere o link de um produto e o valor que deseja pagar.
2. **Agendamento:** Um *Worker* rodando em background (Backend) acorda a cada intervalo de tempo para varrer o banco de dados.
3. **Extração e Disfarce:** O robô vai até a página do e-commerce, aplica disfarces (*User-Agent* e *Headers*) para evitar bloqueios e extrai o nome e o preço atual do produto usando Web Scraping.
4. **Data Cleaning:** O texto sujo extraído da web passa por uma limpeza (Regex) e é convertido em valores matemáticos persistidos no banco.
5. **Notificação:** Se o preço atual for menor ou igual ao preço alvo, o sistema envia um e-mail em tempo real para o usuário.

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot (Web, Data JPA, Mail, Scheduling)
* **Web Scraping:** Jsoup
* **Banco de Dados:** PostgreSQL
* **Frontend:** HTML5, CSS3, JavaScript (Fetch API)

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos
* Java 21 instalado
* Maven instalado
* PostgreSQL rodando localmente (na porta `5432`)
* Uma senha de aplicativo gerada na sua conta do Google (para envio de e-mails)
