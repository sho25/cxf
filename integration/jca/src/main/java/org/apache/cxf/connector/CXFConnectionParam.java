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
name|connector
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|jca
operator|.
name|cxf
operator|.
name|CXFConnectionRequestInfo
import|;
end_import

begin_class
specifier|public
class|class
name|CXFConnectionParam
block|{
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|iface
decl_stmt|;
specifier|private
name|URL
name|wsdlLocation
decl_stmt|;
specifier|private
name|QName
name|serviceName
decl_stmt|;
specifier|private
name|QName
name|portName
decl_stmt|;
specifier|private
name|String
name|address
decl_stmt|;
specifier|public
name|CXFConnectionParam
parameter_list|()
block|{     }
specifier|public
name|CXFConnectionParam
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aIface
parameter_list|,
name|URL
name|aWsdlLocation
parameter_list|,
name|QName
name|aServiceName
parameter_list|,
name|QName
name|aPortName
parameter_list|)
block|{
name|this
operator|.
name|iface
operator|=
name|aIface
expr_stmt|;
name|this
operator|.
name|wsdlLocation
operator|=
name|aWsdlLocation
expr_stmt|;
name|this
operator|.
name|serviceName
operator|=
name|aServiceName
expr_stmt|;
name|this
operator|.
name|portName
operator|=
name|aPortName
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getInterface
parameter_list|()
block|{
return|return
name|iface
return|;
block|}
specifier|public
name|URL
name|getWsdlLocation
parameter_list|()
block|{
return|return
name|wsdlLocation
return|;
block|}
specifier|public
name|QName
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
specifier|public
name|QName
name|getPortName
parameter_list|()
block|{
return|return
name|portName
return|;
block|}
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|Object
name|other
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|other
operator|instanceof
name|CXFConnectionRequestInfo
condition|)
block|{
name|CXFConnectionRequestInfo
name|cri
init|=
operator|(
name|CXFConnectionRequestInfo
operator|)
name|other
decl_stmt|;
name|result
operator|=
name|areEquals
argument_list|(
name|iface
argument_list|,
name|cri
operator|.
name|getInterface
argument_list|()
argument_list|)
operator|&&
name|areEquals
argument_list|(
name|wsdlLocation
argument_list|,
name|cri
operator|.
name|getWsdlLocation
argument_list|()
argument_list|)
operator|&&
name|areEquals
argument_list|(
name|serviceName
argument_list|,
name|cri
operator|.
name|getServiceName
argument_list|()
argument_list|)
operator|&&
name|areEquals
argument_list|(
name|portName
argument_list|,
name|cri
operator|.
name|getPortName
argument_list|()
argument_list|)
operator|&&
name|areEquals
argument_list|(
name|address
argument_list|,
name|cri
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
specifier|public
name|void
name|setPortName
parameter_list|(
name|QName
name|portName
parameter_list|)
block|{
name|this
operator|.
name|portName
operator|=
name|portName
expr_stmt|;
block|}
specifier|public
name|void
name|setServiceName
parameter_list|(
name|QName
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
block|}
specifier|public
name|void
name|setWsdlLocation
parameter_list|(
name|URL
name|wsdlLocation
parameter_list|)
block|{
name|this
operator|.
name|wsdlLocation
operator|=
name|wsdlLocation
expr_stmt|;
block|}
specifier|public
name|void
name|setInterface
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aInterface
parameter_list|)
block|{
name|this
operator|.
name|iface
operator|=
name|aInterface
expr_stmt|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|getServiceName
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getInterface
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|^
name|getServiceName
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
return|return
name|getInterface
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|^
operator|(
name|getAddress
argument_list|()
operator|!=
literal|null
condition|?
name|getAddress
argument_list|()
operator|.
name|hashCode
argument_list|()
else|:
literal|1
operator|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|(
literal|256
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"Interface ["
operator|+
name|getInterface
argument_list|()
operator|+
literal|"] "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"PortName ["
operator|+
name|getPortName
argument_list|()
operator|+
literal|"] "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"ServiceName ["
operator|+
name|getServiceName
argument_list|()
operator|+
literal|"] "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"WsdlLocation ["
operator|+
name|getWsdlLocation
argument_list|()
operator|+
literal|"] "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"Address ["
operator|+
name|getAddress
argument_list|()
operator|+
literal|"] "
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|areEquals
parameter_list|(
name|Object
name|obj1
parameter_list|,
name|Object
name|obj2
parameter_list|)
block|{
if|if
condition|(
name|obj1
operator|==
literal|null
condition|)
block|{
return|return
literal|null
operator|==
name|obj2
return|;
block|}
return|return
name|obj1
operator|.
name|equals
argument_list|(
name|obj2
argument_list|)
return|;
block|}
block|}
end_class

end_unit

