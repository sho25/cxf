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
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|endpoint
operator|.
name|Server
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
name|ServerRegistry
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
name|DestinationWithEndpoint
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
name|http
operator|.
name|UrlUtilities
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
name|transports
operator|.
name|http
operator|.
name|QueryHandlerRegistry
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
name|transports
operator|.
name|http
operator|.
name|StemMatchingQueryHandler
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
class|class
name|JavascriptQueryHandler
implements|implements
name|StemMatchingQueryHandler
block|{
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
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|JavascriptQueryHandler
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Resource
specifier|public
specifier|final
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|!=
name|b
condition|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|QueryHandlerRegistry
name|reg
init|=
name|b
operator|.
name|getExtension
argument_list|(
name|QueryHandlerRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reg
operator|!=
literal|null
condition|)
block|{
name|reg
operator|.
name|registerHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|getResponseContentType
parameter_list|(
name|String
name|fullQueryString
parameter_list|,
name|String
name|ctx
parameter_list|)
block|{
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
name|fullQueryString
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|UrlUtilities
operator|.
name|parseQueryString
argument_list|(
name|uri
operator|.
name|getQuery
argument_list|()
argument_list|)
decl_stmt|;
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
literal|"application/javascript;charset=UTF-8"
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isRecognizedQuery
parameter_list|(
name|String
name|baseUri
parameter_list|,
name|String
name|ctx
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|boolean
name|contextMatchExact
parameter_list|)
block|{
if|if
condition|(
name|baseUri
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
name|baseUri
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|UrlUtilities
operator|.
name|parseQueryString
argument_list|(
name|uri
operator|.
name|getQuery
argument_list|()
argument_list|)
decl_stmt|;
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
name|UrlUtilities
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
comment|// it's amazing that this still has to be coded up.
name|byte
name|buffer
index|[]
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|count
decl_stmt|;
try|try
block|{
while|while
condition|(
operator|(
name|count
operator|=
name|utils
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
literal|1024
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|outputStream
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
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
name|Endpoint
name|findEndpoint
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
block|{
name|ServerRegistry
name|serverRegistry
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ServerRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Server
name|server
range|:
name|serverRegistry
operator|.
name|getServers
argument_list|()
control|)
block|{
comment|// Hypothetically, not all destinations have an endpoint.
comment|// There has to be a better way to do this.
if|if
condition|(
name|server
operator|.
name|getDestination
argument_list|()
operator|instanceof
name|DestinationWithEndpoint
operator|&&
name|endpointInfo
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|DestinationWithEndpoint
operator|)
name|server
operator|.
name|getDestination
argument_list|()
operator|)
operator|.
name|getEndpointInfo
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|server
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|writeResponse
parameter_list|(
name|String
name|fullQueryString
parameter_list|,
name|String
name|ctx
parameter_list|,
name|EndpointInfo
name|endpoint
parameter_list|,
name|OutputStream
name|os
parameter_list|)
block|{
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
name|fullQueryString
argument_list|)
decl_stmt|;
name|String
name|query
init|=
name|uri
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|UrlUtilities
operator|.
name|parseQueryString
argument_list|(
name|query
argument_list|)
decl_stmt|;
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
name|JavascriptQueryHandler
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
name|endpoint
operator|.
name|getService
argument_list|()
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
name|Endpoint
name|serverEndpoint
init|=
name|findEndpoint
argument_list|(
name|endpoint
argument_list|)
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
name|endpoint
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
name|fullQueryString
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|isRecognizedQuery
parameter_list|(
name|String
name|fullQueryString
parameter_list|,
name|String
name|ctx
parameter_list|,
name|EndpointInfo
name|endpoint
parameter_list|)
block|{
return|return
name|isRecognizedQuery
argument_list|(
name|fullQueryString
argument_list|,
name|ctx
argument_list|,
name|endpoint
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

