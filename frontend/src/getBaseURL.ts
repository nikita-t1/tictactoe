function getBaseURL(): string{
    let baseUrl = "127.0.0.1:8080" // Default Ktor URL
    if (import.meta.env.PROD){ // https://vitejs.dev/guide/env-and-mode.html
        // In Prod both backend and frontend share the same host + port
        baseUrl = location.host
    }
    console.log(baseUrl)
    return baseUrl
}

function getBaseURLWithProtocol(): string{
    if (import.meta.env.PROD) { // https://vitejs.dev/guide/env-and-mode.html
        return `https://${getBaseURL()}`
    }
    return `http://${getBaseURL()}`
}

export {getBaseURL, getBaseURLWithProtocol}