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
name|handler
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|PortInfo
import|;
end_import

begin_class
specifier|public
class|class
name|PortInfoImpl
implements|implements
name|PortInfo
block|{
specifier|private
name|String
name|bindingID
decl_stmt|;
specifier|private
name|QName
name|portName
decl_stmt|;
specifier|private
name|QName
name|serviceName
decl_stmt|;
specifier|private
name|String
name|address
decl_stmt|;
specifier|public
name|PortInfoImpl
parameter_list|(
name|String
name|bindingID
parameter_list|,
name|QName
name|portName
parameter_list|,
name|QName
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|bindingID
operator|=
name|bindingID
expr_stmt|;
name|this
operator|.
name|portName
operator|=
name|portName
expr_stmt|;
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
block|}
specifier|public
name|String
name|getBindingID
parameter_list|()
block|{
return|return
name|bindingID
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
name|QName
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
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
block|}
end_class

end_unit

