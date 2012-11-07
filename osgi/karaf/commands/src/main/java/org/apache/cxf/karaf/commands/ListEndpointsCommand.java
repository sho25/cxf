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
name|karaf
operator|.
name|commands
package|;
end_package

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
name|List
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
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Argument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|OsgiCommandSupport
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"cxf"
argument_list|,
name|name
operator|=
literal|"list-endpoints"
argument_list|,
name|description
operator|=
literal|"Lists all CXF Endpoints on a Bus."
argument_list|)
specifier|public
class|class
name|ListEndpointsCommand
extends|extends
name|OsgiCommandSupport
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|HEADER_FORMAT
init|=
literal|"%-25s %-10s %-60s %-40s"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|OUTPUT_FORMAT
init|=
literal|"[%-23s] [%-8s] [%-58s] [%-38s]"
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"bus"
argument_list|,
name|description
operator|=
literal|"The CXF bus name where to look for the Endpoints"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|name
decl_stmt|;
specifier|private
name|CXFController
name|cxfController
decl_stmt|;
specifier|public
name|void
name|setController
parameter_list|(
name|CXFController
name|controller
parameter_list|)
block|{
name|this
operator|.
name|cxfController
operator|=
name|controller
expr_stmt|;
block|}
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Bus
argument_list|>
name|busses
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|busses
operator|=
name|cxfController
operator|.
name|getBusses
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Bus
name|b
init|=
name|cxfController
operator|.
name|getBus
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|busses
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|cxfController
operator|.
name|getBus
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|busses
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|HEADER_FORMAT
argument_list|,
literal|"Name"
argument_list|,
literal|"State"
argument_list|,
literal|"Address"
argument_list|,
literal|"BusID"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Bus
name|b
range|:
name|busses
control|)
block|{
name|ServerRegistry
name|reg
init|=
name|b
operator|.
name|getExtension
argument_list|(
name|ServerRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Server
argument_list|>
name|servers
init|=
name|reg
operator|.
name|getServers
argument_list|()
decl_stmt|;
for|for
control|(
name|Server
name|serv
range|:
name|servers
control|)
block|{
name|String
name|qname
init|=
name|serv
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|started
init|=
name|serv
operator|.
name|isStarted
argument_list|()
condition|?
literal|"Started"
else|:
literal|"Stopped"
decl_stmt|;
name|String
name|address
init|=
name|serv
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|String
name|busId
init|=
name|b
operator|.
name|getId
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|OUTPUT_FORMAT
argument_list|,
name|qname
argument_list|,
name|started
argument_list|,
name|address
argument_list|,
name|busId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

