begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|interop
operator|.
name|client
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
name|List
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
name|tempuri
operator|.
name|IPingServiceContract
import|;
end_import

begin_import
import|import
name|org
operator|.
name|tempuri
operator|.
name|PingService
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
specifier|static
specifier|final
name|String
name|INPUT
init|=
literal|"foo"
decl_stmt|;
comment|/**      * This class is not instantiated      */
specifier|private
name|Client
parameter_list|()
block|{     }
comment|/**      /**      * The main entrypoint into the application      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|argv
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|argv
operator|.
name|length
operator|<
literal|1
operator|||
literal|"ALL"
operator|.
name|equalsIgnoreCase
argument_list|(
name|argv
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|argv
operator|=
operator|new
name|String
index|[]
block|{
literal|"CustomBinding_IPingServiceContract"
block|,
literal|"OasisScenario2Binding_IPingServiceContract"
block|,
literal|"CustomBinding_IPingServiceContract1"
block|,
literal|"OasisScenario4Binding_IPingServiceContract"
block|,
literal|"CustomBinding_IPingServiceContract2"
block|,
comment|//                "CustomBinding_IPingServiceContract3", //NOT WORKING - [1]
literal|"CustomBinding_IPingServiceContract4"
block|,
comment|//                "CustomBinding_IPingServiceContract6", //NOT WORKING
comment|//                "CustomBinding_IPingServiceContract5", //NOT WORKING -[2]
comment|//                "CustomBinding_IPingServiceContract7", //NOT WORKING - service not running on given port
comment|//                "CustomBinding_IPingServiceContract8", //Hanging?
comment|//                "CustomBinding_IPingServiceContract9", //NOT WORKING
literal|"CustomBinding_IPingServiceContract10"
block|,             }
expr_stmt|;
block|}
comment|//argv = new String[] {argv[3]};
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"etc/client.xml"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|argv
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|portPrefix
range|:
name|argv
control|)
block|{
try|try
block|{
specifier|final
name|PingService
name|svc
init|=
operator|new
name|PingService
argument_list|()
decl_stmt|;
specifier|final
name|IPingServiceContract
name|port
init|=
name|svc
operator|.
name|getPort
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://tempuri.org/"
argument_list|,
name|portPrefix
argument_list|)
argument_list|,
name|IPingServiceContract
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|output
init|=
name|port
operator|.
name|ping
argument_list|(
name|INPUT
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|INPUT
operator|.
name|equals
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Expected "
operator|+
name|INPUT
operator|+
literal|" but got "
operator|+
name|output
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
literal|"Unexpected output "
operator|+
name|output
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"OK!"
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
literal|"OK!"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
literal|"Exception: "
operator|+
name|t
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|argv
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|argv
index|[
name|x
index|]
operator|+
literal|": "
operator|+
name|results
operator|.
name|get
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

begin_comment
comment|/* [1] The policy in PingService.wsdl seems to be wrong.   The sp:RequestSecurityTokenTemplate for      it states:<trust:KeyType>http://docs.oasis-open.org/ws-sx/ws-trust/200512/PublicKey</trust:KeyType>     but the "sample" produced from their online tool sends SymetricKey  [2] OasisScenario9/10 (CustomBinding_IPingServiceContract4/5) isn't working yet due to WSS4J      not supporting using RSAKeyValue (KeyInfo, WS-SecurityPolicy/KeyValueToken) things for      creating signatures  */
end_comment

end_unit

