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
name|systest
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|BadRequestException
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
name|client
operator|.
name|ClientRequestContext
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
name|client
operator|.
name|ClientResponseContext
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
name|client
operator|.
name|ClientResponseFilter
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
name|container
operator|.
name|ContainerRequestContext
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
name|container
operator|.
name|ContainerRequestFilter
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
name|container
operator|.
name|ContainerResponseContext
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
name|container
operator|.
name|ContainerResponseFilter
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
name|container
operator|.
name|PreMatching
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
name|Context
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
name|ext
operator|.
name|ExceptionMapper
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
name|ext
operator|.
name|MessageBodyReader
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
name|ext
operator|.
name|MessageBodyWriter
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
name|ext
operator|.
name|ParamConverter
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
name|ext
operator|.
name|ParamConverterProvider
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
name|ext
operator|.
name|Providers
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
name|BusFactory
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
name|PropertyUtils
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
name|ext
operator|.
name|logging
operator|.
name|LoggingInInterceptor
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
name|CastUtils
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
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
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
name|client
operator|.
name|ResponseExceptionMapper
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
name|search
operator|.
name|QueryContextProvider
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
name|search
operator|.
name|SearchBean
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
name|search
operator|.
name|SearchContextProvider
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
name|search
operator|.
name|sql
operator|.
name|SQLPrinterVisitor
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
name|impl
operator|.
name|MetadataMap
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
name|lifecycle
operator|.
name|SingletonResourceProvider
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
name|provider
operator|.
name|BinaryDataProvider
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
name|provider
operator|.
name|JAXBElementProvider
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
name|provider
operator|.
name|StreamingResponseProvider
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
name|systest
operator|.
name|jaxrs
operator|.
name|BookStore
operator|.
name|BookNotReturnedException
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
import|;
end_import

