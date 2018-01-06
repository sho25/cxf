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
name|binding
operator|.
name|corba
operator|.
name|runtime
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
name|namespace
operator|.
name|QName
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
name|binding
operator|.
name|corba
operator|.
name|CorbaDestination
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
name|corba
operator|.
name|TestUtils
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
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ServerRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|Before
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
name|CorbaDSIServantTest
extends|extends
name|Assert
block|{
specifier|protected
specifier|static
name|ORB
name|orb
decl_stmt|;
specifier|protected
specifier|static
name|Bus
name|bus
decl_stmt|;
specifier|public
name|CorbaDSIServantTest
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
name|java
operator|.
name|util
operator|.
name|Properties
name|props
init|=
name|System
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"yoko.orb.id"
argument_list|,
literal|"CXF-CORBA-Server-Binding"
argument_list|)
expr_stmt|;
name|orb
operator|=
name|ORB
operator|.
name|init
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|orb
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|orb
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// Do nothing.  Throw an Exception?
block|}
block|}
block|}
comment|/*public void testCorbaDSIServant() throws Exception {          CorbaDestination destination = testUtils.getSimpleTypesTestDestination();         Service service = new ServiceImpl();         Endpoint endpoint = new EndpointImpl(bus, service, destination.getEndPointInfo());         MessageObserver observer = new ChainInitiationObserver(endpoint, bus);         destination.setMessageObserver(observer);         POA rootPOA = null;         CorbaDSIServant dsiServant = new CorbaDSIServant();         dsiServant.init(orb,                         rootPOA,                         destination,                         observer);          assertNotNull("DSIServant should not be null", dsiServant != null);         assertNotNull("POA should not be null", dsiServant._default_POA() != null);         assertNotNull("Destination should not be null", dsiServant.getDestination() != null);         assertNotNull("ORB should not be null", dsiServant.getOrb() != null);         assertNotNull("MessageObserver should not be null", dsiServant.getObserver() != null);          byte[] objectId = new byte[10];         String[] interfaces = dsiServant._all_interfaces(rootPOA, objectId);         assertNotNull("Interfaces should not be null", interfaces != null);         assertEquals("Interface ID should be equal", interfaces[0], "IDL:Simple:1.0");      }*/
annotation|@
name|Test
specifier|public
name|void
name|testInvoke
parameter_list|()
throws|throws
name|Exception
block|{
name|CorbaDestination
name|dest
init|=
operator|new
name|TestUtils
argument_list|()
operator|.
name|getComplexTypesTestDestination
argument_list|()
decl_stmt|;
name|CorbaDSIServant
name|dsiServant
init|=
operator|new
name|CorbaDSIServant
argument_list|()
decl_stmt|;
name|dsiServant
operator|.
name|init
argument_list|(
name|orb
argument_list|,
literal|null
argument_list|,
name|dest
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ServerRequest
name|request
init|=
operator|new
name|ServerRequest
argument_list|()
block|{
specifier|public
name|String
name|operation
parameter_list|()
block|{
return|return
literal|"greetMe"
return|;
block|}
specifier|public
name|Context
name|ctx
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|MessageObserver
name|incomingObserver
init|=
operator|new
name|TestObserver
argument_list|()
decl_stmt|;
name|dsiServant
operator|.
name|setObserver
argument_list|(
name|incomingObserver
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|QName
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"greetMe"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"greetMe"
argument_list|)
argument_list|)
expr_stmt|;
name|dsiServant
operator|.
name|setOperationMapping
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|dsiServant
operator|.
name|invoke
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
class|class
name|TestObserver
implements|implements
name|MessageObserver
block|{
name|TestObserver
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
comment|//System.out.println("Test OnMessage in TestObserver");
block|}
block|}
block|}
end_class

end_unit

