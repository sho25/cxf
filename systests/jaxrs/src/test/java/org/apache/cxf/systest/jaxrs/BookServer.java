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
name|interceptor
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
name|interceptor
operator|.
name|JAXRSOutExceptionMapperInterceptor
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
name|sf
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
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
argument_list|<
name|Object
argument_list|>
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
name|outInts
operator|.
name|add
argument_list|(
operator|new
name|JAXRSOutExceptionMapperInterceptor
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
name|header
argument_list|(
literal|"BusMapper"
argument_list|,
literal|"the-mapper"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

