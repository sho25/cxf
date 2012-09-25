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
name|jaxrs
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|ResourceInfo
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
name|jaxrs
operator|.
name|model
operator|.
name|OperationResourceInfo
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

begin_class
specifier|public
class|class
name|ResourceInfoImpl
implements|implements
name|ResourceInfo
block|{
specifier|private
name|OperationResourceInfo
name|ori
decl_stmt|;
specifier|public
name|ResourceInfoImpl
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|this
operator|.
name|ori
operator|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Method
name|getResourceMethod
parameter_list|()
block|{
return|return
name|ori
operator|==
literal|null
condition|?
literal|null
else|:
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResourceClass
parameter_list|()
block|{
return|return
name|ori
operator|==
literal|null
condition|?
literal|null
else|:
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
operator|.
name|getResourceClass
argument_list|()
return|;
block|}
block|}
end_class

end_unit

