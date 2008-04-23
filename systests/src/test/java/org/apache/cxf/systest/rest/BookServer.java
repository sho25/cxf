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
name|rest
package|;
end_package

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
name|Map
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
name|XMLInputFactory
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
name|XMLOutputFactory
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
name|http
operator|.
name|HttpBindingFactory
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
name|customer
operator|.
name|book
operator|.
name|BookService
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
name|customer
operator|.
name|book
operator|.
name|BookServiceImpl
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
name|customer
operator|.
name|book
operator|.
name|BookServiceWrapped
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
name|customer
operator|.
name|book
operator|.
name|BookServiceWrappedImpl
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
name|JaxWsServerFactoryBean
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
name|invoker
operator|.
name|BeanInvoker
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

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|MappedXMLInputFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|MappedXMLOutputFactory
import|;
end_import

begin_class
specifier|public
class|class
name|BookServer
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|protected
name|void
name|run
parameter_list|()
block|{
comment|//book service in unwrapped style
name|BookServiceImpl
name|serviceObj
init|=
operator|new
name|BookServiceImpl
argument_list|()
decl_stmt|;
name|JaxWsServerFactoryBean
name|sf
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceClass
argument_list|(
name|BookService
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Use the HTTP Binding which understands the Java Rest Annotations
name|sf
operator|.
name|setBindingId
argument_list|(
name|HttpBindingFactory
operator|.
name|HTTP_BINDING_ID
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9080/xml/"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
name|serviceObj
argument_list|)
argument_list|)
expr_stmt|;
comment|// Turn the "wrapped" style off. This means that CXF won't generate
comment|// wrapper XML elements and we'll have prettier XML text. This
comment|// means that we need to stick to one request and one response
comment|// parameter though.
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWrapped
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
comment|//book service in wrapped style
name|BookServiceWrappedImpl
name|serviceWrappedObj
init|=
operator|new
name|BookServiceWrappedImpl
argument_list|()
decl_stmt|;
name|JaxWsServerFactoryBean
name|sfWrapped
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sfWrapped
operator|.
name|setServiceClass
argument_list|(
name|BookServiceWrapped
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Use the HTTP Binding which understands the Java Rest Annotations
name|sfWrapped
operator|.
name|setBindingId
argument_list|(
name|HttpBindingFactory
operator|.
name|HTTP_BINDING_ID
argument_list|)
expr_stmt|;
name|sfWrapped
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9080/xmlwrapped"
argument_list|)
expr_stmt|;
name|sfWrapped
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
name|serviceWrappedObj
argument_list|)
argument_list|)
expr_stmt|;
name|sfWrapped
operator|.
name|create
argument_list|()
expr_stmt|;
name|JaxWsServerFactoryBean
name|sfJson
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sfJson
operator|.
name|setServiceClass
argument_list|(
name|BookService
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Use the HTTP Binding which understands the Java Rest Annotations
name|sfJson
operator|.
name|setBindingId
argument_list|(
name|HttpBindingFactory
operator|.
name|HTTP_BINDING_ID
argument_list|)
expr_stmt|;
name|sfJson
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9080/json"
argument_list|)
expr_stmt|;
name|sfJson
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
name|serviceObj
argument_list|)
argument_list|)
expr_stmt|;
comment|// Turn the "wrapped" style off. This means that CXF won't generate
comment|// wrapper JSON elements and we'll have prettier JSON text. This
comment|// means that we need to stick to one request and one response
comment|// parameter though.
name|sfJson
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWrapped
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Tell CXF to use a different Content-Type for the JSON endpoint
comment|// This should probably be application/json, but text/plain allows
comment|// us to view easily in a web browser.
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
comment|// Set up the JSON StAX implementation
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nstojns
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
name|nstojns
operator|.
name|put
argument_list|(
literal|"http://book.acme.com"
argument_list|,
literal|"acme"
argument_list|)
expr_stmt|;
name|MappedXMLInputFactory
name|xif
init|=
operator|new
name|MappedXMLInputFactory
argument_list|(
name|nstojns
argument_list|)
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|XMLInputFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|xif
argument_list|)
expr_stmt|;
name|MappedXMLOutputFactory
name|xof
init|=
operator|new
name|MappedXMLOutputFactory
argument_list|(
name|nstojns
argument_list|)
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|XMLOutputFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|xof
argument_list|)
expr_stmt|;
name|sfJson
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|sfJson
operator|.
name|create
argument_list|()
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
block|}
end_class

end_unit

