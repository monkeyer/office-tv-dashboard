package org.jetbrains.office.tv

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.content.*
import io.ktor.features.*
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.experimental.runBlocking
import java.net.URL

fun main(args: Array<String>): Unit = io.ktor.server.tomcat.DevelopmentEngine.main(args)

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

fun Application.module() {

	routing {
		get("/") {
			call.respondText("Hi from Dashboard!", contentType = ContentType.Text.Plain)
		}

		get("/html-dsl") {
			call.respondHtml {
				body {
					h1 { +"HTML" }
					ul {
						for (n in 1..10) {
							li { +"$n" }
						}
					}
				}
			}
		}

		get("/styles.css") {
			call.respondCss {
				body {
					backgroundColor = Color.red
				}
				p {
					fontSize = 2.em
				}
				rule("p.myclass") {
					color = Color.blue
				}
			}
		}

		// Static feature. Try to access `/static/ktor_logo.svg`
		static("/static") {
			resources("static")
		}

		install(StatusPages) {
			exception<AuthenticationException> {  cause ->
				call.respond(HttpStatusCode.Unauthorized)
			}
			exception<AuthorizationException> {  cause ->
				call.respond(HttpStatusCode.Forbidden)
			}
		}
	}
}

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
	style(type = ContentType.Text.CSS.toString()) {
		+CSSBuilder().apply(builder).toString()
	}
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
	this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
	this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
