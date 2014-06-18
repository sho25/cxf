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
operator|.
name|completers
package|;
end_package

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
name|cxf
operator|.
name|karaf
operator|.
name|commands
operator|.
name|CXFController
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
name|Completer
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
name|completer
operator|.
name|StringsCompleter
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|EndpointCompleterSupport
implements|implements
name|Completer
block|{
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
annotation|@
name|Override
specifier|public
name|int
name|complete
parameter_list|(
specifier|final
name|String
name|buffer
parameter_list|,
specifier|final
name|int
name|cursor
parameter_list|,
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|final
name|List
name|candidates
parameter_list|)
block|{
name|StringsCompleter
name|delegate
init|=
operator|new
name|StringsCompleter
argument_list|()
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|Bus
argument_list|>
name|busses
init|=
name|cxfController
operator|.
name|getBusses
argument_list|()
decl_stmt|;
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
if|if
condition|(
name|acceptsFeature
argument_list|(
name|serv
argument_list|)
condition|)
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
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|add
argument_list|(
name|qname
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
return|return
name|delegate
operator|.
name|complete
argument_list|(
name|buffer
argument_list|,
name|cursor
argument_list|,
name|candidates
argument_list|)
return|;
block|}
comment|/**      * Method for filtering endpoint.      *      * @param server The endpoint Server.      * @return True if endpoint Server should be available in completer.      */
specifier|protected
specifier|abstract
name|boolean
name|acceptsFeature
parameter_list|(
name|Server
name|server
parameter_list|)
function_decl|;
block|}
end_class

end_unit

