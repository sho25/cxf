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
name|soap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|MessageFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPFactory
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
name|soap
operator|.
name|SOAPBinding
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
name|SoapBindingConstants
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
name|model
operator|.
name|SoapBindingInfo
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
name|saaj
operator|.
name|SAAJFactoryResolver
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
name|logging
operator|.
name|LogUtils
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
name|SOAPBindingImpl
extends|extends
name|AbstractBindingImpl
implements|implements
name|SOAPBinding
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SOAPBindingImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|LOG
operator|.
name|getResourceBundle
argument_list|()
decl_stmt|;
specifier|private
name|BindingInfo
name|soapBinding
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|roles
decl_stmt|;
specifier|public
name|SOAPBindingImpl
parameter_list|(
name|BindingInfo
name|sb
parameter_list|,
name|JaxWsEndpointImpl
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|soapBinding
operator|=
name|sb
expr_stmt|;
name|addRequiredRoles
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|addRequiredRoles
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|roles
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|roles
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|soapBinding
operator|instanceof
name|SoapBindingInfo
condition|)
block|{
name|SoapBindingInfo
name|bindingInfo
init|=
operator|(
name|SoapBindingInfo
operator|)
name|this
operator|.
name|soapBinding
decl_stmt|;
if|if
condition|(
name|bindingInfo
operator|.
name|getSoapVersion
argument_list|()
operator|instanceof
name|Soap11
condition|)
block|{
name|this
operator|.
name|roles
operator|.
name|add
argument_list|(
name|bindingInfo
operator|.
name|getSoapVersion
argument_list|()
operator|.
name|getNextRole
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|bindingInfo
operator|.
name|getSoapVersion
argument_list|()
operator|instanceof
name|Soap12
condition|)
block|{
name|this
operator|.
name|roles
operator|.
name|add
argument_list|(
name|bindingInfo
operator|.
name|getSoapVersion
argument_list|()
operator|.
name|getNextRole
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|roles
operator|.
name|add
argument_list|(
name|bindingInfo
operator|.
name|getSoapVersion
argument_list|()
operator|.
name|getUltimateReceiverRole
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getRoles
parameter_list|()
block|{
return|return
name|this
operator|.
name|roles
return|;
block|}
specifier|public
name|void
name|setRoles
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|set
parameter_list|)
block|{
if|if
condition|(
name|set
operator|!=
literal|null
operator|&&
operator|(
name|set
operator|.
name|contains
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getNoneRole
argument_list|()
argument_list|)
operator|||
name|set
operator|.
name|contains
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getNoneRole
argument_list|()
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"NONE_ROLE_ERR"
argument_list|)
argument_list|)
throw|;
block|}
name|this
operator|.
name|roles
operator|=
name|set
expr_stmt|;
name|addRequiredRoles
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isMTOMEnabled
parameter_list|()
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|soapBinding
operator|.
name|getProperty
argument_list|(
name|Message
operator|.
name|MTOM_ENABLED
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setMTOMEnabled
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|soapBinding
operator|.
name|setProperty
argument_list|(
name|Message
operator|.
name|MTOM_ENABLED
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MessageFactory
name|getMessageFactory
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|soapBinding
operator|instanceof
name|SoapBindingInfo
condition|)
block|{
name|SoapBindingInfo
name|bindingInfo
init|=
operator|(
name|SoapBindingInfo
operator|)
name|this
operator|.
name|soapBinding
decl_stmt|;
try|try
block|{
return|return
name|SAAJFactoryResolver
operator|.
name|createMessageFactory
argument_list|(
name|bindingInfo
operator|.
name|getSoapVersion
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"SAAJ_FACTORY_ERR"
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|SOAPFactory
name|getSOAPFactory
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|soapBinding
operator|instanceof
name|SoapBindingInfo
condition|)
block|{
name|SoapBindingInfo
name|bindingInfo
init|=
operator|(
name|SoapBindingInfo
operator|)
name|this
operator|.
name|soapBinding
decl_stmt|;
try|try
block|{
return|return
name|SAAJFactoryResolver
operator|.
name|createSOAPFactory
argument_list|(
name|bindingInfo
operator|.
name|getSoapVersion
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"SAAJ_FACTORY_ERR"
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isSoapBinding
parameter_list|(
specifier|final
name|String
name|bindingID
parameter_list|)
block|{
return|return
name|bindingID
operator|.
name|equals
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP11_BINDING_ID
argument_list|)
operator|||
name|bindingID
operator|.
name|equals
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP12_BINDING_ID
argument_list|)
operator|||
name|bindingID
operator|.
name|equals
argument_list|(
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
argument_list|)
operator|||
name|bindingID
operator|.
name|equals
argument_list|(
name|SOAPBinding
operator|.
name|SOAP11HTTP_MTOM_BINDING
argument_list|)
operator|||
name|bindingID
operator|.
name|equals
argument_list|(
name|SOAPBinding
operator|.
name|SOAP12HTTP_BINDING
argument_list|)
operator|||
name|bindingID
operator|.
name|equals
argument_list|(
name|SOAPBinding
operator|.
name|SOAP12HTTP_MTOM_BINDING
argument_list|)
return|;
block|}
specifier|public
name|String
name|getBindingID
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|soapBinding
operator|instanceof
name|SoapBindingInfo
condition|)
block|{
name|SoapBindingInfo
name|bindingInfo
init|=
operator|(
name|SoapBindingInfo
operator|)
name|this
operator|.
name|soapBinding
decl_stmt|;
if|if
condition|(
name|bindingInfo
operator|.
name|getSoapVersion
argument_list|()
operator|instanceof
name|Soap12
condition|)
block|{
return|return
name|SOAP12HTTP_BINDING
return|;
block|}
block|}
return|return
name|SOAP11HTTP_BINDING
return|;
block|}
block|}
end_class

end_unit

