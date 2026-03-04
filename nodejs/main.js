
const express = require("express")
const app = express()

app.get("/hello", (req, res) => {
    const html = `
    
    <html>
        <body>
            <p>teste</p>
            <div>
                <p>Outro Teste</p>
            </div>
        </body>
    </html>
    
    `

    res.type('text/plain')

    res.send(html)
})

app.listen("8080", () => {
    console.log("Hello world")
})