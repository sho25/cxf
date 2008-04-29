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
name|service
operator|.
name|model
package|;
end_package

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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|EndpointReferenceUtils
import|;
end_import

begin_comment
comment|/**  * The EndpointInfo contains the information for a web service 'port' inside of a service.  */
end_comment

begin_class
specifier|public
class|class
name|EndpointInfo
extends|extends
name|AbstractDescriptionElement
implements|implements
name|NamedItem
block|{
name|String
name|transportId
decl_stmt|;
name|ServiceInfo
name|service
decl_stmt|;
name|BindingInfo
name|binding
decl_stmt|;
name|QName
name|name
decl_stmt|;
name|EndpointReferenceType
name|address
decl_stmt|;
specifier|public
name|EndpointInfo
parameter_list|()
block|{     }
specifier|public
name|EndpointInfo
parameter_list|(
name|ServiceInfo
name|serv
parameter_list|,
name|String
name|ns
parameter_list|)
block|{
name|transportId
operator|=
name|ns
expr_stmt|;
name|service
operator|=
name|serv
expr_stmt|;
block|}
specifier|public
name|String
name|getTransportId
parameter_list|()
block|{
return|return
name|transportId
return|;
block|}
specifier|public
name|void
name|setTransportId
parameter_list|(
name|String
name|tid
parameter_list|)
block|{
name|transportId
operator|=
name|tid
expr_stmt|;
block|}
specifier|public
name|InterfaceInfo
name|getInterface
parameter_list|()
block|{
return|return
name|service
operator|.
name|getInterface
argument_list|()
return|;
block|}
specifier|public
name|ServiceInfo
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|QName
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|BindingInfo
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
specifier|public
name|void
name|setBinding
parameter_list|(
name|BindingInfo
name|b
parameter_list|)
block|{
name|binding
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
operator|(
literal|null
operator|!=
name|address
operator|)
condition|?
name|address
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
else|:
literal|null
return|;
block|}
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|addr
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|address
condition|)
block|{
name|address
operator|=
name|EndpointReferenceUtils
operator|.
name|getEndpointReference
argument_list|(
name|addr
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|EndpointReferenceUtils
operator|.
name|setAddress
argument_list|(
name|address
argument_list|,
name|addr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setAddress
parameter_list|(
name|EndpointReferenceType
name|endpointReference
parameter_list|)
block|{
name|address
operator|=
name|endpointReference
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getTraversedExtensor
parameter_list|(
name|T
name|defaultValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|T
name|value
init|=
name|getExtensor
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|binding
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|binding
operator|.
name|getExtensor
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|service
operator|!=
literal|null
operator|&&
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|service
operator|.
name|getExtensor
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|defaultValue
expr_stmt|;
block|}
block|}
return|return
name|value
return|;
block|}
specifier|public
name|EndpointReferenceType
name|getTarget
parameter_list|()
block|{
return|return
name|address
return|;
block|}
specifier|public
name|boolean
name|isSameAs
parameter_list|(
name|EndpointInfo
name|epInfo
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|epInfo
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|epInfo
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|binding
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|epInfo
operator|.
name|binding
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|service
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|epInfo
operator|.
name|service
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|name
operator|.
name|equals
argument_list|(
name|epInfo
operator|.
name|name
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"BindingQName="
operator|+
name|binding
operator|.
name|getName
argument_list|()
operator|+
literal|", ServiceQName="
operator|+
name|binding
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|", QName="
operator|+
name|name
return|;
block|}
block|}
end_class

end_unit

