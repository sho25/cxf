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
name|transport
operator|.
name|http
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
name|transports
operator|.
name|http
operator|.
name|QueryHandler
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
name|transports
operator|.
name|http
operator|.
name|QueryHandlerRegistry
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
class|class
name|QueryHandlerRegistryImpl
implements|implements
name|QueryHandlerRegistry
block|{
name|List
argument_list|<
name|QueryHandler
argument_list|>
name|queryHandlers
decl_stmt|;
name|Bus
name|bus
decl_stmt|;
specifier|public
name|QueryHandlerRegistryImpl
parameter_list|()
block|{     }
specifier|public
name|QueryHandlerRegistryImpl
parameter_list|(
name|Bus
name|b
parameter_list|,
name|List
argument_list|<
name|QueryHandler
argument_list|>
name|handlers
parameter_list|)
block|{
name|queryHandlers
operator|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|QueryHandler
argument_list|>
argument_list|(
name|handlers
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setQueryHandlers
parameter_list|(
name|List
argument_list|<
name|QueryHandler
argument_list|>
name|handlers
parameter_list|)
block|{
name|this
operator|.
name|queryHandlers
operator|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|QueryHandler
argument_list|>
argument_list|(
name|handlers
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
annotation|@
name|Resource
specifier|public
specifier|final
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
if|if
condition|(
name|queryHandlers
operator|==
literal|null
condition|)
block|{
name|queryHandlers
operator|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|QueryHandler
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|WSDLQueryHandler
name|wsdlQueryHandler
init|=
operator|new
name|WSDLQueryHandler
argument_list|()
decl_stmt|;
name|wsdlQueryHandler
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|queryHandlers
operator|.
name|add
argument_list|(
name|wsdlQueryHandler
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|QueryHandlerRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|QueryHandler
argument_list|>
name|getHandlers
parameter_list|()
block|{
return|return
name|queryHandlers
return|;
block|}
specifier|public
name|void
name|registerHandler
parameter_list|(
name|QueryHandler
name|handler
parameter_list|)
block|{
name|queryHandlers
operator|.
name|add
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|registerHandler
parameter_list|(
name|QueryHandler
name|handler
parameter_list|,
name|int
name|position
parameter_list|)
block|{
name|queryHandlers
operator|.
name|add
argument_list|(
name|position
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