begin_class
specifier|public
class|class
name|BookServer
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|BookServer
operator|.
name|class
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
name|server
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|properties
decl_stmt|;
specifier|public
name|BookServer
parameter_list|()
block|{
name|this
argument_list|(
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|Object
operator|>
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Allow to specified custom contextual properties to be passed to factory bean      */
specifier|public
name|BookServer
parameter_list|(
specifier|final
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|bus
operator|.
name|setProperty
argument_list|(
name|ExceptionMapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|BusMapperExceptionMapper
argument_list|()
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
name|SimpleBookStore
operator|.
name|class
argument_list|,
name|BookStorePerRequest
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|//default lifecycle is per-request, change it to singleton
name|BinaryDataProvider
argument_list|<
name|Object
argument_list|>
name|p
init|=
operator|new
name|BinaryDataProvider
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setProduceMediaTypes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"application/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setEnableBuffering
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|p
operator|.
name|setReportByteArraySize
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|BookStore
operator|.
name|PrimitiveIntArrayReaderWriter
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|BookStore
operator|.
name|PrimitiveDoubleArrayReaderWriter
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|BookStore
operator|.
name|StringArrayBodyReaderWriter
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|BookStore
operator|.
name|StringListBodyReaderWriter
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|StreamingResponseProvider
argument_list|<
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|ContentTypeModifyingMBW
argument_list|()
argument_list|)
expr_stmt|;
name|JAXBElementProvider
argument_list|<
name|?
argument_list|>
name|jaxbProvider
init|=
operator|new
name|JAXBElementProvider
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|jaxbElementClassMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|jaxbElementClassMap
operator|.
name|put
argument_list|(
name|BookNoXmlRootElement
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"BookNoXmlRootElement"
argument_list|)
expr_stmt|;
name|jaxbProvider
operator|.
name|setJaxbElementClassMap
argument_list|(
name|jaxbElementClassMap
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|jaxbProvider
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|FormatResponseHandler
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|GenericHandlerWriter
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|FaultyRequestHandler
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|SearchContextProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|QueryContextProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|BlockingRequestFilter
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|FaultyResponseFilter
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|BlockedExceptionMapper
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|ParamConverterImpl
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|inInts
init|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|inInts
operator|.
name|add
argument_list|(
operator|new
name|CustomInFaultyInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|inInts
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setInInterceptors
argument_list|(
name|inInts
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|outInts
init|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|outInts
operator|.
name|add
argument_list|(
operator|new
name|CustomOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setOutInterceptors
argument_list|(
name|outInts
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|outFaultInts
init|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|outFaultInts
operator|.
name|add
argument_list|(
operator|new
name|CustomOutFaultInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setOutFaultInterceptors
argument_list|(
name|outFaultInts
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceProvider
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
operator|new
name|BookStore
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.jaxrs.mediaTypeCheck.strict"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
literal|"search.visitor"
argument_list|,
operator|new
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
literal|"books"
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.http.header.split"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
literal|"default.content.type"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|server
operator|=
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|server
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|BookServer
name|s
init|=
operator|new
name|BookServer
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|BusMapperExceptionMapper
implements|implements
name|ExceptionMapper
argument_list|<
name|BusMapperException
argument_list|>
block|{
specifier|public
name|Response
name|toResponse
parameter_list|(
name|BusMapperException
name|exception
parameter_list|)
block|{
return|return
name|Response
operator|.
name|serverError
argument_list|()
operator|.
name|type
argument_list|(
literal|"text/plain;charset=utf-8"
argument_list|)
operator|.
name|header
argument_list|(
literal|"BusMapper"
argument_list|,
literal|"the-mapper"
argument_list|)
operator|.
name|entity
argument_list|(
literal|"BusMapperException"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
annotation|@
name|PreMatching
specifier|private
specifier|static
class|class
name|BlockingRequestFilter
implements|implements
name|ContainerRequestFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|requestContext
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"/blockAndThrowException"
argument_list|)
condition|)
block|{
name|requestContext
operator|.
name|setProperty
argument_list|(
literal|"blocked"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|abortWith
argument_list|(
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|FaultyResponseFilter
implements|implements
name|ContainerResponseFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|,
name|ContainerResponseContext
name|responseContext
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|requestContext
operator|.
name|getProperty
argument_list|(
literal|"blocked"
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BlockedException
argument_list|()
throw|;
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|BlockedExceptionMapper
implements|implements
name|ExceptionMapper
argument_list|<
name|BlockedException
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|Response
name|toResponse
parameter_list|(
name|BlockedException
name|exception
parameter_list|)
block|{
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
specifier|public
specifier|static
class|class
name|BlockedException
extends|extends
name|RuntimeException
block|{      }
specifier|public
specifier|static
class|class
name|ReplaceContentTypeInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|ReplaceContentTypeInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|READ
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
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"text/plain"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|ReplaceStatusInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|ReplaceStatusInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|READ
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
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|NotReturnedExceptionMapper
implements|implements
name|ResponseExceptionMapper
argument_list|<
name|BookNotReturnedException
argument_list|>
block|{
specifier|public
name|BookNotReturnedException
name|fromResponse
parameter_list|(
name|Response
name|r
parameter_list|)
block|{
name|String
name|status
init|=
name|r
operator|.
name|getHeaderString
argument_list|(
literal|"Status"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"notReturned"
operator|.
name|equals
argument_list|(
name|status
argument_list|)
condition|)
block|{
return|return
operator|new
name|BookNotReturnedException
argument_list|(
name|status
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|NotFoundExceptionMapper
implements|implements
name|ResponseExceptionMapper
argument_list|<
name|BookNotFoundFault
argument_list|>
block|{
specifier|public
name|BookNotFoundFault
name|fromResponse
parameter_list|(
name|Response
name|r
parameter_list|)
block|{
name|String
name|status
init|=
name|r
operator|.
name|getHeaderString
argument_list|(
literal|"Status"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"notFound"
operator|.
name|equals
argument_list|(
name|status
argument_list|)
condition|)
block|{
return|return
operator|new
name|BookNotFoundFault
argument_list|(
name|status
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|TestResponseFilter
implements|implements
name|ClientResponseFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|requestContext
parameter_list|,
name|ClientResponseContext
name|responseContext
parameter_list|)
throws|throws
name|IOException
block|{
comment|// TODO Auto-generated method stub
block|}
block|}
specifier|public
specifier|static
class|class
name|ParamConverterImpl
implements|implements
name|ParamConverterProvider
block|{
annotation|@
name|Context
specifier|private
name|Providers
name|providers
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ParamConverter
argument_list|<
name|T
argument_list|>
name|getConverter
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|rawType
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|)
block|{
if|if
condition|(
name|rawType
operator|==
name|Book
operator|.
name|class
condition|)
block|{
name|MessageBodyReader
argument_list|<
name|Book
argument_list|>
name|mbr
init|=
name|providers
operator|.
name|getMessageBodyReader
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|annotations
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
decl_stmt|;
name|MessageBodyWriter
argument_list|<
name|Book
argument_list|>
name|mbw
init|=
name|providers
operator|.
name|getMessageBodyWriter
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|annotations
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
decl_stmt|;
return|return
operator|(
name|ParamConverter
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|XmlParamConverter
argument_list|(
name|mbr
argument_list|,
name|mbw
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|rawType
operator|==
name|byte
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|ParamConverter
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|ByteConverter
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ByteConverter
implements|implements
name|ParamConverter
argument_list|<
name|Byte
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|Byte
name|fromString
parameter_list|(
name|String
name|t
parameter_list|)
block|{
return|return
operator|new
name|Byte
argument_list|(
name|t
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|(
name|Byte
name|b
parameter_list|)
block|{
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|XmlParamConverter
implements|implements
name|ParamConverter
argument_list|<
name|Book
argument_list|>
block|{
specifier|private
name|MessageBodyReader
argument_list|<
name|Book
argument_list|>
name|mbr
decl_stmt|;
specifier|private
name|MessageBodyWriter
argument_list|<
name|Book
argument_list|>
name|mbw
decl_stmt|;
name|XmlParamConverter
parameter_list|(
name|MessageBodyReader
argument_list|<
name|Book
argument_list|>
name|mbr
parameter_list|,
name|MessageBodyWriter
argument_list|<
name|Book
argument_list|>
name|mbw
parameter_list|)
block|{
name|this
operator|.
name|mbr
operator|=
name|mbr
expr_stmt|;
name|this
operator|.
name|mbw
operator|=
name|mbw
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Book
name|fromString
parameter_list|(
name|String
name|value
parameter_list|)
block|{
try|try
block|{
return|return
name|mbr
operator|.
name|readFrom
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|value
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|(
name|Book
name|value
parameter_list|)
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|mbw
operator|.
name|writeTo
argument_list|(
name|value
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|bos
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|bos
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

