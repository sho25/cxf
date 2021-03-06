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
name|binding
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
name|http
operator|.
name|HTTPBinding
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
name|binding
operator|.
name|AbstractBindingImpl
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
name|support
operator|.
name|JaxWsEndpointImpl
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
name|model
operator|.
name|BindingInfo
import|;
end_import

begin_class
specifier|public
class|class
name|HTTPBindingImpl
extends|extends
name|AbstractBindingImpl
implements|implements
name|HTTPBinding
block|{
specifier|public
name|HTTPBindingImpl
parameter_list|(
name|BindingInfo
name|sb
parameter_list|,
name|JaxWsEndpointImpl
name|ep
parameter_list|)
block|{
name|super
argument_list|(
name|ep
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getBindingID
parameter_list|()
block|{
comment|//REVISIT: JIRA CXF-613
return|return
literal|"http://cxf.apache.org/bindings/xformat"
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
specifier|public
name|void
name|setHandlerChain
parameter_list|(
name|List
argument_list|<
name|Handler
argument_list|>
name|hc
parameter_list|)
block|{
name|super
operator|.
name|setHandlerChain
argument_list|(
name|hc
argument_list|)
expr_stmt|;
name|validate
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|validate
parameter_list|()
block|{
for|for
control|(
name|Handler
argument_list|<
name|?
argument_list|>
name|handler
range|:
name|this
operator|.
name|getHandlerChain
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|handler
operator|instanceof
name|LogicalHandler
operator|)
condition|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
literal|"Adding an incompatible handler in HTTPBinding: "
operator|+
name|handler
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

