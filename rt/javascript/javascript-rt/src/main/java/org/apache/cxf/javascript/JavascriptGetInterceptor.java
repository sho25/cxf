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
name|javascript
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
name|InputStream
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
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|i18n
operator|.
name|UncheckedException
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
name|endpoint
operator|.
name|Endpoint
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
name|helpers
operator|.
name|IOUtils
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
name|interceptor
operator|.
name|Interceptor
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
name|javascript
operator|.
name|service
operator|.
name|ServiceJavascriptBuilder
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
name|javascript
operator|.
name|types
operator|.
name|SchemaJavascriptBuilder
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
name|service
operator|.
name|model
operator|.
name|SchemaInfo
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
name|ServiceInfo
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

begin_class
specifier|public
class|class
name|JavascriptGetInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|INSTANCE
init|=
operator|new
name|JavascriptGetInterceptor
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JS_UTILS_PATH
init|=
literal|"/org/apache/cxf/javascript/cxf-utils.js"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Charset
name|UTF8
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"utf-8"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NO_UTILS_QUERY_KEY
init|=
literal|"nojsutils"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CODE_QUERY_KEY
init|=
literal|"js"
decl_stmt|;
specifier|public
name|JavascriptGetInterceptor
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
name|URI
name|uri
init|=
literal|null
decl_stmt|;
try|try
block|{
name|uri
operator|=
name|URI
operator|.
name|create
argument_list|(
name|baseUri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|iae
parameter_list|)
block|{
comment|//invalid URI, ignore and continue
return|return;
block|}
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
name|uri
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
literal|"application/javascript;charset=UTF-8"
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
name|writeResponse
argument_list|(
name|uri
argument_list|,
name|map
argument_list|,
name|os
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|ioe
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
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
name|URI
name|uri
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
name|CODE_QUERY_KEY
argument_list|)
condition|)
block|{
return|return
name|endpointInfo
operator|.
name|getAddress
argument_list|()
operator|.
name|contains
argument_list|(
name|UrlUtils
operator|.
name|getStem
argument_list|(
name|uri
operator|.
name|getSchemeSpecificPart
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|void
name|writeUtilsToResponseStream
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|referenceClass
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
block|{
name|InputStream
name|utils
init|=
name|referenceClass
operator|.
name|getResourceAsStream
argument_list|(
name|JS_UTILS_PATH
argument_list|)
decl_stmt|;
if|if
condition|(
name|utils
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to get stream for "
operator|+
name|JS_UTILS_PATH
argument_list|)
throw|;
block|}
try|try
block|{
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|utils
argument_list|,
name|outputStream
argument_list|)
expr_stmt|;
name|outputStream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to write javascript utils to HTTP response."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|writeResponse
parameter_list|(
name|URI
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|Endpoint
name|serverEndpoint
parameter_list|)
block|{
name|OutputStreamWriter
name|writer
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|os
argument_list|,
name|UTF8
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|map
operator|.
name|containsKey
argument_list|(
name|NO_UTILS_QUERY_KEY
argument_list|)
condition|)
block|{
name|writeUtilsToResponseStream
argument_list|(
name|JavascriptGetInterceptor
operator|.
name|class
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
name|CODE_QUERY_KEY
argument_list|)
condition|)
block|{
name|ServiceInfo
name|serviceInfo
init|=
name|serverEndpoint
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|SchemaInfo
argument_list|>
name|schemata
init|=
name|serviceInfo
operator|.
name|getSchemas
argument_list|()
decl_stmt|;
comment|// we need to move this to the bus.
name|BasicNameManager
name|nameManager
init|=
name|BasicNameManager
operator|.
name|newNameManager
argument_list|(
name|serviceInfo
argument_list|,
name|serverEndpoint
argument_list|)
decl_stmt|;
name|NamespacePrefixAccumulator
name|prefixManager
init|=
operator|new
name|NamespacePrefixAccumulator
argument_list|(
name|serviceInfo
operator|.
name|getXmlSchemaCollection
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
for|for
control|(
name|SchemaInfo
name|schema
range|:
name|schemata
control|)
block|{
name|SchemaJavascriptBuilder
name|builder
init|=
operator|new
name|SchemaJavascriptBuilder
argument_list|(
name|serviceInfo
operator|.
name|getXmlSchemaCollection
argument_list|()
argument_list|,
name|prefixManager
argument_list|,
name|nameManager
argument_list|)
decl_stmt|;
name|String
name|allThatJavascript
init|=
name|builder
operator|.
name|generateCodeForSchema
argument_list|(
name|schema
operator|.
name|getSchema
argument_list|()
argument_list|)
decl_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|allThatJavascript
argument_list|)
expr_stmt|;
block|}
name|ServiceJavascriptBuilder
name|serviceBuilder
init|=
operator|new
name|ServiceJavascriptBuilder
argument_list|(
name|serviceInfo
argument_list|,
name|serverEndpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|,
name|prefixManager
argument_list|,
name|nameManager
argument_list|)
decl_stmt|;
name|serviceBuilder
operator|.
name|walk
argument_list|()
expr_stmt|;
name|String
name|serviceJavascript
init|=
name|serviceBuilder
operator|.
name|getCode
argument_list|()
decl_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|serviceJavascript
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
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UncheckedException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Invalid query "
operator|+
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

