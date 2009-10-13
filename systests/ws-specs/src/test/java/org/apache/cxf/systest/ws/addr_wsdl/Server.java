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
name|ws
operator|.
name|addr_wsdl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|BindingProvider
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
name|Endpoint
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
name|Provider
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
name|Service
operator|.
name|Mode
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
name|ServiceMode
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
name|WebServiceContext
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
name|WebServiceProvider
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
name|soap
operator|.
name|Addressing
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
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
name|helpers
operator|.
name|XMLUtils
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
name|XPathUtils
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
name|EndpointImpl
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
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|AddNumberImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:9094/jaxws/add"
decl_stmt|;
name|EndpointImpl
name|ep
init|=
operator|new
name|EndpointImpl
argument_list|(
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
argument_list|,
name|implementor
argument_list|,
literal|null
argument_list|,
name|getWsdl
argument_list|()
argument_list|)
decl_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
operator|+
literal|"-provider"
argument_list|,
operator|new
name|AddNumberProvider
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
operator|+
literal|"-providernows"
argument_list|,
operator|new
name|AddNumberProviderNoWsdl
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getWsdl
parameter_list|()
block|{
try|try
block|{
name|java
operator|.
name|net
operator|.
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl_systest_wsspec/add_numbers.wsdl"
argument_list|)
decl_stmt|;
return|return
name|wsdl
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
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
name|Server
name|s
init|=
operator|new
name|Server
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
annotation|@
name|WebServiceProvider
argument_list|(
name|serviceName
operator|=
literal|"AddNumbersService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/cxf/systest/ws/addr_feature/"
argument_list|,
name|wsdlLocation
operator|=
literal|"/wsdl_systest_wsspec/add_numbers.wsdl"
argument_list|)
annotation|@
name|ServiceMode
argument_list|(
name|Mode
operator|.
name|PAYLOAD
argument_list|)
specifier|public
specifier|static
class|class
name|AddNumberProvider
implements|implements
name|Provider
argument_list|<
name|Source
argument_list|>
block|{
specifier|public
name|Source
name|invoke
parameter_list|(
name|Source
name|obj
parameter_list|)
block|{
comment|//CHECK the incoming
name|Element
name|el
decl_stmt|;
try|try
block|{
name|el
operator|=
operator|(
operator|(
name|Document
operator|)
name|XMLUtils
operator|.
name|fromSource
argument_list|(
name|obj
argument_list|)
operator|)
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
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
name|ns
operator|.
name|put
argument_list|(
literal|"ns"
argument_list|,
literal|"http://apache.org/cxf/systest/ws/addr_feature/"
argument_list|)
expr_stmt|;
name|XPathUtils
name|xp
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|String
name|o
init|=
operator|(
name|String
operator|)
name|xp
operator|.
name|getValue
argument_list|(
literal|"/ns:addNumbers/ns:number1"
argument_list|,
name|el
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
decl_stmt|;
name|String
name|o2
init|=
operator|(
name|String
operator|)
name|xp
operator|.
name|getValue
argument_list|(
literal|"/ns:addNumbers/ns:number2"
argument_list|,
name|el
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
decl_stmt|;
name|int
name|i
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|int
name|i2
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|o2
argument_list|)
decl_stmt|;
name|String
name|resp
init|=
literal|"<addNumbersResponse xmlns=\"http://apache.org/cxf/systest/ws/addr_feature/\">"
operator|+
literal|"<return>"
operator|+
operator|(
name|i
operator|+
name|i2
operator|)
operator|+
literal|"</return></addNumbersResponse>"
decl_stmt|;
return|return
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|resp
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|WebServiceProvider
argument_list|(
name|serviceName
operator|=
literal|"AddNumbersService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/cxf/systest/ws/addr_feature/"
argument_list|)
annotation|@
name|ServiceMode
argument_list|(
name|Mode
operator|.
name|PAYLOAD
argument_list|)
annotation|@
name|Addressing
argument_list|(
name|enabled
operator|=
literal|true
argument_list|,
name|required
operator|=
literal|true
argument_list|)
specifier|public
specifier|static
class|class
name|AddNumberProviderNoWsdl
implements|implements
name|Provider
argument_list|<
name|Source
argument_list|>
block|{
annotation|@
name|Resource
name|WebServiceContext
name|ctx
decl_stmt|;
specifier|public
name|Source
name|invoke
parameter_list|(
name|Source
name|obj
parameter_list|)
block|{
comment|//CHECK the incoming
name|Element
name|el
decl_stmt|;
try|try
block|{
name|el
operator|=
operator|(
operator|(
name|Document
operator|)
name|XMLUtils
operator|.
name|fromSource
argument_list|(
name|obj
argument_list|)
operator|)
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
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
name|ns
operator|.
name|put
argument_list|(
literal|"ns"
argument_list|,
literal|"http://apache.org/cxf/systest/ws/addr_feature/"
argument_list|)
expr_stmt|;
name|XPathUtils
name|xp
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|String
name|o
init|=
operator|(
name|String
operator|)
name|xp
operator|.
name|getValue
argument_list|(
literal|"/ns:addNumbers/ns:number1"
argument_list|,
name|el
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
decl_stmt|;
name|String
name|o2
init|=
operator|(
name|String
operator|)
name|xp
operator|.
name|getValue
argument_list|(
literal|"/ns:addNumbers/ns:number2"
argument_list|,
name|el
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
decl_stmt|;
name|int
name|i
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|int
name|i2
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|o2
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|getMessageContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SOAPACTION_URI_PROPERTY
argument_list|,
literal|"http://apache.org/cxf/systest/ws/addr_feature/AddNumbersPortType/addNumbersResponse"
argument_list|)
expr_stmt|;
name|String
name|resp
init|=
literal|"<addNumbersResponse xmlns=\"http://apache.org/cxf/systest/ws/addr_feature/\">"
operator|+
literal|"<return>"
operator|+
operator|(
name|i
operator|+
name|i2
operator|)
operator|+
literal|"</return></addNumbersResponse>"
decl_stmt|;
return|return
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|resp
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

