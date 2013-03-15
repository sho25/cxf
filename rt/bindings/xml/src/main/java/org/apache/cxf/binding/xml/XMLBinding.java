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
name|xml
package|;
end_package

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
name|Binding
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
name|AbstractBasicInterceptorProvider
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
name|Exchange
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
name|message
operator|.
name|MessageImpl
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
name|XMLMessage
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
name|XMLBinding
extends|extends
name|AbstractBasicInterceptorProvider
implements|implements
name|Binding
block|{
specifier|private
name|BindingInfo
name|bindingInfo
decl_stmt|;
specifier|public
name|XMLBinding
parameter_list|(
name|BindingInfo
name|bindingInfo
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|bindingInfo
operator|=
name|bindingInfo
expr_stmt|;
block|}
specifier|public
name|BindingInfo
name|getBindingInfo
parameter_list|()
block|{
return|return
name|bindingInfo
return|;
block|}
specifier|public
name|Message
name|createMessage
parameter_list|()
block|{
return|return
name|createMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Message
name|createMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
operator|!
name|m
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
condition|)
block|{
name|String
name|ct
init|=
literal|null
decl_stmt|;
comment|// Should this be done in ServiceInvokerInterceptor to support a case where the
comment|// response content type is detected early on the inbound chain for all the bindings ?
name|Exchange
name|exchange
init|=
name|m
operator|.
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|ct
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ct
operator|==
literal|null
condition|)
block|{
name|ct
operator|=
literal|"text/xml"
expr_stmt|;
block|}
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|XMLMessage
argument_list|(
name|m
argument_list|)
return|;
block|}
block|}
end_class

end_unit

