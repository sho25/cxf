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
name|Collection
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
name|binding
operator|.
name|AbstractBindingFactory
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
name|binding
operator|.
name|xml
operator|.
name|interceptor
operator|.
name|XMLFaultInInterceptor
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
name|xml
operator|.
name|interceptor
operator|.
name|XMLFaultOutInterceptor
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
name|xml
operator|.
name|interceptor
operator|.
name|XMLMessageInInterceptor
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
name|xml
operator|.
name|interceptor
operator|.
name|XMLMessageOutInterceptor
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
name|interceptor
operator|.
name|AttachmentInInterceptor
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
name|AttachmentOutInterceptor
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
name|DocLiteralInInterceptor
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
name|StaxInInterceptor
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
name|StaxOutInterceptor
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
name|WrappedOutInterceptor
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
name|BindingOperationInfo
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
name|MessageInfo
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
name|MessagePartInfo
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
name|OperationInfo
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
name|ServiceInfo
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
block|{
literal|"bus"
block|}
argument_list|)
specifier|public
class|class
name|XMLBindingFactory
extends|extends
name|AbstractBindingFactory
block|{
specifier|public
specifier|static
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|DEFAULT_NAMESPACES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"http://cxf.apache.org/bindings/xformat"
argument_list|,
literal|"http://www.w3.org/2004/08/wsdl/http"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/http/"
argument_list|)
decl_stmt|;
specifier|public
name|XMLBindingFactory
parameter_list|()
block|{     }
specifier|public
name|XMLBindingFactory
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|b
argument_list|,
name|DEFAULT_NAMESPACES
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Binding
name|createBinding
parameter_list|(
name|BindingInfo
name|binding
parameter_list|)
block|{
name|XMLBinding
name|xb
init|=
operator|new
name|XMLBinding
argument_list|(
name|binding
argument_list|)
decl_stmt|;
name|xb
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|AttachmentInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|xb
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|xb
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DocLiteralInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|xb
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XMLMessageInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|xb
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|AttachmentOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|xb
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|xb
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WrappedOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|xb
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XMLMessageOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|xb
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XMLFaultInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|xb
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|xb
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XMLFaultOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|xb
return|;
block|}
specifier|public
name|BindingInfo
name|createBindingInfo
parameter_list|(
name|ServiceInfo
name|service
parameter_list|,
name|String
name|namespace
parameter_list|,
name|Object
name|config
parameter_list|)
block|{
name|BindingInfo
name|info
init|=
operator|new
name|BindingInfo
argument_list|(
name|service
argument_list|,
literal|"http://cxf.apache.org/bindings/xformat"
argument_list|)
decl_stmt|;
name|info
operator|.
name|setName
argument_list|(
operator|new
name|QName
argument_list|(
name|service
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|service
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"XMLBinding"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|OperationInfo
name|op
range|:
name|service
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|adjustConcreteNames
argument_list|(
name|op
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
name|adjustConcreteNames
argument_list|(
name|op
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|bop
init|=
name|info
operator|.
name|buildOperation
argument_list|(
name|op
operator|.
name|getName
argument_list|()
argument_list|,
name|op
operator|.
name|getInputName
argument_list|()
argument_list|,
name|op
operator|.
name|getOutputName
argument_list|()
argument_list|)
decl_stmt|;
name|info
operator|.
name|addOperation
argument_list|(
name|bop
argument_list|)
expr_stmt|;
block|}
return|return
name|info
return|;
block|}
specifier|private
name|void
name|adjustConcreteNames
parameter_list|(
name|MessageInfo
name|mi
parameter_list|)
block|{
if|if
condition|(
name|mi
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|MessagePartInfo
name|mpi
range|:
name|mi
operator|.
name|getMessageParts
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|mpi
operator|.
name|isElement
argument_list|()
condition|)
block|{
comment|//if it's not an element, we need to make it one
name|mpi
operator|.
name|setConcreteName
argument_list|(
name|mpi
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

