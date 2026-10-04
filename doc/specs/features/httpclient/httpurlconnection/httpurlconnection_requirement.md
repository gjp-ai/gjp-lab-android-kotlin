# Feature: HttpURLConnection

Status: Implemented

## Goal

Let a developer build and send an HTTP request with Android's native `HttpURLConnection` API and inspect the response, as a learning sample for the HTTP Client category.

## Scope

### In scope

- Choose an HTTP method, enter a URL, and optionally enter a JSON payload.
- Send the request and show the status code, response body, and response headers.
- Show a clear message when the request cannot be sent or the response cannot be read.

### Out of scope

- Authentication, custom headers, cookies, and request history.
- Third-party HTTP libraries (covered by separate HTTP Client entries such as Retrofit).
- Background or resumable transfers, file uploads, and streaming.

## Behavior

- The screen opens with `GET` selected and a working sample URL.
- Sending shows a progress indicator and disables the send button until the request finishes.
- A completed request opens a separate response screen, whatever the HTTP status code; 4xx and 5xx responses are shown, not treated as errors.
- A failed request (invalid URL, no network, timeout, untrusted certificate) stays on the request screen and shows an error message.
- Changing the method, URL, or payload clears any visible error.

## UI & navigation

- Entry point: **HTTP Client** category → **HttpURLConnection** catalogue item.
- Request screen controls: method chips (`GET`, `POST`, `PUT`, `DELETE`), URL field, request payload field (only for `POST` and `PUT`), and **Send request** button.
- The send button is disabled while a request is running or when the URL is blank.
- Response screen: HTTP status (colored by success or failure and still readable as text), response body (pretty-printed when it is JSON), and headers. Body and headers are selectable and scroll horizontally.
- Both screens support light and dark themes and enlarged text. The response opens on top of the request inside the feature pane; Back returns to the request with its input unchanged.

## Rules & constraints

- Requests time out after 15 seconds (connect and read).
- Requests send `Accept: application/json`; `POST` and `PUT` also send `Content-Type: application/json; charset=utf-8` and include the payload when it is not blank.
- Error bodies (4xx and 5xx) are read and shown like successful bodies.
- Network work runs off the main thread, and the connection is always closed.
- Use only public `HttpURLConnection` APIs and the app's network security configuration; cleartext traffic is not permitted for the sample host.
- Do not log URLs, payloads, response bodies, or headers; logs may contain only the method, status code, size, and exception type.

## Platform limitations

- The network security configuration blocks cleartext `http://` to the sample domain; other hosts follow Android's default (cleartext blocked from API 28).
- Responses are read as UTF-8 text; binary bodies are not meaningful.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| HUC-AC-01 | Send the default `GET` request with network access | The response screen shows status 200, formatted JSON, and headers. |
| HUC-AC-02 | Enter a malformed URL and send | An error message appears; no response screen opens. |
| HUC-AC-03 | Clear the URL field | The send button is disabled. |
| HUC-AC-04 | Select `POST` or `PUT` | The payload field appears; it is hidden for `GET` and `DELETE`. |
| HUC-AC-05 | The server returns 404 or 500 | The response screen opens and shows the status in the error color, with the error body. |
| HUC-AC-06 | Send with no network, or the server does not answer within 15 seconds | An error message appears on the request screen and can be dismissed. |
| HUC-AC-07 | The server returns non-JSON text | The body is shown unchanged. |

## Technical implementation constraints

- Feature folder: `features/httpclient/httpurlconnection/`.
- Navigation goes through `FeatureRoute.HttpURLConnection` (topic) and `DetailRoute.Response` (pushed screen), drawn by `ContentView`.
- `HttpURLConnectionRepository` owns request construction, timeouts, formatting, and headers; it is injected through Koin and passed to the screen.
- No new dependencies.

## Related documents

- [Detailed design](httpurlconnection_detail_design.md)
- [Application architecture](../../../../architecture/application.md)
