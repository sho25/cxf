begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|handler
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|HandlerChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|jaxb
operator|.
name|JAXBUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|handler
operator|.
name|types
operator|.
name|PortComponentHandlerType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
class|class
name|AnnotationHandlerChainBuilder
extends|extends
name|HandlerChainBuilder
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AnnotationHandlerChainBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|LOG
operator|.
name|getResourceBundle
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|JAXBContext
name|context
decl_stmt|;
specifier|private
name|ClassLoader
name|classLoader
decl_stmt|;
specifier|public
name|AnnotationHandlerChainBuilder
parameter_list|()
block|{     }
specifier|public
name|AnnotationHandlerChainBuilder
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param clz      * @param existingHandlers      * @return      */
specifier|public
name|List
argument_list|<
name|Handler
argument_list|>
name|buildHandlerChainFromClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|List
argument_list|<
name|Handler
argument_list|>
name|existingHandlers
parameter_list|,
name|QName
name|portQName
parameter_list|,
name|QName
name|serviceQName
parameter_list|,
name|String
name|bindingID
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"building handler chain"
argument_list|)
expr_stmt|;
name|classLoader
operator|=
name|getClassLoader
argument_list|(
name|clz
argument_list|)
expr_stmt|;
name|HandlerChainAnnotation
name|hcAnn
init|=
name|findHandlerChainAnnotation
argument_list|(
name|clz
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Handler
argument_list|>
name|chain
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|hcAnn
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"no HandlerChain annotation on "
operator|+
name|clz
argument_list|)
expr_stmt|;
block|}
name|chain
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|hcAnn
operator|.
name|validate
argument_list|()
expr_stmt|;
try|try
block|{
name|URL
name|handlerFileURL
init|=
name|resolveHandlerChainFile
argument_list|(
name|clz
argument_list|,
name|hcAnn
operator|.
name|getFileName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|handlerFileURL
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"HANDLER_CFG_FILE_NOT_FOUND_EXC"
argument_list|,
name|BUNDLE
argument_list|,
name|hcAnn
operator|.
name|getFileName
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|handlerFileURL
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|Element
name|el
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"http://java.sun.com/xml/ns/javaee"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
operator|!
literal|"handler-chains"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|xml
init|=
name|StaxUtils
operator|.
name|toString
argument_list|(
name|el
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BundleUtils
operator|.
name|getFormattedString
argument_list|(
name|BUNDLE
argument_list|,
literal|"NOT_VALID_ROOT_ELEMENT"
argument_list|,
literal|"http://java.sun.com/xml/ns/javaee"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
argument_list|,
literal|"handler-chains"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
argument_list|,
name|xml
argument_list|,
name|handlerFileURL
argument_list|)
argument_list|)
throw|;
block|}
name|chain
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|Node
name|node
init|=
name|el
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|el
operator|=
operator|(
name|Element
operator|)
name|node
expr_stmt|;
if|if
condition|(
operator|!
literal|"http://java.sun.com/xml/ns/javaee"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
operator|!
literal|"handler-chain"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|xml
init|=
name|StaxUtils
operator|.
name|toString
argument_list|(
name|el
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BundleUtils
operator|.
name|getFormattedString
argument_list|(
name|BUNDLE
argument_list|,
literal|"NOT_VALID_ELEMENT_IN_HANDLER"
argument_list|,
name|xml
argument_list|)
argument_list|)
throw|;
block|}
name|processHandlerChainElement
argument_list|(
name|el
argument_list|,
name|chain
argument_list|,
name|portQName
argument_list|,
name|serviceQName
argument_list|,
name|bindingID
argument_list|)
expr_stmt|;
block|}
name|node
operator|=
name|node
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"CHAIN_NOT_SPECIFIED_EXC"
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
assert|assert
name|chain
operator|!=
literal|null
assert|;
if|if
condition|(
name|existingHandlers
operator|!=
literal|null
condition|)
block|{
name|chain
operator|.
name|addAll
argument_list|(
name|existingHandlers
argument_list|)
expr_stmt|;
block|}
return|return
name|sortHandlers
argument_list|(
name|chain
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|ClassLoader
name|getClassLoader
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
specifier|final
name|SecurityManager
name|sm
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|sm
operator|!=
literal|null
condition|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|ClassLoader
argument_list|>
argument_list|()
block|{
specifier|public
name|ClassLoader
name|run
parameter_list|()
block|{
return|return
name|clazz
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
return|return
name|clazz
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
specifier|private
name|void
name|processHandlerChainElement
parameter_list|(
name|Element
name|el
parameter_list|,
name|List
argument_list|<
name|Handler
argument_list|>
name|chain
parameter_list|,
name|QName
name|portQName
parameter_list|,
name|QName
name|serviceQName
parameter_list|,
name|String
name|bindingID
parameter_list|)
block|{
name|Node
name|node
init|=
name|el
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|Node
name|cur
init|=
name|node
decl_stmt|;
name|node
operator|=
name|node
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
if|if
condition|(
name|cur
operator|instanceof
name|Element
condition|)
block|{
name|el
operator|=
operator|(
name|Element
operator|)
name|cur
expr_stmt|;
if|if
condition|(
operator|!
literal|"http://java.sun.com/xml/ns/javaee"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|xml
init|=
name|StaxUtils
operator|.
name|toString
argument_list|(
name|el
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BundleUtils
operator|.
name|getFormattedString
argument_list|(
name|BUNDLE
argument_list|,
literal|"NOT_VALID_ELEMENT_IN_HANDLER"
argument_list|,
name|xml
argument_list|)
argument_list|)
throw|;
block|}
name|String
name|name
init|=
name|el
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"port-name-pattern"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|patternMatches
argument_list|(
name|el
argument_list|,
name|portQName
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
elseif|else
if|if
condition|(
literal|"service-name-pattern"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|patternMatches
argument_list|(
name|el
argument_list|,
name|serviceQName
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
elseif|else
if|if
condition|(
literal|"protocol-bindings"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|protocolMatches
argument_list|(
name|el
argument_list|,
name|bindingID
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
elseif|else
if|if
condition|(
literal|"handler"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|processHandlerElement
argument_list|(
name|el
argument_list|,
name|chain
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|protocolMatches
parameter_list|(
name|Element
name|el
parameter_list|,
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
name|name
init|=
name|el
operator|.
name|getTextContent
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|name
argument_list|,
literal|" "
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|boolean
name|result
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
operator|&&
operator|!
name|result
condition|)
block|{
name|result
operator|=
name|result
operator|||
name|singleProtocolMatches
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
name|boolean
name|singleProtocolMatches
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
literal|"##SOAP11_HTTP"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
operator|.
name|contains
argument_list|(
name|id
argument_list|)
operator|||
literal|"http://schemas.xmlsoap.org/soap/"
operator|.
name|contains
argument_list|(
name|id
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"##SOAP11_HTTP_MTOM"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true"
operator|.
name|contains
argument_list|(
name|id
argument_list|)
operator|||
literal|"http://schemas.xmlsoap.org/soap/?mtom=true"
operator|.
name|contains
argument_list|(
name|id
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"##SOAP12_HTTP"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"http://www.w3.org/2003/05/soap/bindings/HTTP/"
operator|.
name|contains
argument_list|(
name|id
argument_list|)
operator|||
literal|"http://schemas.xmlsoap.org/wsdl/soap12/"
operator|.
name|contains
argument_list|(
name|id
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"##SOAP12_HTTP_MTOM"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"http://www.w3.org/2003/05/soap/bindings/HTTP/?mtom=true"
operator|.
name|contains
argument_list|(
name|id
argument_list|)
operator|||
literal|"http://schemas.xmlsoap.org/wsdl/soap12/?mtom=true"
operator|.
name|contains
argument_list|(
name|id
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"##XML_HTTP"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
literal|"http://www.w3.org/2004/08/wsdl/http"
expr_stmt|;
block|}
return|return
name|name
operator|.
name|contains
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|patternMatches
parameter_list|(
name|Element
name|el
parameter_list|,
name|QName
name|comp
parameter_list|)
block|{
if|if
condition|(
name|comp
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
name|namePattern
init|=
name|el
operator|.
name|getTextContent
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"*"
operator|.
name|equals
argument_list|(
name|namePattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
specifier|final
name|int
name|idx
init|=
name|namePattern
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|String
name|xml
init|=
name|StaxUtils
operator|.
name|toString
argument_list|(
name|el
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BundleUtils
operator|.
name|getFormattedString
argument_list|(
name|BUNDLE
argument_list|,
literal|"NOT_A_QNAME_PATTER"
argument_list|,
name|namePattern
argument_list|,
name|xml
argument_list|)
argument_list|)
throw|;
block|}
name|String
name|pfx
init|=
name|namePattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|String
name|ns
init|=
name|el
operator|.
name|lookupNamespaceURI
argument_list|(
name|pfx
argument_list|)
decl_stmt|;
if|if
condition|(
name|ns
operator|==
literal|null
condition|)
block|{
name|ns
operator|=
name|pfx
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|ns
operator|.
name|equals
argument_list|(
name|comp
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|localPart
init|=
name|namePattern
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|,
name|namePattern
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|localPart
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
comment|//wildcard pattern matching
return|return
name|Pattern
operator|.
name|matches
argument_list|(
name|mapPattern
argument_list|(
name|localPart
argument_list|)
argument_list|,
name|comp
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|localPart
operator|.
name|equals
argument_list|(
name|comp
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|String
name|mapPattern
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|(
name|s
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|buf
operator|.
name|length
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
switch|switch
condition|(
name|buf
operator|.
name|charAt
argument_list|(
name|x
argument_list|)
condition|)
block|{
case|case
literal|'*'
case|:
name|buf
operator|.
name|insert
argument_list|(
name|x
argument_list|,
literal|'.'
argument_list|)
expr_stmt|;
name|x
operator|++
expr_stmt|;
break|break;
case|case
literal|'.'
case|:
case|case
literal|'\\'
case|:
case|case
literal|'^'
case|:
case|case
literal|'$'
case|:
case|case
literal|'{'
case|:
case|case
literal|'}'
case|:
case|case
literal|'('
case|:
case|case
literal|')'
case|:
name|buf
operator|.
name|insert
argument_list|(
name|x
argument_list|,
literal|'\\'
argument_list|)
expr_stmt|;
name|x
operator|++
expr_stmt|;
break|break;
default|default:
comment|//nothing to do
block|}
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|processHandlerElement
parameter_list|(
name|Element
name|el
parameter_list|,
name|List
argument_list|<
name|Handler
argument_list|>
name|chain
parameter_list|)
block|{
try|try
block|{
name|JAXBContext
name|ctx
init|=
name|getContextForPortComponentHandlerType
argument_list|()
decl_stmt|;
name|PortComponentHandlerType
name|pt
init|=
name|JAXBUtils
operator|.
name|unmarshall
argument_list|(
name|ctx
argument_list|,
name|el
argument_list|,
name|PortComponentHandlerType
operator|.
name|class
argument_list|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|chain
operator|.
name|addAll
argument_list|(
name|buildHandlerChain
argument_list|(
name|pt
argument_list|,
name|classLoader
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not unmarshal handler chain"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
specifier|synchronized
name|JAXBContext
name|getContextForPortComponentHandlerType
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|PortComponentHandlerType
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
specifier|public
name|List
argument_list|<
name|Handler
argument_list|>
name|buildHandlerChainFromClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|QName
name|portQName
parameter_list|,
name|QName
name|serviceQName
parameter_list|,
name|String
name|bindingID
parameter_list|)
block|{
return|return
name|buildHandlerChainFromClass
argument_list|(
name|clz
argument_list|,
literal|null
argument_list|,
name|portQName
argument_list|,
name|serviceQName
argument_list|,
name|bindingID
argument_list|)
return|;
block|}
specifier|protected
name|URL
name|resolveHandlerChainAnnotationFile
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|clazz
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|private
name|HandlerChainAnnotation
name|findHandlerChainAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|boolean
name|searchSEI
parameter_list|)
block|{
if|if
condition|(
name|clz
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Checking for HandlerChain annotation on "
operator|+
name|clz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|HandlerChainAnnotation
name|hcAnn
init|=
literal|null
decl_stmt|;
name|HandlerChain
name|ann
init|=
name|clz
operator|.
name|getAnnotation
argument_list|(
name|HandlerChain
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|searchSEI
condition|)
block|{
comment|/* HandlerChain annotation can be specified on the SEI                  * but the implementation bean might not implement the SEI.                  */
name|WebService
name|ws
init|=
name|clz
operator|.
name|getAnnotation
argument_list|(
name|WebService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ws
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|ws
operator|.
name|endpointInterface
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|seiClassName
init|=
name|ws
operator|.
name|endpointInterface
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|seiClass
init|=
literal|null
decl_stmt|;
try|try
block|{
name|seiClass
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|seiClassName
argument_list|,
name|clz
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"SEI_LOAD_FAILURE_EXC"
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// check SEI class and its interfaces for HandlerChain annotation
name|hcAnn
operator|=
name|findHandlerChainAnnotation
argument_list|(
name|seiClass
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|hcAnn
operator|==
literal|null
condition|)
block|{
comment|// check interfaces for HandlerChain annotation
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|iface
range|:
name|clz
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Checking for HandlerChain annotation on "
operator|+
name|iface
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ann
operator|=
name|iface
operator|.
name|getAnnotation
argument_list|(
name|HandlerChain
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|ann
operator|!=
literal|null
condition|)
block|{
name|hcAnn
operator|=
operator|new
name|HandlerChainAnnotation
argument_list|(
name|ann
argument_list|,
name|iface
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|hcAnn
operator|==
literal|null
condition|)
block|{
name|hcAnn
operator|=
name|findHandlerChainAnnotation
argument_list|(
name|clz
operator|.
name|getSuperclass
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|hcAnn
operator|=
operator|new
name|HandlerChainAnnotation
argument_list|(
name|ann
argument_list|,
name|clz
argument_list|)
expr_stmt|;
block|}
return|return
name|hcAnn
return|;
block|}
specifier|static
class|class
name|HandlerChainAnnotation
block|{
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|declaringClass
decl_stmt|;
specifier|private
specifier|final
name|HandlerChain
name|ann
decl_stmt|;
name|HandlerChainAnnotation
parameter_list|(
name|HandlerChain
name|hc
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|)
block|{
name|ann
operator|=
name|hc
expr_stmt|;
name|declaringClass
operator|=
name|clz
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getDeclaringClass
parameter_list|()
block|{
return|return
name|declaringClass
return|;
block|}
specifier|public
name|String
name|getFileName
parameter_list|()
block|{
return|return
name|ann
operator|.
name|file
argument_list|()
return|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|ann
operator|.
name|file
argument_list|()
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|ann
operator|.
name|file
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"ANNOTATION_WITHOUT_URL_EXC"
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"["
operator|+
name|declaringClass
operator|+
literal|","
operator|+
name|ann
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

