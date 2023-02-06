function getBaseURL(): string{
    let baseUrl = "127.0.0.1:8080" // Default Ktor URL
    if (import.meta.env.PROD){ // https://vitejs.dev/guide/env-and-mode.html
        // In Prod both backend and frontend share the same host + port
        baseUrl = location.host
    }
    return baseUrl
}

function getBaseURLWithProtocol(): string{
    return `${location.protocol}//${getBaseURL()}`
}

export {getBaseURL, getBaseURLWithProtocol}