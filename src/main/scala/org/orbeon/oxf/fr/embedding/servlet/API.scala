/**
 * Copyright (C) 2014 Orbeon, Inc.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * The full text of the license is available at http://www.gnu.org/copyleft/lesser.html
 */
package org.orbeon.oxf.fr.embedding.servlet

import java.io.Writer
import javax.servlet.ServletContext
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.apache.commons.io.IOUtils
import org.orbeon.oxf.fr.embedding._
import org.orbeon.oxf.util.Headers._
import org.orbeon.oxf.util.NetUtils
import org.orbeon.oxf.util.ScalaUtils._
import org.orbeon.oxf.xml.XMLUtils

object API {
    
    import org.orbeon.oxf.fr.embedding.servlet.ServletFilter._

    // Embed an Orbeon Forms page by path
    def embedPageJava(
        servletCtx: ServletContext,
        req       : HttpServletRequest,
        writer    : Writer,
        path      : String
     ): Unit =
        withSettings(servletCtx, req, writer) { settings ⇒

            implicit val ctx = new ServletEmbeddingContextWithResponse(
                req,
                servletCtx.log,
                Left(writer),
                nextNamespace(req),
                settings.orbeonPrefix,
                settings.httpClient
            )

            APISupport.proxyPage(settings.formRunnerURL, path)
        }

    // Embed a Form Runner form
    def embedFormJava(
        servletCtx: ServletContext,
        req       : HttpServletRequest,
        writer    : Writer,
        app       : String,
        form      : String,
        action    : String,
        documentId: String,
        query     : String
     ): Unit =
        embedPageJava(
            servletCtx,
            req,
            writer,
            APISupport.formRunnerPath(app, form, action, Option(documentId), Option(query))
        )

    // Embed an Orbeon Forms page by path
    def embedPage(
        servletCtx: ServletContext,
        req       : HttpServletRequest,
        out       : Writer Either HttpServletResponse,
        path      : String
     ): Unit =
        withSettings(servletCtx, req, out.fold(identity, _.getWriter)) { settings ⇒

            implicit val ctx = new ServletEmbeddingContextWithResponse(
                req,
                servletCtx.log,
                out,
                nextNamespace(req),
                settings.orbeonPrefix,
                settings.httpClient
            )

            APISupport.proxyPage(settings.formRunnerURL, path)
        }

    // Embed a Form Runner form
    def embedForm(
        servletCtx: ServletContext,
        req       : HttpServletRequest,
        out       : Writer Either HttpServletResponse,
        app       : String,
        form      : String,
        action    : Action,
        documentId: Option[String],
        query     : Option[String]
    ): Unit =
        embedPage(
            servletCtx,
            req,
            out,
            APISupport.formRunnerPath(app, form, action.name, documentId, query)
        )

    def proxyServletResources(
        servletCtx  : ServletContext,
        req         : HttpServletRequest,
        res         : HttpServletResponse,
        namespace   : String,
        resourcePath: String
     ): Unit =
        withSettings(servletCtx, req, res.getWriter) { settings ⇒

            implicit val ctx = new ServletEmbeddingContextWithResponse(
                req,
                servletCtx.log,
                Right(res),
                namespace,
                settings.orbeonPrefix,
                settings.httpClient
            )

            def contentFromRequest =
                req.getMethod == "POST" option {

                    val body =
                        if (XMLUtils.isXMLMediatype(NetUtils.getContentTypeMediaType(req.getContentType)))
                            Left(IOUtils.toString(req.getInputStream, Option(req.getCharacterEncoding) getOrElse "utf-8"))
                        else
                            Right(IOUtils.toByteArray(req.getInputStream))

                    Content(body, Option(req.getContentType), None)
                }

            // Filter out client cookie headers. We clearly don't want JSESSIONID. If cookie forwarding is needed, this
            // should be seen as a separate configurable feature.
            val RequestHeadersToRemove = Set("cookie", "cookie2")

            val requestHeaders =
                proxyCapitalizeAndCombineHeaders(APISupport.requestHeaders(req).to[List], request = true) filterNot {
                    case (name, _) ⇒ RequestHeadersToRemove(name.toLowerCase)
                }

            APISupport.proxyResource(
                RequestDetails(
                    content  = contentFromRequest,
                    url      = APISupport.formRunnerURL(settings.formRunnerURL, resourcePath, embeddable = false),
                    headers  = requestHeaders.to[List],
                    params   = Nil
                )
            )
        }
    
    private val LastNamespaceIndexKey = "orbeon-form-runner-last-namespace-index"
    private val NamespacePrefix       = "o"

    private def nextNamespace(req: HttpServletRequest) = {

        val newValue =
            Option(req.getAttribute(LastNamespaceIndexKey).asInstanceOf[Integer]) match {
                case Some(value) ⇒ value + 1
                case None        ⇒ 0
            }

        req.setAttribute(LastNamespaceIndexKey, newValue)

        NamespacePrefix + newValue
    }

    private def withSettings[T](servletCtx: ServletContext, req: HttpServletRequest, writer: ⇒ Writer)(body: FilterSettings ⇒ T): Unit =
        Option(req.getAttribute(SettingsKey).asInstanceOf[FilterSettings]) match {
            case Some(settings) ⇒
                body(settings)
            case None ⇒
                val msg = "ERROR: Orbeon Forms embedding filter is not configured."
                servletCtx.log(msg)
                writer.write(msg)
        }
}