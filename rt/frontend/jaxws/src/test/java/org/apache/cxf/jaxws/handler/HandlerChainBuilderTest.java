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
name|Arrays
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|LogicalHandler
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
name|MessageContext
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
name|jaxws
operator|.
name|javaee
operator|.
name|FullyQualifiedClassType
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
name|javaee
operator|.
name|ParamValueType
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
name|javaee
operator|.
name|PortComponentHandlerType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|HandlerChainBuilderTest
extends|extends
name|Assert
block|{
name|Handler
index|[]
name|allHandlers
init|=
block|{
name|EasyMock
operator|.
name|createMock
argument_list|(
name|LogicalHandler
operator|.
name|class
argument_list|)
block|,
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Handler
operator|.
name|class
argument_list|)
block|,
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Handler
operator|.
name|class
argument_list|)
block|,
name|EasyMock
operator|.
name|createMock
argument_list|(
name|LogicalHandler
operator|.
name|class
argument_list|)
block|}
decl_stmt|;
name|Handler
index|[]
name|logicalHandlers
init|=
block|{
name|allHandlers
index|[
literal|0
index|]
block|,
name|allHandlers
index|[
literal|3
index|]
block|}
decl_stmt|;
name|Handler
index|[]
name|protocolHandlers
init|=
block|{
name|allHandlers
index|[
literal|1
index|]
block|,
name|allHandlers
index|[
literal|2
index|]
block|}
decl_stmt|;
name|HandlerChainBuilder
name|builder
init|=
operator|new
name|HandlerChainBuilder
argument_list|(
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testChainSorting
parameter_list|()
block|{
name|List
argument_list|<
name|Handler
argument_list|>
name|sortedHandlerChain
init|=
name|builder
operator|.
name|sortHandlers
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|allHandlers
argument_list|)
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|logicalHandlers
index|[
literal|0
index|]
argument_list|,
name|sortedHandlerChain
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|logicalHandlers
index|[
literal|1
index|]
argument_list|,
name|sortedHandlerChain
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|protocolHandlers
index|[
literal|0
index|]
argument_list|,
name|sortedHandlerChain
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|protocolHandlers
index|[
literal|1
index|]
argument_list|,
name|sortedHandlerChain
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildHandlerChainFromConfiguration
parameter_list|()
block|{
name|List
argument_list|<
name|PortComponentHandlerType
argument_list|>
name|hc
init|=
name|createHandlerChainType
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Handler
argument_list|>
name|chain
init|=
name|builder
operator|.
name|buildHandlerChainFromConfiguration
argument_list|(
name|hc
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|chain
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|chain
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestLogicalHandler
operator|.
name|class
argument_list|,
name|chain
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestLogicalHandler
operator|.
name|class
argument_list|,
name|chain
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestProtocolHandler
operator|.
name|class
argument_list|,
name|chain
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestProtocolHandler
operator|.
name|class
argument_list|,
name|chain
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|TestLogicalHandler
name|tlh
init|=
operator|(
name|TestLogicalHandler
operator|)
name|chain
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|tlh
operator|.
name|initCalled
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|tlh
operator|.
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuilderCallsInit
parameter_list|()
block|{
name|List
argument_list|<
name|PortComponentHandlerType
argument_list|>
name|hc
init|=
name|createHandlerChainType
argument_list|()
decl_stmt|;
name|hc
operator|.
name|remove
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|hc
operator|.
name|remove
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|hc
operator|.
name|remove
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|PortComponentHandlerType
name|h
init|=
name|hc
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ParamValueType
argument_list|>
name|params
init|=
name|h
operator|.
name|getInitParam
argument_list|()
decl_stmt|;
name|ParamValueType
name|p
init|=
operator|new
name|ParamValueType
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|CString
name|pName
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|CString
argument_list|()
decl_stmt|;
name|pName
operator|.
name|setValue
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setParamName
argument_list|(
name|pName
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|XsdStringType
name|pValue
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|XsdStringType
argument_list|()
decl_stmt|;
name|pValue
operator|.
name|setValue
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setParamValue
argument_list|(
name|pValue
argument_list|)
expr_stmt|;
name|params
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|p
operator|=
operator|new
name|ParamValueType
argument_list|()
expr_stmt|;
name|pName
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|CString
argument_list|()
expr_stmt|;
name|pName
operator|.
name|setValue
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setParamName
argument_list|(
name|pName
argument_list|)
expr_stmt|;
name|pValue
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|XsdStringType
argument_list|()
expr_stmt|;
name|pValue
operator|.
name|setValue
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setParamValue
argument_list|(
name|pValue
argument_list|)
expr_stmt|;
name|params
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Handler
argument_list|>
name|chain
init|=
name|builder
operator|.
name|buildHandlerChainFromConfiguration
argument_list|(
name|hc
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|chain
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|TestLogicalHandler
name|tlh
init|=
operator|(
name|TestLogicalHandler
operator|)
name|chain
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tlh
operator|.
name|initCalled
argument_list|)
expr_stmt|;
name|Map
name|cfg
init|=
name|tlh
operator|.
name|config
decl_stmt|;
name|assertNotNull
argument_list|(
name|tlh
operator|.
name|config
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cfg
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|cfg
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|cfg
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuilderCallsInitWithNoInitParamValues
parameter_list|()
block|{
name|List
argument_list|<
name|PortComponentHandlerType
argument_list|>
name|hc
init|=
name|createHandlerChainType
argument_list|()
decl_stmt|;
name|hc
operator|.
name|remove
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|hc
operator|.
name|remove
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|hc
operator|.
name|remove
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|PortComponentHandlerType
name|h
init|=
name|hc
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ParamValueType
argument_list|>
name|params
init|=
name|h
operator|.
name|getInitParam
argument_list|()
decl_stmt|;
name|ParamValueType
name|p
init|=
operator|new
name|ParamValueType
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|CString
name|pName
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|CString
argument_list|()
decl_stmt|;
name|pName
operator|.
name|setValue
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setParamName
argument_list|(
name|pName
argument_list|)
expr_stmt|;
name|params
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Handler
argument_list|>
name|chain
init|=
name|builder
operator|.
name|buildHandlerChainFromConfiguration
argument_list|(
name|hc
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|chain
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|TestLogicalHandler
name|tlh
init|=
operator|(
name|TestLogicalHandler
operator|)
name|chain
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tlh
operator|.
name|initCalled
argument_list|)
expr_stmt|;
name|Map
name|cfg
init|=
name|tlh
operator|.
name|config
decl_stmt|;
name|assertNotNull
argument_list|(
name|tlh
operator|.
name|config
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cfg
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuilderCannotLoadHandlerClass
parameter_list|()
block|{
name|List
argument_list|<
name|PortComponentHandlerType
argument_list|>
name|hc
init|=
name|createHandlerChainType
argument_list|()
decl_stmt|;
name|hc
operator|.
name|remove
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|hc
operator|.
name|remove
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|hc
operator|.
name|remove
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|FullyQualifiedClassType
name|type
init|=
operator|new
name|FullyQualifiedClassType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setValue
argument_list|(
literal|"no.such.class"
argument_list|)
expr_stmt|;
name|hc
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setHandlerClass
argument_list|(
name|type
argument_list|)
expr_stmt|;
try|try
block|{
name|builder
operator|.
name|buildHandlerChainFromConfiguration
argument_list|(
name|hc
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"did not get expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|ex
parameter_list|)
block|{
comment|// ex.printStackTrace();
name|assertNotNull
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ClassNotFoundException
operator|.
name|class
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|PortComponentHandlerType
argument_list|>
name|createHandlerChainType
parameter_list|()
block|{
name|List
argument_list|<
name|PortComponentHandlerType
argument_list|>
name|handlers
init|=
operator|new
name|ArrayList
argument_list|<
name|PortComponentHandlerType
argument_list|>
argument_list|()
decl_stmt|;
name|PortComponentHandlerType
name|h
init|=
operator|new
name|PortComponentHandlerType
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|CString
name|name
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|CString
argument_list|()
decl_stmt|;
name|name
operator|.
name|setValue
argument_list|(
literal|"lh1"
argument_list|)
expr_stmt|;
name|h
operator|.
name|setHandlerName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|FullyQualifiedClassType
name|type
init|=
operator|new
name|FullyQualifiedClassType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setValue
argument_list|(
name|TestLogicalHandler
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|h
operator|.
name|setHandlerClass
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|handlers
operator|.
name|add
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|h
operator|=
operator|new
name|PortComponentHandlerType
argument_list|()
expr_stmt|;
name|name
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|CString
argument_list|()
expr_stmt|;
name|name
operator|.
name|setValue
argument_list|(
literal|"ph1"
argument_list|)
expr_stmt|;
name|h
operator|.
name|setHandlerName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|type
operator|=
operator|new
name|FullyQualifiedClassType
argument_list|()
expr_stmt|;
name|type
operator|.
name|setValue
argument_list|(
name|TestProtocolHandler
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|h
operator|.
name|setHandlerClass
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|handlers
operator|.
name|add
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|h
operator|=
operator|new
name|PortComponentHandlerType
argument_list|()
expr_stmt|;
name|name
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|CString
argument_list|()
expr_stmt|;
name|name
operator|.
name|setValue
argument_list|(
literal|"ph2"
argument_list|)
expr_stmt|;
name|h
operator|.
name|setHandlerName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|type
operator|=
operator|new
name|FullyQualifiedClassType
argument_list|()
expr_stmt|;
name|type
operator|.
name|setValue
argument_list|(
name|TestProtocolHandler
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|h
operator|.
name|setHandlerClass
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|handlers
operator|.
name|add
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|h
operator|=
operator|new
name|PortComponentHandlerType
argument_list|()
expr_stmt|;
name|name
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|javaee
operator|.
name|CString
argument_list|()
expr_stmt|;
name|name
operator|.
name|setValue
argument_list|(
literal|"lh2"
argument_list|)
expr_stmt|;
name|h
operator|.
name|setHandlerName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|type
operator|=
operator|new
name|FullyQualifiedClassType
argument_list|()
expr_stmt|;
name|type
operator|.
name|setValue
argument_list|(
name|TestLogicalHandler
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|h
operator|.
name|setHandlerClass
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|handlers
operator|.
name|add
argument_list|(
name|h
argument_list|)
expr_stmt|;
return|return
name|handlers
return|;
block|}
specifier|public
specifier|static
class|class
name|TestLogicalHandler
implements|implements
name|LogicalHandler
block|{
name|Map
name|config
decl_stmt|;
name|boolean
name|initCalled
decl_stmt|;
specifier|public
name|void
name|close
parameter_list|(
name|MessageContext
name|arg0
parameter_list|)
block|{         }
specifier|public
name|boolean
name|handleFault
parameter_list|(
name|MessageContext
name|arg0
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|handleMessage
parameter_list|(
name|MessageContext
name|arg0
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
specifier|final
name|void
name|init
parameter_list|(
specifier|final
name|Map
name|map
parameter_list|)
block|{
name|config
operator|=
name|map
expr_stmt|;
name|initCalled
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|TestProtocolHandler
implements|implements
name|Handler
block|{
specifier|public
name|void
name|close
parameter_list|(
name|MessageContext
name|arg0
parameter_list|)
block|{         }
specifier|public
name|boolean
name|handleFault
parameter_list|(
name|MessageContext
name|arg0
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|handleMessage
parameter_list|(
name|MessageContext
name|arg0
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

