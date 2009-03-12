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
name|jaxrs
operator|.
name|client
package|;
end_package

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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
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
name|util
operator|.
name|Date
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Cookie
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|EntityTag
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|PathSegment
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
operator|.
name|ResponseBuilder
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|AbstractOutDatabindingInterceptor
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
name|jaxrs
operator|.
name|ext
operator|.
name|form
operator|.
name|Form
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
name|jaxrs
operator|.
name|utils
operator|.
name|HttpUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|MessageContentsList
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
import|;
end_import

begin_comment
comment|/**  * Http-centric web client  *  */
end_comment

begin_class
specifier|public
class|class
name|WebClient
extends|extends
name|AbstractClient
block|{
specifier|protected
name|WebClient
parameter_list|(
name|String
name|baseAddress
parameter_list|)
block|{
name|this
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|WebClient
parameter_list|(
name|URI
name|baseAddress
parameter_list|)
block|{
name|super
argument_list|(
name|baseAddress
argument_list|,
name|baseAddress
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates WebClient      * @param baseAddress baseAddress      */
specifier|public
specifier|static
name|WebClient
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|baseAddress
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|createWebClient
argument_list|()
return|;
block|}
comment|/**      * Creates WebClient      * @param baseURI baseURI      */
specifier|public
specifier|static
name|WebClient
name|create
parameter_list|(
name|URI
name|baseURI
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|baseURI
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Creates WebClient      * @param baseURI baseURI      * @param providers list of providers      */
specifier|public
specifier|static
name|WebClient
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|providers
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|baseAddress
argument_list|,
name|providers
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Creates a Spring-configuration aware WebClient      * @param baseAddress baseAddress      * @param providers list of providers      * @param configLocation classpath location of Spring configuration resource, can be null        * @return WebClient instance      */
specifier|public
specifier|static
name|WebClient
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|providers
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|getBean
argument_list|(
name|baseAddress
argument_list|,
name|configLocation
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|createWebClient
argument_list|()
return|;
block|}
comment|/**      * Creates a Spring-configuration aware WebClient      * @param baseAddress baseAddress      * @param configLocation classpath location of Spring configuration resource, can be null        * @return WebClient instance      */
specifier|public
specifier|static
name|WebClient
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|getBean
argument_list|(
name|baseAddress
argument_list|,
name|configLocation
argument_list|)
decl_stmt|;
return|return
name|bean
operator|.
name|createWebClient
argument_list|()
return|;
block|}
comment|/**      * Creates a Spring-configuration aware WebClient which will do basic authentication      * @param baseAddress baseAddress      * @param username username      * @param password password      * @param configLocation classpath location of Spring configuration resource, can be null        * @return WebClient instance      */
specifier|public
specifier|static
name|WebClient
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|getBean
argument_list|(
name|baseAddress
argument_list|,
name|configLocation
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|createWebClient
argument_list|()
return|;
block|}
comment|/**      * Creates WebClient, baseURI will be set to Client currentURI      * @param client existing client      */
specifier|public
specifier|static
name|WebClient
name|fromClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
return|return
name|fromClient
argument_list|(
name|client
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Creates WebClient, baseURI will be set to Client currentURI      * @param client existing client      * @param inheritHeaders  if existing Client headers can be inherited by new proxy       *        and subresource proxies if any       */
specifier|public
specifier|static
name|WebClient
name|fromClient
parameter_list|(
name|Client
name|client
parameter_list|,
name|boolean
name|inheritHeaders
parameter_list|)
block|{
name|WebClient
name|webClient
init|=
name|create
argument_list|(
name|client
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|inheritHeaders
condition|)
block|{
name|webClient
operator|.
name|headers
argument_list|(
name|client
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|copyProperties
argument_list|(
name|webClient
argument_list|,
name|client
argument_list|)
expr_stmt|;
return|return
name|webClient
return|;
block|}
comment|/**      * Converts proxy to Client      * @param proxy the proxy      * @return proxy as a Client       */
specifier|public
specifier|static
name|Client
name|client
parameter_list|(
name|Object
name|proxy
parameter_list|)
block|{
if|if
condition|(
name|proxy
operator|instanceof
name|Client
condition|)
block|{
return|return
operator|(
name|Client
operator|)
name|proxy
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Does HTTP invocation      * @param httpMethod HTTP method      * @param body request body, can be null      * @return JAXRS Response, entity may hold a string representaion of       *         error message if client or server error occured      */
specifier|public
name|Response
name|invoke
parameter_list|(
name|String
name|httpMethod
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
return|return
name|doInvoke
argument_list|(
name|httpMethod
argument_list|,
name|body
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Does HTTP POST invocation      * @param body request body, can be null      * @return JAXRS Response      */
specifier|public
name|Response
name|post
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
return|return
name|invoke
argument_list|(
literal|"POST"
argument_list|,
name|body
argument_list|)
return|;
block|}
comment|/**      * Does HTTP PUT invocation      * @param body request body, can be null      * @return JAXRS Response      */
specifier|public
name|Response
name|put
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
return|return
name|invoke
argument_list|(
literal|"PUT"
argument_list|,
name|body
argument_list|)
return|;
block|}
comment|/**      * Does HTTP GET invocation      * @return JAXRS Response      */
specifier|public
name|Response
name|get
parameter_list|()
block|{
return|return
name|invoke
argument_list|(
literal|"GET"
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Does HTTP HEAD invocation      * @return JAXRS Response      */
specifier|public
name|Response
name|head
parameter_list|()
block|{
return|return
name|invoke
argument_list|(
literal|"HEAD"
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Does HTTP OPTIONS invocation      * @return JAXRS Response      */
specifier|public
name|Response
name|options
parameter_list|()
block|{
return|return
name|invoke
argument_list|(
literal|"OPTIONS"
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Does HTTP DELETE invocation      * @return JAXRS Response      */
specifier|public
name|Response
name|delete
parameter_list|()
block|{
return|return
name|invoke
argument_list|(
literal|"DELETE"
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Posts form data      * @param values form values      * @return JAXRS Response      */
specifier|public
name|Response
name|form
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|values
parameter_list|)
block|{
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|)
expr_stmt|;
return|return
name|doInvoke
argument_list|(
literal|"POST"
argument_list|,
name|values
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Posts form data      * @param form form values      * @return JAXRS Response      */
specifier|public
name|Response
name|form
parameter_list|(
name|Form
name|form
parameter_list|)
block|{
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|)
expr_stmt|;
return|return
name|doInvoke
argument_list|(
literal|"POST"
argument_list|,
name|form
operator|.
name|getData
argument_list|()
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Does HTTP invocation and returns types response object       * @param httpMethod HTTP method       * @param body request body, can be null      * @param responseClass expected type of response object      * @return typed object, can be null. Response status code and headers       *         can be obtained too, see Client.getResponse()      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|invoke
parameter_list|(
name|String
name|httpMethod
parameter_list|,
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|responseClass
parameter_list|)
block|{
name|Response
name|r
init|=
name|doInvoke
argument_list|(
name|httpMethod
argument_list|,
name|body
argument_list|,
name|responseClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|getStatus
argument_list|()
operator|>=
literal|400
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|r
argument_list|)
throw|;
block|}
return|return
name|responseClass
operator|.
name|cast
argument_list|(
name|r
operator|.
name|getEntity
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Does HTTP POST invocation and returns typed response object      * @param body request body, can be null      * @param responseClass expected type of response object      * @return typed object, can be null. Response status code and headers       *         can be obtained too, see Client.getResponse()      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|post
parameter_list|(
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|responseClass
parameter_list|)
block|{
return|return
name|invoke
argument_list|(
literal|"POST"
argument_list|,
name|body
argument_list|,
name|responseClass
argument_list|)
return|;
block|}
comment|/**      * Does HTTP GET invocation and returns typed response object      * @param body request body, can be null      * @param responseClass expected type of response object      * @return typed object, can be null. Response status code and headers       *         can be obtained too, see Client.getResponse()      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|responseClass
parameter_list|)
block|{
return|return
name|invoke
argument_list|(
literal|"GET"
argument_list|,
literal|null
argument_list|,
name|responseClass
argument_list|)
return|;
block|}
comment|/**      * Updates the current URI path      * @param path new relative path segment      * @return updated WebClient      */
specifier|public
name|WebClient
name|path
parameter_list|(
name|Object
name|path
parameter_list|)
block|{
name|getCurrentBuilder
argument_list|()
operator|.
name|path
argument_list|(
name|path
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Updates the current URI query parameters      * @param name query name      * @param values query values      * @return updated WebClient      */
specifier|public
name|WebClient
name|query
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|getCurrentBuilder
argument_list|()
operator|.
name|queryParam
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Updates the current URI matrix parameters      * @param name matrix name      * @param values matrix values      * @return updated WebClient      */
specifier|public
name|WebClient
name|matrix
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|getCurrentBuilder
argument_list|()
operator|.
name|matrixParam
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Moves WebClient to a new baseURI or forwards to new currentURI        * @param newAddress new URI      * @param forward if true then currentURI will be based on baseURI        * @return updated WebClient      */
specifier|public
name|WebClient
name|to
parameter_list|(
name|String
name|newAddress
parameter_list|,
name|boolean
name|forward
parameter_list|)
block|{
if|if
condition|(
name|forward
condition|)
block|{
if|if
condition|(
operator|!
name|newAddress
operator|.
name|startsWith
argument_list|(
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Base address can not be preserved"
argument_list|)
throw|;
block|}
name|resetCurrentBuilder
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|newAddress
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resetBaseAddress
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|newAddress
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Goes back      * @param fast if true then goes back to baseURI otherwise to a previous path segment       * @return updated WebClient      */
specifier|public
name|WebClient
name|back
parameter_list|(
name|boolean
name|fast
parameter_list|)
block|{
if|if
condition|(
name|fast
condition|)
block|{
name|getCurrentBuilder
argument_list|()
operator|.
name|replacePath
argument_list|(
name|getBaseURI
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|URI
name|uri
init|=
name|getCurrentURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|==
name|getBaseURI
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
name|List
argument_list|<
name|PathSegment
argument_list|>
name|segments
init|=
name|JAXRSUtils
operator|.
name|getPathSegments
argument_list|(
name|uri
operator|.
name|getPath
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|getCurrentBuilder
argument_list|()
operator|.
name|replacePath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|segments
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|getCurrentBuilder
argument_list|()
operator|.
name|path
argument_list|(
name|HttpUtils
operator|.
name|fromPathSegment
argument_list|(
name|segments
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|type
parameter_list|(
name|MediaType
name|ct
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|type
argument_list|(
name|ct
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|type
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|accept
parameter_list|(
name|MediaType
modifier|...
name|types
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|accept
argument_list|(
name|types
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|accept
parameter_list|(
name|String
modifier|...
name|types
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|accept
argument_list|(
name|types
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|language
parameter_list|(
name|String
name|language
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|language
argument_list|(
name|language
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|acceptLanguage
parameter_list|(
name|String
modifier|...
name|languages
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|acceptLanguage
argument_list|(
name|languages
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|encoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|encoding
argument_list|(
name|encoding
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|acceptEncoding
parameter_list|(
name|String
modifier|...
name|encodings
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|acceptEncoding
argument_list|(
name|encodings
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|match
parameter_list|(
name|EntityTag
name|tag
parameter_list|,
name|boolean
name|ifNot
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|match
argument_list|(
name|tag
argument_list|,
name|ifNot
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|modified
parameter_list|(
name|Date
name|date
parameter_list|,
name|boolean
name|ifNot
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|modified
argument_list|(
name|date
argument_list|,
name|ifNot
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|cookie
parameter_list|(
name|Cookie
name|cookie
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|cookie
argument_list|(
name|cookie
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|header
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|header
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|headers
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|headers
argument_list|(
name|map
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|WebClient
name|reset
parameter_list|()
block|{
return|return
operator|(
name|WebClient
operator|)
name|super
operator|.
name|reset
argument_list|()
return|;
block|}
specifier|protected
name|Response
name|doInvoke
parameter_list|(
name|String
name|httpMethod
parameter_list|,
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|responseClass
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|getHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
operator|&&
name|headers
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
operator|==
literal|null
condition|)
block|{
name|headers
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|responseClass
operator|!=
literal|null
operator|&&
name|headers
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|)
operator|==
literal|null
condition|)
block|{
name|headers
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resetResponse
argument_list|()
expr_stmt|;
return|return
name|doChainedInvocation
argument_list|(
name|httpMethod
argument_list|,
name|headers
argument_list|,
name|body
argument_list|,
name|responseClass
argument_list|)
return|;
block|}
specifier|protected
name|Response
name|doChainedInvocation
parameter_list|(
name|String
name|httpMethod
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|responseClass
parameter_list|)
block|{
name|Message
name|m
init|=
name|createMessage
argument_list|(
name|httpMethod
argument_list|,
name|headers
argument_list|,
name|getCurrentURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|MessageContentsList
name|contents
init|=
operator|new
name|MessageContentsList
argument_list|(
name|body
argument_list|)
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|contents
argument_list|)
expr_stmt|;
name|m
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|BodyWriter
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|m
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|doIntercept
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|// we'd like a user to get the whole Response anyway if needed
block|}
comment|// TODO : this needs to be done in an inbound chain instead
name|HttpURLConnection
name|connect
init|=
operator|(
name|HttpURLConnection
operator|)
name|m
operator|.
name|get
argument_list|(
name|HTTPConduit
operator|.
name|KEY_HTTP_CONNECTION
argument_list|)
decl_stmt|;
return|return
name|handleResponse
argument_list|(
name|connect
argument_list|,
name|m
argument_list|,
name|responseClass
argument_list|)
return|;
block|}
specifier|protected
name|Response
name|handleResponse
parameter_list|(
name|HttpURLConnection
name|conn
parameter_list|,
name|Message
name|m
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|responseClass
parameter_list|)
block|{
try|try
block|{
name|ResponseBuilder
name|rb
init|=
name|setResponseBuilder
argument_list|(
name|conn
argument_list|)
operator|.
name|clone
argument_list|()
decl_stmt|;
name|Response
name|currentResponse
init|=
name|rb
operator|.
name|clone
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|Object
name|entity
init|=
name|readBody
argument_list|(
name|currentResponse
argument_list|,
name|conn
argument_list|,
name|m
argument_list|,
name|responseClass
argument_list|,
name|responseClass
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|)
decl_stmt|;
name|rb
operator|.
name|entity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
return|return
name|rb
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|HttpURLConnection
name|getConnection
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
return|return
name|createHttpConnection
argument_list|(
name|getCurrentBuilder
argument_list|()
operator|.
name|clone
argument_list|()
operator|.
name|buildFromEncoded
argument_list|()
argument_list|,
name|methodName
argument_list|)
return|;
block|}
specifier|private
class|class
name|BodyWriter
extends|extends
name|AbstractOutDatabindingInterceptor
block|{
specifier|public
name|BodyWriter
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|WRITE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|m
parameter_list|)
throws|throws
name|Fault
block|{
name|OutputStream
name|os
init|=
name|m
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|os
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|MessageContentsList
name|objs
init|=
name|MessageContentsList
operator|.
name|getContentsList
argument_list|(
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|objs
operator|==
literal|null
operator|||
name|objs
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
operator|(
name|MultivaluedMap
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
decl_stmt|;
name|Object
name|body
init|=
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|writeBody
argument_list|(
name|body
argument_list|,
name|m
argument_list|,
name|body
operator|.
name|getClass
argument_list|()
argument_list|,
name|body
operator|.
name|getClass
argument_list|()
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|headers
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
specifier|static
name|void
name|copyProperties
parameter_list|(
name|Client
name|toClient
parameter_list|,
name|Client
name|fromClient
parameter_list|)
block|{
name|AbstractClient
name|newClient
init|=
name|toAbstractClient
argument_list|(
name|toClient
argument_list|)
decl_stmt|;
name|AbstractClient
name|oldClient
init|=
name|toAbstractClient
argument_list|(
name|fromClient
argument_list|)
decl_stmt|;
name|newClient
operator|.
name|bus
operator|=
name|oldClient
operator|.
name|bus
expr_stmt|;
name|newClient
operator|.
name|conduitSelector
operator|=
name|oldClient
operator|.
name|conduitSelector
expr_stmt|;
name|newClient
operator|.
name|inInterceptors
operator|=
name|oldClient
operator|.
name|inInterceptors
expr_stmt|;
name|newClient
operator|.
name|outInterceptors
operator|=
name|oldClient
operator|.
name|outInterceptors
expr_stmt|;
block|}
specifier|private
specifier|static
name|AbstractClient
name|toAbstractClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
if|if
condition|(
name|client
operator|instanceof
name|AbstractClient
condition|)
block|{
return|return
operator|(
name|AbstractClient
operator|)
name|client
return|;
block|}
else|else
block|{
return|return
call|(
name|AbstractClient
call|)
argument_list|(
operator|(
name|InvocationHandlerAware
operator|)
name|client
argument_list|)
operator|.
name|getInvocationHandler
argument_list|()
return|;
block|}
block|}
specifier|static
name|JAXRSClientFactoryBean
name|getBean
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|String
name|configLocation
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|configLocation
operator|!=
literal|null
condition|)
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|configLocation
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
name|bean
operator|.
name|setAddress
argument_list|(
name|baseAddress
argument_list|)
expr_stmt|;
return|return
name|bean
return|;
block|}
block|}
end_class

end_unit

