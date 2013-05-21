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
name|frontend
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|EndpointSelectionInterceptor
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
name|common
operator|.
name|util
operator|.
name|UrlUtils
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
name|interceptor
operator|.
name|Fault
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
name|message
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
name|message
operator|.
name|MessageImpl
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Conduit
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|WSDLGetInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|WSDLGetInterceptor
name|INSTANCE
init|=
operator|new
name|WSDLGetInterceptor
argument_list|()
decl_stmt|;
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
name|WSDLGetInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|WSDLGetInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|READ
argument_list|)
expr_stmt|;
name|getAfter
argument_list|()
operator|.
name|add
argument_list|(
name|EndpointSelectionInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|doOutput
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|base
parameter_list|,
name|Document
name|doc
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|WSDLQueryException
block|{
name|String
name|enc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|enc
operator|=
name|doc
operator|.
name|getXmlEncoding
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore - not dom level 3
block|}
if|if
condition|(
name|enc
operator|==
literal|null
condition|)
block|{
name|enc
operator|=
literal|"utf-8"
expr_stmt|;
block|}
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|out
argument_list|,
name|enc
argument_list|)
decl_stmt|;
try|try
block|{
name|StaxUtils
operator|.
name|writeNode
argument_list|(
name|doc
argument_list|,
name|writer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WSDLQueryException
argument_list|(
operator|new
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
argument_list|(
literal|"COULD_NOT_PROVIDE_WSDL"
argument_list|,
name|LOG
argument_list|,
name|base
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|StaxUtils
operator|.
name|close
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|String
name|method
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
name|String
name|query
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"GET"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|query
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|baseUri
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URL
argument_list|)
decl_stmt|;
name|String
name|ctx
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PATH_INFO
argument_list|)
decl_stmt|;
comment|//cannot have two wsdl's being written for the same endpoint at the same
comment|//time as the addresses may get mixed up
synchronized|synchronized
init|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
init|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|UrlUtils
operator|.
name|parseQueryString
argument_list|(
name|query
argument_list|)
decl_stmt|;
if|if
condition|(
name|isRecognizedQuery
argument_list|(
name|map
argument_list|,
name|baseUri
argument_list|,
name|ctx
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|Conduit
name|c
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getDestination
argument_list|()
operator|.
name|getBackChannel
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Message
name|mout
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|mout
operator|.
name|setExchange
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutMessage
argument_list|(
name|mout
argument_list|)
expr_stmt|;
name|mout
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|c
operator|.
name|prepare
argument_list|(
name|mout
argument_list|)
expr_stmt|;
name|OutputStream
name|os
init|=
name|mout
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|getDocument
argument_list|(
name|message
argument_list|,
name|baseUri
argument_list|,
name|map
argument_list|,
name|ctx
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|enc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|enc
operator|=
name|doc
operator|.
name|getXmlEncoding
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore - not dom level 3
block|}
if|if
condition|(
name|enc
operator|==
literal|null
condition|)
block|{
name|enc
operator|=
literal|"utf-8"
expr_stmt|;
block|}
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|,
name|enc
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|writeNode
argument_list|(
name|doc
argument_list|,
name|writer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|abort
argument_list|()
expr_stmt|;
try|try
block|{
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Failure writing full wsdl to the stream"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
comment|//we can ignore this.   Likely, whatever has requested the WSDL
comment|//has closed the connection before reading the entire wsdl.
comment|//WSDL4J has a tendency to not read the closing tags and such
comment|//and thus can sometimes hit this.   In anycase, it's
comment|//pretty much ignorable and nothing we can do about it (cannot
comment|//send a fault or anything anyway
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutMessage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|Document
name|getDocument
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|base
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|String
name|ctxUri
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|)
block|{
return|return
operator|new
name|WSDLGetUtils
argument_list|()
operator|.
name|getDocument
argument_list|(
name|message
argument_list|,
name|base
argument_list|,
name|params
argument_list|,
name|ctxUri
argument_list|,
name|endpointInfo
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isRecognizedQuery
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|,
name|String
name|baseUri
parameter_list|,
name|String
name|ctx
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|)
block|{
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"wsdl"
argument_list|)
operator|||
name|map
operator|.
name|containsKey
argument_list|(
literal|"xsd"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

