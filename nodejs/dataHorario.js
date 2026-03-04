
const express = require("express")
const { format } = require("date-fns")


const app = express()



app.use(express.json())

app.get("/data", (req, res) => {
    
    const data = new Date();
    const dataForma = format(data, "dd/MM/yyyy")

    const resposta = {
        "data": dataForma
    }
    res.type('application/json')
    res.send(JSON.stringify(resposta))
})

app.get("/horario", (req, res) => {
    
    const data = new Date();
    const dataForma = format(data, "hh:MM:SS")

    const resposta = {
        "horario": dataForma
    }
    res.type('application/json')
    res.send(JSON.stringify(resposta))
})

app.get("/agora", (req, res) => {
    
    const data = new Date();
    const dataForma = format(data, "dd/MM/yyyy")

    const hor = new Date();
    const horario = format(hor, "hh:MM:SS")

    const resposta = {
        "horario": horario,
        "data": dataForma
    }
    res.type('application/json')
    res.send(JSON.stringify(resposta))
})



app.listen("8080", () => {
    console.log("Hello world")
})