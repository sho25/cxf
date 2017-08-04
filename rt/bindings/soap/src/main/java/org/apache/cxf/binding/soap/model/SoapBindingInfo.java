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
name|soap
operator|.
name|model
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
name|soap
operator|.
name|Soap11
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
name|soap
operator|.
name|Soap12
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
name|soap
operator|.
name|SoapVersion
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|wsdl
operator|.
name|WSDLConstants
import|;
end_import

begin_class
specifier|public
class|class
name|SoapBindingInfo
extends|extends
name|BindingInfo
block|{
specifier|private
name|SoapVersion
name|soapVersion
decl_stmt|;
specifier|private
name|String
name|style
decl_stmt|;
specifier|private
name|String
name|transportURI
decl_stmt|;
specifier|public
name|SoapBindingInfo
parameter_list|(
name|ServiceInfo
name|serv
parameter_list|,
name|String
name|n
parameter_list|)
block|{
name|this
argument_list|(
name|serv
argument_list|,
name|n
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|resolveSoapVersion
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SoapBindingInfo
parameter_list|(
name|ServiceInfo
name|serv
parameter_list|,
name|String
name|n
parameter_list|,
name|SoapVersion
name|soapVersion
parameter_list|)
block|{
name|super
argument_list|(
name|serv
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|this
operator|.
name|soapVersion
operator|=
name|soapVersion
expr_stmt|;
block|}
specifier|private
name|void
name|resolveSoapVersion
parameter_list|(
name|String
name|n
parameter_list|)
block|{
if|if
condition|(
name|WSDLConstants
operator|.
name|NS_SOAP11
operator|.
name|equalsIgnoreCase
argument_list|(
name|n
argument_list|)
condition|)
block|{
name|this
operator|.
name|soapVersion
operator|=
name|Soap11
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|WSDLConstants
operator|.
name|NS_SOAP12
operator|.
name|equalsIgnoreCase
argument_list|(
name|n
argument_list|)
condition|)
block|{
name|this
operator|.
name|soapVersion
operator|=
name|Soap12
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unknow bindingId: "
operator|+
name|n
operator|+
literal|". Can not resolve the SOAP version"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|SoapVersion
name|getSoapVersion
parameter_list|()
block|{
if|if
condition|(
name|soapVersion
operator|==
literal|null
condition|)
block|{
name|resolveSoapVersion
argument_list|(
name|getBindingId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|soapVersion
return|;
block|}
specifier|public
name|void
name|setSoapVersion
parameter_list|(
name|SoapVersion
name|soapVersion
parameter_list|)
block|{
name|this
operator|.
name|soapVersion
operator|=
name|soapVersion
expr_stmt|;
block|}
specifier|public
name|String
name|getStyle
parameter_list|()
block|{
return|return
name|style
return|;
block|}
specifier|public
name|String
name|getStyle
parameter_list|(
name|OperationInfo
name|operation
parameter_list|)
block|{
name|SoapOperationInfo
name|opInfo
init|=
name|getOperation
argument_list|(
name|operation
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getExtensor
argument_list|(
name|SoapOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|opInfo
operator|!=
literal|null
condition|)
block|{
return|return
name|opInfo
operator|.
name|getStyle
argument_list|()
return|;
block|}
return|return
name|style
return|;
block|}
specifier|public
name|OperationInfo
name|getOperationByAction
parameter_list|(
name|String
name|action
parameter_list|)
block|{
for|for
control|(
name|BindingOperationInfo
name|b
range|:
name|getOperations
argument_list|()
control|)
block|{
name|SoapOperationInfo
name|opInfo
init|=
name|b
operator|.
name|getExtensor
argument_list|(
name|SoapOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|opInfo
operator|.
name|getAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
return|return
name|b
operator|.
name|getOperationInfo
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Get the soap action for an operation. Will never return null.      *      * @param operation      * @return      */
specifier|public
name|String
name|getSoapAction
parameter_list|(
name|OperationInfo
name|operation
parameter_list|)
block|{
name|BindingOperationInfo
name|b
init|=
name|getOperation
argument_list|(
name|operation
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|SoapOperationInfo
name|opInfo
init|=
name|b
operator|.
name|getExtensor
argument_list|(
name|SoapOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|opInfo
operator|!=
literal|null
operator|&&
name|opInfo
operator|.
name|getAction
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|opInfo
operator|.
name|getAction
argument_list|()
return|;
block|}
return|return
literal|""
return|;
block|}
specifier|public
name|void
name|setStyle
parameter_list|(
name|String
name|style
parameter_list|)
block|{
name|this
operator|.
name|style
operator|=
name|style
expr_stmt|;
block|}
specifier|public
name|String
name|getTransportURI
parameter_list|()
block|{
return|return
name|transportURI
return|;
block|}
specifier|public
name|void
name|setTransportURI
parameter_list|(
name|String
name|transportURI
parameter_list|)
block|{
name|this
operator|.
name|transportURI
operator|=
name|transportURI
expr_stmt|;
block|}
block|}
end_class

end_unit

